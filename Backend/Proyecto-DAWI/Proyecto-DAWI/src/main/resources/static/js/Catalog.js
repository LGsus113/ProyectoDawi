document.addEventListener("DOMContentLoaded", function () {
    // Inicializa el carrito desde sessionStorage si existe
    if (window.location.pathname === "/start/car-to-shop") {
        cargarCarritoEnCarrito();
    }
});

let carrito = JSON.parse(sessionStorage.getItem("carrito")) || [];

function agregarAlCarrito(button) {
    var idProducto = button.getAttribute('data-idProducto');
    var idOculto = document.getElementById('idOculto' + idProducto).value;
    console.log("Producto ID desde input oculto:", idOculto);

    comprar(idOculto);
}

function comprar(idProducto) {
    const nombreProductoElement = document.querySelector(`.card-title[data-id='${idProducto}']`);
    const precioProductoElement = document.querySelector(`.precio[data-id='${idProducto}']`);
    const stockProductoElement = document.querySelector(`.stock[data-id='${idProducto}']`);

    if (!nombreProductoElement || !precioProductoElement || !stockProductoElement) {
        console.error("No se encontraron elementos necesarios para el producto con ID:", idProducto);
        return;
    }
    const nombreProducto = nombreProductoElement.innerText;
    const precioProducto = parseFloat(precioProductoElement.innerText);
    let stockProducto = parseInt(stockProductoElement.innerText);

    // Busca si el producto ya existe en el carrito
    let productoExistente = carrito.find(item => item.id_prod === idProducto);

    if (productoExistente) {
        // Si hay stock, incrementa la cantidad
        if (productoExistente.cantidad < stockProducto) {
            productoExistente.cantidad += 1;
        } else {
            alert("No hay suficiente stock");
        }
    } else {
        // Si no existe, agrega el producto al carrito
        carrito.push({
            id_prod: idProducto,
            nombre: nombreProducto,
            cantidad: 1,
            precio_unitario: precioProducto
        });
    }

    // Actualiza el stock visualmente
    actualizarStock(idProducto, stockProducto - 1);
    actualizarVistaCarrito();
    console.log("Carrito actualizado:", carrito); // Depuración
}

function actualizarStock(idProducto, nuevoStock) {
    document.querySelector(`.stock[data-id='${idProducto}']`).innerText = nuevoStock;
}

function actualizarVistaCarrito() {
    sessionStorage.setItem("carrito", JSON.stringify(carrito));
    console.log("Guardando carrito en sessionStorage:", carrito); // Depuración
    alert("Producto añadido al carrito.");
}