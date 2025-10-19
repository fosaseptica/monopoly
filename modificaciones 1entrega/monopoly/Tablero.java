package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import partida.*;

public class Tablero {
    private ArrayList<ArrayList<Casilla>> posiciones;
    private HashMap<String, Grupo> grupos;
    private Jugador banca;

    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            posiciones.add(new ArrayList<>());
        }
        
        generarCasillas();
    }

    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    private void insertarLadoSur() {
        ArrayList<Casilla> sur = posiciones.get(0);
        
        sur.add(new Casilla("Salida", "Especial", 0, 0, banca));
        sur.add(new Casilla("Solar1", "Solar", 1, 600000, banca));
        sur.add(new Casilla("Caja", "Comunidad", 2, banca));
        sur.add(new Casilla("Solar2", "Solar", 3, 600000, banca));
        sur.add(new Casilla("Imp1", "Impuesto", 4, 2000000, banca));
        sur.add(new Casilla("Trans1", "Transporte", 5, 500000, banca));
        sur.add(new Casilla("Solar3", "Solar", 6, 1000000, banca));
        sur.add(new Casilla("Suerte", "Suerte", 7, banca));
        sur.add(new Casilla("Solar4", "Solar", 8, 1000000, banca));
        sur.add(new Casilla("Solar5", "Solar", 9, 1200000, banca));
        sur.add(new Casilla("Cárcel", "Especial", 10, banca));
    }

    private void insertarLadoOeste() {
        ArrayList<Casilla> oeste = posiciones.get(1);

        oeste.add(new Casilla("Solar6", "Solar", 11, 1400000, banca));
        oeste.add(new Casilla("Serv1", "Servicio", 12, 500000, banca));
        oeste.add(new Casilla("Solar7", "Solar", 13, 1400000, banca));
        oeste.add(new Casilla("Solar8", "Solar", 14, 1600000, banca));
        oeste.add(new Casilla("Trans2", "Transporte", 15, 500000, banca));
        oeste.add(new Casilla("Solar9", "Solar", 16, 1800000, banca));
        oeste.add(new Casilla("Caja", "Comunidad", 17, banca));
        oeste.add(new Casilla("Solar10", "Solar", 18, 1800000, banca));
        oeste.add(new Casilla("Solar11", "Solar", 19, 2200000, banca));
    }

    private void insertarLadoNorte() {
        ArrayList<Casilla> norte = posiciones.get(2);

        norte.add(new Casilla("Parking", "Especial", 20, banca));
        norte.add(new Casilla("Solar12", "Solar", 21, 2200000, banca));
        norte.add(new Casilla("Suerte", "Suerte", 22, banca));
        norte.add(new Casilla("Solar13", "Solar", 23, 2200000, banca));
        norte.add(new Casilla("Solar14", "Solar", 24, 2400000, banca));
        norte.add(new Casilla("Trans3", "Transporte", 25, 500000, banca));
        norte.add(new Casilla("Solar15", "Solar", 26, 2600000, banca));
        norte.add(new Casilla("Solar16", "Solar", 27, 2600000, banca));
        norte.add(new Casilla("Serv2", "Servicio", 28, 500000, banca));
        norte.add(new Casilla("Solar17", "Solar", 29, 2800000, banca));
        norte.add(new Casilla("IrCárcel", "Especial", 30, banca));
    }

    private void insertarLadoEste() {
        ArrayList<Casilla> este = posiciones.get(3);

        este.add(new Casilla("Solar18", "Solar", 31, 3000000, banca));
        este.add(new Casilla("Solar19", "Solar", 32, 3000000, banca));
        este.add(new Casilla("Caja", "Comunidad", 33, banca));
        este.add(new Casilla("Solar20", "Solar", 34, 3200000, banca));
        este.add(new Casilla("Trans4", "Transporte", 35, 500000, banca));
        este.add(new Casilla("Suerte", "Suerte", 36, banca));
        este.add(new Casilla("Solar21", "Solar", 37, 3500000, banca));
        este.add(new Casilla("Imp2", "Impuesto", 38, 2000000, banca));
        este.add(new Casilla("Solar22", "Solar", 39, 4000000, banca));
    }


    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        ArrayList<Casilla> sur = posiciones.get(0);   // 11 casillas
        ArrayList<Casilla> oeste = posiciones.get(1); // 9 casillas  
        ArrayList<Casilla> norte = posiciones.get(2); // 11 casillas
        ArrayList<Casilla> este = posiciones.get(3);  // 9 casillas
        
        // 1. LADO NORTE
        sb.append("┌─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┐\n");
        sb.append("│");
        for (Casilla c : norte) {
            sb.append(formatearCasilla(c)).append("│");
        }
        sb.append("\n");
        
        // 2. LADOS OESTE Y ESTE - CORREGIDO
        // Oeste: 9 casillas (índices 0-8: Solar6 a Solar11)
        // Este: 9 casillas (índices 0-8: Solar18 a Solar22)
        for (int i = 0; i < 9; i++) {
            // Línea separadora
            sb.append("├─────────┤                                                                                         ├─────────┤\n");
            
            // Contenido
            sb.append("│");
            
            // LADO OESTE (de Solar11 a Solar6 - invertido)
            int idxOeste = oeste.size() - 1 - i; // 8,7,6,...,0
            sb.append(formatearCasilla(oeste.get(idxOeste)));
            sb.append("│");
            
            // CENTRO
            sb.append("                                                                                         ");
            
            // LADO ESTE (de Solar18 a Solar22 - orden normal)
            sb.append("│");
            sb.append(formatearCasilla(este.get(i))); // 0,1,2,...,8
            sb.append("│\n");
        }
        
        // 3. LADO SUR
        sb.append("├─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┤\n");
        sb.append("│");
        for (int i = sur.size() - 1; i >= 0; i--) {
            sb.append(formatearCasilla(sur.get(i))).append("│");
        }
        sb.append("\n");
        sb.append("└─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┘\n");
        
        return sb.toString();
    }

    private String formatearCasilla(Casilla c) {
        String texto = c.getNombre();
        // Limitar a 9 caracteres

        // 1. APLICAR COLORES SOLO AL TEXTO DE LOS SOLARES
        /*
        if (c.getTipo().equals("Solar")) {
            texto = ponerColorSimple(c.getNombre());
        }
        */

        if (texto.length() > 9) {
            texto = texto.substring(0, 9);
        }
        
        // Añadir avatares
        if (!c.getAvatares().isEmpty()) {
            texto += "&";
            for (Avatar a : c.getAvatares()) {
                texto += a.getId();
            }
        }
        
        return String.format("%-9s", texto);
    }
    
    /* 
    private String obtenerTextoCasilla(Casilla casilla) {
        String texto = casilla.getNombre();
        
        // Color simple por nombre del solar (sin grupos complejos por ahora)
        if (casilla.getTipo().equals("Solar")) {
            texto = ponerColorSimple(casilla.getNombre());
        }
        
        // Añadir avatares
        if (!casilla.getAvatares().isEmpty()) {
            texto += "&";
            for (Avatar a : casilla.getAvatares()) {
                texto += a.getId();
            }
        }
        
        return String.format("%-11s", texto);
    }
    */
        

    private String ponerColorSimple(String nombreSolar) {
        // Colores simples sin grupos complejos
        switch(nombreSolar) {
            case "Solar1": case "Solar2": 
                return Valor.YELLOW + nombreSolar + Valor.RESET;
            case "Solar3": case "Solar4": 
                return Valor.CYAN + nombreSolar + Valor.RESET;
            case "Solar5": case "Solar6": case "Solar7": 
                return Valor.PURPLE + nombreSolar + Valor.RESET;
            case "Solar8": case "Solar9": case "Solar10": 
                return Valor.RED + nombreSolar + Valor.RESET;
            case "Solar11": case "Solar12": case "Solar13": 
                return Valor.RED + nombreSolar + Valor.RESET;
            case "Solar14": case "Solar15": case "Solar16": 
                return Valor.YELLOW + nombreSolar + Valor.RESET;
            case "Solar17": case "Solar18": case "Solar19": 
                return Valor.GREEN + nombreSolar + Valor.RESET;
            case "Solar20": case "Solar21": case "Solar22": 
                return Valor.BLUE + nombreSolar + Valor.RESET;
            default: 
                return nombreSolar;
        }
    }
    
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

    public ArrayList<ArrayList<Casilla>> getCasillas() {
        return posiciones;
    }
}