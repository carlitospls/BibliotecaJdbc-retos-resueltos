package es.daw.bibliotecajdbc.mapper;

import es.daw.bibliotecajdbc.dto.LibroDto;
import es.daw.bibliotecajdbc.model.Autor;
import es.daw.bibliotecajdbc.model.Libro;
import org.springframework.stereotype.Component;

/** Convierte entre LibroDto y Libro. Solo convierte: no usa servicios ni repositorios. */
@Component
public class LibroMapper {

    /** Un libro que viene de la BD ya existe: si se abre su formulario, es una edición. */
    // >>> EXAMEN [COLUMNA NUEVA]: añade aquí dto.setX(libro.getX()) y, abajo, el valor nuevo en el constructor de Libro.
    public LibroDto toDto(Libro libro) {
        LibroDto dto = new LibroDto();
        dto.setIsbn(libro.getIsbn());
        dto.setEdicion(true);
        dto.setTitulo(libro.getTitulo());
        dto.setAutorId(libro.getAutor().getId());
        dto.setAutorNombre(libro.getAutor().getNombre());
        dto.setFechaPublicacion(libro.getFechaPublicacion());
        return dto;
    }

    /**
     * El Libro resultante no está completo:
     * - id es null, porque el DTO no lo lleva.
     * - El autor solo lleva su id.
     * LibroService se encarga de completarlo.
     */
    // >>> EXAMEN [COLUMNA NUEVA]: el constructor de Libro lleva los campos en el orden de la clase Libro; respétalo.
    public Libro toModelEntity(LibroDto dto) {
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio EXTRA (efecto colateral) · Otro "new Autor(...)" fuera de los archivos de autor:
        //     del formulario de libro solo llega el id del autor. Un null más para el hueco del año.
        Autor autor = new Autor(dto.getAutorId(), null, null);
        return new Libro(null, dto.getIsbn(), dto.getTitulo(), autor, dto.getFechaPublicacion());
    }
}
