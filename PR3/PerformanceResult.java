/**
 * Clase para almacenar resultados de rendimiento de algoritmos
 * Permite comparar tiempo de ejecución, uso de memoria y eficiencia
 * 
 * @author José y David
 */
public class PerformanceResult {
    
    private String algoritmo;
    private int m, n, t;
    private long tiempoEjecucionNano;
    private double tiempoEjecucionSegundos;
    private long memoriaUsada;
    private boolean timeoutExcedido;
    private boolean solucionCorrecta;
    
    // Resultado del problema
    private int totalBurgers;
    private int burgersM;
    private int burgersN;
    private int tiempoRestante;
    
    // Métricas adicionales
    private int llamadasRecursivas;
    private int hitsCacheMemorizacion;
    private int tamanoTablaDP;
    
    public PerformanceResult(String algoritmo, int m, int n, int t) {
        this.algoritmo = algoritmo;
        this.m = m;
        this.n = n;
        this.t = t;
        this.timeoutExcedido = false;
        this.solucionCorrecta = true;
        this.llamadasRecursivas = 0;
        this.hitsCacheMemorizacion = 0;
        this.tamanoTablaDP = 0;
    }
    
    // Setters para métricas de tiempo
    public void setTiempoEjecucion(long nanoSegundos) {
        this.tiempoEjecucionNano = nanoSegundos;
        this.tiempoEjecucionSegundos = nanoSegundos / 1_000_000_000.0;
    }
    
    public void setMemoriaUsada(long bytes) {
        this.memoriaUsada = bytes;
    }
    
    public void setTimeoutExcedido(boolean timeout) {
        this.timeoutExcedido = timeout;
    }
    
    public void setSolucionCorrecta(boolean correcta) {
        this.solucionCorrecta = correcta;
    }
    
    // Setters para resultado del problema
    public void setResultadoProblema(int total, int burgersM, int burgersN, int tiempoRestante) {
        this.totalBurgers = total;
        this.burgersM = burgersM;
        this.burgersN = burgersN;
        this.tiempoRestante = tiempoRestante;
    }
    
    // Setters para métricas específicas de algoritmo
    public void setLlamadasRecursivas(int llamadas) {
        this.llamadasRecursivas = llamadas;
    }
    
    public void setHitsCacheMemorizacion(int hits) {
        this.hitsCacheMemorizacion = hits;
    }
    
    public void setTamanoTablaDP(int tamano) {
        this.tamanoTablaDP = tamano;
    }
    
    // Getters
    public String getAlgoritmo() { return algoritmo; }
    public int getM() { return m; }
    public int getN() { return n; }
    public int getT() { return t; }
    public long getTiempoEjecucionNano() { return tiempoEjecucionNano; }
    public double getTiempoEjecucionSegundos() { return tiempoEjecucionSegundos; }
    public long getMemoriaUsada() { return memoriaUsada; }
    public boolean isTimeoutExcedido() { return timeoutExcedido; }
    public boolean isSolucionCorrecta() { return solucionCorrecta; }
    
    public int getTotalBurgers() { return totalBurgers; }
    public int getBurgersM() { return burgersM; }
    public int getBurgersN() { return burgersN; }
    public int getTiempoRestante() { return tiempoRestante; }
    
    public int getLlamadasRecursivas() { return llamadasRecursivas; }
    public int getHitsCacheMemorizacion() { return hitsCacheMemorizacion; }
    public int getTamanoTablaDP() { return tamanoTablaDP; }
    
    // Métodos de utilidad
    public String getComplejidadEspacial() {
        switch (algoritmo.toLowerCase()) {
            case "recursivo":
                return "O(" + Math.max(t/Math.min(m, n), 1) + ")"; // profundidad de recursión
            case "memoizacion":
                return "O(" + t + ")"; // tamaño del cache
            case "dinamico":
                return "O(" + (t > 100000 ? "1" : t) + ")"; // optimización para casos grandes
            default:
                return "N/A";
        }
    }
    
    public String getComplejidadTemporal() {
        switch (algoritmo.toLowerCase()) {
            case "recursivo":
                return "O(2^" + (t/Math.min(m, n)) + ")"; // aproximación exponencial
            case "memoizacion":
                return "O(" + t + ")"; // lineal con memoización
            case "dinamico":
                return "O(" + t + ")"; // lineal
            default:
                return "N/A";
        }
    }
    
    public double getEficienciaMemoria() {
        if (memoriaUsada == 0) return 1.0;
        return 1.0 / memoriaUsada; // Inverso del uso de memoria
    }
    
    // Formato para CSV
    public String toCSV() {
        return String.format("%s,%d,%d,%d,%.9f,%d,%b,%b,%d,%d,%d,%d,%d,%d,%d",
            algoritmo, m, n, t, tiempoEjecucionSegundos, memoriaUsada, 
            timeoutExcedido, solucionCorrecta, totalBurgers, burgersM, burgersN, 
            tiempoRestante, llamadasRecursivas, hitsCacheMemorizacion, tamanoTablaDP);
    }
    
    // Formato para tabla legible
    public String toTableRow() {
        String tiempo = timeoutExcedido ? "TIMEOUT" : String.format("%.6f", tiempoEjecucionSegundos);
        String memoria = memoriaUsada > 0 ? String.format("%d KB", memoriaUsada / 1024) : "N/A";
        String resultado = solucionCorrecta ? 
            String.format("%d (%d,%d,%d)", totalBurgers, burgersM, burgersN, tiempoRestante) : 
            "ERROR";
        
        return String.format("%-12s | %-8s | %-10s | %-15s | %3d,%3d,%-6d",
            algoritmo, tiempo, memoria, resultado, m, n, t);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Resultado de Rendimiento ===\n");
        sb.append("Algoritmo: ").append(algoritmo).append("\n");
        sb.append("Parámetros: m=").append(m).append(", n=").append(n).append(", t=").append(t).append("\n");
        sb.append("Tiempo: ").append(timeoutExcedido ? "TIMEOUT" : String.format("%.6f seg", tiempoEjecucionSegundos)).append("\n");
        sb.append("Memoria: ").append(memoriaUsada > 0 ? String.format("%d bytes", memoriaUsada) : "N/A").append("\n");
        sb.append("Solución: ");
        if (solucionCorrecta && !timeoutExcedido) {
            sb.append(totalBurgers).append(" burgers (")
              .append(burgersM).append(" M, ").append(burgersN).append(" N, ")
              .append(tiempoRestante).append(" resto)");
        } else {
            sb.append("No disponible");
        }
        sb.append("\n");
        
        if (llamadasRecursivas > 0) {
            sb.append("Llamadas recursivas: ").append(llamadasRecursivas).append("\n");
        }
        if (hitsCacheMemorizacion > 0) {
            sb.append("Cache hits: ").append(hitsCacheMemorizacion).append("\n");
        }
        if (tamanoTablaDP > 0) {
            sb.append("Tamaño tabla DP: ").append(tamanoTablaDP).append("\n");
        }
        
        return sb.toString();
    }
}