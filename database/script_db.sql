CREATE DATABASE IF NOT EXISTS db_eldorado;
USE db_eldorado;

-- Crear la tabla de prueba
CREATE TABLE IF NOT EXISTS tb_prueba (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
);
