$(document).ready(function () {
    deleteUser();
    formatAllDateTime();
    formatAllCurrency();
});

function deleteUser() {
    $(document).on("click", ".delete-btn", function (e) {
        e.preventDefault();
        let userId = $(this).attr("data-id");

        confirmAction({
            title: "Xóa người dùng?",
            text: "Dữ liệu người dùng sẽ bị xóa vĩnh viễn!",
            confirmText: "Xóa ngay!",
            confirmColor: "#d33",
            onConfirm: function () {
                window.location.href = "admin/user/delete/" + userId;
            }
        });
    });
}

function formatAllDateTime() {
    $("td[data-time]").each(function () {
        let rawDateTime = $(this).attr("data-time");
        $(this).text(formatDateTime(rawDateTime));
    });
}

function formatAllCurrency() {
    $("td[data-balance]").each(function () {
        let rawBalance = $(this).attr("data-balance");
        if (rawBalance) {
            $(this).text(formatCurrency(parseFloat(rawBalance)));
        }
    });
}
