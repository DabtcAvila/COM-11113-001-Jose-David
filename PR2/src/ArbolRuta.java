
public class ArbolRuta {
    private final Nodo raiz;

    public ArbolRuta(Nodo raiz) {
        this.raiz = raiz;
    }

    public Nodo getRaiz() {
        return raiz;
    }

    // Cuenta de aristas (calles)
    public int contarCalles() {
        return contarCalles(raiz);
    }

    private int contarCalles(Nodo n) {
        int suma = 0;
        for (Nodo h : n.getHijos()) {
            suma += 1 + contarCalles(h);
        }
        return suma;
    }

    // Suma de pesos en hojas
    public int sumarPesos() {
        return sumarPesos(raiz);
    }

    private int sumarPesos(Nodo n) {
        if (n.esHoja()) return n.getPeso();
        int s = 0;
        for (Nodo h : n.getHijos()) s += sumarPesos(h);
        return s;
    }
}
