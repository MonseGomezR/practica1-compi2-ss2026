package com.compi2.codexlatinus;

import com.compi2.codexlatinus.gui.EstilosController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CodexLatinus extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 750);
        EstilosController.aplicar(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Codex Latinus — Terminal de la Resistencia");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/*
package com.compi2.codexlatinus;
import com.compi2.codexlatinus.ast.Ast;
import com.compi2.codexlatinus.visitor.AstVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import com.compi2.CodexLatinusLexer;
import com.compi2.CodexLatinusParser;
import com.compi2.codexlatinus.semantico.AnalizadorSemantico;
import com.compi2.codexlatinus.errores.Error;
import com.compi2.codexlatinus.semantico.Scope;
import com.compi2.codexlatinus.semantico.Simbolo;
import com.compi2.codexlatinus.semantico.TablaSimbolos;
public class CodexLatinus {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("ejemplo.lat");
        CodexLatinusLexer lexer = new CodexLatinusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CodexLatinusParser parser = new CodexLatinusParser(tokens);
        
        CodexLatinusParser.ProgramaContext tree = parser.programa();
        // ==========================================
        // AST
        // ==========================================
        AstVisitor astVisitor = new AstVisitor();
        Ast ast = astVisitor.visitPrograma(tree);
        System.out.println("========== AST ==========");
        System.out.println(ast);
        // ==========================================
        // TABLA DE SÍMBOLOS
        // ========================Q==================
        TablaSimbolos tabla = astVisitor.getBuilderTabla().getTabla();
        System.out.println("\n========== TABLA DE SIMBOLOS ==========");
        imprimirScope(tabla.getGlobal(), 0);
        System.out.println("\n========== ERRORES ==========");
        AnalizadorSemantico as = new AnalizadorSemantico(ast, tabla);
        as.analizar();
        StringBuffer sb = new StringBuffer();
        as.getAst().aPigLatin(sb);
        System.out.println(sb);
        if (as.getErrores() != null) {
            for (Error e : as.getErrores()) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println("No se detectaron errores.");
        }
    }
    private static void imprimirScope(Scope scope, int nivel) {
        String sangria = "    ".repeat(nivel);
        System.out.println(sangria + "SCOPE: " + scope.getNombre());
        for (Simbolo simbolo : scope.getSimbolos()) {
            System.out.println(sangria + "    " + simbolo.toString());
        }
        for (Scope hijo : scope.getHijos()) {
            imprimirScope(hijo, nivel + 1);
        }
    }
}*/
