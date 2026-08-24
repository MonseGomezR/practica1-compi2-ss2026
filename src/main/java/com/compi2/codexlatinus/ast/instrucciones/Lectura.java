package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.TraductorPigLatin;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Lectura extends Instruccion {

    private final String id;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("%OINK_OINK");
        if (id != null) {
            sb.append(" ").append(TraductorPigLatin.convert(id));
        }
        sb.append(";\n");
    }

    @Override
    public String toString() {
        return "Lectura(" + id + ")";
    }
}
