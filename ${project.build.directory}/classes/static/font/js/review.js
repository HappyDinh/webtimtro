
document.addEventListener("DOMContentLoaded", function() {
    const reviewLink = document.querySelector('.nav-link-review');
    if (reviewLink) {
        reviewLink.addEventListener('click', function(event){
            event.preventDefault();
            const tabs2 = document.getElementById('tabs-2');
            if (tabs2) {
                tabs2.classList.add('active');
            } else {
                console.error("Element with ID 'tabs-2' not found.");
            }
        });
    }
});


// document.getElementById('openPostForm').addEventListener('click', function(){
//     console.log('Button clicked!')
//
//     var postContainer = document.getElementById('postcontainer');
//     postContainer.style.display = 'block';
// });
document.getElementById('openPostForm').addEventListener('click', function (event) {
    console.log('Button clicked!');

    // Lấy giá trị từ Thymeleaf để kiểm tra đăng nhập
    var isAuthenticated = false; // Mặc định là chưa đăng nhập
    try {
        isAuthenticated = JSON.parse(document.getElementById('authStatus').textContent);
    } catch (e) {
        console.error("Lỗi khi lấy trạng thái đăng nhập:", e);
    }

    if (!isAuthenticated) {
        event.preventDefault(); // Ngăn chặn hành động mặc định
        alert("Vui lòng đăng nhập để đăng tin!");
        window.location.href = '/loginuser'; // Chuyển hướng đến trang đăng nhập
        return;
    }

    var postContainer = document.getElementById('postcontainer');
    postContainer.style.display = 'block';
});


document.getElementById('submitcancel').addEventListener('click', function(){
    var postCancel = document.getElementById('postcontainer');
    postCancel.style.display = 'none';
})




document.getElementById('openFilterForm').addEventListener('click', function() {
    const filterForm = document.getElementById('filterForm');
    filterForm.classList.toggle('hidden'); // Thêm hoặc xóa class 'hidden'
});


document.getElementById("searchForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Ngăn chặn reload trang

    // Thu thập dữ liệu từ form
    let formData = {
        Acreage: document.getElementById("Acreage").value.trim(),
        Distance: document.getElementById("Distance").value.trim(),
        price: document.getElementById("price").value.trim(),
        commonowner: document.getElementById("commonowner").checked, // Checkbox
        closed: document.getElementById("closed").checked, // Checkbox
        airconditioner: document.getElementById("airconditioner").checked, // Checkbox
        hotcold: document.getElementById("hotcold").checked, // Checkbox
        pets: document.getElementById("pets").value,
        wifi: document.getElementById("wifi").checked, // Checkbox
        area: document.getElementById("area").value.trim(),
        type: document.getElementById("type").value
    };

    console.log("Dữ liệu gửi đi:", formData);



    // 🔹 Bước 1: Gửi dữ liệu lên Flask
    fetch("http://127.0.0.1:5000/find_rooms", {  // Flask API
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(flaskData => {
            console.log("Kết quả từ Flask:", flaskData);

            // 🔹 Bước 2: Gửi kết quả từ Flask sang Spring Boot
            return fetch("http://localhost:8081/receive_rooms", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(flaskData) // Gửi toàn bộ dữ liệu từ Flask
            });
        })
        .then(response => {
            if (response.ok) {
                // Chuyển hướng sang trang kết quả
                window.location.href = "/receive_rooms";
            } else {
                console.error("Lỗi khi gửi dữ liệu đến Spring Boot!");
            }
        })
        .catch(error => console.error("Lỗi:", error));
});
