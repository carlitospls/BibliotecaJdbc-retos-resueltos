package es.daw.bibliotecajdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fila de la tabla AUTOR.
 * POJO sin anotaciones: ni de persistencia ni de validación.
 * Las validaciones del formulario están en AutorDto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    private Integer id;
    private String nombre;

    // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 3/10 · El campo en el modelo.
    //     - Integer (no int): así los libros pueden construir un Autor "a medias" pasando null (ver sitio EXTRA).
    //     - @AllArgsConstructor pasa a pedir 3 valores: (id, nombre, anioNacimiento). Dejan de compilar los CUATRO
    //       "new Autor(...)" del proyecto: AutorRepositoryJdbc.mapRow (sitio 5), AutorMapper.toModelEntity (sitio 9)
    //       y, LA TRAMPA DE ESTE RETO, dos archivos de LIBROS: LibroRepositoryJdbc.mapRow y LibroMapper.toModelEntity.
    //       Para encontrarlos todos: Ctrl+Shift+F -> "new Autor(".
    private Integer anioNacimiento;
}