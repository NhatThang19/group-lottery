// Cấu hình CSRF token cho Ajax
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

// Cấu hình mặc định cho tất cả các request Ajax
$.ajaxSetup({
    beforeSend: function (xhr) {
        if (header && token) {
            xhr.setRequestHeader(header, token);
        }
    }
});

// Cấu hình mặc định cho DataTables
const tableConfig = {
    scrollX: true,
    language: {
        processing: "Đang tải dữ liệu...",
        search: "Tìm kiếm",
        lengthMenu: "Hiển thị _MENU_ bản ghi",
        info: "Hiển thị _START_ đến _END_ trong tổng số _TOTAL_ bản ghi",
        infoEmpty: "Không có bản ghi nào để hiển thị",
        infoFiltered: "(Được lọc từ _MAX_ bản ghi gốc)",
        loadingRecords: "Đang tải dữ liệu...",
        zeroRecords: "Không có bản ghi nào để hiển thị",
        emptyTable: "Không có dữ liệu trong bảng",
        paginate: {
            first: "Đầu tiên",
            previous: "Trước",
            next: "Tiếp theo",
            last: "Cuối cùng"
        },
        aria: {
            sortAscending: ": kích hoạt để sắp xếp cột theo thứ tự tăng dần",
            sortDescending: ": kích hoạt để sắp xếp cột theo thứ tự giảm dần"
        }
    }
};

// Hàm hiển thị thông báo toast
function showToast(message, heading, icon, loaderBg) {
    $.toast({
        text: message,
        heading: heading,
        icon: icon,
        showHideTransition: 'fade',
        allowToastClose: true,
        hideAfter: 3000,
        stack: 5,
        position: {
            top: 70,
            right: 10
        },
        textAlign: 'left',
        loader: true,
        loaderBg: loaderBg,
    });
}

// Hàm format ngày giờ
function formatDateTime(data) {
    if (data) {
        let date = new Date(data[0], data[1] - 1, data[2], data[3], data[4], data[5]);
        return date.toLocaleString('vi-VN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        });
    }
    return "";
}

// Hàm chuyển đổi mảng ngày thành định dạng local date
function changeToLocalDateJS(dateArray) {
    const year = dateArray[0];
    const month = (dateArray[1] < 10 ? '0' : '') + dateArray[1];
    const day = (dateArray[2] < 10 ? '0' : '') + dateArray[2];
    return year + '-' + month + '-' + day;
}

// Hàm xử lý preview ảnh
function imagePreview() {
    const avatarFile = $(".avatar-input");
    avatarFile.change(function (e) {
        const imgURL = URL.createObjectURL(e.target.files[0]);
        $(".avatarPreview, .avatarPreviewInfor").attr("src", imgURL).css({ "display": "block" });
    });
}

// Hàm set max date cho input date
function maxDate() {
    const currentDate = new Date();
    const formattedDate = currentDate.toISOString().split('T')[0];
    $('#date_of_birth').attr('max', formattedDate);
}

// Document ready function
$(document).ready(function () {
    maxDate();
    imagePreview();
});
