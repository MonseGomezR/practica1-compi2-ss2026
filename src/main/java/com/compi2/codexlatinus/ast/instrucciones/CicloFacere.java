package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.expresiones.Expresion;
import com.compi2.codexlatinus.semantico.Scope;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Usuario
 */
@RequiredArgsConstructor
@Getter
@Setter
public class CicloFacere extends Instruccion {

    private final List<Instruccion> instrucciones;
    private final Expresion condicion;
    private Scope ambito;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("facere {\n");

        for (Instruccion instruccion : instrucciones) {
            instruccion.aPigLatin(sb);
        }

        sb.append("} dum (");
        condicion.aPigLatin(sb);
        sb.append(");\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CicloFacere(\n");

        for (Instruccion instruccion : instrucciones) {
            sb.append("    ").append(instruccion).append("\n");
        }

        sb.append("    Condicion: ").append(condicion).append("\n");
        sb.append(")");
        return sb.toString();
    }

}
