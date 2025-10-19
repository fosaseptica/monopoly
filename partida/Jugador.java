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
    private int tiradasCarcel; //...
    private int vueltas; //...
    private ArrayList<Casilla> propiedades; //...

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.fortuna = Float.MAX_VALUE; // banca ilimitada
        this.propiedades = new ArrayList<>();
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
    }

    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.fortuna = 15000000; // fortuna inicial
        this.gastos = 0;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
    }

    public void anhadirPropiedad(Casilla casilla) {
        if (!propiedades.contains(casilla)) {
            propiedades.add(casilla);
        }
    }

    public void eliminarPropiedad(Casilla casilla) {
        propiedades.remove(casilla);
    }

    public void sumarFortuna(float valor) {
        this.fortuna += valor;
        if (valor < 0) {
            sumarGastos(-valor);
        }
    }

    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel = true;
        this.tiradasCarcel = 0;
        for (ArrayList<Casilla> lado : pos) {
            for (Casilla c : lado) {
                if (c.getTipo().equalsIgnoreCase("Cárcel")) {
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
            this.fortuna -= cantidad;
            this.sumarGastos(cantidad);
            receptor.sumarFortuna(cantidad);
            return true;
        } else {
            return false;
        }
    }

    // Getters/Setters
    public String getNombre() { return nombre; }
    public Avatar getAvatar() { return avatar; }
    public float getFortuna() { return fortuna; }
    public float getGastos() { return gastos; }
    public boolean isEnCarcel() { return enCarcel; }
    public int getTiradasCarcel() { return tiradasCarcel; }
    public void setTiradasCarcel(int tiradas) { this.tiradasCarcel = tiradas; }
    public int getVueltas() { return vueltas; }
    public void incrementarVueltas() { this.vueltas++; }
    public ArrayList<Casilla> getPropiedades() { return propiedades; }

    // >>> NUEVO: para marcar salida de cárcel
    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    @Override
    public String toString() {
        return "Jugador: " + nombre + ", Fortuna: " + fortuna + ", Avatar: " + (avatar != null ? avatar.getId() : "-");
    }
}
