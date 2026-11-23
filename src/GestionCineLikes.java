public class GestionCineLikes {
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
