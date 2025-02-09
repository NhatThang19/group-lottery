$(document).ready(function () {
    const $signUpButton = $('#signUp');
    const $signInButton = $('#signIn');
    const $container = $('#container');

    $signUpButton.on('click', function () {
        $container.addClass('right-panel-active');
    });

    $signInButton.on('click', function () {
        $container.removeClass('right-panel-active');
    });

    $('.togglePassword').on('click', function (event) {
        event.preventDefault();

        const passwordInput = $(this).closest('.form-container').find('.passwordInput');
        const toggleIcon = $(this).find('i');

        const type = passwordInput.attr('type') === 'password' ? 'text' : 'password';
        passwordInput.attr('type', type);

        toggleIcon.toggleClass('fa-eye fa-eye-slash');
    });

    const currentDate = new Date();
    const formattedDate = currentDate.toISOString().split('T')[0];
    $('#date_of_birth').attr('max', formattedDate);
});