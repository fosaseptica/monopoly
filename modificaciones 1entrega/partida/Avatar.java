package partida; // Define el paquete donde está la clase

import java.util.ArrayList; // Importa la clase ArrayList para manejar listas dinámicas
import java.util.Random;    // Importa la clase Random para generar números aleatorios
import monopoly.*;          // Importa todas las clases del paquete monopoly


public class Avatar { // Declara la clase Avatar

    //Atributos
    private String id; // Identificador único del avatar: una letra generada aleatoriamente
    private String tipo; // Tipo de avatar (Sombrero, Esfinge, Pelota, Coche)
    private Jugador jugador; // Jugador al que pertenece este avatar
    private Casilla lugar; // Casilla del tablero donde se encuentra el avatar

    //Constructor vacío
    public Avatar() {
        // Constructor por defecto, no hace nada
    }

    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar donde estará, y lista de avatares creados
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo=tipo; // Asigna el tipo pasado como parámetro al atributo tipo. // Sintaxis: this.atributo = valor;
        this.jugador=jugador; // Asigna el jugador pasado como parámetro al atributo jugador. // Sintaxis: this.atributo = valor;
        this.lugar=lugar; // Asigna la casilla pasada al atributo lugar. // Sintaxis: this.atributo = valor;

        generarId(avCreados); // Llama al método privado para generar un ID único para el avatar. // Sintaxis: nombreMetodo(parametros);

        avCreados.add(this); // Añade este objeto avatar a la lista de avatares creados. // Sintaxis: lista.add(this); 'this' hace referencia al objeto actual

        lugar.anhadirAvatar(this); // Añade este avatar a la casilla en la que empieza. // Sintaxis: objeto.metodo(this); 'this' es el objeto actual

        /*
         * El proceso es algo así:
         * Reserva espacio en memoria.
         * Asigna los valores de los atributos (tipo, jugador, lugar, etc.).
         * Ejecuta el código del constructor.
         * Devuelve la referencia al objeto creado.
         * Pero si durante el paso 3, mientras el constructor aún se está ejecutando, haces algo como: avCreados.add(this);
         * Entonces estás entregando una referencia a un objeto que aún no está completamente inicializado aun en construccion a o otro sitio (en este caso a ) por eso da warnings
         */
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.

    /*Método que permite mover a un avatar a una casilla concreta. 
    * Parámetros: array de casillas del tablero y número de casillas a moverse
     */
    /*
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        int posActual = lugar.getPosicion() - 1;  // Obtiene la posición actual en el tablero (0-index). // Sintaxis: variable = objeto.metodo() - 1;
        int nuevaPos = (posActual + valorTirada) % 40;  // Calcula la nueva posición con módulo 40 para que sea circular. // Sintaxis: operador módulo %

        // Determinar lado e índice dentro del lado
        int lado, index; // Variables para saber en qué lado del tablero y posición dentro del lado

        if (nuevaPos < 11) {            // Si está en el lado superior
            lado = 0;                    // Lado superior es 0
            index = nuevaPos;            // Índice dentro del lado es la misma posición
        } else if (nuevaPos < 20) {     // Si está en el lado izquierdo
            lado = 1;                    // Lado izquierdo es 1
            index = nuevaPos - 11;       // Ajusta el índice relativo al inicio del lado
        } else if (nuevaPos < 31) {     // Si está en el lado inferior
            lado = 2;                    // Lado inferior es 2
            index = nuevaPos - 20;       // Ajusta índice relativo
        } else {                        // Si está en el lado derecho
            lado = 3;                    // Lado derecho es 3
            index = nuevaPos - 31;       // Ajusta índice relativo
        }

        // Asignar la nueva casilla
        this.lugar = casillas.get(lado).get(index); // Cambia el atributo lugar al nuevo objeto Casilla. // Sintaxis: this.atributo = lista.get(i).get(j);
        lugar.anhadirAvatar(this); // Añade el avatar a la casilla nueva. // Sintaxis: objeto.metodo(this); 'this' es el objeto actual
    }
    */
    
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        // Eliminar de casilla actual
        if (this.lugar != null) {
            this.lugar.eliminarAvatar(this);
        }
        
        int posActual = this.lugar.getPosicion(); // Posición actual (0-39)
        int nuevaPos = (posActual + valorTirada) % 40; // Cálculo circular 0-39
        
        // DEBUG
        System.out.println("DEBUG: Posición actual: " + posActual + " + " + valorTirada + " = " + nuevaPos);
        
        // Buscar la nueva casilla
        Casilla nuevaCasilla = null;
        buscar:
        for (ArrayList<Casilla> lado : casillas) {
            for (Casilla c : lado) {
                if (c.getPosicion() == nuevaPos) {
                    nuevaCasilla = c;
                    break buscar;
                }
            }
        }
        
        if (nuevaCasilla != null) {
            this.lugar = nuevaCasilla;
            this.lugar.anhadirAvatar(this);
            
            // Verificar si pasó por salida (completó una vuelta)
            if (posActual + valorTirada >= 40) {
                jugador.sumarFortuna(2000000); // 2M€ por pasar salida
                System.out.println(jugador.getNombre() + " pasa por Salida y recibe 2.000.000€");
            }
        } else {
            System.out.println("ERROR: No se encontró casilla en posición " + nuevaPos);
        }
    }







    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase */
    private void generarId(ArrayList<Avatar> avCreados) {
        Random rand = new Random(); // Crea un objeto Random para generar números aleatorios
        String candidato; // Variable para guardar la letra candidata
        boolean repetido; // Bandera para saber si el ID ya existe

        do { // Bucle que repite hasta encontrar un ID no repetido
            candidato = String.valueOf((char) ('A' + rand.nextInt(26))); // Genera una letra aleatoria de A a Z. // Sintaxis: (char) ('A' + rand.nextInt(26))
            repetido = false; // Inicializa la bandera como falsa
            for (Avatar a : avCreados) { // Recorre todos los avatares ya creados
                if (a.getId().equals(candidato)) { // Si el ID candidato ya existe
                    repetido = true; // Marca como repetido
                    break; // Sale del bucle
                }
            }
        } while (repetido); // Repite mientras el ID sea repetido

        this.id = candidato; // Asigna el ID único generado al atributo id. // Sintaxis: this.atributo = valor;
    }

    // Getters y Setters
    public String getId() { // Devuelve el ID del avatar
        return id;
    }

    public String getTipo() { // Devuelve el tipo de avatar
        return tipo;
    }

    public Jugador getJugador() { // Devuelve el jugador al que pertenece el avatar
        return jugador;
    }

    public Casilla getLugar() { // Devuelve la casilla en la que se encuentra
        return lugar;
    }

    public void setLugar(Casilla lugar) { // Cambia la casilla donde está el avatar
        this.lugar = lugar; // Asigna el nuevo valor al atributo lugar. // Sintaxis: this.atributo = parametro;
    }

    @Override
    public String toString() { // Devuelve una representación en texto del avatar
        return "Avatar " + id + " (" + tipo + ")"; // Combina ID y tipo en una cadena
    }

}
