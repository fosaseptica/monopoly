package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.
    private float boteParking = 0f;

    // === ANSI ===
    private static final String RESET     = "\u001B[0m";
    private static final String SUBRAYO   = "\u001B[4m";
    private static final String ROJO      = "\u001B[31m";
    private static final String VERDE     = "\u001B[32m";
    private static final String AMARILLO  = "\u001B[33m";
    private static final String AZUL      = "\u001B[34m";
    private static final String MAGENTA   = "\u001B[35m";
    private static final String CIAN      = "\u001B[36m";

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        for (int i = 0; i < 4; i++) posiciones.add(new ArrayList<>());
        generarCasillas();
    }

    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> norte = posiciones.get(2);
        norte.add(new Casilla("Parking", "Especial", 20, banca));
        norte.add(new Casilla("Solar12", 21, 2200000f, 1100000f, 1500000f, 1500000f, 300000f, 600000f, banca));
        norte.add(new Casilla("Suerte", "Suerte", 22, banca));
        norte.add(new Casilla("Solar13", 23, 2200000f, 1100000f, 1500000f, 1500000f, 300000f, 600000f, banca));
        norte.add(new Casilla("Solar14", 24, 2400000f, 1200000f, 1500000f, 1500000f, 300000f, 600000f, banca));
        norte.add(new Casilla("Trans3", "Transporte", 25, 500000f, banca));
        norte.add(new Casilla("Solar15", 26, 2600000f, 1300000f, 1500000f, 1500000f, 300000f, 600000f, banca));
        norte.add(new Casilla("Solar16", 27, 2600000f, 1300000f, 1500000f, 1500000f, 300000f, 600000f, banca));
        norte.add(new Casilla("Serv2", "Servicio", 28, 500000f, banca));
        norte.add(new Casilla("Solar17", 29, 2800000f, 1400000f, 1500000f, 1500000f, 300000f, 600000f, banca));
        norte.add(new Casilla("IrACárcel", "Especial", 30, banca));
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> sur = posiciones.get(0);
        sur.add(new Casilla("Salida", "Especial", 0, 0, banca));
        sur.add(new Casilla("Solar1", 1, 600000f, 300000f, 500000f, 500000f, 100000f, 200000f, banca));
        sur.add(new Casilla("Caja", "Comunidad", 2, banca));
        sur.add(new Casilla("Solar2", 3, 600000f, 300000f, 500000f, 500000f, 100000f, 200000f, banca));
        sur.add(new Casilla("Imp1", 4, 2000000f, banca));
        sur.add(new Casilla("Trans1", "Transporte", 5, 500000f, banca));
        sur.add(new Casilla("Solar3", 6, 1000000f, 500000f, 500000f, 500000f, 100000f, 200000f, banca));
        sur.add(new Casilla("Suerte", "Suerte", 7, banca));
        sur.add(new Casilla("Solar4", 8, 1000000f, 500000f, 500000f, 500000f, 100000f, 200000f, banca));
        sur.add(new Casilla("Solar5", 9, 1200000f, 600000f, 500000f, 500000f, 100000f, 200000f, banca));
        sur.add(new Casilla("Cárcel", "Especial", 10, banca));
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> oeste = posiciones.get(1);
        oeste.add(new Casilla("Solar6", 11, 1400000f, 700000f, 1000000f, 1000000f, 200000f, 400000f, banca));
        oeste.add(new Casilla("Serv1", "Servicio", 12, 500000f, banca));
        oeste.add(new Casilla("Solar7", 13, 1400000f, 700000f, 1000000f, 1000000f, 200000f, 400000f, banca));
        oeste.add(new Casilla("Solar8", 14, 1600000f, 800000f, 1000000f, 1000000f, 200000f, 400000f, banca));
        oeste.add(new Casilla("Trans2", "Transporte", 15, 500000f, banca));
        oeste.add(new Casilla("Solar9", 16, 1800000f, 900000f, 1000000f, 1000000f, 200000f, 400000f, banca));
        oeste.add(new Casilla("Caja", "Comunidad", 17, banca));
        oeste.add(new Casilla("Solar10", 18, 1800000f, 900000f, 1000000f, 1000000f, 200000f, 400000f, banca));
        oeste.add(new Casilla("Solar11", 19, 2200000f, 1000000f, 1000000f, 1000000f, 200000f, 400000f, banca));
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> este = posiciones.get(3);
        este.add(new Casilla("Solar18", 31, 3000000f, 1500000f, 2000000f, 2000000f, 400000f, 800000f, banca));
        este.add(new Casilla("Solar19", 32, 3000000f, 1500000f, 2000000f, 2000000f, 400000f, 800000f, banca));
        este.add(new Casilla("Caja", "Comunidad", 33, banca));
        este.add(new Casilla("Solar20", 34, 3200000f, 1600000f, 2000000f, 2000000f, 400000f, 800000f, banca));
        este.add(new Casilla("Trans4", "Transporte", 35, 500000f, banca));
        este.add(new Casilla("Suerte", "Suerte", 36, banca));
        este.add(new Casilla("Solar21", 37, 3500000f, 1750000f, 2000000f, 2000000f, 400000f, 800000f, banca));
        este.add(new Casilla("Imp2", 38, 2000000f, banca));
        este.add(new Casilla("Solar22", 39, 4000000f, 2000000f, 2000000f, 2000000f, 400000f, 800000f, banca));
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        ArrayList<Casilla> sur   = posiciones.get(0);
        ArrayList<Casilla> oeste = posiciones.get(1);
        ArrayList<Casilla> norte = posiciones.get(2);
        ArrayList<Casilla> este  = posiciones.get(3);

        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┐\n");
        sb.append("│");
        for (Casilla c : norte) sb.append(formatearCasilla(c)).append("│");
        sb.append("\n");

        for (int i = 0; i < 9; i++) {
            sb.append("├─────────┤                                                                                         ├─────────┤\n");
            sb.append("│");
            int idxOeste = oeste.size() - 1 - i;
            sb.append(formatearCasilla(oeste.get(idxOeste)));
            sb.append("│");
            sb.append("                                                                                         ");
            sb.append("│");
            sb.append(formatearCasilla(este.get(i)));
            sb.append("│\n");
        }

        sb.append("├─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┤\n");
        sb.append("│");
        for (int i = sur.size() - 1; i >= 0; i--) sb.append(formatearCasilla(sur.get(i))).append("│");
        sb.append("\n");
        sb.append("└─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┘\n");
        return sb.toString();
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        for (ArrayList<Casilla> lado : posiciones)
            for (Casilla c : lado)
                if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        return null;
    }

    // ---- auxiliares internas ----
    private String formatearCasilla(Casilla c) {
        String nombre = c.getNombre();  // p.ej. "Solar12"
        String texto  = nombre;

        // Añadir avatares si los hay (p.ej. "Solar9 &H")
        if (!c.getAvatares().isEmpty()) {
            StringBuilder s = new StringBuilder(nombre).append(" &");
            for (Avatar a : c.getAvatares()) s.append(a.getId());
            texto = s.toString();
        }

        // Recortar a 9 y padear a 9 para el dibujo
        if (texto.length() > 9) texto = texto.substring(0, 9);
        String padded = String.format("%-9s", texto);

        // Aplicar color/estilo SOLO al nombre de solares, sin romper el ancho de celda
        if ("Solar".equalsIgnoreCase(c.getTipo())) {
            String coloreado = colorParaSolar(nombre) + SUBRAYO + nombre + RESET;
            // Sustituir la primera aparición del nombre dentro del padded
            padded = padded.replaceFirst(java.util.regex.Pattern.quote(nombre), coloreado);
        }
        return padded;
    }

    // Colores por grupos 1–22
    private String colorParaSolar(String nombreSolar) {
        String d = nombreSolar.replaceAll("\\D", "");
        if (d.isEmpty()) return RESET;
        int n = Integer.parseInt(d);

        if (n <= 2)       return AMARILLO; // (marrón aproximado)
        else if (n <= 5)  return CIAN;     // cian
        else if (n <= 7)  return MAGENTA;  // rosa
        else if (n <= 10) return AMARILLO; // naranja aproximada
        else if (n <= 13) return ROJO;     // rojo
        else if (n <= 16) return AMARILLO; // amarillo
        else if (n <= 19) return VERDE;    // verde
        else              return AZUL;     // azul
    }

    // Necesario para Parking 
    public void anadirAlBote(float cantidad){ if (cantidad > 0) boteParking += cantidad; }
    public float cobrarBote(){ float b = boteParking; boteParking = 0f; return b; }
    public float getBote(){ return boteParking; }

    // Acceso desde otras clases
    public ArrayList<ArrayList<Casilla>> getCasillas() { return posiciones; }
}
