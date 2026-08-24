package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.expresiones.Expresion;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Retorno extends Instruccion {

    private final Expresion expresion;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("reddere ");
        expresion.aPigLatin(sb);
        sb.append(";\n");
    }

    @Override
    public String toString() {
        return "Retorno(" + expresion + ")";
    }
}
