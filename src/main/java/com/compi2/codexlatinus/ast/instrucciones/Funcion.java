package com.compi2.codexlatinus.ast.instrucciones;

/**
 *
 * @author Usuario
 */
import com.compi2.codexlatinus.Tipo;
import com.compi2.codexlatinus.TraductorPigLatin;
import com.compi2.codexlatinus.semantico.Scope;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Funcion extends Instruccion {

    private final String id;
    private final List<Parametro> parametros;
    private final List<Instruccion> instrucciones;
    private Scope ambito;

    private final boolean tieneRetorno;
    private final Tipo tipoRetorno;

    @Override
    public void aPigLatin(StringBuffer sb) {

        if (tieneRetorno) {
            sb.append("ratio ");
            tipoRetorno.aPigLatin(sb);
            sb.append(" ");
        } else {
            sb.append("actio ");
        }

        sb.append(TraductorPigLatin.convert(id))
                .append("(");

        if (parametros != null) {
            for (int i = 0; i < parametros.size(); i++) {

                if (i > 0) {
                    sb.append(", ");
                }

                parametros.get(i).aPigLatin(sb);
            }
        }

        sb.append(") {\n");

        if (instrucciones != null) {
            for (Instruccion instruccion : instrucciones) {
                instruccion.aPigLatin(sb);
            }
        }

        sb.append("} finis;\n");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Funcion(")
                .append(id)
                .append(", ")
                .append(tipoRetorno)
                .append(", ")
                .append(parametros)
                .append(", ")
                .append(tieneRetorno)
                .append(")");

        if (instrucciones != null) {
            for (Instruccion instruccion : instrucciones) {
                sb.append("\n    ")
                        .append(instruccion);
            }
        }

        return sb.toString();
    }
}
