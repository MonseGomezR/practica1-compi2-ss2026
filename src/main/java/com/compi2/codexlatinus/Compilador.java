package com.compi2.codexlatinus;

import com.compi2.CodexLatinusLexer;
import com.compi2.CodexLatinusParser;
import com.compi2.codexlatinus.ast.Ast;
import com.compi2.codexlatinus.errores.Error;
import com.compi2.codexlatinus.errores.ErrorListener;
import com.compi2.codexlatinus.pila.EstadoPila;
import com.compi2.codexlatinus.pila.GeneradorPila;
import com.compi2.codexlatinus.semantico.AnalizadorSemantico;
import com.compi2.codexlatinus.semantico.TablaSimbolos;
import com.compi2.codexlatinus.visitor.AstVisitor;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class Compilador {

    public static class ResultadoAnalisis {

        public Ast ast;
        public TablaSimbolos tabla;
        public List<String> errores = new ArrayList<>();
        public List<EstadoPila> estadosPila = new ArrayList<>();
        public boolean valido;
        public String codigoPigLatin;
    }

    public static ResultadoAnalisis analizar(String codigoFuente) {
        ResultadoAnalisis resultado = new ResultadoAnalisis();

        CharStream input = CharStreams.fromString(codigoFuente);
        CodexLatinusLexer lexer = new CodexLatinusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CodexLatinusParser parser = new CodexLatinusParser(tokens);

        ErrorListener el = new ErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(el);

        GeneradorPila generadorPila = new GeneradorPila(parser);
        parser.addParseListener(generadorPila);

        CodexLatinusParser.ProgramaContext tree = parser.programa();

        resultado.estadosPila = generadorPila.getEstados();

        AstVisitor astVisitor = new AstVisitor();
        Ast ast = astVisitor.visitPrograma(tree);
        resultado.ast = ast;
        resultado.tabla = astVisitor.getBuilderTabla().getTabla();

        for (Error e : el.getErrores()) {
            resultado.errores.add(e.toString());
        }

        AnalizadorSemantico as = new AnalizadorSemantico(ast, resultado.tabla);
        as.analizar();

        if (as.getErrores() != null) {
            for (Error e : as.getErrores()) {
                resultado.errores.add(e.toString());
            }
        }

        resultado.valido = resultado.errores.isEmpty();
        if (resultado.valido) {
            StringBuffer sb = new StringBuffer();
            resultado.ast.aPigLatin(sb);
            resultado.codigoPigLatin = sb.toString();
        }
        return resultado;
    }

    public static class ResultadoTokenizacion {

        public List<Token> tokens = new ArrayList<>();
        public List<int[]> erroresLexicos = new ArrayList<>(); // {inicio, fin} absolutos en el texto
    }

    public static ResultadoTokenizacion tokenizar(String codigoFuente) {
        ResultadoTokenizacion resultado = new ResultadoTokenizacion();

        CharStream input = CharStreams.fromString(codigoFuente);
        CodexLatinusLexer lexer = new CodexLatinusLexer(input);

        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                    int line, int charPositionInLine, String msg,
                    RecognitionException e) {
                if (e instanceof LexerNoViableAltException) {
                    int inicio = ((LexerNoViableAltException) e).getStartIndex();
                    int fin = inicio + 1; // el lexer recupera saltando 1 carácter
                    resultado.erroresLexicos.add(new int[]{inicio, fin});
                }
            }
        });

        Token token;
        try {
            while ((token = lexer.nextToken()).getType() != Token.EOF) {
                resultado.tokens.add(token);
            }
        } catch (Exception ex) {
            // código incompleto mientras el usuario escribe; se ignora
        }
        return resultado;
    }
}
