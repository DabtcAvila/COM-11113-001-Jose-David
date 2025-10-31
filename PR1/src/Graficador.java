import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Graficador {

    private static class Punto { double x, y; Punto(double x, double y){this.x=x; this.y=y;} }

    public static void main(String[] args) throws Exception {
        File csv = new File("resultados/tiempos.csv");
        if (!csv.exists()) { System.err.println("No existe resultados/tiempos.csv, ejecuta BancoPruebas primero."); return; }

        Map<String,List<Punto>> series = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String linea = br.readLine(); // encabezado
            while ((linea = br.readLine()) != null) {
                String[] t = linea.split(",");
                int n = Integer.parseInt(t[0]);
                String modo = t[1];
                String hash = t[2];
                double ms = Double.parseDouble(t[4]);
                String clave = modo.equals("lineal") ? "lineal" : "hash:" + hash;
                series.computeIfAbsent(clave, k -> new ArrayList<>()).add(new Punto(n, ms));
            }
        }
        for (List<Punto> pts : series.values())
            pts.sort(Comparator.comparingDouble(p -> p.x));

        dibujar(series, 1200, 700, new File("resultados/tiempos.png"));
        System.out.println("Gráfica generada en resultados/tiempos.png");
    }

    private static void dibujar(Map<String,List<Punto>> series, int W, int H, File salida) throws Exception {
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE); g.fillRect(0,0,W,H);

        int left=90, right=30, top=40, bottom=80;
        int plotW = W-left-right, plotH = H-top-bottom;

        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (List<Punto> pts : series.values()) {
            for (Punto p : pts) {
                minX = Math.min(minX, p.x); maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y); maxY = Math.max(maxY, p.y);
            }
        }
        if (minY == maxY) maxY = minY + 1.0;
        double dx = (maxX - minX) * 0.1, dy = (maxY - minY) * 0.1;
        minX -= dx; maxX += dx; minY = Math.max(0, minY - dy); maxY += dy;

        g.setColor(Color.BLACK);
        g.drawRect(left, top, plotW, plotH);
        g.setColor(new Color(230,230,230));
        for (int i=1;i<=9;i++){
            int x = left + i*plotW/10; g.drawLine(x, top, x, top+plotH);
            int y = top + i*plotH/10; g.drawLine(left, y, left+plotH, y);
        }

        Color[] paleta = { new Color(33,150,243), new Color(76,175,80), new Color(255,152,0), new Color(244,67,54) };
        int idx = 0;
        int leyX = left + 10, leyY = top + 20;

        for (Map.Entry<String,List<Punto>> e : series.entrySet()) {
            Color c = paleta[idx++ % paleta.length];
            g.setColor(c); g.fillRect(leyX, leyY-10, 18, 8);
            g.setColor(Color.BLACK); g.drawString(e.getKey(), leyX + 24, leyY);
            leyY += 18;

            g.setColor(c);
            List<Punto> pts = e.getValue();
            for (int i=0;i<pts.size()-1;i++){
                int x1 = left + (int)((pts.get(i).x   - minX) * plotW / (maxX - minX));
                int y1 = top  + plotH - (int)((pts.get(i).y - minY) * plotH / (maxY - minY));
                int x2 = left + (int)((pts.get(i+1).x - minX) * plotW / (maxX - minX));
                int y2 = top  + plotH - (int)((pts.get(i+1).y - minY) * plotH / (maxY - minY));
                g.drawLine(x1,y1,x2,y2);
                g.fillOval(x1-2,y1-2,4,4);
            }
            if (!pts.isEmpty()){
                int x = left + (int)((pts.get(pts.size()-1).x - minX) * plotW / (maxX - minX));
                int y = top  + plotH - (int)((pts.get(pts.size()-1).y - minY) * plotH / (maxY - minY));
                g.fillOval(x-2,y-2,4,4);
            }
        }

        g.setColor(Color.BLACK);
        g.drawString("n (tamaño de entrada)", W/2 - 60, H - 30);
        g.rotate(-Math.PI/2);
        g.drawString("Tiempo (ms)", -H/2 - 30, 20);
        g.rotate(Math.PI/2);
        g.drawString("Tiempos de ejecución: Lineal vs Hash", left + 10, top - 10);

        g.dispose();
        javax.imageio.ImageIO.write(img, "png", salida);
    }
}
