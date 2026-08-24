package com.compi2.codexlatinus.ast.expresiones;

/**
 *
 * @author Usuario
 */

import com.compi2.codexlatinus.TraductorPigLatin;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LlamadaFuncion extends Expresion {

    private final String id;
    private final List<Expresion> argumentos;

    @Override
    public void aPigLatin(StringBuffer sb) {

        sb.append(TraductorPigLatin.convert(id))
          .append("(");

        if (argumentos != null) {
            for (int i = 0; i < argumentos.size(); i++) {

                if (i > 0) {
                    sb.append(", ");
                }

                argumentos.get(i).aPigLatin(sb);
            }
        }

        sb.append(")");
    }

    @Override
    public String toString() {

        return "LlamadaFuncion("
                + id
                + ", "
                + argumentos
                + ")";
    }
}
