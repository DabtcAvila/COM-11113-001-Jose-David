import java.util.*;

public class TablaHash {
    public enum Metodo { POLINOMIAL, SUMA, XOR }

    private static class Entrada {
        Registro clave;
        int conteo;
        Entrada(Registro k, int c) { clave = k; conteo = c; }
    }

    private final List<Entrada>[] cubetas;
    private final int tam;
    private final Metodo metodo;

    @SuppressWarnings("unchecked")
    public TablaHash(int numCubetas, Metodo m) {
        if (numCubetas < 1) throw new IllegalArgumentException("numCubetas debe ser positivo");
        this.tam = numCubetas;
        this.metodo = m;
        this.cubetas = new List[tam];
        for (int i = 0; i < tam; i++) cubetas[i] = new LinkedList<>();
    }

    public void agregar(Registro r) {
        int idx = indice(r);
        List<Entrada> cubeta = cubetas[idx];
        for (Entrada e : cubeta) {
            if (iguales(e.clave, r)) { e.conteo++; return; }
        }
        cubeta.add(new Entrada(r, 1));
    }

    public int sumarConteosAlMenos(int k) {
        int total = 0;
        for (List<Entrada> cubeta : cubetas)
            for (Entrada e : cubeta)
                if (e.conteo >= k) total += e.conteo;
        return total;
    }

    private int indice(Registro r) {
        switch (metodo) {
            case SUMA:       return (hashSuma(r)       % tam + tam) % tam;
            case XOR:        return (hashXor(r)        % tam + tam) % tam;
            default:         return (hashPolinomial(r) % tam + tam) % tam;
        }
    }

    // --- Hashes ---
    private int hashSuma(Registro r) {
        long s = 0;
        for (int x : r.datos()) s += x;
        return (int)(s & 0x7fffffff);
    }
    private int hashPolinomial(Registro r) {
        long base = 1_000_003L, h = 0;
        for (int x : r.datos()) h = (h * base + (x & 0xffffffffL)) % Integer.MAX_VALUE;
        return (int)h;
    }
    private int hashXor(Registro r) {
        long h = 0;
        for (int x : r.datos()) {
            long v = (x ^ 0x9e3779b9) & 0xffffffffL;
            h ^= v;
            h = ((h << 13) | (h >>> 19)) & 0xffffffffL; // rotación
        }
        return (int)(h & 0x7fffffff);
    }

    private static boolean iguales(Registro a, Registro b) {
        int[] x = a.datos(), y = b.datos();
        for (int i = 0; i < 10; i++) if (x[i] != y[i]) return false;
        return true;
    }
}
