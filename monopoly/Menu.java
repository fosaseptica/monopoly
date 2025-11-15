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

            case "hipotecar":
            if (partes.length >= 2) {
                hipotecar(partes[1]);
            } else {
                System.out.println("Uso: hipotecar <nombre_casilla>");
            }
            break;

            case "deshipotecar":
            if (partes.length >= 2) {
                deshipotecar(partes[1]);
            } else {
                System.out.println("Uso: deshipotecar <nombre_casilla>");
            }
            break;
            case "vender":
            if (partes.length >= 4) {
                String tipoEdificio = partes[1];   // casas / hoteles / piscina / pistas...
                String nombreCasilla = partes[2];  // Solar5, Solar21, ...
                try {
                    int cantidad = Integer.parseInt(partes[3]);
                    venderEdificios(tipoEdificio, nombreCasilla, cantidad);
                } catch (NumberFormatException e) {
                    System.out.println("La cantidad a vender debe ser un número entero.");
                }
            } else {
                System.out.println("Uso: vender <casa/s|hotel/es|piscina/s|pista/s> <nombre_casilla> <cantidad>");
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
        ArrayList<String> registros = monopoly.Tablero.listarEdificiosStatic();
        if (registros.isEmpty()) {
            System.out.println("No hay edificios construidos.");
            return;
        }

        for (int i = 0; i < registros.size(); i++) {
            String r = registros.get(i);
            // formato: id|tipo|propietario|casilla|grupo|coste
            String[] p = r.split("\\|");
            if (p.length < 6) continue;
            String id = p[0];
            String propietario = p[2];
            String casilla = p[3];
            String grupo = p[4];
            String coste = p[5];

            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append(" id: ").append(id).append(",\n");
            sb.append(" propietario: ").append(propietario).append(",\n");
            sb.append(" casilla: ").append(casilla).append(",\n");
            sb.append(" grupo: ").append(grupo).append("\n");
            sb.append(" coste: ").append(coste).append("\n");
            sb.append("}");
            System.out.println(sb.toString() + (i < registros.size() - 1 ? "," : ""));
        }
    }

    private void hipotecar(String nombreCasilla) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombreCasilla);

        if (c == null) {
            System.out.println("Casilla no encontrada.");
            return;
        }

        String tipo = c.getTipo();

        // Solo se hipotecan Solares, Servicios y Transportes
        boolean hipotecable = tipo.equalsIgnoreCase("Solar") ||
                            tipo.equalsIgnoreCase("Servicio") ||
                            tipo.equalsIgnoreCase("Transporte");
        if (!hipotecable) {
            System.out.println(actual.getNombre() + " no puede hipotecar " + c.getNombre() + ". No es una propiedad hipotecable.");
            return;
        }

        // Debe ser propietario
        if (c.getDuenho() != actual) {
            System.out.println(actual.getNombre() + " no puede hipotecar " + c.getNombre() + ". No es una propiedad que le pertenece.");
            return;
        }

        // Ya hipotecada
        if (c.getHipotecada()) {
            System.out.println(actual.getNombre() + " no puede hipotecar " + c.getNombre() + ". Ya está hipotecada.");
            return;
        }

        // Si es Solar, no puede tener edificios construidos
        if (tipo.equalsIgnoreCase("Solar")) {
            int totalEd = c.getNumCasas() + c.getNumHoteles() + c.getNumPiscinas() + c.getNumPistas();
            if (totalEd > 0) {
                System.out.println(actual.getNombre() + " no puede hipotecar " + c.getNombre() + ". Antes debe vender todos los edificios de esa propiedad.");
                return;
            }
        }

        // Ejecutar hipoteca
        float cantidad = c.getHipoteca();
        actual.sumarFortuna(cantidad);
        c.setHipotecada(true);

        // Mensaje similar al del enunciado
        StringBuilder sb = new StringBuilder();
        sb.append(actual.getNombre())
        .append(" recibe ")
        .append((int)cantidad).append("€ por la hipoteca de ")
        .append(c.getNombre()).append(".");

        if (tipo.equalsIgnoreCase("Solar")) {
            String grupoColor = c.getGrupoColor();
            sb.append(" No puede recibir alquileres");
            if (grupoColor != null && !grupoColor.equals("-")) {
                sb.append(" ni edificar en el grupo ").append(grupoColor.toLowerCase());
            }
            sb.append(".");
        } else {
            sb.append(" No puede recibir alquileres de esta propiedad mientras esté hipotecada.");
        }

        System.out.println(sb.toString());
    }

    private void deshipotecar(String nombreCasilla) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombreCasilla);

        if (c == null) {
            System.out.println("Casilla no encontrada.");
            return;
        }

        String tipo = c.getTipo();

        // Solo tiene sentido para solares, servicios y transportes
        boolean hipotecable = tipo.equalsIgnoreCase("Solar") ||
                            tipo.equalsIgnoreCase("Servicio") ||
                            tipo.equalsIgnoreCase("Transporte");
        if (!hipotecable) {
            System.out.println(actual.getNombre() + " no puede deshipotecar " + c.getNombre() + ". No es una propiedad hipotecable.");
            return;
        }

        // Debe ser propietario
        if (c.getDuenho() != actual) {
            System.out.println(actual.getNombre() + " no puede deshipotecar " + c.getNombre() + ". No es una propiedad que le pertenece.");
            return;
        }

        // Debe estar hipotecada
        if (!c.getHipotecada()) {
            // OJO: el enunciado pone literalmente "no puede hipotecar" aquí
            System.out.println(actual.getNombre() + " no puede hipotecar " + c.getNombre() + ". No está hipotecada.");
            return;
        }

        float cantidad = c.getHipoteca();

        // Comprobar que tiene dinero suficiente
        if (actual.getFortuna() < cantidad) {
            System.out.println(actual.getNombre() + " no puede deshipotecar " + c.getNombre() + ". No tiene suficiente dinero.");
            return;
        }

        // Paga a la banca y se elimina la hipoteca
        actual.pagar(cantidad, banca);
        c.setHipotecada(false);

        // Mensaje de salida según el tipo
        if (tipo.equalsIgnoreCase("Solar")) {
            String grupoColor = c.getGrupoColor();
            System.out.println(
                actual.getNombre() + " paga " +
                (int)cantidad + "€ por deshipotecar " + c.getNombre() +
                ". Ahora puede recibir alquileres y edificar en el grupo " +
                (grupoColor != null ? grupoColor.toLowerCase() : "-") + "."
            );
        } else {
            System.out.println(
                actual.getNombre() + " paga " +
                (int)cantidad + "€ por deshipotecar " + c.getNombre() +
                ". Ahora puede recibir alquileres de esta propiedad."
            );
        }
    }

    private void venderEdificios(String tipoArgumento, String nombreCasilla, int cantidad) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        if (cantidad <= 0) {
            System.out.println("La cantidad a vender debe ser mayor que 0.");
            return;
        }

        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombreCasilla);

        if (c == null) {
            System.out.println("Casilla no encontrada.");
            return;
        }

        // Solo solares pueden tener edificios
        if (!c.getTipo().equalsIgnoreCase("Solar")) {
            System.out.println("No se pueden vender edificios en " + c.getNombre() + ". Solo se pueden vender edificios en propiedades de tipo Solar.");
            return;
        }

        // Debe ser propietario
        if (c.getDuenho() != actual) {
            String plural = normalizarPlural(tipoArgumento);
            System.out.println("No se pueden vender " + plural + " en " + c.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
            return;
        }

        // Mapear tipo argumento -> tipo interno y nombres para mensajes
        String tipo = tipoArgumento.toLowerCase();
        String tipoInterno;
        String singular;
        String plural;

        switch (tipo) {
            case "casa":
            case "casas":
                tipoInterno = "casa";
                singular = "casa";
                plural = "casas";
                break;
            case "hotel":
            case "hoteles":
                tipoInterno = "hotel";
                singular = "hotel";
                plural = "hoteles";
                break;
            case "piscina":
            case "piscinas":
                tipoInterno = "piscina";
                singular = "piscina";
                plural = "piscinas";
                break;
            case "pista":
            case "pistas":
                tipoInterno = "pista_deporte";
                singular = "pista de deporte";
                plural = "pistas de deporte";
                break;
            default:
                System.out.println("Tipo de edificio no válido. Use casa/s, hotel/es, piscina/s o pista/s.");
                return;
        }

        int disponibles;
        float precioUnit;

        switch (tipoInterno) {
            case "casa":
                disponibles = c.getNumCasas();
                precioUnit = c.getPrecioCasa();
                break;
            case "hotel":
                disponibles = c.getNumHoteles();
                precioUnit = c.getPrecioHotel();
                break;
            case "piscina":
                disponibles = c.getNumPiscinas();
                precioUnit = c.getPrecioPiscina();
                break;
            case "pista_deporte":
                disponibles = c.getNumPistas();
                precioUnit = c.getPrecioPistaDeporte();
                break;
            default:
                // No debería llegar aquí
                return;
        }

        if (disponibles == 0) {
            System.out.println("No se pueden vender " + plural + " en " + c.getNombre() + ". No hay " + plural + " construidas en esta propiedad.");
            return;
        }

        int aVender = Math.min(cantidad, disponibles);
        float ingreso = aVender * precioUnit;

        // Actualizar contador de edificios
        switch (tipoInterno) {
            case "casa":
                c.reducirCasas(aVender);
                break;
            case "hotel":
                c.reducirHoteles(aVender);
                break;
            case "piscina":
                c.reducirPiscinas(aVender);
                break;
            case "pista_deporte":
                c.reducirPistas(aVender);
                break;
        }

        // El jugador recibe el dinero
        actual.sumarFortuna(ingreso);

        // Mensajes según casos

        // Caso: intenta vender más de los que hay (ejemplo piscina)
        if (aVender < cantidad) {
            System.out.println("Solamente se puede vender " + aVender + " " +
                    (aVender == 1 ? singular : plural) +
                    ", recibiendo " + (int) ingreso + "€.");
            return;
        }

        // Caso normal: vende exactamente lo pedido
        int restantes = disponibles - aVender;

        // Ejemplo del enunciado menciona las casas que quedan
        if (tipoInterno.equals("casa")) {
            System.out.println(
                actual.getNombre() + " ha vendido " + aVender + " " +
                (aVender == 1 ? "casa" : "casas") + " en " + c.getNombre() +
                ", recibiendo " + (int) ingreso + "€. En la propiedad " +
                (restantes == 1
                    ? "queda 1 casa."
                    : "quedan " + restantes + " casas.")
            );
        } else {
            // Para hoteles, piscina, pistas no es obligatorio mencionar restantes
            System.out.println(
                actual.getNombre() + " ha vendido " + aVender + " " +
                (aVender == 1 ? singular : plural) + " en " + c.getNombre() +
                ", recibiendo " + (int) ingreso + "€."
            );
        }
    }

    // Pequeño helper para el mensaje de "no se pueden vender ... en ..."
    private String normalizarPlural(String tipoArgumento) {
        String t = tipoArgumento.toLowerCase();
        switch (t) {
            case "casa":
                return "casas";
            case "hotel":
                return "hoteles";
            case "piscina":
                return "piscinas";
            case "pista":
                return "pistas de deporte";
            case "pistas":
            case "pistasdedeporte":
            case "pistadedeporte":
                return "pistas de deporte";
            default:
                return t; // tal cual lo escribió el usuario
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
