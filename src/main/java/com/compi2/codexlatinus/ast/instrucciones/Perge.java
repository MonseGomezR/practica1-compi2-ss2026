package com.compi2.codexlatinus.ast.instrucciones;

/**
 *
 * @author Usuario
 */
public class Perge extends Instruccion {

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("perge;\n");
    }

    @Override
    public String toString() {
        return "Perge";
    }
}
