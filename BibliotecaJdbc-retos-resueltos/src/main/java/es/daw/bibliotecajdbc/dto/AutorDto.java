package es.daw.bibliotecajdbc.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos del autor entre la vista y el controlador.
 * El id viaja porque el autor no tiene clave de negocio: lo necesitan las URL
 * de edición y borrado y el value del select. Nunca se muestra como dato.
 */
@Data
@NoArgsConstructor
public class AutorDto {

    private Integer id;      // null = alta, con valor = edición

    @NotBlank(message = "{autor.nombre.obligatorio}")
    @Size(max = 100, message = "{autor.nombre.tamanio}")
    private String nombre;

    // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 8/10 · El campo en el DTO, con sus validaciones.
    //     "Obligatorio y entre 1000 y 2026":  @NotNull + @Min(1000) + @Max(2026)   (copiado de FiltroLibrosDto).
    //     - Integer (envoltorio): el campo vacío llega como null y salta @NotNull.
    //     - Las anotaciones dicen lo que SE PERMITE. Acuérdate de los import (Alt+Enter).
    //     - Este proyecto guarda los mensajes en messages.properties: message = "{clave}" y la clave se añade allí.
    //       También vale escribir el texto directamente: message = "El año es obligatorio".
    @NotNull(message = "{autor.anio.obligatorio}")
    @Min(value = 1000, message = "{autor.anio.rango}")
    @Max(value = 2026, message = "{autor.anio.rango}")
    private Integer anioNacimiento;
}