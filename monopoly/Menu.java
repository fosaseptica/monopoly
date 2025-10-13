package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.Scanner;

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
        banca = new Jugador(); // la banca
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

    /*Método que interpreta el comando introducido y toma la acción correspondiente.*/
    private void analizarComando(String comando) {
        String[] partes = comando.split(" ");
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
                else
                    System.out.println("Uso: listar jugadores");
                break;
            case "jugador":
                mostrarTurnoActual();
                break;
            case "lanzar":
                lanzarDados();
                break;
            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno"))
                    acabarTurno();
                else
                    System.out.println("Uso: acabar turno");
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

        // Mover avatar por el tablero
        actual.getAvatar().moverAvatar(tablero.getCasillas(), total);
        Casilla nueva = actual.getAvatar().getLugar();

        System.out.println("Ahora estás en: " + nueva.getNombre());
        solvente = nueva.evaluarCasilla(actual, banca, total);

        if (!solvente)
            System.out.println("No puedes pagar, bancarrota!");

        tirado = true;
    }

    /*Lista todos los jugadores con su información básica.*/
    private void listarJugadores() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores todavía.");
            return;
        }

        for (Jugador j : jugadores) {
            System.out.println("{");
            System.out.println("nombre: " + j.getNombre() + ",");
            System.out.println("avatar: " + j.getAvatar().getId() + ",");
            System.out.println("fortuna: " + j.getFortuna() + ",");
            System.out.println("propiedades: " + (j.getPropiedades().isEmpty() ? "[]" : j.getPropiedades()));
            System.out.println("}");
        }
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
}
