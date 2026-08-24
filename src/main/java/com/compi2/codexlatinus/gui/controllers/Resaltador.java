package com.compi2.codexlatinus.gui.controllers;

import com.compi2.codexlatinus.Compilador;
import com.compi2.CodexLatinusLexer;
import org.antlr.v4.runtime.Token;

import java.util.*;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class Resaltador {

    public static StyleSpans<Collection<String>> calcularEstilos(String texto) {
        Compilador.ResultadoTokenizacion resultado = Compilador.tokenizar(texto);

        List<int[]> segmentos = new ArrayList<>();
        List<String> estilos = new ArrayList<>();

        for (Token token : resultado.tokens) {
            String estilo = clasificar(token.getType());
            if (estilo == null) continue;
            int inicio = token.getStartIndex();
            int fin = token.getStopIndex() + 1;
            if (inicio < 0 || fin <= inicio) continue;
            segmentos.add(new int[]{inicio, fin});
            estilos.add(estilo);
        }

        for (int[] err : resultado.erroresLexicos) {
            segmentos.add(err);
            estilos.add("error-lexico");
        }

        Integer[] orden = new Integer[segmentos.size()];
        for (int i = 0; i < orden.length; i++) orden[i] = i;
        Arrays.sort(orden, (a, b) -> segmentos.get(a)[0] - segmentos.get(b)[0]);

        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        int ultimoFin = 0;
        for (int i : orden) {
            int inicio = segmentos.get(i)[0];
            int fin = segmentos.get(i)[1];
            if (inicio < ultimoFin) continue; // se solapa con algo ya pintado, se descarta
            builder.add(Collections.emptyList(), inicio - ultimoFin);
            builder.add(Collections.singleton(estilos.get(i)), fin - inicio);
            ultimoFin = fin;
        }
        if (texto.length() > ultimoFin) {
            builder.add(Collections.emptyList(), texto.length() - ultimoFin);
        }
        return builder.create();
    }

    private static String clasificar(int tipoToken) {
        switch (tipoToken) {
            case CodexLatinusLexer.ESTO:
            case CodexLatinusLexer.SERIES:
            case CodexLatinusLexer.STRUCTURA:
            case CodexLatinusLexer.FINIS:
            case CodexLatinusLexer.FINIS_PROG:
            case CodexLatinusLexer.SI:
            case CodexLatinusLexer.ALITER:
            case CodexLatinusLexer.DUM:
            case CodexLatinusLexer.FACERE:
            case CodexLatinusLexer.PER:
            case CodexLatinusLexer.PERGE:
            case CodexLatinusLexer.INTERRUMPE:
            case CodexLatinusLexer.RATIO:
            case CodexLatinusLexer.ACTIO:
            case CodexLatinusLexer.REDDERE:
            case CodexLatinusLexer.NON:
            case CodexLatinusLexer.VERUM:
            case CodexLatinusLexer.FALSUS:
                return "palabra-clave";

            case CodexLatinusLexer.NUMERUS:
            case CodexLatinusLexer.TEXTUM:
            case CodexLatinusLexer.DECIMALIS:
            case CodexLatinusLexer.LITTERA:
            case CodexLatinusLexer.BOOL:
                return "tipo-dato";

            case CodexLatinusLexer.CADENA:
            case CodexLatinusLexer.CARACTER:
                return "cadena";

            case CodexLatinusLexer.NUMERO:
            case CodexLatinusLexer.DECIMAL:
                return "numero";

            case CodexLatinusLexer.COMENTARIO_LINEA:
            case CodexLatinusLexer.COMENTARIO_BLOQUE:
                return "comentario";

            default:
                return null;
        }
    }
}