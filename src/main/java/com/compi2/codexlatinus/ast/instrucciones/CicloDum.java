package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.expresiones.Expresion;
import com.compi2.codexlatinus.semantico.Scope;
import java.util.List;
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
public class CicloDum extends Instruccion {

    private final Expresion condicion;
    private final List<Instruccion> instrucciones;
    private Scope ambito;

    @Override
    public void aPigLatin(StringBuffer sb) {

        sb.append("dum (");
        condicion.aPigLatin(sb);
        sb.append(") {\n");

        for (Instruccion instruccion : instrucciones) {
            instruccion.aPigLatin(sb);
        }

        sb.append("} finis;\n");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("CicloDum(\n");
        sb.append("    Condicion: ")
                .append(condicion)
                .append("\n");

        for (Instruccion instruccion : instrucciones) {
            sb.append("    ").append(instruccion).append("\n");
        }

        sb.append(")");

        return sb.toString();
    }

}
