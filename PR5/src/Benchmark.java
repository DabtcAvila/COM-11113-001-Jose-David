public class Benchmark {

    private static final Random rnd = new Random(1234);

    public static void main(String[] args) {
        int[] tamanosTexto = {100, 500, 1000, 2000, 5000};
        char[] alfabeto = {'A', 'B', 'C', 'D', 'E', 'F'};

        try (FileWriter fw = new FileWriter("resultados/tiempos.csv")) {
            fw.write("tamano_texto,tiempo_huffman_ns,tiempo_codificacion_ns\n");

            for (int tam : tamanosTexto) {
                int[] frecuencias = generarFrecuencias(alfabeto.length);
                String texto = generarTexto(alfabeto, tam);

                long t1 = System.nanoTime();
                ArbolHuffman arbol = new ArbolHuffman(alfabeto, frecuencias);
                Map<Character, String> codigos = arbol.generarCodigos();
                long t2 = System.nanoTime();

                long t3 = System.nanoTime();
                String codificado = Codificador.codificar(texto, codigos);
                long t4 = System.nanoTime();

                long tiempoHuff = t2 - t1;
                long tiempoCod = t4 - t3;

                fw.write(tam + "," + tiempoHuff + "," + tiempoCod + "\n");
                System.out.println("Tamaño " + tam + " → Huffman=" + tiempoHuff
                        + "ns Codificación=" + tiempoCod + "ns");
            }

            System.out.println("Benchmark completo. Resultados en resultados/tiempos.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] generarFrecuencias(int n) {
        int[] f = new int[n];
        for (int i = 0; i < n; i++) {
            f[i] = rnd.nextInt(100) + 1;
        }
        return f;
    }

    private static String generarTexto(char[] alfabeto, int tam) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tam; i++) {
            sb.append(alfabeto[rnd.nextInt(alfabeto.length)]);
        }
        return sb.toString();
    }
}