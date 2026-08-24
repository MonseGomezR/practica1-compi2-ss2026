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
public class CicloPer extends Instruccion {

    private final Instruccion inicializacion;
    private final Expresion condicion;
    private final Instruccion actualizacion;
    private final List<Instruccion> instrucciones;
    private Scope ambito;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("per (");
        if (inicializacion != null) {
            inicializacion.aPigLatin(sb);
        }
        sb.append("; ");
        condicion.aPigLatin(sb);
        sb.append("; ");
        if (actualizacion != null) {
            actualizacion.aPigLatin(sb);
        }
        sb.append(") {\n");
        for (Instruccion instruccion : instrucciones) {
            instruccion.aPigLatin(sb);
        }
        sb.append("}\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CicloPer(\n");
        sb.append("    Inicializacion: ").append(inicializacion).append("\n");
        sb.append("    Condicion: ").append(condicion).append("\n");
        sb.append("    Actualizacion: ").append(actualizacion).append("\n");

        for (Instruccion instruccion : instrucciones) {
            sb.append("    ").append(instruccion).append("\n");
        }

        sb.append(")");

        return sb.toString();
    }
}
