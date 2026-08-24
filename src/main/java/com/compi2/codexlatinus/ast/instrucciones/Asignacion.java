package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.expresiones.AccesoVariable;
import com.compi2.codexlatinus.ast.expresiones.Expresion;
import java.util.List;
import lombok.Getter;

@Getter
public class Asignacion extends Instruccion {

    private final AccesoVariable objetivo;
    private final Expresion expresion;
    private final List<AsignacionAtributo> atributos;

    public Asignacion(AccesoVariable objetivo, Expresion expresion) {
        this.objetivo = objetivo;
        this.expresion = expresion;
        this.atributos = null;
    }

    public Asignacion(AccesoVariable objetivo, List<AsignacionAtributo> atributos) {
        this.objetivo = objetivo;
        this.expresion = null;
        this.atributos = atributos;
    }

    @Override
    public void aPigLatin(StringBuffer sb) {

        objetivo.aPigLatin(sb);
        sb.append(" = ");

        if (expresion != null) {
            expresion.aPigLatin(sb);
        } else {
            sb.append("{");

            for (int i = 0; i < atributos.size(); i++) {

                if (i > 0) {
                    sb.append(", ");
                }

                atributos.get(i).aPigLatin(sb);
            }

            sb.append("}");
        }

        sb.append(";\n");
    }
    
    @Override
    public String toString() {
        return "Asignacion("
                + objetivo
                + ", "
                + expresion
                + ", "
                + atributos
                + ")";
    }
}