package com.compi2.codexlatinus.ast.instrucciones;

/**
 *
 * @author Usuario
 */
public class Interrumpe extends Instruccion {

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("interrumpe;\n");
    }

    @Override
    public String toString() {
        return "Interrumpe";
    }
}
