package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.expresiones.Expresion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Escritura extends Instruccion {

    private final List<Expresion> expresiones;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("%OINK ");

        for (int i = 0; i < expresiones.size(); i++) {
            expresiones.get(i).aPigLatin(sb);

            if (i < expresiones.size() - 1) {
                sb.append(" %OINK ");
            }
        }

        sb.append(";\n");
    }

    @Override
    public String toString() {
        return "Escritura(" + expresiones + ")";
    }

}
