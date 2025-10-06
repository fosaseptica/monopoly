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

    // Jugador simple
    static class Jugador {
        String nombre;
        String avatar;
        int fortuna;
        List<String> propiedades = new ArrayList<>();
        List<String> hipotecas = new ArrayList<>();
        List<String> edificios = new ArrayList<>();

        Jugador(String nombre, String avatar) {
            this.nombre = nombre;
            this.avatar = avatar;
            this.fortuna = 15000000;
        }

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

    // Tablero simple
    static class Tablero {
        char generarAvatarAleatorio() {
            return (char) ('A' + (int)(Math.random() * 26));
        }

        void moverJugador(Jugador j, int avance) {
            System.out.println("{");
            System.out.println("avatar: " + j.avatar + " avanza " + avance + " posiciones");
            System.out.println("}");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Tablero tablero = new Tablero();
        Jugador[] jugadores = new Jugador[4];
        int numJugadores = 0;
        Jugador jugadorActual = null;

        System.out.println("=== MONOPOLY ETSE ===");

        while (true) {
            System.out.print("$> ");
            String cmd = sc.nextLine().trim();

            if (cmd.equalsIgnoreCase("salir")) break;

            else if (cmd.startsWith("crear jugador")) {
                if (numJugadores < 4) {
                    String[] partes = cmd.split(" ");
                    String nombre = partes[2];
                    char avatar = tablero.generarAvatarAleatorio();
                    Jugador nuevo = new Jugador(nombre, String.valueOf(avatar));
                    jugadores[numJugadores++] = nuevo;
                    if (numJugadores == 1) jugadorActual = nuevo;
                    System.out.println("{");
                    System.out.println("nombre: " + nombre + ",");
                    System.out.println("avatar: " + avatar);
                    System.out.println("}");
                } else System.out.println("Máximo 4 jugadores.");

            } else if (cmd.equalsIgnoreCase("listar jugadores")) {
                for (int i = 0; i < numJugadores; i++) jugadores[i].mostrar();

            } else if (cmd.equalsIgnoreCase("jugador")) {
                if (jugadorActual != null) {
                    System.out.println("{");
                    System.out.println("nombre: " + jugadorActual.nombre + ",");
                    System.out.println("avatar: " + jugadorActual.avatar);
                    System.out.println("}");
                }

            } else if (cmd.startsWith("lanzar dados")) {
                int dado1 = (int)(Math.random() * 6) + 1;
                int dado2 = (int)(Math.random() * 6) + 1;
                int avance = dado1 + dado2;
                System.out.println("{");
                System.out.println("Dados: " + dado1 + " + " + dado2 + " = " + avance);
                System.out.println("Jugador: " + jugadorActual.nombre);
                System.out.println("Avatar: " + jugadorActual.avatar + " avanza " + avance + " posiciones");
                System.out.println("}");
            }

            else if (cmd.equalsIgnoreCase("acabar turno")) {
                if (numJugadores > 0) {
                    int index = 0;
                    for (int i = 0; i < numJugadores; i++) if (jugadores[i] == jugadorActual) index = i;
                    jugadorActual = jugadores[(index + 1) % numJugadores];
                    System.out.println("{");
                    System.out.println("El jugador actual es " + jugadorActual.nombre + ".");
                    System.out.println("}");
                }

            } else System.out.println("Comando no reconocido.");
        }

        System.out.println("¡Gracias por jugar!");
        sc.close();
    }
}
