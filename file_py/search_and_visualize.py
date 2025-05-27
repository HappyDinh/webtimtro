import os
import pickle
from annoy import AnnoyIndex
from flask import Flask, request, jsonify
from flask_cors import CORS
import pymysql
from feature_extractor_and_index import CombinedFeatureExtractor

app = Flask(__name__)
CORS(app, supports_credentials=True)

SIMILARITY_THRESHOLD = 0.6

# Cấu hình database
DB_CONFIG = {
    'host': '127.0.0.1',
    'user': 'Mungweb',
    'password': 'Mung0403@',
    'database': 'datn2025',
    'port': 3306,
    'cursorclass': pymysql.cursors.DictCursor
}

def connect_to_database():
    try:
        connection = pymysql.connect(**DB_CONFIG)
        return connection
    except Exception as e:
        print(f"Lỗi kết nối CSDL: {e}")
        return None

def get_room_ids_from_filenames(filenames):
    if not filenames:
        return [[], []]

    connection = connect_to_database()
    if not connection:
        return [[], []]

    try:
        query = "SELECT roomid, image FROM room"
        with connection.cursor() as cursor:
            cursor.execute(query)
            rows = cursor.fetchall()

        # Tạo dict ánh xạ ảnh -> roomid
        image_to_roomid = {}
        for row in rows:
            room_id = row['roomid']
            images = [img.strip() for img in row['image'].split(',')]
            for img in images:
                # Nếu ảnh đã từng xuất hiện rồi thì giữ room đầu tiên (tránh ghi đè)
                if img not in image_to_roomid:
                    image_to_roomid[img] = room_id

        room_ids = []
        matched_images = []
        seen_room_ids = set()

        for fname in filenames:  # Theo đúng thứ tự filenames ban đầu
            fname = fname.strip()
            room_id = image_to_roomid.get(fname)
            if room_id is not None and room_id not in seen_room_ids:
                room_ids.append(room_id)
                matched_images.append(fname)
                seen_room_ids.add(room_id)

        return [room_ids, matched_images]
    finally:
        connection.close()

def search_similar(query_image_path, index_path='combined_index.ann', meta_path='filenames.pkl', device='cpu'):
    dim = 2072
    index = AnnoyIndex(dim, 'euclidean')
    index.load(index_path)

    with open(meta_path, 'rb') as f:
        filenames = pickle.load(f)

    extractor = CombinedFeatureExtractor(device=device)
    query_feat = extractor.get_feature(query_image_path)
    if query_feat is None:
        print("Không thể trích xuất đặc trưng từ ảnh query!")
        return []

    found_indices, distances = index.get_nns_by_vector(query_feat, 100, include_distances=True)

    results = []
    for idx, dist in zip(found_indices, distances):
        similarity = 1 / (1 + dist)
        if similarity >= SIMILARITY_THRESHOLD:
            results.append((filenames[idx], dist, similarity))
            print(f" - {filenames[idx]} (distance={dist:.2f})")


    return results



@app.route("/", methods=["GET"])
def home():
    return "Welcome to the Room Finder API! Use /find_image with POST method."

@app.route('/find_image', methods=['POST'])
def find_image():
    if 'image' not in request.files:
        return jsonify({'error': 'Vui lòng upload một ảnh với tên field là "image"'}), 400

    image_file = request.files['image']

    if image_file.filename == '':
        return jsonify({'error': 'Tên file trống'}), 400

    temp_image_path = os.path.join('temp_query.jpg')
    image_file.save(temp_image_path)


    try:
        results = search_similar(temp_image_path)
        # results trả về dạng [(filename, distance, ...), ...]
        results_sorted = sorted(results, key=lambda x: x[1])

        # Lấy danh sách tên file ảnh (chỉ tên, không lấy distance)
        filenames = [fname for fname, _, _ in results_sorted]

        # Gọi hàm get_room_ids_from_filenames, nhận về 2 list
        room_ids, matched_images = get_room_ids_from_filenames(filenames)

        # Trả về kết quả dạng JSON
        return jsonify({
            'room_ids': room_ids,
            'matched_images': matched_images
        })
    finally:
        # Xoá ảnh tạm sau khi xử lý xong
        if os.path.exists(temp_image_path):
            os.remove(temp_image_path)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
