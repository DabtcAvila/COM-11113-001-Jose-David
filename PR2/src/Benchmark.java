
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Benchmark {

    // Genera un árbol sintético balanceado con 'profundidad' y 'ramas' fijas
    public static Nodo generarArbol(int profundidad, int ancho, Random rnd) {
        if (profundidad == 0) {
            return new Nodo(rnd.nextInt(90) + 10); // hoja con peso 10–99
        }
        Nodo raiz = new Nodo();
        for (int i = 0; i < ancho; i++) {
            raiz.agregarHijo(generarArbol(profundidad - 1, ancho, rnd));
        }
        return raiz;
    }

    public static void main(String[] args) {
        int[] profundidades = {3, 5, 7};   // cambia para distintos tamaños
        int ancho = 3;                     // cada nodo tendrá 3 hijos
        Random rnd = new Random();

        try (FileWriter fw = new FileWriter("resultados/tiempos.csv")) {
            fw.write("tamano,recursivo_ns,iterativo_ns\n");

            for (int d : profundidades) {
                Nodo raiz = generarArbol(d, ancho, rnd);
                ArbolRuta arbol = new ArbolRuta(raiz);

                // Versión recursiva
                long t1 = System.nanoTime();
                RecolectorRecursivo.recolectar(arbol);
                long t2 = System.nanoTime();

                // Versión iterativa
                long t3 = System.nanoTime();
                RecolectorIterativo.recolectar(arbol);
                long t4 = System.nanoTime();

                long tiempoRec = t2 - t1;
                long tiempoIt = t4 - t3;

                int nodos = contarNodos(raiz);

                System.out.printf("Árbol con %d nodos → Recursivo: %d ns, Iterativo: %d ns%n",
                                  nodos, tiempoRec, tiempoIt);

                fw.write(nodos + "," + tiempoRec + "," + tiempoIt + "\n");
            }

            System.out.println("Benchmark completado. Resultados en resultados/tiempos.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int contarNodos(Nodo n) {
        if (n.esHoja()) return 1;
        int s = 1;
        for (Nodo h : n.getHijos()) s += contarNodos(h);
        return s;
    }
}
