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
    private ArrayList<String> edificiosPropios; // ids de edificios que posee el jugador

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
        this.edificiosPropios = new ArrayList<>();
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
    }

    public void anhadirEdificio(String id) {
        if (edificiosPropios == null) edificiosPropios = new ArrayList<>();
        if (!edificiosPropios.contains(id)) edificiosPropios.add(id);
    }

    public void eliminarEdificio(String id) {
        if (edificiosPropios != null) edificiosPropios.remove(id);
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
                if (c.getNombre().equalsIgnoreCase("Cárcel") || c.getNombre().equalsIgnoreCase("Carcel")) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("{").append('\n');
        sb.append("nombre: ").append(nombre).append(",").append('\n');
        sb.append("avatar: ").append(avatar != null ? avatar.getId() : "-").append(",").append('\n');
        String fortunaFmt = String.format(java.util.Locale.GERMANY, "%,d", Math.round(fortuna));
        String gastosFmt  = String.format(java.util.Locale.GERMANY, "%,d", Math.round(gastos));
        sb.append("fortuna: ").append(fortunaFmt).append("€").append(",").append('\n');
        sb.append("gastos: ").append(gastosFmt).append("€").append(",").append('\n');

        // Propiedades
        sb.append("propiedades: [");
        if (propiedades.isEmpty()) {
            sb.append("],").append('\n');
        } else {
            for (int i = 0; i < propiedades.size(); i++) {
                Casilla c = propiedades.get(i);
                sb.append(c.getNombre());
                if (i < propiedades.size() - 1) sb.append(", ");
            }
            sb.append("],").append('\n');
        }

        // Hipotecas (no implementado aún)
        sb.append("hipotecas: -,").append('\n');

        // Edificios: listados por casilla con número de cada tipo
        sb.append("edificios: ");
        if (edificiosPropios == null || edificiosPropios.isEmpty()) {
            sb.append("-").append('\n');
        } else {
            sb.append("[");
            for (int i = 0; i < edificiosPropios.size(); i++) {
                sb.append(edificiosPropios.get(i));
                if (i < edificiosPropios.size() - 1) sb.append(", ");
            }
            sb.append("]").append('\n');
        }

        sb.append("}");
        return sb.toString();
    }
}
