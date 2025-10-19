package partida;

import monopoly.*;

import java.util.ArrayList;
import java.util.Random;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    public Avatar() {
    }

    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo=tipo;
        this.jugador=jugador;
        this.lugar=lugar;
        generarId(avCreados);
        avCreados.add(this);
        lugar.anhadirAvatar(this);
        
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        Casilla casillaAnterior = lugar;
        int posActual = casillaAnterior.getPosicion() - 1; 
        int nuevaPos = (posActual + valorTirada) % 40;  // 40 casillas

        // Determinar lado e índice dentro del lado
        int lado, index;
        if (nuevaPos < 11) {            // lado superior
            lado = 0;
            index = nuevaPos;
        } else if (nuevaPos < 20) {     // lado izquierdo
            lado = 1;
            index = nuevaPos - 11;
        } else if (nuevaPos < 31) {     // lado inferior
            lado = 2;
            index = nuevaPos - 20;
        } else {                        // lado derecho
            lado = 3;
            index = nuevaPos - 31;
        }

        // Asignar la nueva casilla
        Casilla nuevaCasilla = casillas.get(lado).get(index);
        casillaAnterior.eliminarAvatar(this);
        this.lugar = nuevaCasilla;
        lugar.anhadirAvatar(this);
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        Random rand = new Random();
        String candidato;
        boolean repetido;

        do {
            candidato = String.valueOf((char) ('A' + rand.nextInt(26)));
            repetido = false;
            for (Avatar a : avCreados) {
                if (a.getId().equals(candidato)) {
                    repetido = true;
                    break;
                }
            }
        } while (repetido);

        this.id = candidato;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }
    @Override
    public String toString() {
        return "Avatar " + id + " (" + tipo + ")";
    }

}
