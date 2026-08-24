package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.TraductorPigLatin;
import com.compi2.codexlatinus.ast.expresiones.Expresion;
import java.util.List;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Usuario
 */
@RequiredArgsConstructor
@Setter
@Getter
public class AsignacionAtributo extends Instruccion {

    private final String id;
    private final Expresion valor;
    private List<AsignacionAtributo> atributos;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append(TraductorPigLatin.convert(id));
        sb.append(": ");

        if (valor != null) {
            valor.aPigLatin(sb);
        }
    }

    @Override
    public String toString() {
        return id + ": " + valor;
    }
}
