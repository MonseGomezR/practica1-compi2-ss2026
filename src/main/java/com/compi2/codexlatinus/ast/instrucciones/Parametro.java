package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.Tipo;
import com.compi2.codexlatinus.ast.NodoAst;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */

@AllArgsConstructor
@Getter
public class Parametro implements NodoAst {

    private final String id;
    private final Tipo tipo;

    @Override
    public void aPigLatin(StringBuffer sb) {
        sb.append("esto ")
          .append(id)
          .append(": ");
        tipo.aPigLatin(sb);
    }

    @Override
    public String toString() {
        return "Parametro(" + id + ", " + tipo + ")";
    }
}