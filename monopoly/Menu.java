package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.

    // === Campos internos auxiliares (no alteran el esqueleto) ===
    private Scanner sc; //Para leer comandos de consola

    // ------------------------- Constructor -------------------------
    public Menu() {
        jugadores   = new ArrayList<>();
        avatares    = new ArrayList<>();
        dado1       = new Dado();
        dado2       = new Dado();
        banca       = new Jugador();        // Banca "neutra"
        tablero     = new Tablero(banca);
        lanzamientos= 0;
        tirado      = false;
        solvente    = true;
        sc          = new Scanner(System.in);
        System.out.println("=== MONOPOLY ETSE ===");
        iniciarPartida();
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {
        while (true) {
            System.out.print("$> ");
            String comando = sc.nextLine().trim();
            if (comando.equalsIgnoreCase("salir")) {
                System.out.println("¡Gracias por jugar!");
                break;
            }
            analizarComando(comando);
        }
    }
    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {
        String[] partes = comando.split(" ");
        if (partes.length == 0) return;

        switch (partes[0].toLowerCase()) {
            case "crear":
                if (partes.length >= 4 && partes[1].equalsIgnoreCase("jugador")) {
                    // crear jugador <nombre> <tipo_avatar>
                    if (jugadores.size() >= 4) {
                        System.out.println("Máximo 4 jugadores.");
                        break;
                    }
                    // Colocamos en Salida
                    Casilla salida = tablero.getCasillas().get(0).get(0);
                    Jugador nuevo = new Jugador(partes[2], partes[3], salida, avatares);
                    jugadores.add(nuevo);
                    System.out.println("{");
                    System.out.println("nombre: " + partes[2] + ",");
                    System.out.println("avatar: " + nuevo.getAvatar().getId());
                    System.out.println("}");
                    verTablero();
                } else {
                    System.out.println("Uso: crear jugador <nombre> <tipo_avatar>");
                }
                break;

            case "listar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("jugadores")) {
                    listarJugadores();
                } else if (partes.length >= 2 && partes[1].equalsIgnoreCase("avatares")) {
                    listarAvatares();
                } else if (partes.length >= 2 && partes[1].equalsIgnoreCase("enventa")) {
                    listarVenta();
                } else if (partes.length >= 2 && partes[1].equalsIgnoreCase("edificios")) {
                    listarEdificios();
                } else {
                    System.out.println("Uso: listar jugadores | listar avatares | listar enventa");
                }
                break;

            case "jugador":
                if (jugadores.isEmpty()) {
                    System.out.println("No hay jugadores en la partida.");
                    break;
                }
                Jugador actual = jugadores.get(turno);
                System.out.println("{");
                System.out.println("nombre: " + actual.getNombre() + ",");
                System.out.println("avatar: " + actual.getAvatar().getId());
                System.out.println("}");
                break;

            case "lanzar":
                if (partes.length == 1) {
                    lanzarDados(-1,-1);
                } else if (partes.length == 3 && partes[1].equalsIgnoreCase("dados")) {
                    String[] nums = partes[2].split("\\+");
                    if (nums.length == 2) {
                        try {
                            int d1 = Integer.parseInt(nums[0]);
                            int d2 = Integer.parseInt(nums[1]);
                            lanzarDados(d1, d2);
                        } catch (NumberFormatException e) {
                            System.out.println("Formato: lanzar dados X+Y");
                        }
                    } else {
                        System.out.println("Formato: lanzar dados X+Y");
                    }
                } else {
                    System.out.println("Uso: lanzar o lanzar dados X+Y");
                }
                break;

            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno")) {
                    acabarTurno();
                } else {
                    System.out.println("Uso: acabar turno");
                }
                break;

            case "describir":
                if (partes.length >= 3 && partes[1].equalsIgnoreCase("jugador")) {
                    descJugador(partes);
                } else if (partes.length >= 3 && partes[1].equalsIgnoreCase("avatar")) {
                    descAvatar(partes[2]);
                } else if (partes.length >= 2) {
                    descCasilla(partes[1]);
                } else {
                    System.out.println("Uso: describir jugador <nombre> | describir avatar <id> | describir <casilla>");
                }
                break;

            case "comprar":
                if (partes.length >= 2) {
                    comprar(partes[1]);
                } else {
                    System.out.println("Uso: comprar <nombre_casilla>");
                }
                break;

            case "salir":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("carcel")) {
                    salirCarcel();
                } else {
                    System.out.println("Uso: salir carcel");
                }
                break;

            case "ver":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("tablero")) {
                    verTablero();
                } else {
                    System.out.println("Uso: ver tablero");
                }
                break;

            case "comandos":
                if (partes.length >= 2) {
                    ejecutarComandosFichero(partes[1]);
                } else {
                    System.out.println("Uso: comandos <fichero>");
                }
                break;

            case "edificar":
                // Uso: edificar <tipo>
                if (partes.length >= 2) {
                    if (jugadores.isEmpty()) {
                        System.out.println("No hay jugadores en la partida.");
                        break;
                    }
                    Jugador actualEd = jugadores.get(turno);
                    Casilla donde = actualEd.getAvatar().getLugar();
                    String tipoEd = partes[1];
                    // Permitir tipos con guion bajo o espacio: "pista_deporte" o "pista deporte"
                    if (partes.length >= 3) {
                        // juntar resto de partes por si el tipo tiene espacios
                        StringBuilder sb = new StringBuilder(tipoEd);
                        for (int i = 2; i < partes.length; i++) {
                            sb.append("_").append(partes[i]);
                        }
                        tipoEd = sb.toString();
                    }
                    // Llamamos al método existente en Casilla
                    donde.edificar(tipoEd, actualEd);
                } else {
                    System.out.println("Uso: edificar <casa|hotel|piscina|pista_deporte>");
                }
                break;

            default:
                System.out.println("Comando no reconocido");
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String[] partes) {
        if (partes.length < 3) {
            System.out.println("Uso: describir jugador <nombre>");
            return;
        }
        String nombre = partes[2];
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println(j); // toString() de Jugador debe mostrar los campos requeridos
                return;
            }
        }
        System.out.println("Jugador no encontrado.");
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
        for (Avatar a : avatares) {
            if (a.getId().equalsIgnoreCase(ID)) {
                System.out.println(a); // toString() de Avatar debe mostrar id, tipo, jugador, casilla
                return;
            }
        }
        System.out.println("Avatar no encontrado.");
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) {
            System.out.println(c.infoCasilla()); // infoCasilla() debe mostrar los campos requeridos
        } else {
            System.out.println("Casilla no encontrada.");
        }
    }

    
    private void lanzarDados(int d1, int d2) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = jugadores.get(turno);
        
        // Bloquea si ya tiró y no hay dobles pendientes
        if (tirado) {
            System.out.println("Ya has tirado en este turno. Debes acabar el turno o esperar a un doble.");
            return;
        }

        // Generar valores aleatorios si no se pasan
        if (d1 < 0 || d2 < 0) {
            d1 = dado1.hacerTirada();
            d2 = dado2.hacerTirada();
        }

        int total = d1 + d2;

        // ---- Control de dobles ----
        if (d1 == d2) {
            lanzamientos++;
        } else {
            lanzamientos = 0;
        }

        // ---- Tres dobles consecutivos ----
        if (lanzamientos == 3) {
            System.out.println("¡Tres dobles seguidos! Vas directamente a la cárcel.");

            // Mover avatar a la cárcel
            actual.encarcelar(tablero.getCasillas());
            lanzamientos = 0;
            tirado = true;
            
            System.out.println("El avatar " + actual.getAvatar().getId() + " ha sido enviado a la cárcel.");
            
            verTablero();
            return;
        }

        // ---- Movimiento normal ----
        System.out.println("{");
        System.out.println("Dados: " + d1 + " + " + d2 + " = " + total);
        System.out.println("Jugador: " + actual.getNombre());
        System.out.println("Avatar: " + actual.getAvatar().getId() + " avanza " + total + " posiciones");
        System.out.println("}");

        int posAnteriorIdx = actual.getAvatar().getLugar().getPosicion();
        boolean pasaPorSalida = (posAnteriorIdx + total) >= 40;

        actual.getAvatar().moverAvatar(tablero.getCasillas(), total);
        Casilla nueva = actual.getAvatar().getLugar();

        if (pasaPorSalida && !nueva.getNombre().equalsIgnoreCase("IrACárcel")) {
            actual.sumarFortuna(2000000f);
        }

        System.out.println("Ahora estás en: " + nueva.getNombre());
        solvente = nueva.evaluarCasilla(actual, banca, total, tablero);
        if (!solvente) System.out.println("No puedes pagar, ¡bancarrota!");

        // ---- Dobles para tirar de nuevo ----
        if (d1 == d2) {
            System.out.println("¡Has sacado dobles! Puedes tirar de nuevo.");
            tirado = false;
        } else {
            tirado = true;
            lanzamientos = 0;
        }

        verTablero();
    }

    
    
    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombre);

        if (c == null) {
            System.out.println("Casilla no encontrada");
            return;
        }

        String t = c.getTipo();
        boolean comprable = t.equalsIgnoreCase("Solar") ||
                            t.equalsIgnoreCase("Servicio") ||
                            t.equalsIgnoreCase("Transporte");
        if (!comprable) {
            System.out.println("Esa casilla no se puede comprar (" + c.getTipo() + ").");
            return;
        }

        // Debe estar en venta (dueño = banca)
        if (c.getDuenho() != banca) {
            System.out.println("La casilla ya tiene dueño.");
            return;
        }

        // Debe estar físicamente en la casilla
        Casilla dondeEstoy = actual.getAvatar().getLugar();
        if (dondeEstoy != c) {
            System.out.println("Debes estar en '" + c.getNombre() + "' para comprarla. Ahora estás en '" + dondeEstoy.getNombre() + "'.");
            return;
        }

        // Intento de compra
        c.comprarCasilla(actual, banca);
        if (c.getDuenho() == actual) {
            System.out.println("Has comprado: " + c.getNombre());
            verTablero();
        } else {
            System.out.println("No tienes suficiente dinero para comprar " + c.getNombre() + ".");
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            actual.sumarFortuna(-500000); // paga 500.000€
            actual.setTiradasCarcel(0);
            actual.setEnCarcel(false);
            // El jugador queda libre pero permanece en la casilla Cárcel (no se teletransporta a Salida)
            System.out.println(actual.getNombre() + " paga 500000€ y sale de la cárcel. Puede lanzar los dados.");
            verTablero();
        } else {
            System.out.println("No estás en la cárcel.");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        ArrayList<String> bloques = new ArrayList<>();
        for (ArrayList<Casilla> lado : tablero.getCasillas()) {
            for (Casilla c : lado) {
                String tipo = c.getTipo();
                boolean comprable = tipo.equalsIgnoreCase("Solar") ||
                                    tipo.equalsIgnoreCase("Servicio") ||
                                    tipo.equalsIgnoreCase("Transporte");
                if (comprable && c.getDuenho() == banca) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{\n");
                    sb.append("tipo: ").append(tipo.toLowerCase()).append(",\n");
                    if (tipo.equalsIgnoreCase("Solar")) {
                        sb.append("grupo: ").append(c.getGrupoColor()).append(",\n");
                    }
                    sb.append("valor: ").append(formatearCantidad(c.getValor())).append("\n");
                    sb.append("}");
                    bloques.add(sb.toString());
                }
            }
        }
        if (bloques.isEmpty()) {
            System.out.println("No hay propiedades en venta.");
            return;
        }
        for (int i = 0; i < bloques.size(); i++) {
            String sufijo = (i < bloques.size() - 1) ? "," : "";
            System.out.println(bloques.get(i) + sufijo);
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
    if (jugadores.isEmpty()) {
        System.out.println("No hay jugadores todavía.");
        return;
    }

    System.out.println("[");
    for (int i = 0; i < jugadores.size(); i++) {
        Jugador j = jugadores.get(i);
        System.out.print(j.toString());
        if (i < jugadores.size() - 1) System.out.print(",\n");
    }
    System.out.println("\n]");
    }


    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
        if (avatares.isEmpty()) {
            System.out.println("No hay avatares creados.");
            return;
        }
        for (Avatar a : avatares) {
            System.out.println(a);
        }
    }

    private void listarEdificios() {
    ArrayList<String> bloques = new ArrayList<>();

    for (Jugador j : jugadores) {
        for (Casilla c : j.getPropiedades()) {
            if (!c.getTipo().equalsIgnoreCase("Solar")) continue;

            // --- CASAS ---
            for (int i = 1; i <= c.getNumCasas(); i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                sb.append(" id: casa-").append(i).append(",\n");
                sb.append(" propietario: ").append(j.getNombre()).append(",\n");
                sb.append(" casilla: ").append(c.getNombre()).append(",\n");
                sb.append(" grupo: ").append(c.getGrupoColor()).append(",\n");
                sb.append(" coste: ").append((int) c.getPrecioCasa()).append("\n");
                sb.append("}");
                bloques.add(sb.toString());
            }

            // --- HOTELES ---
            for (int i = 1; i <= c.getNumHoteles(); i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                sb.append(" id: hotel-").append(i).append(",\n");
                sb.append(" propietario: ").append(j.getNombre()).append(",\n");
                sb.append(" casilla: ").append(c.getNombre()).append(",\n");
                sb.append(" grupo: ").append(c.getGrupoColor()).append(",\n");
                sb.append(" coste: ").append((int) c.getPrecioHotel()).append("\n");
                sb.append("}");
                bloques.add(sb.toString());
            }

            // --- PISCINAS ---
            for (int i = 1; i <= c.getNumPiscinas(); i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                sb.append(" id: piscina-").append(i).append(",\n");
                sb.append(" propietario: ").append(j.getNombre()).append(",\n");
                sb.append(" casilla: ").append(c.getNombre()).append(",\n");
                sb.append(" grupo: ").append(c.getGrupoColor()).append(",\n");
                sb.append(" coste: ").append((int) c.getPrecioPiscina()).append("\n");
                sb.append("}");
                bloques.add(sb.toString());
            }

            // --- PISTAS DE DEPORTE ---
            for (int i = 1; i <= c.getNumPistas(); i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                sb.append(" id: pista-").append(i).append(",\n");
                sb.append(" propietario: ").append(j.getNombre()).append(",\n");
                sb.append(" casilla: ").append(c.getNombre()).append(",\n");
                sb.append(" grupo: ").append(c.getGrupoColor()).append(",\n");
                sb.append(" coste: ").append((int) c.getPrecioPistaDeporte()).append("\n");
                sb.append("}");
                bloques.add(sb.toString());
            }
        }
    }

    if (bloques.isEmpty()) {
        System.out.println("No hay edificios construidos.");
        return;
    }

    for (int i = 0; i < bloques.size(); i++) {
        System.out.println(bloques.get(i) + (i < bloques.size() - 1 ? "," : ""));
    }
}


    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        tirado = false;
        lanzamientos = 0; //  Reiniciamos contador de dobles al cambiar de jugador
        Jugador siguiente = jugadores.get(turno);
        System.out.println("{");
        System.out.println("El jugador actual es " + siguiente.getNombre() + ".");
        System.out.println("}");
    }

    // ------------------------- Helpers privados -------------------------
    private void lanzarDadosForzados(int d1, int d2) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        int total = d1 + d2;
        System.out.println("{");
        System.out.println("Dados: " + d1 + " + " + d2 + " = " + total);
        System.out.println("Jugador: " + actual.getNombre());
        System.out.println("Avatar: " + actual.getAvatar().getId() + " avanza " + total + " posiciones");
        System.out.println("}");

        int posAnteriorIdx = actual.getAvatar().getLugar().getPosicion();
        boolean pasaPorSalida = (posAnteriorIdx + total) >= 40;

        actual.getAvatar().moverAvatar(tablero.getCasillas(), total);
        Casilla nueva = actual.getAvatar().getLugar();

        if (pasaPorSalida && !nueva.getNombre().equalsIgnoreCase("IrACárcel")) {
            actual.sumarFortuna(2000000f);
        }

        System.out.println("Ahora estás en: " + nueva.getNombre());
        solvente = nueva.evaluarCasilla(actual, banca, total, tablero);
        if (!solvente) System.out.println("No puedes pagar, ¡bancarrota!");
        tirado = true;
        verTablero();
    }

    private void verTablero() {
        System.out.println(tablero.toString());
    }

    private String formatearCantidad(float valor) {
        if (Math.abs(valor - Math.round(valor)) < 0.01f) {
            return String.format(java.util.Locale.US, "%.0f", valor);
        }
        return String.format(java.util.Locale.US, "%.2f", valor);
    }

    private void ejecutarComandosFichero(String fichero) {
        try (Scanner fileScanner = new Scanner(new File(fichero))) {
            while (fileScanner.hasNextLine()) {
                String linea = fileScanner.nextLine().trim();
                if (!linea.isEmpty()) {
                    System.out.println("$> " + linea);
                    analizarComando(linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo abrir el fichero: " + fichero);
        }
    }
}
