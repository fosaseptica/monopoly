package monopoly;

import partida.*;
import java.util.ArrayList;


class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.

    //Constructor vacío.
    public Grupo() {
        miembros = new ArrayList<>();
        colorGrupo = "";
        numCasillas = 0;
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        miembros = new ArrayList<>();
        miembros.add(cas1);
        miembros.add(cas2);
        this.colorGrupo = colorGrupo;
        numCasillas = 2;
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        miembros = new ArrayList<>();
        miembros.add(cas1);
        miembros.add(cas2);
        miembros.add(cas3);
        this.colorGrupo = colorGrupo;
        numCasillas = 3;
    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
    * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirCasilla(Casilla miembro) {
        miembros.add(miembro);
        numCasillas++;
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
    * Parámetro: jugador que se quiere evaluar.
    * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for(Casilla c : miembros) {
            if(c.getDuenho() != jugador) {
                return false;//alguna casilla no es del jugador
            }
        }
    return true;// todas las casillas son del jugador
    }

    /*necesario esto en casilla
    // Getter para obtener el dueño de la casilla
    public Jugador getDuenho() {
        return duenho;
    }
    */

}

