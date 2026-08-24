package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.Tipo;
import com.compi2.codexlatinus.TraductorPigLatin;
import com.compi2.codexlatinus.ast.expresiones.Expresion;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Declaracion extends Instruccion {

    private final String id;
    private final Tipo tipo;
    private final Expresion expresion;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("estoway ").append(TraductorPigLatin.convert(id))
                .append(" : ");
        tipo.aPigLatin(sb);
        sb.append(" ");
        if (expresion != null) {
            expresion.aPigLatin(sb);
        }
        sb.append(";\n");
    }

    @Override
    public String toString() {
        return "Declaracion("
                + id
                + ", "
                + tipo.toString()
                + ", "
                + expresion
                + ")";
    }
}
