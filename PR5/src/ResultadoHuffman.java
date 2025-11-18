package practica5;

import java.util.Map;

public class ResultadoHuffman {

    private final char[] alfabeto;
    private final Map<Character, String> codigos;
    private final int numSimbolosOriginales;
    private final int bitsCodificados;
    private final int bitsUtf8;
    private final long tiempoConstruccionNs;
    private final long tiempoCodificacionNs;
    private final String textoCodificado;
    private final String textoDecodificado;

    public ResultadoHuffman(char[] alfabeto,
                            Map<Character, String> codigos,
                            int numSimbolosOriginales,
                            int bitsCodificados,
                            int bitsUtf8,
                            long tiempoConstruccionNs,
                            long tiempoCodificacionNs,
                            String textoCodificado,
                            String textoDecodificado) {
        this.alfabeto = alfabeto;
        this.codigos = codigos;
        this.numSimbolosOriginales = numSimbolosOriginales;
        this.bitsCodificados = bitsCodificados;
        this.bitsUtf8 = bitsUtf8;
        this.tiempoConstruccionNs = tiempoConstruccionNs;
        this.tiempoCodificacionNs = tiempoCodificacionNs;
        this.textoCodificado = textoCodificado;
        this.textoDecodificado = textoDecodificado;
    }

    public char[] getAlfabeto() { return alfabeto; }
    public Map<Character, String> getCodigos() { return codigos; }
    public int getNumSimbolosOriginales() { return numSimbolosOriginales; }
    public int getBitsCodificados() { return bitsCodificados; }
    public int getBitsUtf8() { return bitsUtf8; }
    public long getTiempoConstruccionNs() { return tiempoConstruccionNs; }
    public long getTiempoCodificacionNs() { return tiempoCodificacionNs; }
    public String getTextoCodificado() { return textoCodificado; }
    public String getTextoDecodificado() { return textoDecodificado; }
}