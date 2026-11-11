-- Se borran en orden inverso a las dependencias (primero la tabla "hija")
DROP TABLE IF EXISTS libro;
DROP TABLE IF EXISTS autor;

-- Tabla "padre": lado 1 de la relación
CREATE TABLE autor (
                       id     INT          AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       -- >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 1/10 · La columna en la tabla autor.
                       --     OJO a la coma: la línea de "nombre" antes no la llevaba (era la última) y ahora sí.
                       --     Un año es un número entero: INT. NOT NULL = obligatorio en TODOS los INSERT (sitio 2) y en save() (sitio 10).
                       anio_nacimiento INT NOT NULL
);

-- Tabla "hija": lado N de la relación (cada libro tiene UN autor)
CREATE TABLE libro (
                       id                INT          AUTO_INCREMENT PRIMARY KEY,  -- clave técnica: no se publica
                       isbn              VARCHAR(13)  NOT NULL,                    -- clave de negocio: se guarda como texto
                       titulo            VARCHAR(150) NOT NULL,
                       autor_id          INT          NOT NULL,
                       -- >>> EXAMEN [COLUMNA NUEVA]: añade la columna aquí. Después reinicia: la BD (en memoria) se recrea al arrancar.
                       fecha_publicacion DATE         NOT NULL,
                       CONSTRAINT uq_libro_isbn UNIQUE (isbn),
    -- ON DELETE CASCADE: al borrar un autor, la BD borra también sus libros.
    -- Sin esta cláusula, el DELETE de un autor con libros daría un error de integridad.
                       CONSTRAINT fk_libro_autor
                           FOREIGN KEY (autor_id) REFERENCES autor (id) ON DELETE CASCADE
);