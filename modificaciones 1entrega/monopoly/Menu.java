package monopoly;

import java.util.ArrayList;
import java.util.Scanner;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente al jugador que tiene el turno
    private Tablero tablero; //Tablero del juego
    private Dado dado1;
    private Dado dado2;
    private Jugador banca; //Jugador especial banca
    private Scanner sc; //Para leer comandos de consola

    //Constructor: inicia el juego y el bucle de comandos
    public Menu() {
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
        dado1 = new Dado();
        dado2 = new Dado();
        banca = new Jugador();
        tablero = new Tablero(banca);
        sc = new Scanner(System.in);
        System.out.println("=== MONOPOLY ETSE ===");
        //iniciarPartida();
    }

    // Método público para iniciar partida desde fuera
    public void iniciarPartidaPublico() {
        iniciarPartida();
    }

    // Método principal de la partida: bucle de comandos
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

    /* Interpreta el comando introducido y toma la acción correspondiente */
    private void analizarComando(String comando) {
        String[] partes = comando.split(" ");
        if (partes.length == 0) return;

        switch (partes[0].toLowerCase()) {
            case "comandos":
                if (partes.length >= 2)
                    leerComandosDesdeFichero(partes[1]);
                else
                    System.out.println("Uso: comandos <nombre_fichero>");
                break;
            case "crear":
                if (partes.length >= 4 && partes[1].equalsIgnoreCase("jugador"))
                    crearJugador(partes[2], partes[3]);
                else
                    System.out.println("Uso: crear jugador <nombre> <tipo_avatar>");
                break;
            case "listar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("jugadores"))
                    listarJugadores();
                else if (partes.length >= 2 && partes[1].equalsIgnoreCase("avatares"))
                    listarAvatares();
                else if (partes.length >= 2 && partes[1].equalsIgnoreCase("enventa"))
                    listarVenta();
                else
                    System.out.println("Uso: listar jugadores | listar avatares | listar enventa");
                break;
            case "jugador":
                mostrarTurnoActual();
                break;


            case "lanzar":
                if (partes.length == 1) {
                    lanzarDados();
                } else if (partes.length == 3 && partes[1].equalsIgnoreCase("dados")) {
                    // lanzar dados X+Y
                    String[] nums = partes[2].split("\\+");
                    if (nums.length == 2) {
                        try {
                            int d1 = Integer.parseInt(nums[0]);
                            int d2 = Integer.parseInt(nums[1]);
                            lanzarDadosForzados(d1, d2);
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
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno"))
                    acabarTurno();
                else
                    System.out.println("Uso: acabar turno");
                break;
            case "describir":
                if (partes.length >= 3 && partes[1].equalsIgnoreCase("jugador"))
                    descJugador(partes);
                else if (partes.length >= 3 && partes[1].equalsIgnoreCase("avatar"))
                    descAvatar(partes[2]);
                else if (partes.length >= 2)
                    descCasilla(partes[1]);
                else
                    System.out.println("Uso: describir jugador <nombre> | describir avatar <id> | describir <casilla>");
                break;
            case "comprar":
                if (partes.length >= 2)
                    comprar(partes[1]);
                else
                    System.out.println("Uso: comprar <nombre_casilla>");
                break;
            case "salir":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("carcel"))
                    salirCarcel();
                else
                    System.out.println("Uso: salir carcel");
                break;
            case "ver":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("tablero"))
                    verTablero();
                else
                    System.out.println("Uso: ver tablero");
                break;
            default:
                System.out.println("Comando no reconocido");
        }
    }

    /* Método para leer comandos desde un fichero */
    public void leerComandosDesdeFichero(String nombreFichero) {
        try (Scanner fileScanner = new Scanner(new java.io.File(nombreFichero))) {
            while (fileScanner.hasNextLine()) {
                String comando = fileScanner.nextLine().trim();
                if (!comando.isEmpty()) {
                    System.out.println("$> " + comando);
                    analizarComando(comando);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: Fichero no encontrado: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }

    /*Crea un jugador con su tipo de avatar y lo coloca en la casilla de salida.*/
    private void crearJugador(String nombre, String tipoAvatar) {
        if (jugadores.size() >= 4) {
            System.out.println("Máximo 4 jugadores.");
            return;
        }
        
        // Buscar casilla de Salida (posición 1)
        Casilla salida = null;
        for (ArrayList<Casilla> lado : tablero.getCasillas()) {
            for (Casilla c : lado) {
                if (c.getPosicion() == 0 && c.getNombre().equalsIgnoreCase("Salida")) {
                    salida = c;
                    break;
                }
            }
            if (salida != null) break;
        }
        
        if (salida == null) {
            System.out.println("Error: No se encontró la casilla de Salida");
            return;
        }
        
        Jugador nuevo = new Jugador(nombre, tipoAvatar, salida, avatares);
        jugadores.add(nuevo);

        System.out.println("{");
        System.out.println("nombre: " + nombre + ",");
        System.out.println("avatar: " + nuevo.getAvatar().getId());
        System.out.println("}");
        
        verTablero();
    }


    /*Muestra el jugador que tiene el turno actual.*/
    private void mostrarTurnoActual() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        System.out.println("{");
        System.out.println("nombre: " + actual.getNombre() + ",");
        System.out.println("avatar: " + actual.getAvatar().getId());
        System.out.println("}");
    }


    /*Lanza los dados de forma aleatoria (comando: lanzar dados)*/
    private void lanzarDados() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        
        Jugador actual = jugadores.get(turno);
        int tirada1 = dado1.hacerTirada();
        int tirada2 = dado2.hacerTirada();
        int total = tirada1 + tirada2;
        
        System.out.println("{");
        System.out.println("Dados: " + tirada1 + " + " + tirada2 + " = " + total);
        System.out.println("Jugador: " + actual.getNombre());
        System.out.println("Avatar: " + actual.getAvatar().getId() + " avanza " + total + " posiciones");
        System.out.println("}");
        
        // Mover avatar por el tablero
        Avatar avatar = actual.getAvatar();
        Casilla casillaAnterior = avatar.getLugar();
        avatar.moverAvatar(tablero.getCasillas(), total);
        Casilla nuevaCasilla = avatar.getLugar();
        
        System.out.println("El avatar " + avatar.getId() + " avanza " + total + 
                        " posiciones, desde " + casillaAnterior.getNombre() + 
                        " hasta " + nuevaCasilla.getNombre() + ".");
        
        // Evaluar la casilla según las reglas del juego
        evaluarCasillaConReglas(actual, nuevaCasilla, total);
        
        verTablero(); // Repinta el tablero tras mover
    }

    /*Lanza los dados con valores forzados (comando: lanzar dados X+Y)*/
    private void lanzarDadosForzados(int d1, int d2) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        
        Jugador actual = jugadores.get(turno);
        int total = d1 + d2;
        
        System.out.println("{");
        System.out.println("Dados forzados: " + d1 + " + " + d2 + " = " + total);
        System.out.println("Jugador: " + actual.getNombre());
        System.out.println("Avatar: " + actual.getAvatar().getId() + " avanza " + total + " posiciones");
        System.out.println("}");
        
        // Mover avatar por el tablero
        Avatar avatar = actual.getAvatar();
        Casilla casillaAnterior = avatar.getLugar();
        avatar.moverAvatar(tablero.getCasillas(), total);
        Casilla nuevaCasilla = avatar.getLugar();
        
        System.out.println("El avatar " + avatar.getId() + " avanza " + total + 
                        " posiciones, desde " + casillaAnterior.getNombre() + 
                        " hasta " + nuevaCasilla.getNombre() + ".");
        
        // Evaluar la casilla según las reglas del juego
        evaluarCasillaConReglas(actual, nuevaCasilla, total);
        
        verTablero(); // Repinta el tablero tras mover
    }
    /* Evalúa la casilla según las reglas del juego */
    private void evaluarCasillaConReglas(Jugador jugador, Casilla casilla, int tirada) {
        String tipo = casilla.getTipo();
        
        switch(tipo) {
            case "Solar":
                if (casilla.getDuenho() != null && casilla.getDuenho() != banca && casilla.getDuenho() != jugador) {
                    float alquiler = 150000;
                    if (jugador.pagar(alquiler, casilla.getDuenho())) {
                        System.out.println("Se han pagado " + (int)alquiler + " € de alquiler.");
                    } else {
                        System.out.println("El jugador no tiene dinero suficiente. Debe hipotecar propiedades o declararse en bancarrota.");
                    }
                }
                break;
                
            case "Servicio":
                if (casilla.getDuenho() != null && casilla.getDuenho() != banca && casilla.getDuenho() != jugador) {
                    float pago = tirada * 4 * 50000;
                    if (jugador.pagar(pago, casilla.getDuenho())) {
                        System.out.println("Se han pagado " + (int)pago + " € por servicio.");
                    }
                }
                break;
                
            case "Transporte":
                if (casilla.getDuenho() != null && casilla.getDuenho() != banca && casilla.getDuenho() != jugador) {
                    float alquiler = 250000;
                    if (jugador.pagar(alquiler, casilla.getDuenho())) {
                        System.out.println("Se han pagado " + (int)alquiler + " € de transporte.");
                    }
                }
                break;
                
            case "IrACárcel":
                jugador.encarcelar(tablero.getCasillas());
                System.out.println("El avatar se coloca en la casilla de Cárcel.");
                break;
                
            case "Parking":
                System.out.println("El jugador " + jugador.getNombre() + " recibe X€ del Parking.");
                break;
                
            case "Impuesto":
                float impuesto = 2000000;
                if (jugador.pagar(impuesto, banca)) {
                    System.out.println("El jugador paga " + (int)impuesto + "€ que se depositan en el Parking.");
                }
                break;
                
            case "Suerte":
            case "Comunidad":
                // No se realiza ninguna acción como pide el punto 6
                break;
        }
    }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.*/
    private void comprar(String nombre) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c == null) {
            System.out.println("Casilla '" + nombre + "' no encontrada.");
            return;
        }
        // Verificar que la casilla se puede comprar
        if (c.getDuenho() != banca) {
            System.out.println("No se puede comprar " + nombre + ". Ya tiene dueño: " + c.getDuenho().getNombre());
            return;
        }
        // Verificar tipos de casillas que NO se pueden comprar
        if (!c.getTipo().equals("Solar") && !c.getTipo().equals("Transporte") && !c.getTipo().equals("Servicio")) {
            System.out.println("No se puede comprar " + nombre + ". Las casillas de tipo " + c.getTipo() + " no son comprables.");
            return;
        }
        // Obtener el valor REAL de la casilla
        float valorCasilla = c.getValor();
        // Verificar si el jugador tiene suficiente dinero
        if (actual.getFortuna() >= valorCasilla) {
            // Realizar la compra
            c.comprarCasilla(actual, banca);
            System.out.println("El jugador " + actual.getNombre() + " compra la casilla " + c.getNombre() + " por " + (int)valorCasilla + "€. Su fortuna actual es " + (int)actual.getFortuna() + "€.");
        } else {
            System.out.println("No tienes suficiente dinero para comprar " + nombre + ". Necesitas " + (int)valorCasilla + "€ pero solo tienes " + (int)actual.getFortuna() + "€.");
        }
    }
    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            if (actual.pagar(500000, banca)) {
                actual.setTiradasCarcel(0);
                Casilla salida = tablero.getCasillas().get(0).get(0);
                actual.getAvatar().setLugar(salida);
                System.out.println(actual.getNombre() + " paga 500.000€ y sale de la cárcel. Puede lanzar los dados.");
                verTablero();
            } else {
                System.out.println(actual.getNombre() + " no tiene dinero para salir de la cárcel.");
            }
        } else {
            System.out.println("No estás en la cárcel.");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        for (ArrayList<Casilla> lado : tablero.getCasillas()) {
            for (Casilla c : lado) {
                if (c.getDuenho() == banca) {
                    System.out.println(c.casEnVenta());
                }
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores todavía.");
            return;
        }
        
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            System.out.println("{");
            System.out.println("nombre: " + j.getNombre() + ",");
            System.out.println("avatar: " + j.getAvatar().getId() + ",");
            System.out.println("fortuna: " + (int)j.getFortuna() + ",");
            
            System.out.print("propiedades: [");
            ArrayList<Casilla> propiedades = j.getPropiedades();
            for (int k = 0; k < propiedades.size(); k++) {
                System.out.print(propiedades.get(k).getNombre());
                if (k < propiedades.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
            
            System.out.println("hipotecas: -");
            System.out.println("edificios: []");
            
            System.out.println(i < jugadores.size() - 1 ? "}," : "}");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
        if (avatares.isEmpty()) {
            System.out.println("No hay avatares creados.");
            return;
        }
        for (Avatar a : avatares) {
            System.out.println("{ id: " + a.getId() + ", tipo: " + a.getTipo() + 
                    ", jugador: " + a.getJugador().getNombre() + " }");
        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    /*Pasa el turno al siguiente jugador.*/
    private void acabarTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        Jugador siguiente = jugadores.get(turno);
        System.out.println("El jugador actual es " + siguiente.getNombre() + ".");
    }

    // Método para mostrar el tablero
    private void verTablero() {
        System.out.println(tablero.toString());
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.*/
    private void descJugador(String[] partes) {
        if (partes.length < 3) {
            System.out.println("Uso: describir jugador <nombre>");
            return;
        }
        String nombre = partes[2];
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("{");
                System.out.println("nombre: " + j.getNombre() + ",");
                System.out.println("avatar: " + j.getAvatar().getId() + ",");
                System.out.println("fortuna: " + (int)j.getFortuna() + ",");
                
                System.out.print("propiedades: [");
                ArrayList<Casilla> propiedades = j.getPropiedades();
                for (int i = 0; i < propiedades.size(); i++) {
                    System.out.print(propiedades.get(i).getNombre());
                    if (i < propiedades.size() - 1) System.out.print(", ");
                }
                System.out.println("]");
                
                System.out.println("hipotecas: -");
                System.out.println("edificios: []");
                System.out.println("}");
                return;
            }
        }
        System.out.println("Jugador no encontrado.");
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.*/
    private void descAvatar(String ID) {
        for (Avatar a : avatares) {
            if (a.getId().equalsIgnoreCase(ID)) {
                System.out.println(a + " del jugador " + a.getJugador().getNombre() +
                        " en " + a.getLugar().getNombre());
                return;
            }
        }
        System.out.println("Avatar no encontrado.");
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.*/
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null)
            System.out.println(c.infoCasilla());
        else
            System.out.println("Casilla no encontrada.");
    }
}