package es.daw.bibliotecajdbc.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Filtros del listado de libros. Todos son opcionales. */
@Data
@NoArgsConstructor
public class FiltroLibrosDto {

    // >>> EXAMEN [FILTRO NUEVO]: añade aquí el campo con su validación (Integer = opcional: vacío llega como null).
    //     Su nombre debe coincidir con th:field="*{campo}" del formulario de lista.html.
    private Integer autorId;

    @Min(value = 1000, message = "{filtro.anio.rango}")
    @Max(value = 2100, message = "{filtro.anio.rango}")
    private Integer anioDesde;

    @Min(value = 1000, message = "{filtro.anio.rango}")
    @Max(value = 2100, message = "{filtro.anio.rango}")
    private Integer anioHasta;
}