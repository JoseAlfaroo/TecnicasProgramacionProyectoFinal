CREATE DATABASE IF NOT EXISTS db_eldorado;
USE db_eldorado;

-- 1. Tabla de Clientes
CREATE TABLE IF NOT EXISTS tb_cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    num_documento VARCHAR(15) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    estado BOOLEAN DEFAULT TRUE
);

-- 2. Tabla de Productos
CREATE TABLE IF NOT EXISTS tb_producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio_compra DECIMAL(10,2) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    stock_actual INT NOT NULL,
    stock_minimo INT NOT NULL,
    fecha_vencimiento DATE,
    activo BOOLEAN DEFAULT TRUE
);

-- 3. Tabla de Ventas
CREATE TABLE IF NOT EXISTS tb_venta (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    igv DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venta_cliente 
        FOREIGN KEY (id_cliente) REFERENCES tb_cliente(id_cliente)
);

-- 4. Tabla de Detalles de Venta
CREATE TABLE IF NOT EXISTS tb_detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_venta 
        FOREIGN KEY (id_venta) REFERENCES tb_venta(id_venta) 
        ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto 
        FOREIGN KEY (id_producto) REFERENCES tb_producto(id_producto)
);