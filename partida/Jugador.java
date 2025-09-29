package partida;

import java.util.ArrayList;

import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
    
        this.nombre = nombre; //Asignamos un nombre al jugador
        this.fortuna = 15000000; //Asignamos la fortuna inicial (15.000.000)
        this.gastos = 0; //Inicialmente no tiene gastos 
        this.enCarcel = false; //Inicialmente no está en la carcel
        this.tiradasCarcel = 0; //Inicialmente no ha tirado para salir de la carcel
        this.vueltas = 0; //Inicialmente no ha dado ninguna vuelta al tablero
        this.propiedades = new ArrayList<Casilla>(); //Inicialmente no tiene propiedades
        this.avatar = new Avatar(tipoAvatar, avCreados); //Creamos el avatar del jugador
        this.avatar.setCasilla(inicio); //Colocamos el avatar en la casilla de inicio
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        this.propiedades.add(casilla); //Añadimos la casilla al arraylist de propiedades del jugador.
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        this.propiedades.remove(casilla); //Eliminamos la casilla del arraylist de propiedades del jugador.
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor; //Sumamos el valor a la fortuna del jugador.
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor; //Sumamos el valor a los gastos del jugador.
    }

    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> tablero) {
    this.enCarcel = true;       // Marcamos que está en la cárcel
    this.tiradasCarcel = 0;     // Reiniciamos el contador de tiradas
    // Colocamos el avatar en la casilla de cárcel (suponiendo que tablero[5][0] es la cárcel, por ejemplo)
    Casilla carcel = null;
    for (ArrayList<Casilla> fila : tablero) {
        for (Casilla c : fila) {
            if (c.getTipo().equals("Cárcel")) {
                carcel = c;
                break;
            }
        }
        if (carcel != null) break;
    }
    if (carcel != null) {
        this.avatar.setCasilla(carcel);
    }
}
