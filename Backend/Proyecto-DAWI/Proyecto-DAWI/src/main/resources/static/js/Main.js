document.addEventListener("DOMContentLoaded", function () {
    const toggleListadoButton = document.getElementById("toggleListado");
    const productosContainer = document.getElementById("productosContainer");

    toggleListadoButton.addEventListener("click", function () {
        productosContainer.classList.toggle("d-none");
    });
});