package com.compi2.codexlatinus.ast.expresiones;

import com.compi2.codexlatinus.Tipo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Literal extends Expresion {

    private final Tipo tipo;
    private final Object content;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append(content.toString());
    }

    @Override
    public String toString() {
        return "Literal("
                + tipo.toString()
                + ", "
                + content.toString()
                + ")";
    }
}
