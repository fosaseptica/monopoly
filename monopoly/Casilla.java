package monopoly;

import java.util.ArrayList;
import partida.*;

public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (precio de compra para solares).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private float precioCasa; //Precio para construir una casa
    private float precioHotel; //Precio para construir un hotel
    private float precioPiscina; //Precio para construir una piscina
    private float precioPistaDeporte; //Precio para construir una pista de deporte
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    private int numCasas = 0;
    private int numHoteles = 0;
    private int numPiscinas = 0;
    private int numPistas = 0;

    // Contadores para llevar el seguimiento de las cartas
    private static int contadorSuerte = 0;
    private static int contadorComunidad = 0;


    //Constructores:
    public Casilla() {
        this.avatares = new ArrayList<>();
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar:
    * Parámetros: nombre casilla, posición en el tablero, precio, hipoteca, precio casa,
    * precio hotel, precio piscina, precio pista deporte y dueño.
     */
    public Casilla(String nombre, int posicion, float valor, float hipoteca, float precioCasa,
                   float precioHotel, float precioPiscina, float precioPistaDeporte, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "Solar";
        this.posicion = posicion;
        this.valor = valor;
        this.hipoteca = hipoteca;
        this.precioCasa = precioCasa;
        this.precioHotel = precioHotel;
        this.precioPiscina = precioPiscina;
        this.precioPistaDeporte = precioPistaDeporte;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    /*Constructor para casillas tipo Servicios o Transporte:
    * Parámetros: nombre casilla, tipo (debe ser serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
    * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "Impuesto";
        this.posicion = posicion;
        this.impuesto = impuesto;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public Grupo getGrupo() { return grupo; }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
    * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
    }

    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  AÑADIR Y ELIMINAR
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (!avatares.contains(av)) {
            avatares.add(av);
        }
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        avatares.remove(av);
    }

    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  COMPRAR
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (this.duenho == banca && solicitante.getFortuna() >= valor) {
            solicitante.pagar(valor, banca);
            this.duenho = solicitante;
            solicitante.anhadirPropiedad(this);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  UTILES
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Determina el número de Solar (SolarX -> X), o -1 si no es solar
    public int numeroSolar() {
        if (!nombre.startsWith("Solar")) return -1;
        String d = nombre.replaceAll("\\D", "");
        if (d.isEmpty()) return -1;
        return Integer.parseInt(d);
    }

    // Nombre del grupo (color) por rango de SolarX 
    private String nombreGrupo() {
        int n = numeroSolar();
        if (n < 0) return "-";
        if (n <= 2) return "Marrón";
        if (n <= 5) return "Cian";
        if (n <= 7) return "Rosa";
        if (n <= 10) return "Naranja";
        if (n <= 13) return "Rojo";
        if (n <= 16) return "Amarillo";
        if (n <= 19) return "Verde";
        return "Azul";
    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta(Jugador banca) {
        if (duenho == banca) return nombre + " está en venta por " + valor + "€";
        return nombre + " ya tiene dueño (" + duenho.getNombre() + ")";
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  EVALUAR CASILLA. ACCIONES AL MOVERSE ENTRE CASILLAS
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Procesa todas las acciones cuando un jugador cae en esta casilla
     * @param jugadorActual El jugador que cayó en la casilla
     * @param banca El jugador banca
     * @param valorTirada El valor total de los dados
     * @param tablero El tablero completo
     * @return true si el jugador sigue siendo solvente, false si entra en bancarrota
     */
    public boolean evaluarCasilla(Jugador jugadorActual, Jugador banca, int valorTirada, Tablero tablero, ArrayList<Jugador> todosJugadores) {
        System.out.println("Procesando casilla: " + this.nombre + " (" + this.tipo + ")");
        
        // Casillas especiales que requieren acción inmediata
        if (this.nombre.equalsIgnoreCase("IrACárcel")) {
            System.out.println("¡Vas a la cárcel!");
            jugadorActual.encarcelar(tablero.getCasillas());
            return true;
        }
        
        if (this.nombre.equalsIgnoreCase("Parking")) {
            float bote = tablero.cobrarBote();
            jugadorActual.sumarFortuna(bote);
            System.out.println(jugadorActual.getNombre() + " recibe el bote de Parking: " + (int)bote + "€");
            return true;
        }
        
        if (this.nombre.equalsIgnoreCase("Salida")) {
            // No hay acción específica al caer en salida, solo al pasar por ella
            return true;
        }
        
        if (this.nombre.equalsIgnoreCase("Cárcel")) {
            // Solo de visita - no hay acción
            System.out.println("Estás de visita en la cárcel");
            return true;
        }
        
        // Casillas de impuesto
        if (this.tipo.equalsIgnoreCase("Impuesto")) {
            if (jugadorActual.getFortuna() >= this.impuesto) {
                jugadorActual.sumarFortuna(-this.impuesto);
                tablero.anadirAlBote(this.impuesto);
                System.out.println(jugadorActual.getNombre() + " paga " + (int)this.impuesto + "€ de impuestos al Parking");
                return true;
            } else {
                System.out.println(jugadorActual.getNombre() + " no puede pagar los impuestos de " + (int)this.impuesto + "€");
                return false; // Bancarrota
            }
        }
        
         // Casillas de Suerte y Comunidad
        if (this.tipo.equalsIgnoreCase("Suerte") || this.tipo.equalsIgnoreCase("Comunidad")) {
            System.out.println("Casilla de " + this.tipo + " - Seleccionando carta...");
            return procesarCarta(jugadorActual, tablero, todosJugadores);
        }

        // Propiedades (Solar, Servicio, Transporte)
        if (this.tipo.equalsIgnoreCase("Solar") || this.tipo.equalsIgnoreCase("Servicio") || this.tipo.equalsIgnoreCase("Transporte")) {
            return procesarPropiedad(jugadorActual, banca, valorTirada, tablero.getCasillas());
        }
        
        return true; // Por defecto, solvente
    }

    /**
     * Procesa propiedades (Solar, Servicio, Transporte)
     */
    private boolean procesarPropiedad(Jugador jugadorActual, Jugador banca, int valorTirada, ArrayList<ArrayList<Casilla>> tablero) {
        // Si no tiene dueño o es la banca, se puede comprar
        if (this.duenho == null || this.duenho == banca) {
            System.out.println(this.nombre + " está en venta por " + (int)this.valor + "€");
            // La compra se maneja con el comando "comprar" por separado
            return true;
        }
        
        // Si el dueño es el propio jugador, no hay acción
        if (this.duenho == jugadorActual) {
            System.out.println("Eres el dueño de " + this.nombre);
            return true;
        }
        
        // Si está hipotecada, no se paga alquiler
        if (this.estaHipotecada()) {
            System.out.println(this.nombre + " está hipotecada. No se paga alquiler.");
            return true;
        }
        
        // Calcular alquiler según el tipo de propiedad
        float alquiler = calcularAlquiler(valorTirada, tablero);
        
        if (alquiler > 0) {
            System.out.println(jugadorActual.getNombre() + " debe pagar " + (int)alquiler + "€ de alquiler a " + this.duenho.getNombre());
            return jugadorActual.pagar(alquiler, this.duenho);
        }
        
        return true;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  ALQUILER 
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Calcula el alquiler según el tipo de propiedad
     */
    private float calcularAlquiler(int valorTirada, ArrayList<ArrayList<Casilla>> tablero) {
        switch (this.tipo) {
            case "Solar":
                return calcularAlquilerSolar(tablero);
                
            case "Servicio":
                // Parte 1: siempre 4 × valorTirada × 50000 (sin contar cuántos servicios tiene)
                return 4 * valorTirada * 50000f;
                
            case "Transporte":
                // Parte 1: siempre 250000 fijos (sin contar cuántos transportes tiene)
                return 250000f;
                
            default:
                return 0f;
        }
    }

    /**
     * Calcula alquiler para solares
     */
    private float calcularAlquilerSolar(ArrayList<ArrayList<Casilla>> tablero) {
        int n = this.numeroSolar();
        if (n < 0) return 0f;
        
        float alquiler = this.alquilerTotal(n);
        
        boolean tieneMonopolio = duenhoTieneMonopolio(tablero, n, this.duenho);
        boolean tieneEdificios = (this.numCasas > 0 || this.numHoteles > 0 || this.numPiscinas > 0 || this.numPistas > 0);
        
        if (tieneMonopolio && !tieneEdificios) {
            alquiler *= 2f;
            System.out.println("¡Monopolio! Alquiler doblado por no tener edificios");
        }
        
        return alquiler;
    }

    /**
     * Calcula el alquiler total de una Solar teniendo en cuenta los edificios construidos.
     */
    public float alquilerTotal(int n) {
        float base = alquilerBaseSolar(n);
        if (base <= 0f) return 0f;

        float total = base;
        total += numCasas * (base * 0.5f);
        total += numHoteles * (base * 2.0f);
        total += numPiscinas * (base * 0.25f);
        total += numPistas * (base * 0.30f);

        return total;
    }

    /**
     * Alquiler base de SolarX según el Apéndice I
     */
    private float alquilerBaseSolar(int n) {
        switch (n) {
            case 1:  return 20000f;
            case 2:  return 40000f;
            case 3:  return 60000f;
            case 4:  return 60000f;
            case 5:  return 80000f;
            case 6:  return 100000f;
            case 7:  return 100000f;
            case 8:  return 120000f;
            case 9:  return 140000f;
            case 10: return 140000f;
            case 11: return 160000f;
            case 12: return 180000f;
            case 13: return 180000f;
            case 14: return 200000f;
            case 15: return 220000f;
            case 16: return 220000f;
            case 17: return 240000f;
            case 18: return 260000f;
            case 19: return 260000f;
            case 20: return 280000f;
            case 21: return 350000f;
            case 22: return 500000f;
            default: return 0f;
        }
    }

    /**
     * Comprueba monopolio por rangos con el tablero ya creado
     */
    public boolean duenhoTieneMonopolio(ArrayList<ArrayList<Casilla>> casillas, int n, Jugador dueno) {
        if (n < 0) return false;
        int inicio, fin;
        if (n <= 2)      { inicio = 1;  fin = 2;  }
        else if (n <= 5) { inicio = 3;  fin = 5;  }
        else if (n <= 7) { inicio = 6;  fin = 7;  }
        else if (n <= 10){ inicio = 8;  fin = 10; }
        else if (n <= 13){ inicio = 11; fin = 13; }
        else if (n <= 16){ inicio = 14; fin = 16; }
        else if (n <= 19){ inicio = 17; fin = 19; }
        else             { inicio = 20; fin = 22; }

        int encontrados = 0;
        int delDueno = 0;
        for (ArrayList<Casilla> lado : casillas) {
            for (Casilla c : lado) {
                if (c.getNombre().startsWith("Solar")) {
                    String d = c.getNombre().replaceAll("\\D", "");
                    if (!d.isEmpty()) {
                        int num = Integer.parseInt(d);
                        if (num >= inicio && num <= fin) {
                            encontrados++;
                            if (c.getDuenho() == dueno) delDueno++;
                        }
                    }
                }
            }
        }
        return encontrados > 0 && encontrados == delDueno;
    }

 



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  DESCRIBIR CASILLA
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Método para mostrar información sobre una casilla.
    * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {
        StringBuilder sb = new StringBuilder();
        sb.append("Casilla ").append(posicion).append(": ").append(nombre).append(" (").append(tipo).append(")\n");
        if (duenho != null) sb.append("Dueño: ").append(duenho.getNombre()).append("\n");
        sb.append("Valor: ").append(valor).append("€\n");
        if (tipo.equalsIgnoreCase("Solar")) {
            sb.append("Grupo: ").append(nombreGrupo()).append("\n");
            sb.append("Hipoteca: ").append(hipoteca).append("€\n");
            sb.append("Precio casa: ").append(precioCasa).append("€\n");
            sb.append("Precio hotel: ").append(precioHotel).append("€\n");
            sb.append("Precio piscina: ").append(precioPiscina).append("€\n");
            sb.append("Precio pista deporte: ").append(precioPistaDeporte).append("€\n");

            int totalEdificios = numCasas + numHoteles + numPiscinas + numPistas;
            if (totalEdificios == 0) {
                sb.append("No hay edificios construidos.\n");
            } else {
                sb.append("Edificios construidos:\n");
                if (numCasas > 0) sb.append(" - Casas: ").append(numCasas).append("\n");
                if (numHoteles > 0) sb.append(" - Hoteles: ").append(numHoteles).append("\n");
                if (numPiscinas > 0) sb.append(" - Piscinas: ").append(numPiscinas).append("\n");
                if (numPistas > 0) sb.append(" - Pistas de deporte: ").append(numPistas).append("\n");
            }
            int n = numeroSolar();
            sb.append("Alquiler total actual: ").append(alquilerTotal(n)).append("€\n");
        } else if (tipo.equalsIgnoreCase("Servicio")) {
            sb.append("Alquiler (Parte 1): 4 × tirada × 50000\n");
        } else if (tipo.equalsIgnoreCase("Transporte")) {
            sb.append("Alquiler (Parte 1): 250000€\n");
        } else if (nombre.equalsIgnoreCase("Parking")) {
            sb.append("Bote acumulado: (se muestra al describir desde Tablero)\n");
        } else {
            sb.append("Impuesto: ").append(impuesto).append("€\n");
        }
        sb.append("Avatares: ");
        for (Avatar av : avatares) {
            sb.append(av.toString()).append(" ");
        }
        return sb.toString();
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  EDIFICAR
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    public void edificar(String tipoEdificio, Jugador jugador) {
        if (!tipo.equalsIgnoreCase("Solar")) {
            System.out.println("Solo se puede edificar en solares.");
            return;
        }

        // VERIFICACIÓN 1: ¿Es del jugador?
        if (this.duenho != jugador) {
            System.out.println("No puedes edificar en " + this.nombre + ". No es tu propiedad.");
            return;
        }

        // VERIFICACIÓN 2: ¿Está hipotecada?
        if (this.estaHipotecada()) {
            System.out.println("No puedes edificar en " + this.nombre + ". La propiedad está hipotecada.");
            return;
        }

        // VERIFICACIÓN 3: ¿Tiene el monopolio del grupo?
        if (this.grupo == null || !this.grupo.esDuenhoGrupo(jugador)) {
            System.out.println("No puedes edificar en " + this.nombre + ". No tienes el monopolio del grupo " + this.getGrupoColor() + ".");
            return;
        }

        // VERIFICACIÓN 4: ¿Hay propiedades hipotecadas en el grupo?
        for (Casilla propiedad : this.grupo.getMiembros()) {
            if (propiedad != null && propiedad.estaHipotecada()) {
                System.out.println("No puedes edificar en " + this.nombre + ". El grupo " + this.getGrupoColor() + " tiene propiedades hipotecadas.");
                return;
            }
        }

        float coste = 0;

        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                // Verificar que no hay hotel
                if (numHoteles > 0) {
                    System.out.println("No se pueden construir casas cuando ya hay un hotel.");
                    return;
                }
                if (numCasas >= 4) {
                    System.out.println("No se pueden construir más casas en esta casilla.");
                    return;
                }
                coste = precioCasa;
                numCasas++;
                break;

            case "hotel":
                // Verificar que hay 4 casas y no hay hotel
                if (numCasas < 4) {
                    System.out.println("Debe tener 4 casas antes de construir un hotel.");
                    return;
                }
                if (numHoteles >= 1) {
                    System.out.println("Ya hay un hotel en esta casilla.");
                    return;
                }
                // Substituir las 4 casas por 1 hotel
                coste = precioHotel;
                numCasas = 0; // Se eliminan las 4 casas
                numHoteles = 1;
                break;

            case "piscina":
                // Verificar que hay hotel y no hay piscina
                if (numHoteles < 1) {
                    System.out.println("No se puede edificar una piscina sin tener un hotel.");
                    return;
                }
                if (numPiscinas >= 1) {
                    System.out.println("Ya existe una piscina en esta casilla.");
                    return;
                }
                coste = precioPiscina;
                numPiscinas++;
                break;

            case "pista_deporte":
                // Verificar que hay hotel, piscina y no hay pista
                if (numHoteles < 1) {
                    System.out.println("Debe existir un hotel antes de construir una pista de deporte.");
                    return;
                }
                if (numPiscinas < 1) {
                    System.out.println("Debe existir una piscina antes de construir una pista de deporte.");
                    return;
                }
                if (numPistas >= 1) {
                    System.out.println("Ya hay una pista de deporte en esta casilla.");
                    return;
                }
                coste = precioPistaDeporte;
                numPistas++;
                break;

            default:
                System.out.println("Tipo de edificio no válido.");
                return;
        }

        // Comprobamos si el jugador tiene dinero
        if (jugador.getFortuna() < coste) {
            System.out.println("La fortuna de " + jugador.getNombre() + " no es suficiente para edificar " + tipoEdificio + " en " + nombre + ".");
            // Revertir cambios si no tiene dinero
            switch (tipoEdificio.toLowerCase()) {
                case "casa": numCasas--; break;
                case "hotel": numCasas = 4; numHoteles = 0; break; // Restaurar las 4 casas
                case "piscina": numPiscinas--; break;
                case "pista_deporte": numPistas--; break;
            }
            return;
        }

        // Actualizamos fortuna y gastos
        jugador.sumarFortuna(-coste);
        jugador.sumarGastos(coste);

        System.out.println("Se ha edificado una " + tipoEdificio + " en " + nombre + ". La fortuna de " + jugador.getNombre() + " se reduce en " + (int)coste + "€.");
    }







    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  vender
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Vende edificios de esta casilla solar
     * @param tipoEdificio Tipo de edificio a vender (casa, hotel, piscina, pista_deporte)
     * @param cantidad Cantidad a vender
     * @param jugador Jugador que vende los edificios
     * @return true si la venta fue exitosa, false en caso contrario
     */
    public boolean venderEdificio(String tipoEdificio, int cantidad, Jugador jugador) {
        if (!this.tipo.equalsIgnoreCase("Solar")) {
            System.out.println("Solo se pueden vender edificios en solares.");
            return false;
        }
        
        // Verificar que el jugador es el dueño
        if (this.duenho != jugador) {
            System.out.println("No se pueden vender edificios en " + this.nombre + ". Esta propiedad no pertenece a " + jugador.getNombre() + ".");
            return false;
        }
        
        // Verificar que no está hipotecada
        if (this.estaHipotecada()) {
            System.out.println("No se pueden vender edificios en " + this.nombre + ". La propiedad está hipotecada.");
            return false;
        }
        
        float precioVenta = 0;
        int cantidadVendida = 0;
        String mensaje = "";
        
        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                if (numCasas == 0) {
                    System.out.println("No hay casas para vender en " + this.nombre + ".");
                    return false;
                }
                cantidadVendida = Math.min(cantidad, numCasas);
                precioVenta = this.precioCasa * cantidadVendida;
                numCasas -= cantidadVendida;
                mensaje = jugador.getNombre() + " ha vendido " + cantidadVendida + " casa" + (cantidadVendida > 1 ? "s" : "") + 
                        " en " + this.nombre + ", recibiendo " + (int)precioVenta + "€.";
                if (numCasas > 0) {
                    mensaje += " En la propiedad queda" + (numCasas > 1 ? "n" : "") + " " + numCasas + " casa" + (numCasas > 1 ? "s" : "") + ".";
                } else {
                    mensaje += " En la propiedad no quedan casas.";
                }
                break;
                
            case "hotel":
                if (numHoteles == 0) {
                    System.out.println("No hay hoteles para vender en " + this.nombre + ".");
                    return false;
                }
                // Solo se puede vender 1 hotel máximo
                cantidadVendida = Math.min(cantidad, 1);
                precioVenta = this.precioHotel * cantidadVendida;
                numHoteles -= cantidadVendida;
                mensaje = jugador.getNombre() + " ha vendido " + cantidadVendida + " hotel en " + this.nombre + 
                        ", recibiendo " + (int)precioVenta + "€.";
                break;
                
            case "piscina":
                if (numPiscinas == 0) {
                    System.out.println("No hay piscinas para vender en " + this.nombre + ".");
                    return false;
                }
                // Solo se puede vender 1 piscina máximo
                cantidadVendida = Math.min(cantidad, 1);
                precioVenta = this.precioPiscina * cantidadVendida;
                numPiscinas -= cantidadVendida;
                if (cantidad > 1) {
                    mensaje = "Solamente se puede vender 1 piscina, recibiendo " + (int)precioVenta + "€.";
                } else {
                    mensaje = jugador.getNombre() + " ha vendido 1 piscina en " + this.nombre + 
                            ", recibiendo " + (int)precioVenta + "€.";
                }
                break;
                
            case "pista_deporte":
                if (numPistas == 0) {
                    System.out.println("No hay pistas de deporte para vender en " + this.nombre + ".");
                    return false;
                }
                // Solo se puede vender 1 pista máximo
                cantidadVendida = Math.min(cantidad, 1);
                precioVenta = this.precioPistaDeporte * cantidadVendida;
                numPistas -= cantidadVendida;
                if (cantidad > 1) {
                    mensaje = "Solamente se puede vender 1 pista de deporte, recibiendo " + (int)precioVenta + "€.";
                } else {
                    mensaje = jugador.getNombre() + " ha vendido 1 pista de deporte en " + this.nombre + 
                            ", recibiendo " + (int)precioVenta + "€.";
                }
                break;
                
            default:
                System.out.println("Tipo de edificio no válido: " + tipoEdificio);
                return false;
        }
        
        // Realizar el pago al jugador
        jugador.sumarFortuna(precioVenta);
        System.out.println(mensaje);
        return true;
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  CARTA Y SUERTE
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Procesa cartas de Suerte y Caja de Comunidad
     */
    private boolean procesarCarta(Jugador jugadorActual, Tablero tablero, ArrayList<Jugador> todosJugadores) {
        if (this.tipo.equalsIgnoreCase("Suerte")) {
            return procesarCartaSuerte(jugadorActual, tablero, todosJugadores);
        } else if (this.tipo.equalsIgnoreCase("Comunidad")) {
            return procesarCartaComunidad(jugadorActual, tablero, todosJugadores);
        }
        return true;
    }


    /**
     * Procesa carta de Suerte según el orden del Apéndice A
     */
    private boolean procesarCartaSuerte(Jugador jugadorActual, Tablero tablero, ArrayList<Jugador> todosJugadores) {
        contadorSuerte = (contadorSuerte % 7) + 1; // Cartas 1-7
        System.out.println("Carta de Suerte " + contadorSuerte + " seleccionada");
        
        switch (contadorSuerte) {
            case 1:
                return cartaSuerte1(jugadorActual, tablero);
            case 2:
                return cartaSuerte2(jugadorActual, tablero);
            case 3:
                return cartaSuerte3(jugadorActual);
            case 4:
                return cartaSuerte4(jugadorActual, todosJugadores);
            case 5:
                return cartaSuerte5(jugadorActual, tablero);
            case 6:
                return cartaSuerte6(jugadorActual, tablero);
            case 7:
                return cartaSuerte7(jugadorActual, tablero);
            default:
                return true;
        }
    }

    /**
     * Procesa carta de Caja de Comunidad según el orden del Apéndice A
     */
    private boolean procesarCartaComunidad(Jugador jugadorActual, Tablero tablero, ArrayList<Jugador> todosJugadores) {
        contadorComunidad = (contadorComunidad % 6) + 1; // Cartas 1-6
        System.out.println("Carta de Caja de Comunidad " + contadorComunidad + " seleccionada");
        
        switch (contadorComunidad) {
            case 1:
                return cartaComunidad1(jugadorActual, tablero);
            case 2:
                return cartaComunidad2(jugadorActual, tablero);
            case 3:
                return cartaComunidad3(jugadorActual, tablero);
            case 4:
                return cartaComunidad4(jugadorActual);
            case 5:
                return cartaComunidad5(jugadorActual, tablero);
            case 6:
                return cartaComunidad6(jugadorActual, tablero);
            default:
                return true;
        }
    }

    // ==================== CARTAS MODIFICADAS ====================

        // ==================== IMPLEMENTACIONES DE CARTAS DE SUERTE ====================

    /**
     * Carta Suerte 1: Decides hacer un viaje de placer. Avanza hasta Solar19.
     * Si pasas por la casilla de Salida, cobra 2.000.000€.
     */
    private boolean cartaSuerte1(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Decides hacer un viaje de placer. Avanza hasta Solar19.");
        
        Casilla solar19 = tablero.encontrar_casilla("Solar19");
        if (solar19 != null) {
            // Mover al jugador a Solar19
            Casilla posActual = jugadorActual.getAvatar().getLugar();
            int posActualIdx = posActual.getPosicion();
            
            // Verificar si pasa por salida
            boolean pasaPorSalida = (posActualIdx < 19) || (posActualIdx > 19 && posActualIdx >= 30);
            
            // Mover avatar
            jugadorActual.getAvatar().moverAvatarDirecto(tablero.getCasillas(), solar19);
            
            if (pasaPorSalida) {
                jugadorActual.sumarFortuna(2000000f);
                System.out.println("¡Has pasado por la salida! Recibes 2.000.000€");
            }
            
            // Procesar la nueva casilla
            return solar19.evaluarCasilla(jugadorActual, duenho, 0, tablero, new ArrayList<>());
        }
        return true;
    }

    /**
     * Carta Suerte 2: Los acreedores te persiguen por impago. Ve a la Cárcel.
     * Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.
     */
    private boolean cartaSuerte2(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Los acreedores te persiguen por impago. Ve a la Cárcel.");
        jugadorActual.encarcelar(tablero.getCasillas());
        return true;
    }

    /**
     * Carta Suerte 3: ¡Has ganado el bote de la lotería! Recibe 1.000.000€.
     */
    private boolean cartaSuerte3(Jugador jugadorActual) {
        System.out.println("¡Has ganado el bote de la lotería! Recibe 1.000.000€.");
        jugadorActual.sumarFortuna(1000000f);
        return true;
    }

    /**
     * Carta Suerte 4: Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€.
     */
    private boolean cartaSuerte4(Jugador jugadorActual, ArrayList<Jugador> todosJugadores) {
        System.out.println("Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€.");
        
        // Buscar todos los jugadores excepto el actual
        ArrayList<Jugador> otrosJugadores = new ArrayList<>();
        for (Jugador j : todosJugadores) {
            if (j != jugadorActual && j.getNombre() != null) { // Excluir banca (nombre null)
                otrosJugadores.add(j);
            }
        }
        
        float totalAPagar = otrosJugadores.size() * 250000f;
        System.out.println("Debes pagar " + totalAPagar + "€ a " + otrosJugadores.size() + " jugadores");
        
        if (jugadorActual.getFortuna() >= totalAPagar) {
            for (Jugador otro : otrosJugadores) {
                boolean exito = jugadorActual.pagar(250000f, otro);
                if (!exito) {
                    System.out.println("Error al pagar a " + otro.getNombre());
                    return false;
                }
            }
            System.out.println("Pago completado exitosamente");
            return true;
        } else {
            System.out.println("No tienes suficiente dinero para pagar a todos los jugadores.");
            return false; // Bancarrota
        }
    }

    /**
     * Carta Suerte 5: ¡Hora punta de tráfico! Retrocede tres casillas.
     */
    private boolean cartaSuerte5(Jugador jugadorActual, Tablero tablero) {
        System.out.println("¡Hora punta de tráfico! Retrocede tres casillas.");
        
        // Mover hacia atrás 3 posiciones
        int movimiento = -3;
        jugadorActual.getAvatar().moverAvatar(tablero.getCasillas(), movimiento);
        Casilla nueva = jugadorActual.getAvatar().getLugar();
        
        System.out.println("Has retrocedido a: " + nueva.getNombre());
        
        // Procesar la nueva casilla
        return nueva.evaluarCasilla(jugadorActual, duenho, 0, tablero, new ArrayList<>());
    }

    /**
     * Carta Suerte 6: Te multan por usar el móvil mientras conduces. Paga 150.000€.
     */
    private boolean cartaSuerte6(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Te multan por usar el móvil mientras conduces. Paga 150.000€.");
        
        if (jugadorActual.getFortuna() >= 150000f) {
            jugadorActual.sumarFortuna(-150000f);
            tablero.anadirAlBote(150000f); // El dinero de la multa va al Parking
            System.out.println("Multa pagada. 150.000€ añadidos al bote del Parking.");
            return true;
        } else {
            System.out.println("No tienes suficiente dinero para pagar la multa.");
            return false;
        }
    }

    /**
     * Carta Suerte 7: Avanza hasta la casilla de transporte más cercana.
     * Si no tiene dueño, puedes comprarla. Si tiene dueño, paga al dueño el doble de la operación indicada.
     */
    private boolean cartaSuerte7(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Avanza hasta la casilla de transporte más cercana.");
        
        Casilla transporteMasCercano = encontrarTransporteMasCercano(jugadorActual, tablero);
        if (transporteMasCercano != null) {
            // Mover al jugador al transporte más cercano
            jugadorActual.getAvatar().moverAvatarDirecto(tablero.getCasillas(), transporteMasCercano);
            
            // Procesar la casilla con doble alquiler si tiene dueño
            if (transporteMasCercano.getDuenho() != null && transporteMasCercano.getDuenho() != jugadorActual) {
                float alquilerNormal = 250000f; // Alquiler base de transporte
                float alquilerDoble = alquilerNormal * 2;
                System.out.println("Paga el doble del alquiler: " + alquilerDoble + "€");
                return jugadorActual.pagar(alquilerDoble, transporteMasCercano.getDuenho());
            } else {
                return transporteMasCercano.evaluarCasilla(jugadorActual, duenho, 0, tablero, new ArrayList<>());
            }
        }
        return true;
    }

    // ==================== IMPLEMENTACIONES DE CARTAS DE COMUNIDAD ====================

    /**
     * Carta Comunidad 1: Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.
     */
    private boolean cartaComunidad1(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.");
        
        if (jugadorActual.getFortuna() >= 500000f) {
            jugadorActual.sumarFortuna(-500000f);
            tablero.anadirAlBote(500000f); // El dinero va al Parking
            System.out.println("Pago realizado. 500.000€ añadidos al bote del Parking.");
            return true;
        } else {
            System.out.println("No tienes suficiente dinero para pagar el balneario.");
            return false;
        }
    }

    /**
     * Carta Comunidad 2: Te investigan por fraude de identidad. Ve a la Cárcel.
     * Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.
     */
    private boolean cartaComunidad2(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Te investigan por fraude de identidad. Ve a la Cárcel.");
        jugadorActual.encarcelar(tablero.getCasillas());
        return true;
    }

    /**
     * Carta Comunidad 3: Colócate en la casilla de Salida. Cobra 2.000.000€.
     */
    private boolean cartaComunidad3(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Colócate en la casilla de Salida. Cobra 2.000.000€.");
        
        Casilla salida = tablero.encontrar_casilla("Salida");
        if (salida != null) {
            jugadorActual.getAvatar().moverAvatarDirecto(tablero.getCasillas(), salida);
            jugadorActual.sumarFortuna(2000000f);
            System.out.println("¡Has llegado a la Salida y recibes 2.000.000€!");
        }
        return true;
    }

    /**
     * Carta Comunidad 4: Devolución de Hacienda. Cobra 500.000€.
     */
    private boolean cartaComunidad4(Jugador jugadorActual) {
        System.out.println("Devolución de Hacienda. Cobra 500.000€.");
        jugadorActual.sumarFortuna(500000f);
        return true;
    }

    /**
     * Carta Comunidad 5: Retrocede hasta Solar1 para comprar antigüedades exóticas.
     */
    private boolean cartaComunidad5(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Retrocede hasta Solar1 para comprar antigüedades exóticas.");
        
        Casilla solar1 = tablero.encontrar_casilla("Solar1");
        if (solar1 != null) {
            jugadorActual.getAvatar().moverAvatarDirecto(tablero.getCasillas(), solar1);
            return solar1.evaluarCasilla(jugadorActual, duenho, 0, tablero, new ArrayList<>());
        }
        return true;
    }

    /**
     * Carta Comunidad 6: Ve a Solar20 para disfrutar del San Fermín.
     * Si pasas por la casilla de Salida, cobra 2.000.000€.
     */
    private boolean cartaComunidad6(Jugador jugadorActual, Tablero tablero) {
        System.out.println("Ve a Solar20 para disfrutar del San Fermín.");
        
        Casilla solar20 = tablero.encontrar_casilla("Solar20");
        if (solar20 != null) {
            Casilla posActual = jugadorActual.getAvatar().getLugar();
            int posActualIdx = posActual.getPosicion();
            
            // Verificar si pasa por salida
            boolean pasaPorSalida = (posActualIdx < 20) || (posActualIdx > 20 && posActualIdx >= 30);
            
            // Mover avatar
            jugadorActual.getAvatar().moverAvatarDirecto(tablero.getCasillas(), solar20);
            
            if (pasaPorSalida) {
                jugadorActual.sumarFortuna(2000000f);
                System.out.println("¡Has pasado por la salida! Recibes 2.000.000€");
            }
            
            // Procesar la nueva casilla
            return solar20.evaluarCasilla(jugadorActual, duenho, 0, tablero, new ArrayList<>());
        }
        return true;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Encuentra el transporte más cercano a la posición actual del jugador
     */
    private Casilla encontrarTransporteMasCercano(Jugador jugador, Tablero tablero) {
        Casilla actual = jugador.getAvatar().getLugar();
        int posActual = actual.getPosicion();
        
        // Lista de transportes ordenados por posición
        int[] posicionesTransporte = {5, 15, 25, 35}; // Trans1, Trans2, Trans3, Trans4
        
        // Encontrar el transporte más cercano (en dirección hacia adelante)
        for (int i = 0; i < posicionesTransporte.length; i++) {
            if (posicionesTransporte[i] > posActual) {
                return tablero.encontrar_casilla("Trans" + (i + 1));
            }
        }
        
        // Si no hay transporte adelante, tomar el primero
        return tablero.encontrar_casilla("Trans1");
    }






    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////  GETTERS Y SETTER
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // Getters
    public int getPosicion() { return posicion; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public Jugador getDuenho() { return duenho; }
    public ArrayList<Avatar> getAvatares() { return avatares; }
    public float getValor() { return valor; }
    public float getPrecioCasa() { return precioCasa; }
    public float getPrecioHotel() { return precioHotel; }
    public float getPrecioPiscina() { return precioPiscina; }
    public float getPrecioPistaDeporte() { return precioPistaDeporte; }
    public float getHipoteca() { return hipoteca; }
    public String getGrupoColor() { return nombreGrupo(); }
    public int getNumCasas() { return numCasas; }
    public int getNumHoteles() { return numHoteles; }
    public int getNumPiscinas() { return numPiscinas; }
    public int getNumPistas() { return numPistas; }

    // === AÑADE ESTOS ATRIBUTOS SI NO LOS TIENES ===
    protected boolean hipotecada = false;
    
    // ... tus métodos existentes ...
    
    // === AÑADE SOLO ESTOS MÉTODOS (si no los tienes) ===
    
    // 1. Método para verificar hipoteca (PROBABLEMENTE FALTA)
    public boolean estaHipotecada() {
        return hipotecada;
    }
    
    // 2. Método para establecer hipoteca (PROBABLEMENTE FALTA)  
    public void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
 
}
    

