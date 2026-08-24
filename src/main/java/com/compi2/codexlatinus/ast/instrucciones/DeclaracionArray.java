package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.Tipo;
import com.compi2.codexlatinus.ast.expresiones.Expresion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeclaracionArray extends Instruccion {

    private final String id;
    private final Expresion size;
    private final Tipo tipo;
    private final List<Expresion> valores;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("series ")
          .append(id)
          .append("[");

        size.aPigLatin(sb);

        sb.append("]: ");

        tipo.aPigLatin(sb);

        if (valores != null) {
            sb.append(" {");

            for (int i = 0; i < valores.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }

                valores.get(i).aPigLatin(sb);
            }

            sb.append("}");
        }

        sb.append(";\n");
    }

    @Override
    public String toString() {
        return "DeclaracionArray("
                + id
                + ", "
                + size
                + ", "
                + tipo
                + ", "
                + valores
                + ")";
    }
}