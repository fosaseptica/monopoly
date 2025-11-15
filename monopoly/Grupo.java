package monopoly;

import java.util.ArrayList;
import partida.*;

// Clase Grupo
/*
Objetivo:
Representa un conjunto de casillas solares que comparten el mismo color. 
Sirve para determinar si un jugador posee todas las propiedades de un color,
lo cual afecta a alquileres y la posibilidad de construir edificios.
*/
public class Grupo {

    // Atributos
    private ArrayList<Casilla> miembros; // Lista que contiene todas las casillas que forman parte del grupo
    private String colorGrupo; // Color que identifica el grupo (por ejemplo, rojo, azul)
    private int numCasillas; // Número total de casillas que hay en el grupo

    // Constructor vacío
    public Grupo() {
        // Inicializa la lista de miembros vacía
        miembros = new ArrayList<>();
        // Inicializa el color como cadena vacía
        colorGrupo = "";
        // Inicializa el número de casillas a 0
        numCasillas = 0;
    }

    /* Constructor para grupos de DOS CASILLAS
    * Parámetros:
    *   cas1, cas2: las casillas que forman parte del grupo
    *   colorGrupo: el color del grupo
    */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this();                     // Crea el estado base
        anhadirCasilla(cas1);       // Añade la primera casilla
        anhadirCasilla(cas2);       // Añade la segunda casilla
        this.colorGrupo = colorGrupo; // Asigna el color del grupo
    }

    /* Constructor para grupos de TRES CASILLAS
    * Parámetros:
    *   cas1, cas2, cas3: las casillas que forman parte del grupo
    *   colorGrupo: el color del grupo
    */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        this();                     // Crea el estado base
        anhadirCasilla(cas1);       // Añade la primera casilla
        anhadirCasilla(cas2);       // Añade la segunda casilla
        anhadirCasilla(cas3);       // Añade la tercera casilla
        this.colorGrupo = colorGrupo; // Asigna el color del grupo
    }

    /* Método para añadir una casilla al grupo
    * Parámetro:
    *   miembro: la casilla que se quiere añadir al grupo
    */
    public void anhadirCasilla(Casilla miembro) {
        if (!miembros.contains(miembro)) { // Evita duplicados
            miembros.add(miembro); // Añade la casilla a la lista
            numCasillas++;         // Incrementa el contador de casillas del grupo
        }
    }

    /* Método que comprueba si un jugador es dueño de todas las casillas del grupo
    * Parámetro:
    *   jugador: jugador que queremos evaluar
    * Retorno:
    *   true si el jugador posee todas las casillas del grupo, false si falta alguna
    */
    public boolean esDuenhoGrupo(Jugador jugador) {
        // Recorre todas las casillas del grupo
        for (Casilla c : miembros) {
            // Si alguna casilla no pertenece al jugador, devuelve false
            if (c.getDuenho() != jugador) {
                return false;
            }
        }
        // Si todas las casillas pertenecen al jugador, devuelve true
        return true;
    }

    // Getters útiles (suelen hacer falta en impresión/lógica)
    public String getColorGrupo() {
        return colorGrupo;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

    public ArrayList<Casilla> getMiembros() {
        return miembros;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grupo " + colorGrupo + ": ");
        for (Casilla c : miembros) sb.append(c.getNombre()).append(" ");
        return sb.toString();
    }

    public boolean hayHipotecasEnGrupo() {
    for (Casilla c : miembros) {
        if (c.getHipotecada()) {
            return true;
        }
    }
    return false;
}

}

/* necesario esto en Casilla
// Getter para obtener el dueño de la casilla
public Jugador getDuenho() {
    return duenho;
}
*/
