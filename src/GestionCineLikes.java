public class GestionCineLikes {
    // R4: búsqueda binaria sobre la colección descendente. Devuelve el índice o -1.
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
    public Pelicula buscarPorNumero(int numeroID) {
        int indice = buscarIndicePorNumero(numeroID);
        return indice == -1 ? null : peliculas.get(indice);
    }
    // R4: valida todos los valores antes de modificar; si falla uno, no cambia nada.

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
