function confirmAction({
    title = "Bạn có chắc chắn?",
    text = "Hành động này không thể hoàn tác!",
    confirmText = "Xác nhận",
    cancelText = "Hủy",
    confirmColor = "#d33",
    onConfirm
}) {
    Swal.fire({
        title: title,
        text: text,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: confirmColor,
        cancelButtonColor: "#3085d6",
        confirmButtonText: confirmText,
        cancelButtonText: cancelText
    }).then((result) => {
        if (result.isConfirmed && typeof onConfirm === "function") {
            onConfirm();
        }
    });
}

function formatDateTime(localDateTime) {
    if (!localDateTime) return "N/A";

    let date = new Date(localDateTime);

    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let year = date.getFullYear();

    return `${hours}:${minutes} - ${day}/${month}/${year}`;
}

function formatCurrency(amount) {
    return amount.toLocaleString("vi-VN").replace(/,/g, ".");
}

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
        loaderBg: loaderBg
    });
}