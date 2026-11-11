package es.daw.bibliotecajdbc.repository;

import es.daw.bibliotecajdbc.exception.AccesoDatosException;
import es.daw.bibliotecajdbc.model.Autor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// CAPA DE DATOS de autores: una sola tabla, sin JOIN. Mismo patrón JDBC que el resto.
@Repository
@RequiredArgsConstructor
public class AutorRepositoryJdbc implements AutorRepository {

    private final DataSource dataSource;


    @Override
    public Optional<Autor> findById(Integer id) {
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 4/10 (a) · PEDIR la columna. Aquí NO hay SELECT_BASE: hay DOS SELECT que tocar
        //     (este y el de findAll). Si te dejas uno: compila, y falla al usar ESE método
        //     (este se usa al pulsar Editar; el de findAll, al abrir el listado).
        String sql = "SELECT id, nombre, anio_nacimiento FROM autor WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el autor " + id, e);
        }
    }

    @Override
    public List<Autor> findAll() {
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 4/10 (b) · El segundo SELECT.
        String sql = "SELECT id, nombre, anio_nacimiento FROM autor ORDER BY nombre";
        List<Autor> autores = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                autores.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los autores", e);
        }
        return autores;
    }

    @Override
    public void save(Autor autor) {
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 10/10 (a) · INSERT: columna + un ? más + su setInt, las tres cosas cuadradas.
        //     Si se olvida y la columna es NOT NULL: "Error de base de datos" al CREAR un autor.
        String sql = "INSERT INTO autor (nombre, anio_nacimiento) VALUES (?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, autor.getNombre());
            ps.setInt(2, autor.getAnioNacimiento());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    autor.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar el autor", e);
        }
    }

    @Override
    public void update(Autor autor) {
        // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 10/10 (b) · UPDATE: "anio_nacimiento = ?" en el SET.
        //     TRAMPA: el ? del WHERE es el último -> el id pasa de setInt(2, ...) a setInt(3, ...).
        //     Si se olvida este método: no hay error, pero al editar el año nunca cambia.
        String sql = "UPDATE autor SET nombre = ?, anio_nacimiento = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, autor.getNombre());
            ps.setInt(2, autor.getAnioNacimiento());
            ps.setInt(3, autor.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar el autor " + autor.getId(), e);
        }
    }

    /**
     * Solo borra de la tabla autor.
     * Sus libros los borra la BD por el ON DELETE CASCADE de schema.sql.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM autor WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al borrar el autor " + id, e);
        }
    }

    // >>> RETO AUTOR [COLUMNA NUEVA en tabla PADRE] · sitio 5/10 · LEER la columna de la fila, en el orden del constructor de Autor (id, nombre, anioNacimiento).
    //     rs.getInt("anio_nacimiento") usa el nombre de la COLUMNA (con guion bajo), no el del campo Java.
    private Autor mapRow(ResultSet rs) throws SQLException {
        return new Autor(rs.getInt("id"), rs.getString("nombre"), rs.getInt("anio_nacimiento"));
    }
}