package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.ast.NodoAst;
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
public class RamaCondicional implements NodoAst{
    private final Expresion condicion;
    private final List<Instruccion> instrucciones;
    private Scope ambito;
    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (condicion == null) {
            sb.append("Rama(else)");
        } else {
            sb.append("Rama(")
              .append(condicion)
              .append(")");
        }

        if (instrucciones != null) {
            for (Instruccion instruccion : instrucciones) {
                sb.append("\n        ")
                  .append(instruccion);
            }
        }

        return sb.toString();
    }

    @Override
    public void aPigLatin(StringBuffer sb) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
