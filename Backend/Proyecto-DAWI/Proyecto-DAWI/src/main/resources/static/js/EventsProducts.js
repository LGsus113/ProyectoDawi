document.addEventListener("DOMContentLoaded", () => {
            let listado = true;
            const productosContainer = document.getElementById("productosContainer");

            // Función para asignar eventos a los elementos dinámicos
            function asignarEventosModal() {
                const nuevoStockModal = document.getElementById("nuevoStock");

                if (!nuevoStockModal) {
                    console.warn("No se encontró el modal en el DOM.");
                    return;
                }

                nuevoStockModal.addEventListener("show.bs.modal", function (event) {
                    const button = event.relatedTarget;

                    if (!button) {
                        console.warn("No se encontró el botón relacionado con el evento.");
                        return;
                    }

                    const productId = button.getAttribute("data-id");
                    const currentStock = button.getAttribute("data-stock");

                    const modalInput = nuevoStockModal.querySelector("#stock");
                    modalInput.value = currentStock;

                    const form = nuevoStockModal.querySelector("form");
                    if (!form.querySelector("#productId")) {
                        const inputHidden = document.createElement("input");
                        inputHidden.type = "hidden";
                        inputHidden.id = "productId";
                        inputHidden.name = "idProducto";
                        form.appendChild(inputHidden);
                    }
                    form.querySelector("#productId").value = productId;
                });
            }

            // Delegación de eventos para el botón "Cambiar Estado"
            document.addEventListener("click", (event) => {
                const toggleButton = event.target.closest("#toggleListado"); // Detecta el clic en el botón
                if (!toggleButton) return; // Ignorar clics fuera del botón

                listado = !listado; // Cambiar el estado
                fetch(`/start/products-fragment?listado=${listado}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("Error al cargar los datos.");
                        }
                        return response.text(); // Devuelve el fragmento HTML como texto
                    })
                    .then(fragment => {
                        // Reemplazar el contenido de la tabla
                        productosContainer.innerHTML = fragment;

                        // Reasignar eventos al modal después de actualizar el DOM
                        asignarEventosModal();
                    })
                    .catch(error => {
                        console.error("Error al actualizar los datos:", error);
                        alert("Hubo un error al actualizar los datos.");
                    });
            });

            asignarEventosModal();
});