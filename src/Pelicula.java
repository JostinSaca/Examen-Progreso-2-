public class Pelicula {

    // R4: setters solo para los campos editables (no numeroID ni meGusta).
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    // R5: único medio para cambiar meGusta. Solo suma uno por clic válido.
    public void darMeGusta() {
        meGusta++;
    }
    @Override
    public String toString() {
        return numeroID + " - " + titulo + " (" + anio + ") - " + director
                + " [" + genero + "] - Me gusta: " + meGusta;
    }
}
    }

