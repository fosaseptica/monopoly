package monopoly;

public class MonopolyETSE {
    public static void main(String[] args) {
        Menu menu = new Menu();
        
        if (args.length > 0) {
            menu.leerComandosDesdeFichero(args[0]);
        }
        
        menu.iniciarPartidaPublico();  // ← Iniciar partida DESPUÉS
    }
}