package com.compi2.codexlatinus.ast.expresiones;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class ParteAcceso {

    private final String id;
    private final Expresion indice;

    @Override
    public String toString() {
        if (indice == null) {
            return id;
        }

        return id + "[" + indice + "]";
    }
}
