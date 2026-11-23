import javax.swing.*;
import java.text.ParseException;
import java.time.Year;
import java.util.ArrayList;

public class Ventana {
    private JPanel principal;
    private JTabbedPane tabbedPane1;
    private JLabel lblCantidad;
    private JList<Pelicula> lstCatalogo;
    private JSpinner spiCodigo;
    private JTextField txtTitulo;
    private JTextField txtDirector;
    private JComboBox<String> cboGenero;
    private JSpinner spiAnio;
    private JButton btnREgistrar;
    private JTextField txtTexto;
    private JButton btnBuscarTexto;
    private JTextArea txtResultados;
    private JSpinner spiBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtEditTitulo;
    private JTextField txtEditDirector;
    private JComboBox<String> cboEditGenero;
    private JSpinner spiEditAnio;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnMeGusta;
    private JLabel lblMegusta;
    private JButton btnRanking;
    private JList<String> lstRanking;

    private final GestionCineLikes gestion = new GestionCineLikes();
    private Integer numeroSeleccionado = null;
    private String ultimoTexto = null;

    public Ventana() {
        int anioActual = Year.now().getValue();
        cboGenero.setModel(new DefaultComboBoxModel<>(GestionCineLikes.GENEROS));
        cboEditGenero.setModel(new DefaultComboBoxModel<>(GestionCineLikes.GENEROS));
        spiAnio.setModel(new SpinnerNumberModel(anioActual, GestionCineLikes.ANIO_MINIMO, anioActual, 1));
        spiEditAnio.setModel(new SpinnerNumberModel(anioActual, GestionCineLikes.ANIO_MINIMO, anioActual, 1));
        spiCodigo.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
        spiBuscarCodigo.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
        txtResultados.setEditable(false);

        btnREgistrar.addActionListener(e -> registrar());
        btnBuscarTexto.addActionListener(e -> buscarTexto());
        btnBuscarCodigo.addActionListener(e -> buscarCodigo());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnMeGusta.addActionListener(e -> darMeGusta());
        btnRanking.addActionListener(e -> actualizarRanking());
        tabbedPane1.addChangeListener(e -> actualizarVistas());

        limpiarSeleccion();
        actualizarVistas();
    }

    private void registrar() {
        try {
            gestion.registrar(leerEntero(spiCodigo), txtTitulo.getText(), txtDirector.getText(),
                    (String) cboGenero.getSelectedItem(), leerEntero(spiAnio));
            JOptionPane.showMessageDialog(principal, "Película registrada");
            spiCodigo.setValue(0);
            txtTitulo.setText("");
            txtDirector.setText("");
            cboGenero.setSelectedIndex(0);
            actualizarVistas();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void buscarTexto() {
        try {
            ArrayList<Pelicula> resultados = gestion.buscarPorTextoExacto(txtTexto.getText());
            ultimoTexto = txtTexto.getText();
            mostrarResultados(resultados);
        } catch (IllegalArgumentException ex) {
            ultimoTexto = null;
            txtResultados.setText("");
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarResultados(ArrayList<Pelicula> resultados) {
        if (resultados.isEmpty()) {
            txtResultados.setText("No se encontraron coincidencias");
            return;
        }
        StringBuilder texto = new StringBuilder("Coincidencias: " + resultados.size() + "\n");
        for (Pelicula p : resultados) {
            texto.append(p).append("\n");
        }
        txtResultados.setText(texto.toString());
    }

    private void buscarCodigo() {
        try {
            int numero = leerEntero(spiBuscarCodigo);
            int indice = gestion.buscarIndicePorNumero(numero);
            if (indice == -1) {
                limpiarSeleccion();
                JOptionPane.showMessageDialog(principal, "No existe una película con el código " + numero);
                return;
            }
            cargarSeleccion(gestion.getPeliculas().get(indice));
        } catch (IllegalArgumentException ex) {
            limpiarSeleccion();
            mostrarError(ex.getMessage());
        }
    }

    private void guardar() {
        if (!haySeleccion()) {
            return;
        }
        try {
            gestion.editar(numeroSeleccionado, txtEditTitulo.getText(), txtEditDirector.getText(),
                    (String) cboEditGenero.getSelectedItem(), leerEntero(spiEditAnio));
            JOptionPane.showMessageDialog(principal, "Película actualizada");
            actualizarVistas();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        if (!haySeleccion()) {
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(principal,
                "¿Eliminar la película " + numeroSeleccionado + "?", "Eliminar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        boolean eliminada = gestion.eliminar(numeroSeleccionado);
        limpiarSeleccion();
        JOptionPane.showMessageDialog(principal, eliminada ? "Película eliminada" : "La película ya no existe");
        actualizarVistas();
    }

    private void darMeGusta() {
        if (!haySeleccion()) {
            return;
        }
        try {
            int contador = gestion.darMeGusta(numeroSeleccionado);
            lblMegusta.setText("Me gusta: " + contador);
            actualizarVistas();
        } catch (IllegalArgumentException ex) {
            limpiarSeleccion();
            mostrarError(ex.getMessage());
        }
    }

    private boolean haySeleccion() {
        if (numeroSeleccionado == null) {
            JOptionPane.showMessageDialog(principal, "Primero busque una película válida por su código");
            return false;
        }
        return true;
    }

    private void cargarSeleccion(Pelicula p) {
        numeroSeleccionado = p.getNumeroID();
        txtEditTitulo.setText(p.getTitulo());
        txtEditDirector.setText(p.getDirector());
        cboEditGenero.setSelectedItem(p.getGenero());
        spiEditAnio.setValue(p.getAnio());
        lblMegusta.setText("Me gusta: " + p.getMeGusta());
        habilitarEdicion(true);
    }

    private void limpiarSeleccion() {
        numeroSeleccionado = null;
        txtEditTitulo.setText("");
        txtEditDirector.setText("");
        cboEditGenero.setSelectedIndex(0);
        lblMegusta.setText("Me gusta: -");
        habilitarEdicion(false);
    }

    private void habilitarEdicion(boolean activo) {
        txtEditTitulo.setEnabled(activo);
        txtEditDirector.setEnabled(activo);
        cboEditGenero.setEnabled(activo);
        spiEditAnio.setEnabled(activo);
        btnGuardar.setEnabled(activo);
        btnEliminar.setEnabled(activo);
        btnMeGusta.setEnabled(activo);
    }

    private void actualizarVistas() {
        DefaultListModel<Pelicula> modelo = new DefaultListModel<>();
        for (Pelicula p : gestion.getPeliculas()) {
            modelo.addElement(p);
        }
        lstCatalogo.setModel(modelo);
        lblCantidad.setText("Cantidad de películas: " + gestion.getCantidad());

        if (ultimoTexto != null) {
            mostrarResultados(gestion.buscarPorTextoExacto(ultimoTexto));
        }
        if (numeroSeleccionado != null && gestion.buscarIndicePorNumero(numeroSeleccionado) == -1) {
            limpiarSeleccion();
        }
        actualizarRanking();
    }

    private void actualizarRanking() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Pelicula p : gestion.ranking()) {
            modelo.addElement(p.getNumeroID() + " - " + p.getTitulo() + " - Me gusta: " + p.getMeGusta());
        }
        lstRanking.setModel(modelo);
    }

    private int leerEntero(JSpinner spinner) {
        try {
            spinner.commitEdit();
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Ingrese un número entero válido");
        }
        return ((Number) spinner.getValue()).intValue();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(principal, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CineLikes");
            frame.setContentPane(new Ventana().principal);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}