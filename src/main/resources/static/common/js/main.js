$(window).on('load', function () {
    $('.loader').addClass('hidden');
});


$(document).ready(function () {
    deleteUser();
    formatAllDateTime();
    formatAllCurrency();
    imagePreview();
    showPassword();
});

function deleteUser() {
    $(document).on("click", ".delete-btn", function (e) {
        e.preventDefault();
        let form = $(this).closest("form");

        Swal.fire({
            title: "Bạn có chắc chắn muốn xóa?",
            text: "Dữ liệu người dùng sẽ bị xóa vĩnh viễn!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Xóa ngay!",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                form.submit();
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
    $("[data-balance]").each(function () {
        let rawBalance = $(this).attr("data-balance");
        if (rawBalance) {
            $(this).text(formatCurrency(parseFloat(rawBalance)));
        }
    });
}

function imagePreview() {
    $(document).ready(function () {
        var avatarImg = $(".avatarPreview");

        if (avatarImg.attr("src") && avatarImg.attr("src").trim() !== "") {
            avatarImg.show();
        }

        $(".avatar-input").on("change", function () {
            var file = this.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    avatarImg.attr("src", e.target.result).show();
                };
                reader.readAsDataURL(file);
            }
        });
    });
}

function showPassword() {
    $("#togglePassword").click(function () {
        let passwordField = $("#password");
        let type = passwordField.attr("type") === "password" ? "text" : "password";
        passwordField.attr("type", type);

        let icon = $(this).find("i");
        icon.toggleClass("fa-eye fa-eye-slash");
    });
}




