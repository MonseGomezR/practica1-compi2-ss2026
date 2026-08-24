package com.compi2.codexlatinus.ast.expresiones;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Operacion extends Expresion {

    private final Expresion izquierda;
    private final Expresion derecha;
    private final String operador;

    @Override
    public void aPigLatin(StringBuffer sb) {

        if (izquierda != null) {
            izquierda.aPigLatin(sb);
            sb.append(" ");
        }

        sb.append(operador);

        if (derecha != null) {
            sb.append(" ");
            derecha.aPigLatin(sb);
        }
    }

    @Override
    public String toString() {
        return "\n    Operacion("
                + izquierda
                + ", "
                + derecha
                + ", "
                + operador
                + ")";
    }

}
