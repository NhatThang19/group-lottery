function formatCurrency(amount) {
    return amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

document.addEventListener("DOMContentLoaded", function () {
    const balanceElement = document.querySelector('.balance');
    if (balanceElement) {
        const balance = balanceElement.textContent;
        balanceElement.textContent = formatCurrency(balance);
    }
});