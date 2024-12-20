CREATE DATABASE IF NOT EXISTS ProyectoSpring;
USE ProyectoSpring;

CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY, 
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('Admin', 'Usuario') NOT NULL,
    estado TINYINT(1) DEFAULT 1 -- 1: Activo, 0: Inactivo
);

CREATE TABLE Productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL CHECK (stock >= 0),
    estado TINYINT(1) DEFAULT 1 -- 1: Disponible, 0: No disponible
);

CREATE TABLE Compras (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_usu INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    tarjeta VARCHAR(16) NOT NULL CHECK (LENGTH(tarjeta) = 16),
    total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (id_usu) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE Detalle_Compra (
    id_deta_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_com INT NOT NULL,
    id_prod INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal DECIMAL(10, 2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    FOREIGN KEY (id_com) REFERENCES Compras(id_compra) ON DELETE CASCADE,
    FOREIGN KEY (id_prod) REFERENCES Productos(id_producto) ON DELETE CASCADE
);

-- Indices adicionales por si hay necesidad
CREATE INDEX idx_email ON Usuarios(email);
CREATE INDEX idx_producto_nombre ON Productos(nombre);


-- Procedures basicos

DELIMITER $$
CREATE PROCEDURE sp_autenticar_usuario(
    IN p_email VARCHAR(100),
    IN p_contrasena VARCHAR(255)
)
BEGIN
    DECLARE v_estado TINYINT;

    SELECT estado INTO v_estado
    FROM Usuarios
    WHERE email = p_email;

    IF v_estado = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Usuario inactivo';
    ELSE
        SELECT id_usuario, nombre, rol, estado, email, contrasena
        FROM Usuarios
        WHERE email = p_email AND contrasena = p_contrasena AND estado = 1;
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_registrar_compra(
    IN p_id_usu INT,
    IN p_tarjeta VARCHAR(16),
    IN p_detalle JSON -- JSON con los detalles de la compra
)
BEGIN
    DECLARE v_id_compra INT;
    DECLARE v_total DECIMAL(10, 2) DEFAULT 0.00;
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_cantidad INT;
    DECLARE v_precio_unitario DECIMAL(10, 2);
    DECLARE v_id_prod INT;
    DECLARE v_length INT;

    -- Variable para manejo de errores
    DECLARE v_error_message TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Capturar información del error
        GET DIAGNOSTICS CONDITION 1 
            v_error_message = MESSAGE_TEXT;

        -- Rollback de la transacción
        ROLLBACK;

        -- Generar un error genérico para el cliente
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = v_error_message;
    END;

    START TRANSACTION;

    -- Crear la compra inicial
    INSERT INTO Compras (id_usu, tarjeta, total) VALUES (p_id_usu, p_tarjeta, 0);
    SET v_id_compra = LAST_INSERT_ID();

    -- Obtener la longitud del JSON (cantidad de productos)
    SET v_length = JSON_LENGTH(p_detalle);

    -- Iterar sobre cada elemento del JSON y registrar los detalles
    WHILE v_index < v_length DO
        -- Extraer datos del JSON
        SET v_id_prod = JSON_UNQUOTE(JSON_EXTRACT(p_detalle, CONCAT('$[', v_index, '].id_prod')));
        SET v_cantidad = JSON_UNQUOTE(JSON_EXTRACT(p_detalle, CONCAT('$[', v_index, '].cantidad')));
        SET v_precio_unitario = JSON_UNQUOTE(JSON_EXTRACT(p_detalle, CONCAT('$[', v_index, '].precio_unitario')));

        -- Insertar el detalle de la compra
        INSERT INTO Detalle_Compra (id_com, id_prod, cantidad, precio_unitario)
        VALUES (v_id_compra, v_id_prod, v_cantidad, v_precio_unitario);

        -- Actualizar el total acumulado
        SET v_total = v_total + (v_cantidad * v_precio_unitario);

        -- Avanzar al siguiente producto
        SET v_index = v_index + 1;
    END WHILE;

    -- Actualizar el total de la compra en la tabla Compras
    UPDATE Compras SET total = v_total WHERE id_compra = v_id_compra;

    COMMIT;
END$$
DELIMITER ;


-- Procedures por separado para generar la venta con detalle -------------------------------------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_registrar_compra_inicial(
    IN p_id_usu INT,
    IN p_tarjeta VARCHAR(16)
)
BEGIN
	INSERT INTO Compras(id_usu, tarjeta, total) VALUES (p_id_usu, p_tarjeta, 0);
    SELECT LAST_INSERT_ID() AS id_compra; -- Aquí obtenemos el id de la compra registrada y lo devolvemos en el parametro de salida (OUT)
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_registrar_detalle_compra(
    IN p_id_compra INT,
    IN p_id_prod INT,
    IN p_cantidad INT,
    IN p_precio_unitario DECIMAL(10, 2)
)
BEGIN
	INSERT INTO Detalle_Compra (id_com, id_prod, cantidad, precio_unitario)
    VALUES (p_id_compra, p_id_prod, p_cantidad, p_precio_unitario);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_actualizar_total_compra(
    IN p_id_compra INT
)
BEGIN
	DECLARE v_total DECIMAL(10, 2);
    
    -- Aquí calculamos el total de la compra con los productos del detalle de compra
    SELECT SUM(cantidad * precio_unitario)
    INTO v_total
    FROM Detalle_Compra
    WHERE id_com = p_id_compra;
    
    -- Una vez obtenido el total, lo actualizamos en la tabla compra dependiendo de su id
    UPDATE Compras SET total = v_total WHERE id_compra = p_id_compra;
END$$
DELIMITER ;
-- Fin -------------------------------------------------------------------------------------------------------------------------------------------


DELIMITER $$
CREATE PROCEDURE sp_listar_productos(
    IN p_disponibles TINYINT -- 1: Disponibles, 0: Inactivos
)
BEGIN
    IF p_disponibles = 1 THEN
        -- Listar disponibles
        SELECT id_producto, nombre, descripcion, precio, stock, estado
        FROM Productos
        WHERE estado = 1 AND stock > 0;
    ELSE
        -- Listar no disponibles o inactivos
        SELECT id_producto, nombre, descripcion, precio, stock, estado
        FROM Productos
        WHERE estado = 0 OR stock = 0;
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_deshabilitar_producto(
    IN p_id_prod INT
)
BEGIN
    DECLARE resultado INT;

    -- Intenta actualizar el producto
    UPDATE Productos
    SET estado = 0
    WHERE id_producto = p_id_prod AND estado != 0;

    -- Verifica si la actualización afectó alguna fila
    SET resultado = ROW_COUNT();

    -- Retorna el resultado
    SELECT resultado;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_habilitar_producto(
    IN p_id_prod INT
)
BEGIN
    DECLARE resultado INT;

    -- Intenta actualizar el producto
    UPDATE Productos
    SET estado = 1
    WHERE id_producto = p_id_prod AND estado != 1;

    -- Verifica si la actualización afectó alguna fila
    SET resultado = ROW_COUNT();

    -- Retorna el resultado
    SELECT resultado;
END$$
DELIMITER ;



-- Triggers basicos

DELIMITER $$
CREATE TRIGGER trg_actualizar_stock
AFTER INSERT ON Detalle_Compra
FOR EACH ROW
BEGIN
	UPDATE Productos
    SET stock = stock - NEW.cantidad
    WHERE id_producto = NEW.id_prod;
    
    IF (SELECT stock FROM Productos WHERE id_producto = NEW.id_prod) < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_prevenir_stock_negativo
AFTER UPDATE ON Productos
FOR EACH ROW
BEGIN
    IF NEW.stock < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El stock no puede ser negativo';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_verificar_producto_activo
BEFORE INSERT ON Detalle_Compra
FOR EACH ROW
BEGIN
    IF (SELECT estado FROM Productos WHERE id_producto = NEW.id_prod) = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El producto no está disponible';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_actualizar_estado_producto
AFTER UPDATE ON Productos
FOR EACH ROW
BEGIN
    IF NEW.stock = 0 THEN
        UPDATE Productos SET estado = 0 WHERE id_producto = NEW.id_producto;
    END IF;
END$$
DELIMITER ;