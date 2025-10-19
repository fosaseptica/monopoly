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
        this.fortuna = Float.MAX_VALUE; // banca ilimitada
        this.propiedades = new ArrayList<>();
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.fortuna = 15000000; // fortuna inicial
        this.gastos = 0;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();

        // Crear avatar y situarlo en la casilla inicial
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if (!propiedades.contains(casilla)) {
            propiedades.add(casilla);
        }
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        propiedades.remove(casilla);
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
        if (valor < 0) {
            sumarGastos(-valor);
        }
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel = true;
        this.tiradasCarcel = 0;

        // Buscar casilla de cárcel en el tablero
        for (ArrayList<Casilla> lado : pos) {
            for (Casilla c : lado) {
                if (c.getTipo().equalsIgnoreCase("Cárcel")) {
                    // Mover avatar a la casilla de cárcel
                    avatar.getLugar().eliminarAvatar(avatar);
                    avatar.setLugar(c);
                    c.anhadirAvatar(avatar);
                    return;
                }
            }
        }
    }
    public boolean pagar(float cantidad, Jugador receptor) {
        if (this.fortuna >= cantidad) {
            this.fortuna -= cantidad;          // Restar dinero al jugador
            this.sumarGastos(cantidad);        // Registrar gasto
            receptor.sumarFortuna(cantidad);   // Sumar dinero al receptor
            return true;                        // Pago exitoso
        } else {
            // No tiene suficiente dinero → bancarrota
            return false;
        }
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public float getFortuna() {
        return fortuna;
    }

    public float getGastos() {
        return gastos;
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }

    public void setTiradasCarcel(int tiradas) {
        this.tiradasCarcel = tiradas;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void incrementarVueltas() {
        this.vueltas++;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    @Override
    public String toString() {
        return "Jugador: " + nombre + ", Fortuna: " + fortuna + ", Avatar: " + avatar.getId();
    }

}
