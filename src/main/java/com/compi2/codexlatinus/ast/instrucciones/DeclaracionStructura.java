package com.compi2.codexlatinus.ast.instrucciones;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@Getter
@AllArgsConstructor
public class DeclaracionStructura extends Instruccion {

    private final String id;
    private final String nombreEstructura;
    private final List<AsignacionAtributo> atributos;

    @Override
    public void aPigLatin(StringBuffer sb) {

        sb.append(id)
                .append(": ")
                .append(nombreEstructura)
                .append(" {\n");

        if (atributos != null) {
            for (AsignacionAtributo atributo : atributos) {
                atributo.aPigLatin(sb);
                sb.append("\n");
            }
        }

        sb.append("}\n");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("DeclaracionEstructuraUso(")
                .append(id)
                .append(", ")
                .append(nombreEstructura)
                .append(")\n");

        if (atributos != null) {
            for (AsignacionAtributo atributo : atributos) {
                sb.append("    ")
                        .append(atributo)
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
