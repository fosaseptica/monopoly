/*
package monopoly;

public class MonopolyETSE {

    public static void main(String[] args) {
        new Menu();
    }
    
}
*/
package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MonopolyETSE {

    // =========================
    // Clase Jugador
    // =========================
    // Representa un jugador del Monopoly.
    static class Jugador {
        String nombre;                   // Nombre del jugador
        String avatar;                   // Avatar del jugador (una letra)
        int fortuna;                      // Dinero del jugador
        List<String> propiedades = new ArrayList<>();  // Lista de propiedades que posee
        List<String> hipotecas = new ArrayList<>();    // Lista de propiedades hipotecadas
        List<String> edificios = new ArrayList<>();    // Lista de edificios (casas, hoteles, etc.)

        // Constructor: asigna nombre y avatar, y establece la fortuna inicial
        Jugador(String nombre, String avatar) {
            this.nombre = nombre;
            this.avatar = avatar;
            this.fortuna = 15000000; // Fortuna inicial
        }

        // Mostrar jugador en formato JSON simplificado
        void mostrar() {
            System.out.println("{");
            System.out.println("nombre: " + nombre + ",");
            System.out.println("avatar: " + avatar + ",");
            System.out.println("fortuna: " + fortuna + ",");
            System.out.println("propiedades: " + propiedades + ",");
            System.out.println("hipotecas: " + (hipotecas.isEmpty() ? "-" : hipotecas) + ",");
            System.out.println("edificios: " + edificios);
            System.out.println("}");
        }

        // Descripción completa del jugador (igual que mostrar, pero pensado para info más detallada)
        void descripcionCompleta() {
            System.out.println("{");
            System.out.println("nombre: " + nombre + ",");
            System.out.println("avatar: " + avatar + ",");
            System.out.println("fortuna: " + fortuna + ",");
            System.out.println("propiedades: " + propiedades + ",");
            System.out.println("hipotecas: " + (hipotecas.isEmpty() ? "-" : hipotecas) + ",");
            System.out.println("edificios: " + edificios);
            System.out.println("}");
        }
    }

    // =========================
    // Clase Tablero
    // =========================
    // Representa un tablero simplificado del Monopoly
    static class Tablero {

        // Genera un avatar aleatorio (letra A-Z) para el jugador
        char generarAvatarAleatorio() {
            return (char) ('A' + (int)(Math.random() * 26));
        }

        // Mueve al jugador un número de casillas y muestra en pantalla
        void moverJugador(Jugador j, int avance) {
            System.out.println("{");
            System.out.println("avatar: " + j.avatar + " avanza " + avance + " posiciones");
            System.out.println("}");
        }
    }

    // =========================
    // Método principal
    // =========================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);      // Para leer comandos del usuario
        Tablero tablero = new Tablero();           // Crear tablero
        Jugador[] jugadores = new Jugador[4];     // Array para almacenar hasta 4 jugadores
        int numJugadores = 0;                      // Contador de jugadores
        Jugador jugadorActual = null;             // Jugador que tiene el turno

        System.out.println("=== MONOPOLY ETSE ===");

        // Bucle principal de comandos
        while (true) {
            System.out.print("$> ");               // Prompt para el usuario
            String cmd = sc.nextLine().trim();    // Leer comando y eliminar espacios al inicio y fin

            // =========================
            // Comando salir
            // =========================
            if (cmd.equalsIgnoreCase("salir")) break;

            // =========================
            // Comando crear jugador
            // =========================
            else if (cmd.startsWith("crear jugador")) {
                if (numJugadores < 4) {                  // Solo se permiten hasta 4 jugadores
                    String[] partes = cmd.split(" ");   // Separar comando por espacios
                    String nombre = partes[2];          // Extraer nombre
                    char avatar = tablero.generarAvatarAleatorio(); // Generar avatar aleatorio
                    Jugador nuevo = new Jugador(nombre, String.valueOf(avatar));
                    jugadores[numJugadores++] = nuevo;  // Añadir jugador al array
                    if (numJugadores == 1) jugadorActual = nuevo; // El primer jugador es el actual
                    // Mostrar jugador creado en formato JSON
                    System.out.println("{");
                    System.out.println("nombre: " + nombre + ",");
                    System.out.println("avatar: " + avatar);
                    System.out.println("}");
                } else System.out.println("Máximo 4 jugadores.");
            }

            // =========================
            // Comando listar jugadores
            // =========================
            else if (cmd.equalsIgnoreCase("listar jugadores")) {
                for (int i = 0; i < numJugadores; i++) jugadores[i].mostrar();
            }

            // =========================
            // Comando jugador (turno actual)
            // =========================
            else if (cmd.equalsIgnoreCase("jugador")) {
                if (jugadorActual != null) {
                    System.out.println("{");
                    System.out.println("nombre: " + jugadorActual.nombre + ",");
                    System.out.println("avatar: " + jugadorActual.avatar);
                    System.out.println("}");
                }
            }

            // =========================
            // Comando lanzar dados
            // =========================
            else if (cmd.startsWith("lanzar dados")) {
                int dado1 = (int)(Math.random() * 6) + 1;  // Dado 1 aleatorio (1-6)
                int dado2 = (int)(Math.random() * 6) + 1;  // Dado 2 aleatorio (1-6)
                int avance = dado1 + dado2;                 // Suma de ambos dados
                System.out.println("{");
                System.out.println("Dados: " + dado1 + " + " + dado2 + " = " + avance);
                System.out.println("Jugador: " + jugadorActual.nombre);
                System.out.println("Avatar: " + jugadorActual.avatar + " avanza " + avance + " posiciones");
                System.out.println("}");
            }

            // =========================
            // Comando acabar turno
            // =========================
            else if (cmd.equalsIgnoreCase("acabar turno")) {
                if (numJugadores > 0) {
                    int index = 0;
                    // Buscar índice del jugador actual
                    for (int i = 0; i < numJugadores; i++) if (jugadores[i] == jugadorActual) index = i;
                    // Pasar turno al siguiente jugador
                    jugadorActual = jugadores[(index + 1) % numJugadores];
                    System.out.println("{");
                    System.out.println("El jugador actual es " + jugadorActual.nombre + ".");
                    System.out.println("}");
                }
            }

            // =========================
            // Comando no reconocido
            // =========================
            else System.out.println("Comando no reconocido.");
        }

        // Mensaje de despedida
        System.out.println("¡Gracias por jugar!");
        sc.close();
    }
}
