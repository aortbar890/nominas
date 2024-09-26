
CREATE DATABASE nominas;


USE nominas;


CREATE TABLE empleados (
    dni VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(50),
    sexo CHAR(1),          
    anyos INT,             
    categoria INT          
);


CREATE TABLE nominas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(10),
    sueldo DECIMAL(10, 2),
    FOREIGN KEY (dni) REFERENCES empleados(dni)
);
