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
class Grupo {

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
        miembros = new ArrayList<>(); // Crea la lista de miembros
        miembros.add(cas1);           // Añade la primera casilla
        miembros.add(cas2);           // Añade la segunda casilla
        this.colorGrupo = colorGrupo; // Asigna el color del grupo
        numCasillas = 2;              // Indica que el grupo tiene 2 casillas
    }

    /* Constructor para grupos de TRES CASILLAS
    * Parámetros:
    *   cas1, cas2, cas3: las casillas que forman parte del grupo
    *   colorGrupo: el color del grupo
    */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        miembros = new ArrayList<>(); // Crea la lista de miembros
        miembros.add(cas1);           // Añade la primera casilla
        miembros.add(cas2);           // Añade la segunda casilla
        miembros.add(cas3);           // Añade la tercera casilla
        this.colorGrupo = colorGrupo; // Asigna el color del grupo
        numCasillas = 3;              // Indica que el grupo tiene 3 casillas
    }

    /* Método para añadir una casilla al grupo
    * Parámetro:
    *   miembro: la casilla que se quiere añadir al grupo
    */
    public void anhadirCasilla(Casilla miembro) {
        miembros.add(miembro); // Añade la casilla a la lista
        numCasillas++;         // Incrementa el contador de casillas del grupo
    }

    /* Método que comprueba si un jugador es dueño de todas las casillas del grupo
    * Parámetro:
    *   jugador: jugador que queremos evaluar
    * Retorno:
    *   true si el jugador posee todas las casillas del grupo, false si falta alguna
    */
    public boolean esDuenhoGrupo(Jugador jugador) {
        // Recorre todas las casillas del grupo
        for(Casilla c : miembros) {
            // Si alguna casilla no pertenece al jugador, devuelve false
            if(c.getDuenho() != jugador) {
                return false;
            }
        }
        // Si todas las casillas pertenecen al jugador, devuelve true
        return true;
    }

}
