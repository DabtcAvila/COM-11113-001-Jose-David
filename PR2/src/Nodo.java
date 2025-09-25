
import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private Integer peso;              // null si es interno
    private final List<Nodo> hijos;

    public Nodo() {
        this.peso = null;
        this.hijos = new ArrayList<>();
    }

    public Nodo(int peso) {
        this.peso = peso;
        this.hijos = new ArrayList<>();
    }

    public boolean esHoja() {
        return peso != null && hijos.isEmpty();
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    public void agregarHijo(Nodo hijo) {
        hijos.add(hijo);
    }
}
