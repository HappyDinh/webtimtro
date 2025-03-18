from flask import Flask, request, jsonify
import pandas as pd
import pymysql
import requests
from flask_cors import CORS
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import StandardScaler

app = Flask(__name__)
CORS(app, supports_credentials= True)  # Cho phép tất cả các nguồn truy cập API

# Thông tin kết nối MySQL
DB_CONFIG = {
    'host': '127.0.0.1',
    'user': 'Mungweb',
    'password': 'Mung0403@',
    'database': 'datn2025',
    'port': 3306,
    'cursorclass': pymysql.cursors.DictCursor
}

# Danh sách cột dạng BIT cần chuyển đổi thành số nguyên
BIT_COLUMNS = ['hotcold', 'airconditioner', 'closed', 'commonowner', 'pets', 'wifi']
FLOAT_COLUMNS = ['Acreage', 'Distance', 'price']
SPRING_BOOT_URL = "http://localhost:8081/receive_rooms"  # API của Spring Boot

def connect_to_database():
    """ Kết nối đến MySQL và trả về đối tượng connection. """
    try:
        connection = pymysql.connect(**DB_CONFIG)
        return connection
    except Exception as e:
        print(f"Lỗi kết nối CSDL: {e}")
        return None

def fetch_data(query):
    """ Lấy dữ liệu từ MySQL theo truy vấn SQL. """
    connection = connect_to_database()
    if not connection:
        return pd.DataFrame()

    try:
        query = "SELECT roomid, Acreage, Distance, price, commonowner, closed, airconditioner, hotcold, pets, wifi, area, type FROM room"
        with connection.cursor() as cursor:
            cursor.execute(query)
            result = cursor.fetchall()
        return pd.DataFrame(result)
    finally:
        connection.close()

def preprocess_data(df):
    """ Tiền xử lý dữ liệu, chuyển đổi kiểu dữ liệu phù hợp. """
    if df.empty:
        return df

    columns = ['roomid', 'Acreage', 'Distance', 'price', 'commonowner', 'closed', 'airconditioner',
               'hotcold', 'pets', 'wifi', 'area', 'type']
    data = df[columns]

    # Chuyển BIT(1) về số nguyên
    for col in BIT_COLUMNS:
        data.loc[:, col] = data[col].apply(lambda x: int.from_bytes(x, "big") if isinstance(x, bytes) else int(x))

    # Chuyển price thành float
    for column in FLOAT_COLUMNS:
        data.loc[:, column] = data[column].astype(float)

    return data

def find_nearest_rooms(data, user_input, n_neighbors=3):
    """ Tìm các phòng trọ gần nhất dựa trên dữ liệu đầu vào của người dùng. """
    if not isinstance(data, pd.DataFrame):
        return pd.DataFrame(), None, []

    message = None
    # Lọc theo khu vực nếu có
    if user_input.get('area'):
        area_user = user_input['area'].strip().lower()
        data_filtered = data[data['area'].str.strip().str.lower() == area_user]
        if data_filtered.empty:
            message = {
                "message": f"❌ Không có phòng trọ nào trong khu vực '{user_input['area']}'.",
                "suggestion": "➡️ Bạn có thể tham khảo các phòng trọ khác.",
            }
            data_filtered = data.copy()
    else:
        data_filtered = data.copy()

    # Lọc theo loại phòng nếu có
    if user_input.get('type'):
        type_user = user_input['type'].strip().lower()
        data_filtered = data_filtered[data_filtered['type'].str.strip().str.lower() == type_user]

    if data_filtered.empty:
        return pd.DataFrame(), None, []

    # Lưu lại index
    index_data_filtered = data_filtered.index.tolist()

    # Lưu lại RoomID để dùng sau khi tìm KNN
    room_ids = data_filtered['roomid'].tolist()

    # Xóa cột không cần thiết
    data_filtered = data_filtered.drop(columns=['roomid','area', 'type'], errors='ignore')

    # Chuyển dữ liệu đầu vào thành DataFrame
    user_input_df = pd.DataFrame([user_input])

    # Xóa cột không cần thiết
    user_input_df = user_input_df.drop(columns=['type', 'area'], errors='ignore')

    # Điền giá trị NaN
    user_input_df = user_input_df.fillna(0)

    # Đảm bảo user_input_df có cùng số cột với data_filtered
    for col in data_filtered.columns:
        if col not in user_input_df.columns:
            user_input_df[col] = 0

    user_input_df = user_input_df[data_filtered.columns]

    if data_filtered.empty:
        return pd.DataFrame(), None, []

    n_neighbors = min(n_neighbors, len(data_filtered))
    if n_neighbors < 1:
        return pd.DataFrame(), None, []

    # Chuẩn hóa dữ liệu
    scaler = StandardScaler()
    scaled_features = scaler.fit_transform(data_filtered)
    user_scaled = scaler.transform(user_input_df)

    # Xây dựng mô hình KNN
    knn = NearestNeighbors(n_neighbors=n_neighbors, metric='euclidean')
    knn.fit(scaled_features)

    # Tìm các phòng trọ gần nhất
    distances, indices = knn.kneighbors(user_scaled)

    # Chuyển kết quả về DataFrame
    results = pd.DataFrame({
        'index': [index_data_filtered[i] for i in indices[0]],
        'roomid': [room_ids[i] for i in indices[0]],
        'distance': distances[0]
    })

    results = results.sort_values(by='distance', ascending=True)

    # Lấy danh sách room_id đã được sắp xếp đúng thứ tự
    nearest_room_ids = results['roomid'].tolist()

    return data.loc[results['index']].reset_index(drop=True), message, nearest_room_ids


def normalize_value(value):
    if value is True:
        return 1
    elif value is False:
        return 0
    elif value == "allowed":
        return 1
    elif value == "not_allowed":
        return 0
    return value  # Giữ nguyên nếu không phải các giá trị trên




def send_data_to_spring_boot(data):
    try:
        print(f"Sending request to {SPRING_BOOT_URL} with method POST")
        response = requests.post(SPRING_BOOT_URL, json=data)
        print(f"Response status: {response.status_code}")
        print(f"Response body: {response.text}")  # Xem Spring Boot trả về gì
        return response.json()  # Lỗi xảy ra ở đây nếu JSON rỗng
    except requests.exceptions.RequestException as e:
        print(f"Lỗi gửi dữ liệu đến Spring Boot: {e}")
        return None
    except ValueError as e:
        print(f"Lỗi parse JSON từ phản hồi Spring Boot: {e}")  # Bắt lỗi JSON rỗng
        return None



@app.route("/", methods=["GET"])
def home():
    return "Welcome to the Room Finder API! Use /find_rooms to find rooms."

@app.route('/find_rooms', methods=['POST'])
def find_rooms():
    """ API nhận dữ liệu từ request và trả về danh sách phòng trọ gợi ý. """
    data = fetch_data("SELECT * FROM room")
    data = preprocess_data(data)
    print(data)
    print(data.dtypes)


    # request_data = request.get_json()
    # if not request_data:
    #     return jsonify({"error": "Invalid input"}), 400
    # print("request_data", request_data)

    # Nhận dữ liệu từ request
    user_input = request.get_json()
    if not user_input:
        return jsonify({"error": "Dữ liệu đầu vào không hợp lệ"}), 400
    # Chuẩn hóa dictionary
    user_input = {k: normalize_value(v) for k, v in user_input.items()}
    # Chuyển đổi các giá trị cần sang float, nếu không chuyển được thì gán 0.0
    for key in ['Acreage', 'Distance', 'price']:
        try:
            user_input[key] = float(user_input[key])
        except (ValueError, TypeError):
            user_input[key] = 0.0  # Gán 0.0 nếu giá trị không hợp lệ


    print("Dữ liệu người dùng:", user_input)
    for key, value in user_input.items():
        print(f"🔹 {key}: {value} (Kiểu: {type(value).__name__})")

    result, message, nearest_room_ids = find_nearest_rooms(data, user_input, n_neighbors=6)
    print(message)
    print(nearest_room_ids)

    # Sắp xếp thứ tự: message trước, data sau
    response = {"List_room_id": nearest_room_ids}
    if message:
        response.update(message)  # Đưa message lên đầu

    response["data"] = result.to_dict(orient="records")  # Dữ liệu phòng trọ


    # Gửi dữ liệu đến Spring Boot
    spring_response = send_data_to_spring_boot(response)
    return jsonify({"spring_response": spring_response, "flask_data": response}), 200


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
