package es.daw.bibliotecajdbc.controller;

import es.daw.bibliotecajdbc.dto.AutorDto;
import es.daw.bibliotecajdbc.dto.FiltroLibrosDto;
import es.daw.bibliotecajdbc.dto.LibroDto;
import es.daw.bibliotecajdbc.exception.IsbnDuplicadoException;
import es.daw.bibliotecajdbc.mapper.AutorMapper;
import es.daw.bibliotecajdbc.mapper.LibroMapper;
import es.daw.bibliotecajdbc.model.Libro;
import es.daw.bibliotecajdbc.service.AutorService;
import es.daw.bibliotecajdbc.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// CAPA WEB. Aquí se usan MAPPERS: el controlador habla con DTO hacia la vista y con modelo (Libro) hacia el servicio.
// Mapa: GET /libros (lista+filtro) | /libros/nuevo | /libros/editar/{isbn} | POST /libros/guardar | POST /libros/borrar/{isbn}
@Controller
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;
    private final AutorService autorService;
    private final LibroMapper libroMapper;
    private final AutorMapper autorMapper;

    /** Disponible en todas las vistas de este controlador, también al volver con errores. */
    @ModelAttribute("autores")
    public List<AutorDto> autores() {
        return autorService.listar().stream()
                .map(autorMapper::toDto)
                .toList();
    }

    // READ: listado con filtros opcionales
    @GetMapping
    // Listado con filtro por GET. Flujo: @Valid valida el DTO -> validación cruzada a mano (rejectValue) -> si no hay errores se busca.
    // Con errores NO se añade 'libros' al modelo y la vista no pinta la tabla.
    // >>> EXAMEN [FILTRO NUEVO]: pasa aquí el valor nuevo a libroService.buscar(...). Para una validación entre dos campos,
    //     imita el if de abajo: result.rejectValue("campo", "clave.en.messages.properties").
    public String listar(@Valid @ModelAttribute("filtro") FiltroLibrosDto filtro,
                         BindingResult result,
                         Model model) {

        Integer desde = filtro.getAnioDesde();
        Integer hasta = filtro.getAnioHasta();
        if (desde != null && hasta != null && hasta < desde) {
            result.rejectValue("anioHasta", "filtro.rango.invalido");
        }

        // Con errores no se añade "libros": la vista no muestra la tabla
        if (!result.hasErrors()) {
            List<LibroDto> libros = libroService.buscar(filtro.getAutorId(), desde, hasta).stream()
                    .map(libroMapper::toDto)
                    .toList();
            model.addAttribute("libros", libros);
        }

        return "libros/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libroDto", new LibroDto());
        return "libros/formulario";
    }

    @GetMapping("/editar/{isbn}")
    public String editar(@PathVariable String isbn, Model model) {
        Libro libro = libroService.buscarPorIsbn(isbn);   // 404 si no existe
        model.addAttribute("libroDto", libroMapper.toDto(libro));
        return "libros/formulario";
    }

    @PostMapping("/guardar")
    // Alta y edición en un solo método: libroDto.isEdicion() (campo oculto del formulario) decide cuál.
    // Orden: 1) errores de validación -> volver al formulario  2) convertir DTO -> modelo con el mapper  3) llamar al servicio
    // 4) redirect con mensaje flash (PRG). Una regla de negocio fallida (ISBN duplicado) se traduce a rejectValue para
    // mostrarla junto al campo sin perder los datos.
    public String guardar(@Valid @ModelAttribute LibroDto libroDto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "libros/formulario";
        }

        Libro libro = libroMapper.toModelEntity(libroDto);

        if (libroDto.isEdicion()) {
            Libro actualizado = libroService.actualizar(libro);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Libro " + actualizado.getTitulo() + " actualizado");
        } else {
            try {
                Libro creado = libroService.crear(libro);
                redirectAttributes.addFlashAttribute("mensaje",
                        "Libro " + creado.getTitulo() + " creado");
            } catch (IsbnDuplicadoException e) {
                // Regla de negocio: se muestra junto al campo, sin perder los datos
                result.rejectValue("isbn", "libro.isbn.duplicado");
                return "libros/formulario";
            }
        }

        return "redirect:/libros";
    }

    @PostMapping("/borrar/{isbn}")
    public String borrar(@PathVariable String isbn,
                         RedirectAttributes redirectAttributes) {
        libroService.borrar(isbn);
        redirectAttributes.addFlashAttribute("mensaje", "Libro con ISBN " + isbn + " borrado");
        return "redirect:/libros";
    }
}