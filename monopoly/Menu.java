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
    private int turno = 0; //Índice correspondiente al jugador que tiene el turno
    private int lanzamientos; //Número de lanzamientos en el turno actual
    private Tablero tablero; //Tablero del juego
    private Dado dado1;
    private Dado dado2;
    private Jugador banca; //Jugador especial banca
    private boolean tirado;
    private boolean solvente;
    private Scanner sc; //Para leer comandos de consola

    //Constructor: inicia el juego y el bucle de comandos
    public Menu() {
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
        dado1 = new Dado();
        dado2 = new Dado();
        banca = new Jugador();
        tablero = new Tablero(banca);
        lanzamientos = 0;
        tirado = false;
        solvente = true;
        sc = new Scanner(System.in);
        System.out.println("=== MONOPOLY ETSE ===");
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
            case "comandos":
                if (partes.length >= 2)
                    ejecutarComandosFichero(partes[1]);
                else
                    System.out.println("Uso: comandos <fichero>");
                break;
            default:
                System.out.println("Comando no reconocido");
        }
    }

    /*Crea un jugador con su tipo de avatar y lo coloca en la casilla de salida.*/
    private void crearJugador(String nombre, String tipoAvatar) {
        if (jugadores.size() >= 4) {
            System.out.println("Máximo 4 jugadores.");
            return;
        }
        // Casilla de inicio (Salida)
        Casilla salida = tablero.getCasillas().get(0).get(0);
        // Crear jugador con el tipo de avatar y colocarlo en la salida
        Jugador nuevo = new Jugador(nombre, tipoAvatar, salida, avatares);
        jugadores.add(nuevo);
        System.out.println("{");
        System.out.println("nombre: " + nombre + ",");
        System.out.println("avatar: " + nuevo.getAvatar().getId());
        System.out.println("}");
        verTablero(); // Repinta el tablero tras crear jugador
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

    /*Lanza los dados y muestra la tirada.*/
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
        // calcular paso por Salida ANTES de mover
        int posAnteriorIdx = actual.getAvatar().getLugar().getPosicion() - 1;
        boolean pasaPorSalida = (posAnteriorIdx + total) >= 40;

        // Mover avatar por el tablero
        actual.getAvatar().moverAvatar(tablero.getCasillas(), total);
        Casilla nueva = actual.getAvatar().getLugar();

        // ingreso por Salida si procede y NO cae en IrACárcel
        if (pasaPorSalida && !nueva.getNombre().equalsIgnoreCase("IrACárcel")) {
            actual.sumarFortuna(2000000f);
        }

        System.out.println("Ahora estás en: " + nueva.getNombre());
        // pasar tablero a evaluarCasilla
        solvente = nueva.evaluarCasilla(actual, banca, total, tablero);
        if (!solvente) System.out.println("No puedes pagar, bancarrota!");
        tirado = true;
        verTablero(); // Repinta el tablero tras mover
    }

    // Lanzar dados con valores forzados
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
        // calcular paso por Salida ANTES de mover
        int posAnteriorIdx = actual.getAvatar().getLugar().getPosicion() - 1;
        boolean pasaPorSalida = (posAnteriorIdx + total) >= 40;

        actual.getAvatar().moverAvatar(tablero.getCasillas(), total);
        Casilla nueva = actual.getAvatar().getLugar();

        // ingreso por Salida si procede y NO cae en IrACárcel
        if (pasaPorSalida && !nueva.getNombre().equalsIgnoreCase("IrACárcel")) {
            actual.sumarFortuna(2000000f);
        }

        System.out.println("Ahora estás en: " + nueva.getNombre());
        // pasar tablero a evaluarCasilla
        solvente = nueva.evaluarCasilla(actual, banca, total, tablero);
        if (!solvente) System.out.println("No puedes pagar, bancarrota!");
        tirado = true;
        verTablero(); // Repinta el tablero tras mover
    }

    /*Lista todos los jugadores con su información básica.*/
    private void listarJugadores() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores todavía.");
            return;
        }
        for (Jugador j : jugadores) {
            System.out.println(j); // Asegúrate de que Jugador.toString() muestra todos los campos requeridos
        }
    }

    /*Lista todos los avatares creados.*/
    private void listarAvatares() {
        if (avatares.isEmpty()) {
            System.out.println("No hay avatares creados.");
            return;
        }
        for (Avatar a : avatares) {
            System.out.println(a); // Asegúrate de que Avatar.toString() muestra id, tipo y jugador
        }
    }

    /*Lista todas las casillas en venta. (La Parte 1 pide imprimir el tablero)*/
    private void listarVenta() {
        verTablero();
    }

    /*Pasa el turno al siguiente jugador.*/
    private void acabarTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        tirado = false;
        Jugador siguiente = jugadores.get(turno);
        System.out.println("{");
        System.out.println("El jugador actual es " + siguiente.getNombre() + ".");
        System.out.println("}");
    }

    /*Muestra la información de un jugador.*/
    private void descJugador(String[] partes) {
        if (partes.length < 3) {
            System.out.println("Uso: describir jugador <nombre>");
            return;
        }
        String nombre = partes[2];
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println(j); // Asegúrate de que Jugador.toString() muestra todos los campos requeridos
                return;
            }
        }
        System.out.println("Jugador no encontrado.");
    }

    /*Muestra la información de un avatar.*/
    private void descAvatar(String ID) {
        for (Avatar a : avatares) {
            if (a.getId().equalsIgnoreCase(ID)) {
                System.out.println(a); // Asegúrate de que Avatar.toString() muestra id, tipo, jugador, casilla
                return;
            }
        }
        System.out.println("Avatar no encontrado.");
    }

    /*Muestra la información de una casilla.*/
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null)
            System.out.println(c.infoCasilla()); // Asegúrate de que infoCasilla() muestra todos los campos requeridos
        else
            System.out.println("Casilla no encontrada.");
    }

    /*Permite comprar una casilla si está en venta.*/
    private void comprar(String nombre) {
        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null && c.getDuenho() == banca) {
            c.comprarCasilla(actual, banca);
            System.out.println("Has comprado: " + c.getNombre());
        } else {
            System.out.println("Casilla no encontrada o ya comprada");
        }
    }

    /*Permite salir de la cárcel pagando.*/
    private void salirCarcel() {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            actual.sumarFortuna(-500000); // paga 500.000€
            actual.setTiradasCarcel(0);
            actual.setEnCarcel(false);    // <<< NUEVO: marcar que ya no está en cárcel
            // Buscar casilla de salida
            Casilla salida = tablero.getCasillas().get(0).get(0);
            actual.getAvatar().setLugar(salida);
            System.out.println("Has salido de la cárcel.");
        } else {
            System.out.println("No estás en la cárcel.");
        }
    }

    // Mostrar el tablero en modo texto
    private void verTablero() {
        System.out.println(tablero.toString()); // Asegúrate de que Tablero.toString() imprime el tablero como pide el enunciado
    }

    // Ejecutar comandos desde fichero
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
