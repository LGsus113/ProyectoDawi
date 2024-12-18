document.addEventListener("DOMContentLoaded", function () {
    cargarCarritoEnCarrito();
});

let carrito = JSON.parse(sessionStorage.getItem("carrito")) || [];

function cargarCarritoEnCarrito() {
    carrito = JSON.parse(sessionStorage.getItem("carrito")) || [];
    console.log("Carrito cargado desde sessionStorage:", carrito); // Depuración
    let tbody = document.querySelector("tbody");
    tbody.innerHTML = "";

    // Si el carrito está vacío
    if (carrito.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center">
                    <img src="https://img.icons8.com/ios/50/000000/shopping-cart.png" alt="Carrito Vacío">
                    <p>Carrito Vacío</p>
                </td>
            </tr>
        `;
        return;
    }

    // Renderiza los productos del carrito
    carrito.forEach((producto, index) => {
        let fila = `
            <tr>
                <td>${producto.nombre} (ID: ${producto.id_prod})</td>
                <td>$${producto.precio_unitario.toFixed(2)}</td>
                <td>
                    <input type="number" value="${producto.cantidad}" min="1" onchange="actualizarCantidad(${index}, this.value)">
                </td>
                <td>$${(producto.cantidad * producto.precio_unitario).toFixed(2)}</td>
                <td><button onclick="eliminarProducto(${index})" class="btn btn-danger">X</button></td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", fila);
    });

    actualizarTotales();
}

function actualizarCantidad(index, nuevaCantidad) {
    nuevaCantidad = parseInt(nuevaCantidad);
    if (nuevaCantidad < 1) {
        alert("La cantidad no puede ser menor a 1");
        return;
    }
    carrito[index].cantidad = nuevaCantidad;
    sessionStorage.setItem("carrito", JSON.stringify(carrito));
    cargarCarritoEnCarrito();
}

function eliminarProducto(index) {
    carrito.splice(index, 1); // Elimina el producto del carrito
    sessionStorage.setItem("carrito", JSON.stringify(carrito));
    cargarCarritoEnCarrito();
    actualizarTotales()
}

function actualizarTotales() {
    let subtotal = carrito.reduce((acc, producto) => acc + producto.precio_unitario * producto.cantidad, 0);
    let descuento = subtotal > 100 ? subtotal * 0.05 : 0; // Ejemplo: 5% de descuento si subtotal > 100
    let total = subtotal - descuento;

    // Actualiza los valores en el DOM
    document.getElementById("subtotal").textContent = `S/. ${subtotal > 0 ? subtotal.toFixed(2) : "0.00"}`;
    document.getElementById("descuento").textContent = `S/. ${descuento > 0 ? descuento.toFixed(2) : "0.00"}`;
    document.getElementById("total").textContent = `S/. ${total > 0 ? total.toFixed(2) : "0.00"}`;
}

function procesarCarrito() {
    // Obtener el idUsuario dinámicamente del input en el HTML
    const idUsuario = document.getElementById("idUsuario").value;
    const tarjeta = document.getElementById("tarjeta").value;

    // Validaciones
    if (!idUsuario) {
        alert("Error: No se pudo obtener el ID del usuario.");
        return;
    }

    if (!tarjeta) {
        alert("Debe ingresar el número de tarjeta.");
        return;
    }

    // Datos de la compra
    const datosCompra = {
        idUsuario: idUsuario,
        tarjeta: tarjeta,
        detalleJson: carrito
    };

    // Envío al backend
    fetch("/start/procesar-carrito", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datosCompra)
    })
    .then(response => {
        if (response.ok) return response.json();
        throw new Error("Error al procesar la compra.");
    })
    .then(data => {
        alert("Compra procesada con éxito!");
        sessionStorage.removeItem("carrito"); // Limpia el carrito
        window.location.href = "/start/home"; // Redirige al home
    })
    .catch(error => {
        console.error("Error al procesar carrito:", error);
        alert("Hubo un error al procesar la compra.");
    });
}