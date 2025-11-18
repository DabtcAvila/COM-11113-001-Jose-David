package practica5;

import java.util.Map;

public class Codificador {

    public static String codificar(String texto, Map<Character, String> codigos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            String codigo = codigos.get(c);
            if (codigo == null) {
                throw new IllegalArgumentException(
                        "No existe código de Huffman para el símbolo: '" + c + "'"
                );
            }
            sb.append(codigo);
        }
        return sb.toString();
    }

    public static int bitsCodificados(String textoCodificado) {
        return textoCodificado.length();
    }

    public static int bitsUtf8(String texto) {
        // Para caracteres ASCII: 8 bits por carácter
        return texto.length() * 8;
    }
}