import java.util.ArrayList;
import java.util.Calendar;

public class GestionCineLikes {

    public static final String[] GENEROS = {"Drama", "Aventura", "Comedia", "Animación", "Documental"};
    public static final int ANIO_MINIMO = 1930;


    private ArrayList<Pelicula> peliculas;

    public GestionCineLikes() {
        peliculas = new ArrayList<>();
        precargarPeliculas();
    }


    private void precargarPeliculas() {
        registrar(2001, "Titanic", "James Cameron", "Drama", 1997);
        registrar(2002, "Avatar", "James Cameron", "Aventura", 2009);
        registrar(2003, "Interstellar", "Christopher Nolan", "Aventura", 2014);
        registrar(2004, "Inception", "Christopher Nolan", "Aventura", 2010);
        registrar(2005, "The Dark Knight", "Christopher Nolan", "Aventura", 2008);
        registrar(2006, "Coco", "Lee Unkrich", "Animación", 2017);
        registrar(2007, "Toy Story", "John Lasseter", "Animación", 1995);
        registrar(2008, "Up", "Pete Docter", "Animación", 2009);
        registrar(2009, "El viaje de Chihiro", "Hayao Miyazaki", "Animación", 2001);
        registrar(2010, "Forrest Gump", "Robert Zemeckis", "Drama", 1994);
        registrar(2011, "Cadena perpetua", "Frank Darabont", "Drama", 1994);
        registrar(2012, "Pulp Fiction", "Quentin Tarantino", "Drama", 1994);
        registrar(2013, "Superbad", "Greg Mottola", "Comedia", 2007);
        registrar(2014, "Groundhog Day", "Harold Ramis", "Comedia", 1993);
        registrar(2015, "Mi pobre angelito", "Chris Columbus", "Comedia", 1990);
        registrar(2016, "El gran hotel Budapest", "Wes Anderson", "Comedia", 2014);
        registrar(2017, "Jurassic Park", "Steven Spielberg", "Aventura", 1993);
        registrar(2018, "En busca del arca perdida", "Steven Spielberg", "Aventura", 1981);
        registrar(2019, "Free Solo", "Elizabeth Chai Vasarhelyi", "Documental", 2018);
        registrar(2020, "La marcha de los pingüinos", "Luc Jacquet", "Documental", 2005);
    }


    public int getCantidad() {
        return peliculas.size();
    }


    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }



    // R2: registra una película validando todo antes de insertarla.
    // ===================== Responsable: Steven Vaca (R2) =====================
    public void registrar(int numeroID, String titulo, String director, String genero, int anio) {
        if (numeroID <= 0) {
            throw new IllegalArgumentException("El código debe ser positivo");
        }
        if (existeNumero(numeroID)) {
            throw new IllegalArgumentException("Ya existe una película con el código " + numeroID);
        }
        validarDatos(titulo, director, genero, anio);
        insertarOrdenado(new Pelicula(numeroID, titulo, director, genero, anio));
    }

    // Búsqueda lineal para verificar que el código no exista.
    // ===================== Responsable: Steven Vaca (R2) =====================
    public boolean existeNumero(int numeroID) {
        for (int i = 0; i < peliculas.size(); i++) {
            if (peliculas.get(i).getNumeroID() == numeroID) {
                return true;
            }
        }
        return false;
    }

    // Inserta manteniendo el orden numeroID descendente (el código puede ser menor que los existentes).
    // ===================== Responsable: Steven Vaca (R2) =====================
    private void insertarOrdenado(Pelicula nueva) {
        int i = 0;
        while (i < peliculas.size() && peliculas.get(i).getNumeroID() > nueva.getNumeroID()) {
            i++;
        }
        peliculas.add(i, nueva);
    }

    // ===================== Responsable: Steven Vaca (R2) =====================
    private void validarDatos(String titulo, String director, String genero, int anio) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El director es obligatorio");
        }
        if (!esGeneroValido(genero)) {
            throw new IllegalArgumentException("Seleccione un género válido");
        }
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        if (anio < ANIO_MINIMO || anio > anioActual) {
            throw new IllegalArgumentException("El año debe estar entre " + ANIO_MINIMO + " y " + anioActual);
        }
    }

    // ===================== Responsable: Steven Vaca (R2) =====================
    private boolean esGeneroValido(String genero) {
        for (String g : GENEROS) {
            if (g.equals(genero)) {
                return true;
            }
        }
        return false;
    }

    // R4: búsqueda binaria sobre la colección descendente. Devuelve el índice o -1.
    // ===================== Responsable: Jostin Saca (R4) =====================
    public int buscarIndicePorNumero(int numeroID) {
        if (numeroID <= 0) {
            throw new IllegalArgumentException("El código debe ser positivo");
        }
        int inicio = 0;
        int fin = peliculas.size() - 1;
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            int actual = peliculas.get(medio).getNumeroID();
            if (actual == numeroID) {
                return medio;
            } else if (actual < numeroID) {
                // Orden descendente: el código buscado está a la izquierda (es mayor).
                fin = medio - 1;
            } else {
                inicio = medio + 1;
            }
        }
        return -1;
    }

    // ===================== Responsable: Jostin Saca (R4) =====================
    public Pelicula buscarPorNumero(int numeroID) {
        int indice = buscarIndicePorNumero(numeroID);
        return indice == -1 ? null : peliculas.get(indice);
    }

    // R4: valida todos los valores antes de modificar; si falla uno, no cambia nada.
    // ===================== Responsable: Jostin Saca (R4) =====================
    public void editar(int numeroID, String titulo, String director, String genero, int anio) {
        int indice = buscarIndicePorNumero(numeroID);
        if (indice == -1) {
            throw new IllegalArgumentException("No existe una película con el código " + numeroID);
        }
        validarDatos(titulo, director, genero, anio);
        Pelicula p = peliculas.get(indice);
        p.setTitulo(titulo);
        p.setDirector(director);
        p.setGenero(genero);
        p.setAnio(anio);
    }


    // R4: elimina la película por completo de la colección.
    public boolean eliminar(int numeroID) {
        int indice = buscarIndicePorNumero(numeroID);
        if (indice == -1) {
            return false;
        }
        peliculas.remove(indice);
        return true;
    }
    // R5: suma exactamente uno al contador. Devuelve el contador actualizado.
    public int darMeGusta(int numeroID) {
        int indice = buscarIndicePorNumero(numeroID);
        if (indice == -1) {
            throw new IllegalArgumentException("Seleccione una película válida");
        }
        Pelicula p = peliculas.get(indice);
        p.darMeGusta();
        return p.getMeGusta();
    }

    // R3: coincidencia exacta (sensible a mayúsculas) en título o director.
    public ArrayList<Pelicula> buscarPorTextoExacto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un texto para buscar");
        }
        ArrayList<Pelicula> resultados = new ArrayList<>();
        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula p = peliculas.get(i);
            if (p.getTitulo().equals(texto) || p.getDirector().equals(texto)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    // R6: copia ordenada por me gusta descendente. La colección principal no se altera.
    public ArrayList<Pelicula> ranking() {
        ArrayList<Pelicula> copia = new ArrayList<>(peliculas);
        for (int i = 1; i < copia.size(); i++) {
            Pelicula actual = copia.get(i);
            int j = i - 1;
            while (j >= 0 && copia.get(j).getMeGusta() < actual.getMeGusta()) {
                copia.set(j + 1, copia.get(j));
                j--;
            }
            copia.set(j + 1, actual);
        }
        return copia;
    }
}