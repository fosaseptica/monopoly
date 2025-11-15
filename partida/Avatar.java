package partida; // Paquete donde se encuentra la clase Avatar

import java.util.ArrayList; // Importa clases del paquete monopoly (como Casilla, Tablero, etc.)
import java.util.Random; // Para manejar listas dinámicas
import monopoly.*; // Para generar el ID aleatorio

public class Avatar { // Clase que representa un avatar en el juego (una ficha)

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    public Avatar() {
        // No hace nada especial, solo deja el objeto inicializado con valores por defecto (null)
    }

    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo; // Asigna el tipo (Sombrero, Esfinge, etc.)
        this.jugador = jugador; // Asigna el jugador dueño del avatar
        this.lugar = lugar; // Coloca el avatar en una casilla inicial
        generarId(avCreados); // Genera un ID único (una letra)
        avCreados.add(this); // Añade el nuevo avatar a la lista de avatares creados
        lugar.anhadirAvatar(this); // Inserta el avatar en la lista de avatares de la casilla actual
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.

    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siempre es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        Casilla casillaAnterior = lugar; // Guarda la casilla actual
        int posActual = casillaAnterior.getPosicion(); // Obtiene la posición actual (0..39)
        int nuevaPos = (posActual + valorTirada) % 40; // Calcula la nueva posición (cíclica en el tablero)


         // Verificar si pasa por salida
        if (posActual + valorTirada >= 40) {
            this.jugador.sumarFortuna(2000000f);
            this.jugador.incrementarVueltas();
            System.out.println("¡Has pasado por la salida! Recibes 2.000.000€");
        }

        // Determina en qué lado del tablero está la nueva posición:
        int lado, index;
        if (nuevaPos <= 10) {          // Lado sur (casillas 0 a 10)
            lado = 0; index = nuevaPos;
        } else if (nuevaPos <= 19) {   // Lado oeste (casillas 11 a 19)
            lado = 1; index = nuevaPos - 11;
        } else if (nuevaPos <= 30) {   // Lado norte (casillas 20 a 30)
            lado = 2; index = nuevaPos - 20;
        } else {                       // Lado este (casillas 31 a 39)
            lado = 3; index = nuevaPos - 31;
        }

        Casilla nuevaCasilla = casillas.get(lado).get(index); // Obtiene la nueva casilla a la que se moverá
        casillaAnterior.eliminarAvatar(this); // Elimina el avatar de la casilla anterior
        this.lugar = nuevaCasilla; // Actualiza el atributo "lugar" del avatar
        lugar.anhadirAvatar(this); // Añade el avatar a la nueva casilla
            
        //System.out.println("Avatar " + this.id + " se mueve de " + casillaAnterior.getNombre() + " a " + nuevaCasilla.getNombre());
    }

    /**
     * Mueve el avatar directamente a una casilla específica (sin contar posiciones)
     * @param casillas El tablero completo con todas las casillas
     * @param destino La casilla destino a la que mover el avatar
     */
    public void moverAvatarDirecto(ArrayList<ArrayList<Casilla>> casillas, Casilla destino) {
        // Remover de la casilla actual
        Casilla actual = this.lugar;
        if (actual != null) {
            actual.eliminarAvatar(this);
            System.out.println("Avatar " + this.id + " sale de " + actual.getNombre());
        }
        
        // Mover a la nueva casilla
        this.lugar = destino;
        destino.anhadirAvatar(this);
        
        System.out.println("Avatar " + this.id + " se mueve directamente a " + destino.getNombre());
        
        // Verificar si pasó por salida (para casos donde el movimiento es hacia atrás)
        if (actual != null) {
            int posActual = actual.getPosicion();
            int posDestino = destino.getPosicion();
            
            // Si se mueve hacia atrás y cruza la posición 0, pasa por salida
            if (posDestino < posActual && (posDestino <= 10 && posActual >= 30)) {
                this.jugador.sumarFortuna(2000000f);
                this.jugador.incrementarVueltas();
                System.out.println("¡Has pasado por la salida! Recibes 2.000.000€");
            }
        }
    }



    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        Random rand = new Random(); // Generador de números aleatorios
        String candidato; // Posible ID candidato
        boolean repetido; // Indica si ya está en uso
        do {
            // Genera una letra mayúscula aleatoria entre 'A' y 'Z'
            candidato = String.valueOf((char) ('A' + rand.nextInt(26)));
            repetido = false;
            // Comprueba si ya existe un avatar con ese mismo ID
            for (Avatar a : avCreados) {
                if (a.getId().equals(candidato)) { 
                    repetido = true; 
                    break; 
                }
            }
        } while (repetido); // Si el ID ya existe, repite el proceso
        this.id = candidato; // Asigna el ID final al avatar
    }

    // Métodos getter (acceso a los atributos)
    public String getId() { return id; } // Devuelve el ID (la letra)
    public String getTipo() { return tipo; } // Devuelve el tipo de avatar
    public Jugador getJugador() { return jugador; } // Devuelve el jugador al que pertenece
    public Casilla getLugar() { return lugar; } // Devuelve la casilla donde está el avatar actualmente
    public void setLugar(Casilla lugar) { this.lugar = lugar; } // Permite cambiar manualmente su casilla
}
