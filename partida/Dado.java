package partida;

public class Dado {
    // Atributo: valor actual del dado
    private int valor;

    // Constructor: inicializa el dado en 1
    public Dado() {
        this.valor = 1;
    }

    // Método para simular el lanzamiento del dado: devuelve un valor aleatorio entre 1 y 6
    public int hacerTirada() {
        // Math.random() devuelve un double entre 0 y 1, lo escalamos a 1-6
        valor = (int)(Math.random() * 6) + 1;
        return valor;
    }

    // Getter: obtener el valor actual del dado
    public int getValor() {
        return valor;
    }

    // Setter: opcional, permite cambiar el valor manualmente (solo 1-6)
    public void setValor(int valor) {
        if (valor >= 1 && valor <= 6) {
            this.valor = valor;
        }
    }
}
