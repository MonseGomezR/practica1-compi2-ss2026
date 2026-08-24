package com.compi2.codexlatinus;

/**
 *
 * @author Usuario
 */
import com.compi2.codexlatinus.ast.NodoAst;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArbolBuilder {

    private static final Set<String> CAMPOS_IGNORADOS = Set.of("ambito");

    public static NodoGrafico construir(Object obj) {
        NodoGrafico nodo = new NodoGrafico();

        if (obj == null) {
            nodo.etiqueta = "null";
            return nodo;
        }

        StringBuilder etiqueta = new StringBuilder(obj.getClass().getSimpleName());
        List<Object> hijosNodoAst = new ArrayList<>();

        for (Class<?> clase = obj.getClass(); clase != null && clase != Object.class; clase = clase.getSuperclass()) {
            for (Field campo : clase.getDeclaredFields()) {
                if (campo.isSynthetic() || CAMPOS_IGNORADOS.contains(campo.getName())) continue;
                campo.setAccessible(true);
                try {
                    Object valor = campo.get(obj);
                    if (valor == null) continue;

                    if (valor instanceof NodoAst) {
                        hijosNodoAst.add(valor);
                    } else if (valor instanceof List<?> lista) {
                        boolean listaDeNodos = false;
                        for (Object item : lista) {
                            if (item instanceof NodoAst) {
                                hijosNodoAst.add(item);
                                listaDeNodos = true;
                            }
                        }
                        if (!listaDeNodos && !lista.isEmpty()) {
                            etiqueta.append("\n").append(campo.getName()).append("=").append(lista);
                        }
                    } else {
                        etiqueta.append("\n").append(campo.getName()).append("=").append(valor);
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        nodo.etiqueta = etiqueta.toString();
        for (Object hijo : hijosNodoAst) {
            nodo.hijos.add(construir(hijo));
        }

        return nodo;
    }
}
