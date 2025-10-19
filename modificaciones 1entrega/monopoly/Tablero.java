package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import partida.*;

public class Tablero {
    private ArrayList<ArrayList<Casilla>> posiciones;
    private HashMap<String, Grupo> grupos;
    private Jugador banca;

    // Bote del Parking
    private float boteParking = 0f;

    // ANSI (colores)
    private static final String RESET   = "\u001B[0m";
    private static final String ROJO    = "\u001B[31m";
    private static final String VERDE   = "\u001B[32m";
    private static final String AMARILLO= "\u001B[33m";
    private static final String AZUL    = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CIAN    = "\u001B[36m";

    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        for (int i = 0; i < 4; i++) posiciones.add(new ArrayList<>());
        generarCasillas();
    }

    private void generarCasillas() {
        insertarLadoSur();
        insertarLadoOeste();
        insertarLadoNorte();
        insertarLadoEste();
    }

    // ----- Creación de casillas  -----

    private void insertarLadoSur() {
        ArrayList<Casilla> sur = posiciones.get(0);
        sur.add(new Casilla("Salida", "Especial", 0, 0, banca));
        sur.add(new Casilla("Solar1", "Solar", 1, 600000, banca));
        sur.add(new Casilla("Caja", "Comunidad", 2, banca));
        sur.add(new Casilla("Solar2", "Solar", 3, 600000, banca));
        sur.add(new Casilla("Imp1", "Impuesto", 4, 2000000, banca));
        sur.add(new Casilla("Trans1", "Transporte", 5, 500000, banca));
        sur.add(new Casilla("Solar3", "Solar", 6, 1000000, banca));
        sur.add(new Casilla("Suerte", "Suerte", 7, banca));
        sur.add(new Casilla("Solar4", "Solar", 8, 1000000, banca));
        sur.add(new Casilla("Solar5", "Solar", 9, 1200000, banca));
        sur.add(new Casilla("Cárcel", "Especial", 10, banca));
    }

    private void insertarLadoOeste() {
        ArrayList<Casilla> oeste = posiciones.get(1);
        oeste.add(new Casilla("Solar6", "Solar", 11, 1400000, banca));
        oeste.add(new Casilla("Serv1", "Servicio", 12, 500000, banca));
        oeste.add(new Casilla("Solar7", "Solar", 13, 1400000, banca));
        oeste.add(new Casilla("Solar8", "Solar", 14, 1600000, banca));
        oeste.add(new Casilla("Trans2", "Transporte", 15, 500000, banca));
        oeste.add(new Casilla("Solar9", "Solar", 16, 1800000, banca));
        oeste.add(new Casilla("Caja", "Comunidad", 17, banca));
        oeste.add(new Casilla("Solar10", "Solar", 18, 1800000, banca));
        oeste.add(new Casilla("Solar11", "Solar", 19, 2200000, banca));
    }

    private void insertarLadoNorte() {
        ArrayList<Casilla> norte = posiciones.get(2);
        norte.add(new Casilla("Parking", "Especial", 20, banca));
        norte.add(new Casilla("Solar12", "Solar", 21, 2200000, banca));
        norte.add(new Casilla("Suerte", "Suerte", 22, banca));
        norte.add(new Casilla("Solar13", "Solar", 23, 2200000, banca));
        norte.add(new Casilla("Solar14", "Solar", 24, 2400000, banca));
        norte.add(new Casilla("Trans3", "Transporte", 25, 500000, banca));
        norte.add(new Casilla("Solar15", "Solar", 26, 2600000, banca));
        norte.add(new Casilla("Solar16", "Solar", 27, 2600000, banca));
        norte.add(new Casilla("Serv2", "Servicio", 28, 500000, banca));
        norte.add(new Casilla("Solar17", "Solar", 29, 2800000, banca));
        norte.add(new Casilla("IrACárcel", "Especial", 30, banca)); // usa la misma etiqueta que tu menú
    }

    private void insertarLadoEste() {
        ArrayList<Casilla> este = posiciones.get(3);
        este.add(new Casilla("Solar18", "Solar", 31, 3000000, banca));
        este.add(new Casilla("Solar19", "Solar", 32, 3000000, banca));
        este.add(new Casilla("Caja", "Comunidad", 33, banca));
        este.add(new Casilla("Solar20", "Solar", 34, 3200000, banca));
        este.add(new Casilla("Trans4", "Transporte", 35, 500000, banca));
        este.add(new Casilla("Suerte", "Suerte", 36, banca));
        este.add(new Casilla("Solar21", "Solar", 37, 3500000, banca));
        este.add(new Casilla("Imp2", "Impuesto", 38, 2000000, banca));
        este.add(new Casilla("Solar22", "Solar", 39, 4000000, banca));
    }

    // ----- Pintado ASCII rectangular (11 celdas arriba/abajo, 9 laterales) -----

    @Override
    public String toString() {
        ArrayList<Casilla> sur   = posiciones.get(0);
        ArrayList<Casilla> oeste = posiciones.get(1);
        ArrayList<Casilla> norte = posiciones.get(2);
        ArrayList<Casilla> este  = posiciones.get(3);

        StringBuilder sb = new StringBuilder();

        // Lado norte (izq->der)
        sb.append("┌─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┐\n");
        sb.append("│");
        for (Casilla c : norte) sb.append(formatearCasilla(c)).append("│");
        sb.append("\n");

        // Laterales: oeste (arriba->abajo invertido), centro vacío, este (arriba->abajo normal)
        for (int i = 0; i < 9; i++) {
            sb.append("├─────────┤                                                                                         ├─────────┤\n");
            sb.append("│");
            int idxOeste = oeste.size() - 1 - i; // 8..0
            sb.append(formatearCasilla(oeste.get(idxOeste)));
            sb.append("│");
            sb.append("                                                                                         ");
            sb.append("│");
            sb.append(formatearCasilla(este.get(i)));
            sb.append("│\n");
        }

        // Separador antes del sur
        sb.append("├─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┤\n");

        // Lado sur (der->izq para que 'Salida' quede en la esquina inferior derecha)
        sb.append("│");
        for (int i = sur.size() - 1; i >= 0; i--) sb.append(formatearCasilla(sur.get(i))).append("│");
        sb.append("\n");
        sb.append("└─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┘\n");

        return sb.toString();
    }

    
    private String formatearCasilla(Casilla c) {
        String nombre = c.getNombre();
        String texto = nombre;

        if (!c.getAvatares().isEmpty()) {
            StringBuilder s = new StringBuilder(nombre).append("&");
            for (Avatar a : c.getAvatares()) s.append(a.getId());
            texto = s.toString();
        }

        if (texto.length() > 9) texto = texto.substring(0, 9);
        String padded = String.format("%-9s", texto);

        if ("Solar".equalsIgnoreCase(c.getTipo())) {
            String colored = colorNombreSolar(nombre);
            padded = padded.replaceFirst(java.util.regex.Pattern.quote(nombre), colored);
        }
        return padded;
    }

    // Colores por grupos
    private String colorNombreSolar(String nombreSolar) {
        String d = nombreSolar.replaceAll("\\D", "");
        if (d.isEmpty()) return nombreSolar;
        int n = Integer.parseInt(d);

        String color;
        if (n <= 2)       color = AMARILLO; // amarillo casi marron
        else if (n <= 5)  color = CIAN;     // celeste
        else if (n <= 7)  color = MAGENTA;  // rosa
        else if (n <= 10) color = AMARILLO; // naranja casi amarillo
        else if (n <= 13) color = ROJO;     // rojo
        else if (n <= 16) color = AMARILLO; // amarillo
        else if (n <= 19) color = VERDE;    // verde
        else              color = AZUL;     // azul
        return color + nombreSolar + RESET;
    }

    // ----- Búsquedas / acceso -----

    public Casilla encontrar_casilla(String nombre){
        for (ArrayList<Casilla> lado : posiciones)
            for (Casilla c : lado)
                if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        return null;
    }

    public ArrayList<ArrayList<Casilla>> getCasillas() {
        return posiciones;
    }

    // ----- Parking -----

    public void anadirAlBote(float cantidad) {
        if (cantidad > 0) boteParking += cantidad;
    }

    public float cobrarBote() {
        float b = boteParking;
        boteParking = 0f;
        return b;
    }

    public float getBote() {
        return boteParking;
    }
}
