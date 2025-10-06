/*
package monopoly;

public class MonopolyETSE {

    public static void main(String[] args) {
        new Menu();
    }
    
}
*/

package monopoly;

import java.util.Scanner;

public class MonopolyETSE {

    // Clase interna Banca
    public static class Banca {
        // Puedes añadir atributos y métodos más adelante
    }

    // Clase interna Tablero mínima
    public static class Tablero {
        public void inicializarCasillas() {
            // Inicializa las casillas (vacío por ahora)
        }

        public void colocarJugadorEnSalida(Jugador j) {
            // Coloca al jugador en salida (vacío por ahora)
        }

        public void moverJugador(Jugador j, int avance) {
            // Mueve al jugador (vacío por ahora)
            System.out.println(j.getNombre() + " avanza " + avance + " casillas.");
        }

        public char generarAvatarAleatorio() {
            return (char) ('A' + (int) (Math.random() * 26));
        }
    }

    // Clase interna Jugador mínima
    public static class Jugador {
        private String nombre;
        private String avatar;
        private String tipoAvatar;
        private int fortuna;

        public Jugador(String nombre, String avatar, String tipoAvatar, int fortuna) {
            this.nombre = nombre;
            this.avatar = avatar;
            this.tipoAvatar = tipoAvatar;
            this.fortuna = fortuna;
        }

        public String getNombre() { return nombre; }
        public String getAvatar() { return avatar; }
        public int getFortuna() { return fortuna; }

        public String descripcionCompleta() {
            return "{nombre: " + nombre + ", avatar: " + avatar + ", tipo: " + tipoAvatar +
                    ", fortuna: " + fortuna + "}";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Crear la banca
        Banca banca = new Banca();

        // Inicializar el tablero
        Tablero tablero = new Tablero();
        tablero.inicializarCasillas();

        // Lista de jugadores
        Jugador[] jugadores = new Jugador[4];
        int numJugadores = 0;
        Jugador jugadorActual = null;

        System.out.println("=== MONOPOLY ETSE 2025-2026 ===");
        System.out.println("Introduce comandos para jugar. Escribe 'salir' para terminar.");

        boolean continuar = true;

        while (continuar) {
            System.out.print("$> ");
            String comando = sc.nextLine().trim();

            if (comando.equalsIgnoreCase("salir")) {
                continuar = false;

            } else if (comando.startsWith("crear jugador")) {
                String[] partes = comando.split(" ");
                if (partes.length == 4 && numJugadores < 4) {
                    String nombre = partes[2];
                    String tipoAvatar = partes[3];
                    char avatarChar = tablero.generarAvatarAleatorio();
                    String avatar = String.valueOf(avatarChar);

                    Jugador nuevo = new Jugador(nombre, avatar, tipoAvatar, 15000000);
                    jugadores[numJugadores++] = nuevo;

                    if (numJugadores == 1) jugadorActual = nuevo;

                    tablero.colocarJugadorEnSalida(nuevo);

                    System.out.println("{nombre: " + nombre + ", avatar: " + avatar + "}");
                } else {
                    System.out.println("Error al crear jugador. Máximo 4 jugadores.");
                }

            } else if (comando.equalsIgnoreCase("jugador")) {
                if (jugadorActual != null) {
                    System.out.println("{nombre: " + jugadorActual.getNombre() +
                            ", avatar: " + jugadorActual.getAvatar() + "}");
                }

            } else if (comando.equalsIgnoreCase("listar jugadores")) {
                for (int i = 0; i < numJugadores; i++) {
                    Jugador j = jugadores[i];
                    System.out.println("{nombre: " + j.getNombre() + ", avatar: " + j.getAvatar() +
                            ", fortuna: " + j.getFortuna() + "}");
                }

            } else if (comando.startsWith("describir jugador")) {
                String[] partes = comando.split(" ");
                if (partes.length == 3) {
                    String nombre = partes[2];
                    for (int i = 0; i < numJugadores; i++) {
                        if (jugadores[i].getNombre().equalsIgnoreCase(nombre)) {
                            System.out.println(jugadores[i].descripcionCompleta());
                        }
                    }
                }

            } else if (comando.startsWith("lanzar dados")) {
                int dado1, dado2, avance;
                try {
                    if (comando.contains("+")) {
                        String valores = comando.substring(12).trim();
                        String[] dados = valores.split("\\+");
                        dado1 = Integer.parseInt(dados[0]);
                        dado2 = Integer.parseInt(dados[1]);
                    } else {
                        dado1 = (int) (Math.random() * 6) + 1;
                        dado2 = (int) (Math.random() * 6) + 1;
                    }
                    avance = dado1 + dado2;
                    System.out.println("Dados: " + dado1 + " + " + dado2 + " = " + avance);
                    tablero.moverJugador(jugadorActual, avance);
                } catch (Exception e) {
                    System.out.println("Formato de comando incorrecto. Ej: lanzar dados 2+4");
                }

            } else if (comando.equalsIgnoreCase("acabar turno")) {
                if (numJugadores > 0) {
                    int indexActual = 0;
                    for (int i = 0; i < numJugadores; i++) {
                        if (jugadores[i] == jugadorActual) {
                            indexActual = i;
                            break;
                        }
                    }
                    jugadorActual = jugadores[(indexActual + 1) % numJugadores];
                    System.out.println("El jugador actual es " + jugadorActual.getNombre() + ".");
                }

            } else {
                System.out.println("Comando no reconocido.");
            }
        }

        System.out.println("Gracias por jugar a Monopoly ETSE.");
        sc.close();
    }
}


