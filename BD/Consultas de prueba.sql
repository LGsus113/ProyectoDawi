USE ProyectoSpring;

-- Insersion de Usurios ficticios
INSERT INTO Usuarios (nombre, email, contrasena, rol, estado) 
VALUES
	('Juan Pérez', 'juan.perez@example.com', '$2a$10$2aow1GFZY5hJUz9D5DUIMOl9GXe5EeQKYzZaUv6yIrcZrG2F6xSyu', 'Usuario', 1), -- contrasena: password123
	('Ana García', 'ana.garcia@example.com', '$2a$10$5Xfo7PtVejyf5wdSU6AXSOG82KQXTYq19rZPTQKIGs/uxY7TwFy0y', 'Usuario', 1), -- contrasena: password456
	('Carlos López', 'carlos.lopez@example.com', '$2a$10$jWfKh/1yTNPgFi5NcV7o0ee7Kxws/miHCRbBRv25oN5uZV1bk2i4u', 'Admin', 1), -- contrasena: password789
	('María Rodríguez', 'maria.rodriguez@example.com', '$2a$10$l8T4T87Sc5b4iQzvjo8t5OIqdRJePEwwJ7De8HJQ31iQunbyNzFZG', 'Usuario', 1), -- contrasena: password000
	('Luisa Martínez', 'luisa.martinez@example.com', '$2a$10$LsBth9F4CLqmSupCY/09w.rh9hx.5OjDdupXTOPKYPlDF5TW/Vsu6', 'Usuario', 1); -- contrasena: password321


-- Insersion de productos perifericos y tecnología
INSERT INTO Productos (nombre, descripcion, precio, stock, estado) 
VALUES
	('Laptop Lenovo', 'Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD.', 750.00, 10, 1),
	('Smartphone Samsung Galaxy S21', 'Pantalla AMOLED 6.2", 128GB de almacenamiento.', 800.00, 15, 1),
	('Audífonos Sony WH-1000XM4', 'Audífonos inalámbricos con cancelación de ruido.', 300.00, 20, 1),
	('Monitor Dell 24"', 'Monitor Full HD con puerto HDMI y DisplayPort.', 200.00, 8, 1),
	('Teclado Mecánico Logitech', 'Teclado mecánico RGB con switches silenciosos.', 120.00, 25, 1),
	('Mochila Samsonite', 'Mochila para laptop resistente al agua.', 60.00, 30, 1),
	('Cámara Canon EOS Rebel T7', 'Cámara DSLR con lente 18-55mm.', 500.00, 5, 1),
	('Mouse Inalámbrico Logitech', 'Mouse ergonómico con conectividad Bluetooth.', 30.00, 40, 1),
	('Batería Externa Anker', 'Power bank de 20,000 mAh con carga rápida.', 40.00, 50, 1),
	('Botella de Agua Nalgene', 'Botella de agua de 1 litro, libre de BPA.', 15.00, 60, 1);

select * from Usuarios;
select * from Productos;
select * from Compras;
select * from Detalle_Compra;


-- Prueba de procedure

CALL sp_registrar_compra(
	1, -- ID usurio
    '1234567890123456', -- Tarjeta
    '[{"id_prod":1,"cantidad":2,"precio_unitario":750.00},{"id_prod":3,"cantidad":1,"precio_unitario":300.00}]' -- JSON de detalle de compra
);

CALL sp_listar_productos(1);-- Disponible = 1, No disponible = 0

CALL sp_autenticar_usuario(
	'maria.rodriguez@example.com',
    'password000'
);

CALL sp_deshabilitar_producto(1);
CALL sp_habilitar_producto(2);
CALL sp_listar_datos_compras_cliente(1);
CALL sp_listar_detalle_compra_cliente(1);