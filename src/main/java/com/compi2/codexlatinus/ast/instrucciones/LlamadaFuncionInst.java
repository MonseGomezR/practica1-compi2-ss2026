package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.expresiones.LlamadaFuncion;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */

@AllArgsConstructor
@Getter
public class LlamadaFuncionInst extends Instruccion {

    private final LlamadaFuncion llamada;

    @Override
    public void aPigLatin(StringBuffer sb) {
        llamada.aPigLatin(sb);
        sb.append(";\n");
    }

    @Override
    public String toString() {
        return "LlamadaFuncionStmt(" + llamada + ")";
    }
}
