package monopoly;

import partida.*;
import java.util.ArrayList;

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
    private boolean hipotecada = false; // Indica si la casilla está hipotecada
    private float precioCasa; //Precio para construir una casa
    private float precioHotel; //Precio para construir un hotel
    private float precioPiscina; //Precio para construir una piscina
    private float precioPistaDeporte; //Precio para construir una pista de deporte
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    private int numCasas = 0;
    private int numHoteles = 0;
    private int numPiscinas = 0;
    private int numPistas = 0;


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

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
    * - Jugador cuyo avatar está en esa casilla.
    * - La banca (para ciertas comprobaciones).
    * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
    * - El tablero: para mover a Cárcel, Parking (bote), etc.
    * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
    * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada, Tablero tablero) {
        // Especiales directas
        if (nombre.equalsIgnoreCase("IrACárcel")) {
            actual.encarcelar(tablero.getCasillas());
            return true;
        }
        if (nombre.equalsIgnoreCase("Parking")) {
            float bote = tablero.cobrarBote();
            actual.sumarFortuna(bote); // El jugador cobra el bote
            return true;
        }

        if (tipo.equalsIgnoreCase("Impuesto")) {
            // Parte 1: el pago va al Parking (bote), no a la banca
            if (actual.getFortuna() >= impuesto) {
                actual.sumarFortuna(-impuesto);
                tablero.anadirAlBote(impuesto);
                // registrar pago de impuestos en estadísticas del jugador
                actual.registrarPagoTasa(impuesto);
                return true;
            } else {
                return false;
            }
        }
        else if (tipo.equalsIgnoreCase("Solar") || tipo.equalsIgnoreCase("Transporte") || tipo.equalsIgnoreCase("Servicio")) {
            // Si la casilla está hipotecada NO se cobra alquiler
            if (hipotecada) {
                return true; // no hay pago, pero el jugador sigue siendo solvente
            }

            if (duenho != null && duenho != banca && duenho != actual) {
                float alquiler;
                if (tipo.equalsIgnoreCase("Servicio")) {
                    alquiler = tirada * 4 * 50000f;
                } else if (tipo.equalsIgnoreCase("Transporte")) {
                    alquiler = 250000f;
                } else {
                    int n = numeroSolar();
                    alquiler = alquilerTotal(n);
                    int totalEd = numCasas + numHoteles + numPiscinas + numPistas;
                    if (grupo != null && grupo.esDuenhoGrupo(duenho) && totalEd == 0) {
                        alquiler *= 2f;
                    }
                }
                boolean ok = actual.pagar(alquiler, duenho);
                if (ok) {
                    // registrar estadísticas de pago/cobro de alquiler
                    actual.registrarPagoAlquiler(alquiler);
                    if (duenho != null) duenho.registrarCobroAlquiler(alquiler);
                    // registrar ingreso global por casilla
                    if (tablero != null) tablero.registrarIngresoPorCasilla(this.getNombre(), alquiler);
                }
                return ok;
            }
        }

        // Suerte/Comunidad/Cárcel/Salida: sin efecto aquí en Parte 1
        return true; // Por defecto, siempre solvente
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (this.duenho == banca && solicitante.getFortuna() >= valor) {
            solicitante.pagar(valor, banca);
            this.duenho = solicitante;
            solicitante.anhadirPropiedad(this);
            // registrar inversión del jugador
            solicitante.registrarCompra(valor);
        }
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

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

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta(Jugador banca) {
        if (duenho == banca) return nombre + " está en venta por " + valor + "€";
        return nombre + " ya tiene dueño (" + duenho.getNombre() + ")";
    }

    public void edificar(String tipoEdificio, Jugador jugador) {
        if (!tipo.equalsIgnoreCase("Solar")) {
            System.out.println("Solo se puede edificar en solares.");
            return;
        }

        // Validar que el jugador es el propietario de la casilla
        if (this.duenho != jugador) {
            System.out.println(jugador.getNombre() + " no puede edificar en " + nombre + ". No es una propiedad que le pertenece.");
            return;
        }

         // no se puede edificar en una casilla hipotecada
        if (this.hipotecada) {
            System.out.println("No se puede edificar en " + nombre + " porque está hipotecada.");
            return;
        }


        // Validar monopolio del grupo si procede (no se permite edificar si no se posee todo el grupo)
        if (this.grupo != null && !this.grupo.esDuenhoGrupo(jugador)) {
            System.out.println("No se puede edificar en " + nombre + ", porque no se posee el monopolio del grupo " + nombreGrupo() + ".");
            return;
        }

        // ⬇️ NUEVO: no se puede edificar si en el grupo hay alguna casilla hipotecada
        if (this.grupo != null && this.grupo.hayHipotecasEnGrupo()) {
            System.out.println("No se puede edificar en el grupo " + nombreGrupo() + " porque hay propiedades hipotecadas en él.");
            return;
        }

        float coste = 0;
        String idEdificio = "";

        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                if (numCasas >= 4) {
                    System.out.println("No se pueden construir más casas en esta casilla.");
                    return;
                }
                // usar precio específico si está definido
                coste = this.precioCasa > 0 ? this.precioCasa : (valor * 0.6f);
                numCasas++;
                idEdificio = monopoly.Tablero.generarIdEdificio("casa");
                break;

            case "hotel":
                if (numCasas < 4) {
                    System.out.println("Debe tener 4 casas antes de construir un hotel.");
                    return;
                }
                if (numHoteles >= 1) {
                    System.out.println("Ya hay un hotel en esta casilla.");
                    return;
                }
                coste = this.precioHotel > 0 ? this.precioHotel : (valor * 0.6f);
                numHoteles++;
                idEdificio = monopoly.Tablero.generarIdEdificio("hotel");
                break;

            case "piscina":
                if (numHoteles < 1) {
                    System.out.println("No se puede edificar una piscina sin tener un hotel.");
                    return;
                }
                if (numPiscinas >= 1) {
                    System.out.println("Ya existe una piscina en esta casilla.");
                    return;
                }
                coste = this.precioPiscina > 0 ? this.precioPiscina : (valor * 0.4f);
                numPiscinas++;
                idEdificio = monopoly.Tablero.generarIdEdificio("piscina");
                break;

            case "pista_deporte":
            case "pista-deporte":
            case "pista":
                if (numHoteles < 1) {
                    System.out.println("Debe existir un hotel antes de construir una pista de deporte.");
                    return;
                }
                if (numPistas >= 1) {
                    System.out.println("Ya hay una pista de deporte en esta casilla.");
                    return;
                }
                coste = this.precioPistaDeporte > 0 ? this.precioPistaDeporte : (valor * 0.8f);
                numPistas++;
                idEdificio = monopoly.Tablero.generarIdEdificio("pista_deporte");
                break;

            default:
                System.out.println("Tipo de edificio no válido.");
                return;
        }

        // Comprobamos si el jugador tiene dinero
        if (jugador.getFortuna() < coste) {
            System.out.println("La fortuna de " + jugador.getNombre() + " no es suficiente para edificar " + tipoEdificio + " en " + nombre + ".");
            // revertir contador local incrementado por seguridad
            if (tipoEdificio.equalsIgnoreCase("casa") && numCasas > 0) numCasas--;
            if (tipoEdificio.equalsIgnoreCase("hotel") && numHoteles > 0) numHoteles--;
            if (tipoEdificio.equalsIgnoreCase("piscina") && numPiscinas > 0) numPiscinas--;
            if (tipoEdificio.toLowerCase().startsWith("pista") && numPistas > 0) numPistas--;
            return;
        }

        // Actualizamos fortuna y gastos
        jugador.sumarFortuna(-coste);
        jugador.sumarGastos(coste);

        // Mensaje con id generado
        if (idEdificio.isEmpty()) {
            System.out.println("Se ha edificado una " + tipoEdificio + " en " + nombre + ". La fortuna de " + jugador.getNombre() + " se reduce en " + (int)coste + "€.");
        } else {
            // Registrar el edificio en el registro global (Tablero) y en el jugador
            monopoly.Tablero.registrarEdificioStatic(idEdificio, tipoEdificio, jugador.getNombre(), nombre, nombreGrupo(), coste);
            jugador.anhadirEdificio(idEdificio);
            System.out.println("Se ha edificado una " + tipoEdificio + " (id: " + idEdificio + ") en " + nombre + ". La fortuna de " + jugador.getNombre() + " se reduce en " + (int)coste + "€.");
        }
}


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
    public boolean getHipotecada() { return hipotecada; }


    public void setHipotecada(boolean hipotecada) { 
        this.hipotecada = hipotecada; 
    }

    public void reducirCasas(int n) {
        if (n <= 0) return;
        numCasas = Math.max(0, numCasas - n);
    }

    public void reducirHoteles(int n) {
        if (n <= 0) return;
        numHoteles = Math.max(0, numHoteles - n);
    }

    public void reducirPiscinas(int n) {
        if (n <= 0) return;
        numPiscinas = Math.max(0, numPiscinas - n);
    }

    public void reducirPistas(int n) {
        if (n <= 0) return;
        numPistas = Math.max(0, numPistas - n);
    }

    // Getter público que devuelve el alquiler actual según el estado de la casilla
    public float getAlquilerActual() {
        int n = numeroSolar();
        return alquilerTotal(n);
    }

    // Determina el número de Solar (SolarX -> X), o -1 si no es solar
    private int numeroSolar() {
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

    // Alquiler base de SolarX según el Apéndice I
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
     * Calcula el alquiler total de una Solar teniendo en cuenta los edificios construidos.
     * Nota: no comprueba el monopolio del dueño (doblaría el alquiler si existe monopolio y no hay edificios)
     * porque para eso se necesita el tablero completo; el efecto del monopolio sin edificios se aplica
     * al calcular el alquiler en tiempo de pago (ver evaluarCasilla).
     */
    private float alquilerTotal(int n) {
        float base = alquilerBaseSolar(n);
        if (base <= 0f) return 0f;

        // Incrementos por tipo de edificio (valores de ejemplo - ajustar según reglas deseadas)
        float total = base;
        // Cada casa añade 50% del alquiler base
        total += numCasas * (base * 0.5f);
        // Cada hotel añade 200% del alquiler base
        total += numHoteles * (base * 2.0f);
        // Piscina y pista de deporte son mejoras especiales: añaden un porcentaje del alquiler base
        total += numPiscinas * (base * 0.25f);
        total += numPistas * (base * 0.30f);

        return total;
    }

    // Comprueba monopolio por rangos con el tablero ya creado
    private boolean duenhoTieneMonopolio(ArrayList<ArrayList<Casilla>> casillas, int n, Jugador dueno) {
        if (n < 0) return false;
        int inicio, fin; // ambos inclusive
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
}
