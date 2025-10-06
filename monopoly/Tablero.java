package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

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
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoSur() {
        ArrayList<Casilla> sur = posiciones.get(2);
        
        sur.add(new Casilla("Salida", "Especial", 1, 0, banca));
        sur.add(new Casilla("Solar1", "Solar", 2, 600000, banca));
        sur.add(new Casilla("Solar2", "Solar", 3, 600000, banca));
        sur.add(new Casilla("Caja", "Comunidad", 4, banca));
        sur.add(new Casilla("Solar3", "Solar", 5, 1000000, banca));
        sur.add(new Casilla("Trans1", "Transporte", 6, 500000, banca));
        sur.add(new Casilla("Imp1", "Impuesto", 7, 2000000, banca));
        sur.add(new Casilla("Solar4", "Solar", 8, 1000000, banca));
        sur.add(new Casilla("Suerte", "Suerte", 9, banca));
        sur.add(new Casilla("Solar5", "Solar", 10, 1200000, banca));
        sur.add(new Casilla("Cárcel", "Especial", 11, banca));
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoOeste() {
    ArrayList<Casilla> oeste = posiciones.get(1);

        oeste.add(new Casilla("Solar6", "Solar", 12, 1400000, banca));
        oeste.add(new Casilla("Serv1", "Servicio", 13, 500000, banca));
        oeste.add(new Casilla("Solar7", "Solar", 14, 1400000, banca));
        oeste.add(new Casilla("Suerte", "Suerte", 15, banca));
        oeste.add(new Casilla("Solar8", "Solar", 16, 1600000, banca));
        oeste.add(new Casilla("Trans2", "Transporte", 17, 500000, banca));
        oeste.add(new Casilla("Solar9", "Solar", 18, 1800000, banca));
        oeste.add(new Casilla("Caja &H", "Comunidad", 19, banca));
        oeste.add(new Casilla("Solar10", "Solar", 20, 1800000, banca));
        oeste.add(new Casilla("Trans3", "Transporte", 21, 500000, banca));
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoNorte() {
    ArrayList<Casilla> norte = posiciones.get(2);

        norte.add(new Casilla("Solar11", "Solar", 22, 2200000, banca));
        norte.add(new Casilla("Solar12", "Solar", 23, 2200000, banca));
        norte.add(new Casilla("Suerte", "Suerte", 24, banca));
        norte.add(new Casilla("Solar13", "Solar", 25, 2200000, banca));
        norte.add(new Casilla("Solar14", "Solar", 26, 2400000, banca));
        norte.add(new Casilla("Trans4", "Transporte", 27, 500000, banca));
        norte.add(new Casilla("Solar15", "Solar", 28, 2600000, banca));
        norte.add(new Casilla("Caja &J", "Comunidad", 29, banca));
        norte.add(new Casilla("Solar16", "Solar", 30, 2600000, banca));
        norte.add(new Casilla("Suerte", "Suerte", 31, banca));
        norte.add(new Casilla("Solar17", "Solar", 32, 2800000, banca));
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
    ArrayList<Casilla> este = posiciones.get(3);

       este.add(new Casilla("Solar18", "Solar", 33, 3000000, banca));
        este.add(new Casilla("Solar19", "Solar", 34, 3000000, banca));
        este.add(new Casilla("Imp2", "Impuesto", 35, 2000000, banca));
        este.add(new Casilla("Solar20", "Solar", 36, 3200000, banca));
        este.add(new Casilla("Solar21", "Solar", 37, 3500000, banca));
        este.add(new Casilla("Solar22", "Solar", 38, 4000000, banca));
        este.add(new Casilla("Parking", "Especial", 39, banca));
        este.add(new Casilla("IrACárcel", "Especial", 40, banca));
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < posiciones.size(); i++) {
            sb.append("Lado ").append(i).append(": ");
            for (Casilla c : posiciones.get(i)) {
                sb.append(c.getNombre()).append(" | ");
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
}
