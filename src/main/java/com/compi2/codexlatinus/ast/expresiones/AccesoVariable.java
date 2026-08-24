package com.compi2.codexlatinus.ast.expresiones;

import com.compi2.codexlatinus.TraductorPigLatin;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccesoVariable extends Expresion {

    private final List<ParteAcceso> partes;

    @Override
    public void aPigLatin(StringBuffer sb) {
        for (int i = 0; i < partes.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }

            ParteAcceso parte = partes.get(i);

            sb.append(TraductorPigLatin.convert(parte.getId()));

            if (parte.getIndice() != null) {
                sb.append("[");
                parte.getIndice().aPigLatin(sb);
                sb.append("]");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Acceso Variable(");

        for (int i = 0; i < partes.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }

            ParteAcceso parte = partes.get(i);

            sb.append(parte.getId());

            if (parte.getIndice() != null) {
                sb.append("[");
                sb.append(parte.getIndice());
                sb.append("]");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < partes.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }

            sb.append(partes.get(i).getId());
        }

        return sb.toString();
    }

}
