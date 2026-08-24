package com.compi2.codexlatinus;

import com.compi2.codexlatinus.ast.NodoAst;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Usuario
 */
@Getter
@Setter
public class Tipo implements NodoAst{
    private TipoEnum tipo;
    private String nombreStructura;

    public Tipo() {
    }

    public Tipo(TipoEnum tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public void aPigLatin(StringBuffer sb) {
        if (tipo != null) {
            sb.append(TraductorPigLatin.convert(tipo.toString()));
        }else {
            sb.append(TraductorPigLatin.convert(nombreStructura));
        }
    }

    @Override
    public String toString() {
        if (tipo != null) {
            return tipo.toString();
        }else {
            return nombreStructura;
        }
    }
    
}
