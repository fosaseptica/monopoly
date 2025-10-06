package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            posiciones.add(new ArrayList<>());
        }
        generarCasillas();
        }

    
    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = posiciones.get(2);
        
        sur.add(new CasillaEspecial("Salida"));
        sur.add(new CasillaSolar("Solar1","Marron",600000,300000,500000,500000,100000,200000,20000,400000,2500000,500000,500000));
        sur.add(new CasillaSolar("Solar2","Marron",600000,300000,500000,500000,100000,200000,40000,800000,4500000,900000,900000));
        sur.add(new CasillaCaja("Caja"));
        sur.add(new CasillaSolar("Solar3","Celeste",1000000,500000,500000,500000,100000,200000,60000,1000000,5500000,1100000,1100000));
        sur.add(new CasillaTransporte("Trans1",500000,250000));
        sur.add(new CasillaImpuesto("Imp1",2000000));
        sur.add(new CasillaSolar("Solar4","Celeste",1000000,500000,500000,500000,100000,200000,60000,1000000,5500000,1100000,1100000));
        sur.add(new CasillaSuerte("Suerte"));
        sur.add(new CasillaSolar("Solar5","Celeste",1200000,600000,500000,500000,100000,200000,80000,1250000,6000000,1200000,1200000));
        sur.add(new CasillaEspecial("Cárcel"));

    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
    ArrayList<Casilla> oeste = posiciones.get(1);

        oeste.add(new CasillaSolar("Solar6","Rosa",1400000,700000,1000000,1000000,200000,400000,100000,1500000,7500000,1500000,1500000));
        oeste.add(new CasillaServicio("Serv1",500000,50000));
        oeste.add(new CasillaSolar("Solar7","Rosa",1400000,700000,1000000,1000000,200000,400000,100000,1500000,7500000,1500000,1500000));
        oeste.add(new CasillaSuerte("Suerte"));
        oeste.add(new CasillaSolar("Solar8","Rosa",1600000,800000,1000000,1000000,200000,400000,120000,1750000,9000000,1800000,1800000));
        oeste.add(new CasillaTransporte("Trans2",500000,250000));
        oeste.add(new CasillaSolar("Solar9","Naranja",1800000,900000,1000000,1000000,200000,400000,140000,1850000,9500000,1900000,1900000));
        oeste.add(new CasillaCaja("Caja &H"));
        oeste.add(new CasillaSolar("Solar10","Naranja",1800000,900000,1000000,1000000,200000,400000,140000,1850000,9500000,1900000,1900000));
        oeste.add(new CasillaTransporte("Trans3",500000,250000));

    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
    ArrayList<Casilla> norte = posiciones.get(2);

        norte.add(new CasillaSolar("Solar11","Rojo",2200000,1000000,1000000,1000000,200000,400000,160000,2000000,10000000,2000000,2000000));
        norte.add(new CasillaSolar("Solar12","Rojo",2200000,1100000,1500000,1500000,300000,600000,180000,2200000,10500000,2100000,2100000));
        norte.add(new CasillaSuerte("Suerte"));
        norte.add(new CasillaSolar("Solar13","Rojo",2200000,1100000,1500000,1500000,300000,600000,180000,2200000,10500000,2100000,2100000));
        norte.add(new CasillaSolar("Solar14","Amarillo",2400000,1200000,1500000,1500000,300000,600000,200000,2325000,11000000,2200000,2200000));
        norte.add(new CasillaTransporte("Trans4",500000,250000));
        norte.add(new CasillaSolar("Solar15","Amarillo",2600000,1300000,1500000,1500000,300000,600000,220000,2450000,11500000,2300000,2300000));
        norte.add(new CasillaCaja("Caja &J"));
        norte.add(new CasillaSolar("Solar16","Amarillo",2600000,1300000,1500000,1500000,300000,600000,220000,2450000,11500000,2300000,2300000));
        norte.add(new CasillaSuerte("Suerte"));
        norte.add(new CasillaSolar("Solar17","Verde",2800000,1400000,1500000,1500000,300000,600000,240000,2600000,12000000,2400000,2400000));
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
    ArrayList<Casilla> este = posiciones.get(3);

        este.add(new CasillaSolar("Solar18","Verde",3000000,1500000,2000000,2000000,400000,800000,260000,2750000,12750000,2550000,2550000));
        este.add(new CasillaSolar("Solar19","Verde",3000000,1500000,2000000,2000000,400000,800000,260000,2750000,12750000,2550000,2550000));
        este.add(new CasillaImpuesto("Imp2",2000000));
        este.add(new CasillaSolar("Solar20","Azul",3200000,1600000,2000000,2000000,400000,800000,280000,3000000,14000000,2800000,2800000));
        este.add(new CasillaSolar("Solar21","Azul",3500000,1750000,2000000,2000000,400000,800000,350000,3250000,17000000,3400000,3400000));
        este.add(new CasillaSolar("Solar22","Azul",4000000,2000000,2000000,2000000,400000,800000,500000,4250000,20000000,4000000,4000000));
        este.add(new CasillaEspecial("Parking"));
        este.add(new CasillaEspecial("IrACárcel"));
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < posiciones.size(); i++) {
            sb.append("Lado ").append(i).append(": ");
            for (Casilla c : posiciones.get(i)) {
                sb.append(c.getNombre()).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla c : lado) {
                if (c.getNombre().equalsIgnoreCase(nombre)) {
                    return c;
                }
            }
        }
        return null;
    }
}
