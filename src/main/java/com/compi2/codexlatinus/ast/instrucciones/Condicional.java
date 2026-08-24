package com.compi2.codexlatinus.ast.instrucciones;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Condicional extends Instruccion {

    private final List<RamaCondicional> ramas;

    @Override
    public void aPigLatin(StringBuffer sb) {

        for (int i = 0; i < ramas.size(); i++) {

            RamaCondicional rama = ramas.get(i);

            if (i == 0) {
                sb.append("si");
            } else if (rama.getCondicion() == null) {
                sb.append("aliter");
            } else {
                sb.append("aliter si");
            }

            if (rama.getCondicion() != null) {
                sb.append(" (");
                rama.getCondicion().aPigLatin(sb);
                sb.append(")");
            }

            sb.append(" {\n");

            for (Instruccion instruccion : rama.getInstrucciones()) {
                instruccion.aPigLatin(sb);
            }

            sb.append("}\n");
        }

        sb.append("finis;\n");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Condicional");

        for (RamaCondicional rama : ramas) {
            sb.append("\n    ")
                    .append(rama);
        }

        return sb.toString();
    }
}
