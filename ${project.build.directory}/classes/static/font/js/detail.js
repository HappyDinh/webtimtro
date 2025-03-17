function previewImage(input) {
  if (input.files && input.files[0]) {
	var reader = new FileReader();
	reader.onload = function(e) {
	  document.getElementById('preview-image').src = e.target.result;
	  document.getElementById('preview-image').style.display = 'block';
	};
	reader.readAsDataURL(input.files[0]);
  }
}



function showExcellentMessage(radio) {
  var message = document.getElementById('excellent-message');
  if (radio.value == 5) {
    message.style.display = 'block';
  } else {
    message.style.display = 'none';
  }
}

function previewImage(input) {
  var preview = document.getElementById('preview-image');
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function(e) {
      preview.src = e.target.result;
      preview.style.display = 'block';
    }
    reader.readAsDataURL(input.files[0]);
  }
}

  
 function showToast() {
  const toast = document.getElementById("toast");
  toast.className = "show";
  setTimeout(function() {
	toast.className = toast.className.replace("show", "");
  }, 3000);
}


function startVoiceSearch() {
  const searchInput = document.getElementById('searchInput');

  // Kiểm tra xem trình duyệt có hỗ trợ Web Speech API không
  if ('webkitSpeechRecognition' in window) {
	const recognition = new webkitSpeechRecognition();
	recognition.lang = 'vi-VN'; // Thiết lập ngôn ngữ tiếng Việt
	recognition.continuous = false;
	recognition.interimResults = false;

	recognition.onstart = function() {
	  console.log('Recognition started');  // Log to console
      showToast();
	};

	recognition.onend = function() {
	  console.log('Recognition ended');
	};

	recognition.onresult = function(event) {
	  searchInput.value = event.results[0][0].transcript;
	  // Thực hiện tìm kiếm hoặc bất kỳ hành động nào khác ở đây
	  console.log('Kết quả tìm kiếm:', event.results[0][0].transcript);
	};

	recognition.start();
  } else {
	alert('Trình duyệt của bạn không hỗ trợ tính năng tìm kiếm bằng giọng nói.');
  }
}



function showToast(message) {
  const toast = document.getElementById("toast");
  toast.textContent = message;
  toast.className = "show";
  setTimeout(function() {
	toast.className = toast.className.replace("show", "");
  }, 3000);
}

function startVoiceSearch() {
  const searchInput = document.getElementById('searchInput');

  if ('webkitSpeechRecognition' in window) {
	const recognition = new webkitSpeechRecognition();
	recognition.lang = 'vi-VN';
	recognition.continuous = false;
	recognition.interimResults = false;

	recognition.onstart = function() {
	  console.log('Recognition started');
	  showToast("Đang ghi âm...");
	};

	recognition.onend = function() {
	  console.log('Recognition ended');
	};

	recognition.onresult = function(event) {
	  searchInput.value = event.results[0][0].transcript;
	  console.log('Kết quả tìm kiếm:', event.results[0][0].transcript);
	};

	recognition.onerror = function(event) {
	  console.error('Recognition error:', event);
	};

	recognition.start();
  } else {
	alert('Trình duyệt của bạn không hỗ trợ tính năng tìm kiếm bằng giọng nói.');
  }
}



function triggerImageUpload() {
  document.getElementById('imageInput').click();
}


function displayResults(results) {
  const resultsDiv = document.getElementById('results');
  resultsDiv.innerHTML = '';

  results.forEach(result => {
	const div = document.createElement('div');
	div.textContent = `${result.label} (${Math.round(result.probability * 100)}%)`;
	resultsDiv.appendChild(div);
  });
}

function toggleDropdown(button) {
	let dropdownMenu = button.nextElementSibling;

	// Kiểm tra nếu dropdown đang hiển thị thì ẩn đi, nếu đang ẩn thì hiển thị
	if (dropdownMenu.style.display === "block") {
		dropdownMenu.style.display = "none";
	} else {
		// Ẩn tất cả các menu khác trước khi hiển thị menu này
		document.querySelectorAll(".dropdown-menu").forEach(menu => {
			menu.style.display = "none";
		});

		dropdownMenu.style.display = "block";

		// Đóng menu khi click ra ngoài
		setTimeout(() => {
			document.addEventListener("click", function hideDropdown(event) {
				if (!button.parentElement.contains(event.target)) {
					dropdownMenu.style.display = "none";
					document.removeEventListener("click", hideDropdown);
				}
			});
		}, 10);
	}
}

