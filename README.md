# Sistema de Gestión - Distribuidora El Dorado S.A.C.

Sistema de escritorio desarrollado en Java Swing para la automatización del control de inventario, gestión de clientes, procesamiento de ventas en caja y consulta de reportes históricos.

---

## Requisitos Previos

- Java Development Kit (JDK): Versión 17+.
- Base de Datos: MySQL Server 8.0+ (o XAMPP).
- IDE: Eclipse, VS Code o IntelliJ IDEA.

---

## Configuración Inicial

### 1. Ejecución del Script de Base de Datos
Antes de iniciar la aplicación, debe crearse la estructura de la base de datos MySQL:

1. Ubique el archivo del script en la carpeta del proyecto:
   `database/script_db.sql`
2. Abra y ejecute el contenido completo de `script_db.sql` en su gestor de base de datos (MySQL Workbench, phpMyAdmin o terminal de MySQL).
3. El script creará la base de datos `db_eldorado` junto con sus tablas relacionales (`tb_cliente`, `tb_producto`, `tb_venta`, `tb_detalle_venta`) y llaves foráneas.

---

### 2. Configuración de Credenciales
Ajuste los parámetros de conexión según su entorno local de MySQL:

1. Abra el archivo de propiedades ubicado en:
   `src/db.properties`
2. Modifique el usuario y la contraseña según su configuración local:

```properties
db.url=jdbc:mysql://localhost:3306/db_eldorado?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=mysql
```

---

## Ejecución del Proyecto

Para iniciar la aplicación, ejecute la clase principal desde su IDE:

- Ruta de la clase principal: `src/gui/AppMain.java`
- Acción: Clic derecho sobre `AppMain.java` -> Run As -> Java Application.
