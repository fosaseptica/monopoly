package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    // === Códigos ANSI para color en consola (reset + 8 colores básicos) ===
    private static final String RESET   = "\u001B[0m";
    private static final String NEGRO   = "\u001B[30m";
    private static final String ROJO    = "\u001B[31m";
    private static final String VERDE   = "\u001B[32m";
    private static final String AMARILLO= "\u001B[33m";
    private static final String AZUL    = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CIAN    = "\u001B[36m";
    private static final String BLANCO  = "\u001B[37m";

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            posiciones.add(new ArrayList<>());
        }
        generarCasillas();
    }

    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> sur = posiciones.get(0);

        sur.add(new Casilla("Salida", "Especial", 1, banca));
        sur.add(new Casilla("Solar1", 2, 600000, 300000, 500000, 500000, 100000, 200000, banca));
        sur.add(new Casilla("Solar2", 3, 600000, 300000, 500000, 500000, 100000, 200000, banca));
        sur.add(new Casilla("Caja", "Comunidad", 4, banca));
        sur.add(new Casilla("Solar3", 5, 1000000, 500000, 500000, 500000, 100000, 200000, banca));
        sur.add(new Casilla("Trans1", "Transporte", 6, 500000, banca));
        sur.add(new Casilla("Imp1", "Impuesto", 7, 2000000, banca));
        sur.add(new Casilla("Solar4", 8, 1000000, 500000, 500000, 500000, 100000, 200000, banca));
        sur.add(new Casilla("Suerte", "Suerte", 9, banca));
        sur.add(new Casilla("Solar5", 10, 1200000, 600000, 500000, 500000, 100000, 200000, banca));
        sur.add(new Casilla("Cárcel", "Especial", 11, banca));
    }

    //Método para insertar las casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> oeste = posiciones.get(1);

        oeste.add(new Casilla("Solar6", 12, 1400000, 700000, 1000000, 1000000, 200000, 400000, banca));
        oeste.add(new Casilla("Serv1", "Servicio", 13, 500000, banca));
        oeste.add(new Casilla("Solar7", 14, 1400000, 700000, 1000000, 1000000, 200000, 400000, banca));
        oeste.add(new Casilla("Suerte", "Suerte", 15, banca));
        oeste.add(new Casilla("Solar8", 16, 1600000, 800000, 1000000, 1000000, 200000, 400000, banca));
        oeste.add(new Casilla("Trans2", "Transporte", 17, 500000, banca));
        oeste.add(new Casilla("Solar9", 18, 1800000, 900000, 1000000, 1000000, 200000, 400000, banca));
        oeste.add(new Casilla("Caja &H", "Comunidad", 19, banca));
        oeste.add(new Casilla("Solar10", 20, 1800000, 900000, 1000000, 1000000, 200000, 400000, banca));
        oeste.add(new Casilla("Trans3", "Transporte", 21, 500000, banca));
    }

    //Método que inserta casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> norte = posiciones.get(2);

        norte.add(new Casilla("Solar11", 22, 2200000, 1000000, 1000000, 1000000, 200000, 400000, banca));
        norte.add(new Casilla("Solar12", 23, 2200000, 1100000, 1500000, 1500000, 300000, 600000, banca));
        norte.add(new Casilla("Suerte", "Suerte", 24, banca));
        norte.add(new Casilla("Solar13", 25, 2200000, 1100000, 1500000, 1500000, 300000, 600000, banca));
        norte.add(new Casilla("Solar14", 26, 2400000, 1200000, 1500000, 1500000, 300000, 600000, banca));
        norte.add(new Casilla("Trans4", "Transporte", 27, 500000, banca));
        norte.add(new Casilla("Solar15", 28, 2600000, 1300000, 1500000, 1500000, 300000, 600000, banca));
        norte.add(new Casilla("Caja &J", "Comunidad", 29, banca));
        norte.add(new Casilla("Solar16", 30, 2600000, 1300000, 1500000, 1500000, 300000, 600000, banca));
        norte.add(new Casilla("Suerte", "Suerte", 31, banca));
        norte.add(new Casilla("Solar17", 32, 2800000, 1400000, 1500000, 1500000, 300000, 600000, banca));
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> este = posiciones.get(3);

        este.add(new Casilla("Solar18", 33, 3000000, 1500000, 2000000, 2000000, 400000, 800000, banca));
        este.add(new Casilla("Solar19", 34, 3000000, 1500000, 2000000, 2000000, 400000, 800000, banca));
        este.add(new Casilla("Imp2", "Impuesto", 35, 2000000, banca));
        este.add(new Casilla("Solar20", 36, 3200000, 1600000, 2000000, 2000000, 400000, 800000, banca));
        este.add(new Casilla("Solar21", 37, 3500000, 1750000, 2000000, 2000000, 400000, 800000, banca));
        este.add(new Casilla("Solar22", 38, 4000000, 2000000, 2000000, 2000000, 400000, 800000, banca));
        este.add(new Casilla("Parking", "Especial", 39, banca));
        este.add(new Casilla("IrACárcel", "Especial", 40, banca));
    }

    // === Helper: devuelve el nombre coloreado si es SolarX ===
    private String colorizar(Casilla c) {
        String nombre = c.getNombre();
        // Coloreamos solo solares, el resto se devuelve tal cual
        if (!nombre.startsWith("Solar")) return nombre;

        // Extraemos el número del solar (e.g., "Solar17" -> "17")
        String digitos = nombre.replaceAll("\\D", "");
        if (digitos.isEmpty()) return nombre;
        int n = Integer.parseInt(digitos);

        String color = colorParaSolar(n);
        return color + nombre + RESET;
    }

    
    private String colorParaSolar(int n) {
        if (n >= 1  && n <= 2)  return MARRON();
        if (n >= 3  && n <= 5)  return CIAN;          // Celeste
        if (n >= 6  && n <= 7)  return MAGENTA;       // Rosa
        if (n >= 8  && n <= 10) return AMARILLO;      // Naranja/Amarillo
        if (n >= 11 && n <= 13) return ROJO;          // Rojo
        if (n >= 14 && n <= 16) return AMARILLO_FUERTE();
        if (n >= 17 && n <= 19) return VERDE;         // Verde
        if (n >= 20 && n <= 22) return AZUL;          // Azul oscuro
        return BLANCO; // fallback
    }

    private String MARRON() {
        return ROJO;
    }
    private String AMARILLO_FUERTE() {
        return AMARILLO;
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < posiciones.size(); i++) {
            sb.append("Lado ").append(i).append(": ");
            for (Casilla c : posiciones.get(i)) {
                // ANTES: sb.append(c.getNombre()).append(" | ");
                sb.append(colorizar(c)).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla c : lado) {
                if (c.getNombre().equalsIgnoreCase(nombre)) {
                    return c;
                }
            }
        }
        return null;
    }

    //Método añadido necesario para acceder a todas las casillas desde otras clases.
    public ArrayList<ArrayList<Casilla>> getCasillas() {
        return posiciones;
    }
}
