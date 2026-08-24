package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.TraductorPigLatin;
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
public class Structura extends Instruccion {

    private final String nombre;
    private final List<Atributo> atributos;

    private Scope ambito;

    @Override
    public void aPigLatin(StringBuffer sb) {

        sb.append("struct ")
                .append(nombre)
                .append(" {\n");

        for (Atributo atributo : atributos) {
            sb.append("    ");
            TraductorPigLatin.convert(atributo.getNombre());
            sb.append("\n");
        }

        sb.append("}\n");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Estructura(")
                .append(nombre)
                .append(")\n");

        for (Atributo atributo : atributos) {
            sb.append("    ")
                    .append(atributo)
                    .append("\n");
        }

        return sb.toString();
    }
}
