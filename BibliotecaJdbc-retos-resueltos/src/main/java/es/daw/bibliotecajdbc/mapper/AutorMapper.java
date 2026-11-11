package es.daw.bibliotecajdbc.mapper;

import es.daw.bibliotecajdbc.dto.AutorDto;
import es.daw.bibliotecajdbc.model.Autor;
import org.springframework.stereotype.Component;

/** Convierte entre AutorDto y Autor. Solo convierte: no usa servicios ni repositorios. */
@Component
public class AutorMapper {

    public AutorDto toDto(Autor autor) {
        AutorDto dto = new AutorDto();
        dto.setId(autor.getId());
        dto.setNombre(autor.getNombre());
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 9/10 (a) · LA SILENCIOSA. En este proyecto la conversión modelo -> DTO
        //     está en esta clase (en Productos está dentro del propio DTO, en toDto()).
        //     Sin esta línea NO hay error: el campo sale vacío al Editar Y la columna sale vacía en el listado.
        dto.setAnioNacimiento(autor.getAnioNacimiento());
        return dto;
    }

    public Autor toModelEntity(AutorDto dto) {
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 9/10 (b) · El valor nuevo en el constructor, en el orden de la clase Autor.
        return new Autor(dto.getId(), dto.getNombre(), dto.getAnioNacimiento());
    }
}
