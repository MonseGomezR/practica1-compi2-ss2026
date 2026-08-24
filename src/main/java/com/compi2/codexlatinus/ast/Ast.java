package com.compi2.codexlatinus.ast;

import com.compi2.codexlatinus.ast.instrucciones.Instruccion;
import com.compi2.codexlatinus.visitor.AgregarStmt;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@Getter
public class Ast implements NodoAst, AgregarStmt {

    private final List<Instruccion> instrucciones;

    public Ast() {
        this.instrucciones = new ArrayList<>();
    }

    @Override
    public void addStatement(Instruccion statement) {
        instrucciones.add(statement);
    }

    @Override
    public void aPigLatin(StringBuffer sb) {
        for (Instruccion instruccion : instrucciones) {
            instruccion.aPigLatin(sb);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("AST\n");

        for (Instruccion instruccion : instrucciones) {
            sb.append(instruccion).append("\n");
        }

        return sb.toString();
    }

}
