package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.Tipo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Usuario
 */
@RequiredArgsConstructor
@Getter
@Setter
public class Atributo {

    private final String nombre;
    private Tipo tipo;
    private boolean esArray;

    @Override
    public String toString() {
        return "Atributo(" + nombre + ", " + tipo + ")";
    }
}