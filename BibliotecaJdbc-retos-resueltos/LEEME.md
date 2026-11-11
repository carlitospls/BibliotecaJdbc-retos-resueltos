# BibliotecaJdbc · reto del autor resuelto y comentado

**Qué es:** mi BibliotecaJdbc (el proyecto de la profe con mis marcas `>>> EXAMEN`) con el reto
**"Año de nacimiento del autor"** resuelto. El recorrido está en `GUIA-RETOS.md` (sección 3).

**Abrir:** IntelliJ → File → Open → esta carpeta → ejecutar `BibliotecaJdbcApplication` → `http://localhost:8080`.

**Buscar** (`Ctrl+Shift+F`): `RETO AUTOR` (los 10 sitios + 2 extra) · `>>> EXAMEN` (mis marcas: filtro, validación, regla, mensaje…).

**Archivos tocados por el reto (11):** `schema.sql`, `data.sql`, `messages.properties`, `model/Autor`, `dto/AutorDto`,
`mapper/AutorMapper`, `repository/AutorRepositoryJdbc`, `autores/lista.html`, `autores/formulario.html` y, como efecto
colateral del constructor de `Autor`, `repository/LibroRepositoryJdbc` y `mapper/LibroMapper`.
(Controladores y servicios: ninguno.)

**Pruebas a mano:** alta con año vacío / 999 / 2027 → error junto al campo sin perder el nombre · alta con 1950 →
sale en el listado · editar *Miguel Delibes* → sale 1920; cambiarlo y verlo en el listado · el listado de libros
y crear/editar un libro siguen funcionando.

**Comprobado:** tipos de todo el Java (compilación contra versiones simuladas de Spring/Lombok), todas las sentencias SQL
contra una BD de prueba, y las plantillas. **No comprobado:** arrancarlo de verdad con Spring Boot (no tenía Maven
disponible). Arráncalo y haz las pruebas a mano.
