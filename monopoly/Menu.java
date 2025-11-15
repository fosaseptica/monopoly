package monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import partida.*;

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
            System.out.print("\n$> ");
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

            case "hipotecar":
                if (partes.length >= 2) {
                    hipotecarPropiedad(partes[1]); 
                } else {
                    System.out.println("Uso: hipotecar <nombre-propiedad>");
                }
                break;

            case "deshipotecar":
                if (partes.length >= 2) {
                    deshipotecarPropiedad(partes[1]);
                } else {
                    System.out.println("Uso: deshipotecar <nombre-propiedad>");
                }
                break;


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
                } else if (partes.length >= 3 && partes[1].equalsIgnoreCase("edificios")) {
                    // listar edificios <grupo>
                    listarEdificiosGrupo(partes[2]);
                } else {
                    System.out.println("Uso: listar jugadores | listar avatares | listar enventa | listar edificios [grupo]");
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
                } else if (partes.length >= 3 && partes[1].equalsIgnoreCase("casilla")) {
                    descCasilla(partes[2]);
                } else {
                    System.out.println("Uso: describir jugador <nombre> | describir avatar <id> | describir casilla <casilla>");
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
                if (partes.length >= 2) {
                    edificar(partes);  
                }else{
                    System.out.println("Uso: edificar <casa|hotel|piscina|pista_deporte>");
                }
                break;

            case "vender":
                if (partes.length >= 2) {
                    venderEdificio(partes);
                } else {
                    System.out.println("Uso: vender <tipo_edificio> <nombre_solar> <cantidad>");
                }
                break;

            default:
                System.out.println("Comando no reconocido");
        }
    }

    // ============================== EDIFICAR ==============================


    private void edificar(String[] partes) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casillaActual = jugadorActual.getAvatar().getLugar();
        
        if (partes.length < 2) {
            System.out.println("Uso: edificar <casa|hotel|piscina|pista_deporte>");
            return;
        }
        
        String tipoEdificio = partes[1];
        
        // Procesar tipo de edificio con espacios
        if (partes.length >= 3) {
            StringBuilder sb = new StringBuilder(tipoEdificio);
            for (int i = 2; i < partes.length; i++) {
                sb.append("_").append(partes[i]);
            }
            tipoEdificio = sb.toString();
        }
        
        // Todas las verificaciones se hacen dentro de casillaActual.edificar()
        casillaActual.edificar(tipoEdificio, jugadorActual);
    }


    // ============================== HIPOTECAR ==============================

    private void hipotecarPropiedad(String nombrePropiedad) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        
        Jugador jugadorActual = jugadores.get(turno);
        
        // 1. Buscar la propiedad
        Casilla propiedad = tablero.encontrar_casilla(nombrePropiedad);
        if (propiedad == null) {
            System.out.println("Error: " + nombrePropiedad + " no existe.");
            return;
        }
        
        // 2. Verificar que es una propiedad comprable
        String tipo = propiedad.getTipo();
        boolean esPropiedad = tipo.equalsIgnoreCase("Solar") || tipo.equalsIgnoreCase("Servicio") || tipo.equalsIgnoreCase("Transporte");
        if (!esPropiedad) {
            System.out.println("Error: " + nombrePropiedad + " no es una propiedad hipotecable.");
            return;
        }
        
        // 3. Verificar dueño
        if (propiedad.getDuenho() != jugadorActual) {
            System.out.println(jugadorActual.getNombre() + " no puede hipotecar " + nombrePropiedad + ". No es una propiedad que le pertenece.");
            return;
        }
        
        // 4. Verificar si ya está hipotecada
        if (propiedad.estaHipotecada()) {
            System.out.println(jugadorActual.getNombre() + " no puede hipotecar " + nombrePropiedad +  ". Ya está hipotecada.");
            return;
        }

        // 5. VERIFICACIÓN DE EDIFICIOS EN SOLARES. Si es solar y tiene edificios se deben vender antes de hipotecar. Se da el aviso
        if (tipo.equalsIgnoreCase("Solar")) {
            // Verificar si hay edificios construidos
            boolean tieneEdificios = propiedad.getNumCasas() > 0 || propiedad.getNumHoteles() > 0 || propiedad.getNumPiscinas() > 0 || propiedad.getNumPistas() > 0;
            
            if (tieneEdificios) {
                System.out.println("Error: No se puede hipotecar " + nombrePropiedad + " porque contiene edificios. Debes vender todos los edificios primero.");
                return;
            }
        }
        

        // 6. Verificar si pierde monopolio usando la función existente
        String mensajeGrupo = "";
        if (tipo.equalsIgnoreCase("Solar")) {
            int n = propiedad.numeroSolar();
            boolean teniaMonopolio = propiedad.duenhoTieneMonopolio(tablero.getCasillas(), n, jugadorActual);
            
            if (teniaMonopolio) {
                mensajeGrupo = " ni edificar en el grupo " + propiedad.getGrupoColor() + ".";
            }
        }
        
        // 7. Ejecutar hipoteca
        float valorHipoteca = propiedad.getValor() / 2; // Mitad del valor
        propiedad.setHipotecada(true);
        jugadorActual.sumarFortuna(valorHipoteca);
        
        System.out.println(jugadorActual.getNombre() + " recibe " + formatearCantidad(valorHipoteca) + "€ por la hipoteca de " + nombrePropiedad + ". No puede recibir alquileres" + mensajeGrupo);
    }

    // ============================== DESHIPOTECAR ==============================

    private void deshipotecarPropiedad(String nombrePropiedad) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        
        Jugador jugadorActual = jugadores.get(turno);
        Casilla propiedad = tablero.encontrar_casilla(nombrePropiedad);
        
        if (propiedad == null) {
            System.out.println("Error: " + nombrePropiedad + " no existe.");
            return;
        }
        
        // Verificaciones
        if (propiedad.getDuenho() != jugadorActual) {
            System.out.println(jugadorActual.getNombre() + " no puede deshipotecar " + nombrePropiedad + ". No es una propiedad que le pertenece.");
            return;
        }
        
        if (!propiedad.estaHipotecada()) {
            System.out.println(jugadorActual.getNombre() + " no puede deshipotecar " + nombrePropiedad + ". No está hipotecada.");
            return;
        }
        
        // Calcular costo (valor hipoteca + 10%)
        float costoDeshipoteca = (propiedad.getValor() / 2) * 1.10f;
        
        if (jugadorActual.getFortuna() < costoDeshipoteca) {
            System.out.println(jugadorActual.getNombre() + " no puede deshipotecar " + nombrePropiedad + ". No tiene suficiente dinero (" + formatearCantidad(costoDeshipoteca) + "€ necesarios).");
            return;
        }
        
         // Verificar si al deshipotecar se recupera el monopolio usando la función existente
        String mensajeGrupo = "";
        if (propiedad.getTipo().equalsIgnoreCase("Solar")) {
            int n = propiedad.numeroSolar();
            boolean tieneMonopolio = propiedad.duenhoTieneMonopolio(tablero.getCasillas(), n, jugadorActual);
            
            if (tieneMonopolio) {
                mensajeGrupo = " Ahora puede edificar en el grupo " + propiedad.getGrupoColor() + ".";
            }
        }
            
        // Ejecutar deshipoteca
        propiedad.setHipotecada(false);
        jugadorActual.sumarFortuna(-costoDeshipoteca);
        
        System.out.println(jugadorActual.getNombre() + " ha deshipotecado " + nombrePropiedad + " por " + formatearCantidad(costoDeshipoteca) + "€. Ahora puede recibir alquileres." + mensajeGrupo);
    }
           
    // ============================== VENDER EDIFICIOS ==============================
    //vender edificio
    private void venderEdificio(String[] partes) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        
        Jugador jugadorActual = jugadores.get(turno);
        
        // Verificar formato mínimo: vender <tipo> <solar> [cantidad]
        if (partes.length < 3) {
            System.out.println("Uso: vender <casa|hotel|piscina|pista_deporte> <solar> [cantidad]");
            return;
        }
        
        // Procesar tipo de edificio con espacios
        String tipoEdificio = partes[1];
        int inicioSolar = 2;
        
        // Manejar tipos de edificio con espacios (como "pista_deporte")
        if (partes.length >= 4) {
            // Verificar si los siguientes tokens son parte del tipo de edificio
            StringBuilder sb = new StringBuilder(tipoEdificio);
            for (int i = 2; i < partes.length - 1; i++) {
                if (!partes[i].matches("Solar\\d+")) {
                    sb.append("_").append(partes[i]);
                    inicioSolar = i + 1;
                } else {
                    break;
                }
            }
            tipoEdificio = sb.toString();
        }
        
        // Obtener nombre del solar
        String nombreSolar = partes[inicioSolar];
        
        // Obtener cantidad (por defecto 1)
        int cantidad = 1;
        if (partes.length > inicioSolar + 1) {
            try {
                cantidad = Integer.parseInt(partes[inicioSolar + 1]);
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad no válida: " + partes[inicioSolar + 1]);
                return;
            }
        }
        
        // Buscar la casilla
        Casilla propiedad = tablero.encontrar_casilla(nombreSolar);
        if (propiedad == null) {
            System.out.println("Error: " + nombreSolar + " no existe.");
            return;
        }
        
        // Verificar que es un solar
        if (!propiedad.getTipo().equalsIgnoreCase("Solar")) {
            System.out.println("Error: " + nombreSolar + " no es un solar.");
            return;
        }
        
        // Ejecutar la venta
        propiedad.venderEdificio(tipoEdificio, cantidad, jugadorActual);
    }

    // ============================== LANZAR DADOS ==============================

    // En Menu.java - MODIFICA el método lanzarDados

    private void lanzarDados(int d1, int d2) {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = jugadores.get(turno);

        // Verificar si está en la cárcel
        if (actual.isEnCarcel()) {
            System.out.println(actual.getNombre() + " está en la cárcel. Usa 'salir carcel' para intentar salir.");
            return;
        }

        // Bloquea si ya tiró y no hay dobles pendientes
        if (tirado && lanzamientos == 0) {
            System.out.println("Ya has tirado en este turno. Debes acabar el turno.");
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
            System.out.println("¡Dobles! (" + d1 + "," + d2 + ")");
        } else {
            lanzamientos = 0;
        }

        // ---- Tres dobles consecutivos ----
        if (lanzamientos == 3) {
            System.out.println("¡Tres dobles seguidos! Vas directamente a la cárcel.");
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
        System.out.println("Avatar: " + actual.getAvatar().getId());
        
        // Obtener posición anterior
        Casilla posAnterior = actual.getAvatar().getLugar();
        int posAnteriorIdx = posAnterior.getPosicion();
        
        System.out.println("Desde: " + posAnterior.getNombre() + " (posición " + posAnteriorIdx + ")");
        
        // Mover avatar
        actual.getAvatar().moverAvatar(tablero.getCasillas(), total);
        Casilla nueva = actual.getAvatar().getLugar();
        
        System.out.println("Hasta: " + nueva.getNombre() + " (posición " + nueva.getPosicion() + ")");
        System.out.println("}");

        // Verificar si pasó por salida
        boolean pasaPorSalida = (posAnteriorIdx + total) >= 40;
        if (pasaPorSalida && !nueva.getNombre().equalsIgnoreCase("IrACárcel")) {
            actual.sumarFortuna(2000000f);
            System.out.println("¡Has pasado por la salida! Recibes 2.000.000€");
        }

        // ---- Procesar la casilla donde cayó ----
        System.out.println("--- Acciones en " + nueva.getNombre() + " ---");
        boolean solvente = nueva.evaluarCasilla(actual, banca, total, tablero, jugadores);
        
        if (!solvente) {
            System.out.println("¡BANCARROTA! " + actual.getNombre() + " no puede pagar sus deudas.");
            // Aquí manejarías la bancarrota (eliminar jugador, transferir propiedades, etc.)
        }

        // ---- Dobles para tirar de nuevo ----
        if (d1 == d2 && lanzamientos < 3) {
            System.out.println("¡Has sacado dobles! Puedes tirar de nuevo.");
            tirado = false; // Permite tirar otra vez
        } else {
            tirado = true;
            if (lanzamientos >= 3) {
                lanzamientos = 0; // Reset después de 3 dobles
            }
        }

        verTablero();
    }


    
    // ============================== COMPRAR ==============================

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
        boolean comprable = t.equalsIgnoreCase("Solar") || t.equalsIgnoreCase("Servicio") ||t.equalsIgnoreCase("Transporte");
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

        /*
        // Intento de compra
        c.comprarCasilla(actual, banca);
        if (c.getDuenho() == actual) {
            System.out.println("Has comprado: " + c.getNombre());
            verTablero();
        } else {
            System.out.println("No tienes suficiente dinero para comprar " + c.getNombre() + ".");
        }
        */
    }

    // ============================== SALIR CARCEL ==============================

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            // Verificar que tiene suficiente dinero
            if (actual.getFortuna() < 500000) {
                System.out.println("No tienes suficiente dinero para pagar la fianza de 500.000€.");
                return;
            }
            
            //  DEPURACIÓN: Ver posición antes
            Casilla posicionActual = actual.getAvatar().getLugar();
            System.out.println("DEBUG: Posición actual: " + posicionActual.getNombre());
            
            // Pagar fianza
            actual.sumarFortuna(-500000);
            actual.setTiradasCarcel(0);
            actual.setEnCarcel(false);
            
            //  NO MOVER - el avatar se queda donde está
            System.out.println("DEBUG: Posición después: " + actual.getAvatar().getLugar().getNombre());
            
            System.out.println(actual.getNombre() + " paga 500.000€ y sale de la cárcel. Puede lanzar los dados.");
        } else {
            System.out.println("No estás en la cárcel.");
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  LISTAR
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        ArrayList<String> bloques = new ArrayList<>();
        for (ArrayList<Casilla> lado : tablero.getCasillas()) {
            for (Casilla c : lado) {
                String tipo = c.getTipo();
                boolean comprable = tipo.equalsIgnoreCase("Solar") || tipo.equalsIgnoreCase("Servicio") || tipo.equalsIgnoreCase("Transporte");
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
    

    //listar edificios
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

        // Imprimir todos los bloques
        if (bloques.isEmpty()) {
            System.out.println("No hay edificios construidos.");
            return;
        }
        
        System.out.println("[");
        for (int i = 0; i < bloques.size(); i++) {
            System.out.println(bloques.get(i));
            if (i < bloques.size() - 1) {
                System.out.println(",");
            }
        }
        System.out.println("]");
    }

    // Método para listar edificios por grupo
    private void listarEdificiosGrupo(String colorGrupo) {
        ArrayList<String> bloques = new ArrayList<>();
        boolean sePuedeEdificarPista = false;

        // Buscar todas las propiedades del grupo
        for (ArrayList<Casilla> lado : tablero.getCasillas()) {
            for (Casilla c : lado) {
                if (c.getTipo().equalsIgnoreCase("Solar") && c.getGrupoColor().equalsIgnoreCase(colorGrupo)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{\n");
                    sb.append(" propiedad: ").append(c.getNombre()).append(",\n");
                    
                    // Hoteles
                    if (c.getNumHoteles() > 0) {
                        sb.append(" hoteles: [");
                        for (int i = 1; i <= c.getNumHoteles(); i++) {
                            sb.append("hotel-").append(i);
                            if (i < c.getNumHoteles()) sb.append(", ");
                        }
                        sb.append("],\n");
                    } else {
                        sb.append(" hoteles: -,\n");
                    }
                    
                    // Casas
                    if (c.getNumCasas() > 0) {
                        sb.append(" casas: [");
                        for (int i = 1; i <= c.getNumCasas(); i++) {
                            sb.append("casa-").append(i);
                            if (i < c.getNumCasas()) sb.append(", ");
                        }
                        sb.append("],\n");
                    } else {
                        sb.append(" casas: -,\n");
                    }
                    
                    // Piscinas
                    if (c.getNumPiscinas() > 0) {
                        sb.append(" piscinas: [");
                        for (int i = 1; i <= c.getNumPiscinas(); i++) {
                            sb.append("piscina-").append(i);
                            if (i < c.getNumPiscinas()) sb.append(", ");
                        }
                        sb.append("],\n");
                    } else {
                        sb.append(" piscinas: -,\n");
                        // Si no tiene piscina pero tiene hotel, se puede construir
                        if (c.getNumHoteles() > 0) sePuedeEdificarPista = true;
                    }
                    
                    // Pistas de deporte
                    if (c.getNumPistas() > 0) {
                        sb.append(" pistasDeDeporte: [");
                        for (int i = 1; i <= c.getNumPistas(); i++) {
                            sb.append("pista-").append(i);
                            if (i < c.getNumPistas()) sb.append(", ");
                        }
                        sb.append("],\n");
                    } else {
                        sb.append(" pistasDeDeporte: -,\n");
                    }
                    
                    // Alquiler
                    int n = c.numeroSolar();
                    sb.append(" alquiler: ").append((int)c.alquilerTotal(n)).append("\n");
                    sb.append("}");
                    
                    bloques.add(sb.toString());
                }
            }
        }

        if (bloques.isEmpty()) {
            System.out.println("No se encontraron propiedades del grupo " + colorGrupo + ".");
            return;
        }
        
        // Imprimir bloques
        System.out.println("[");
        for (int i = 0; i < bloques.size(); i++) {
            System.out.println(bloques.get(i));
            if (i < bloques.size() - 1) {
                System.out.println(",");
            }
        }
        System.out.println("]");
        
        // Mensaje sobre qué se puede edificar
        if (sePuedeEdificarPista) {
            System.out.println("Aún se puede edificar una pista");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  DESCRIBIR
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    *LOGICA EN casilla.java
    */
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) {
            System.out.println(c.infoCasilla()); // infoCasilla() debe mostrar los campos requeridos
        } else {
            System.out.println("Casilla no encontrada.");
        }
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

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
    *LOGICA EN jugador.java
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
                    System.out.println("\n$> " + linea);
                    analizarComando(linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo abrir el fichero: " + fichero);
        }
    }


}



