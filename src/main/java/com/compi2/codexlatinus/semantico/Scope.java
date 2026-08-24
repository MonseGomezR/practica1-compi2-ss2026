package com.compi2.codexlatinus.semantico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@Getter
public class Scope {

    private final String nombre;
    private final Scope padre;
    private final List<Scope> hijos;

    private final Map<String, Simbolo> simbolos;

    public Scope(String nombre, Scope padre) {
        this.nombre = nombre;
        this.padre = padre;
        this.hijos = new ArrayList<>();
        this.simbolos = new LinkedHashMap<>();
    }
    
    /**
     * Agrega un símbolo solamente si no existe otro símbolo con el mismo nombre
     * en este scope.
     *
     * @param simbolo
     * @return true si se agregó correctamente, false si ya existía.
     */
    public boolean declarar(Simbolo simbolo) {
        if (simbolos.containsKey(simbolo.getNombre())) {
            return false;
        }
        simbolos.put(simbolo.getNombre(), simbolo);
        return true;
    }

    /**
     * Agrega un hijo
     *
     * @param scope
     */
    public void agregarHijo(Scope scope) {
        hijos.add(scope);
    }

    /**
     * Busca un símbolo únicamente en este scope.
     *
     * @param nombre
     * @return
     */
    public Simbolo buscarLocal(String nombre) {
        return simbolos.get(nombre);
    }

    /**
     * Busca un símbolo en este scope y posteriormente en todos sus scopes
     * padres.
     *
     * @param nombre
     * @return
     */
    public Simbolo buscar(String nombre) {
        Scope scopeActual = this;
        while (scopeActual != null) {
            Simbolo simbolo = scopeActual.buscarLocal(nombre);

            if (simbolo != null) {
                return simbolo;
            }

            scopeActual = scopeActual.getPadre();
        }

        return null;
    }

    /**
     * Verifica si el símbolo ya existe específicamente en este scope.
     *
     * @param nombre
     * @return
     */
    public boolean existeLocal(String nombre) {
        return simbolos.containsKey(nombre);
    }

    /**
     * Devuelve todos los símbolos declarados en este scope.
     *
     * @return
     */
    public Collection<Simbolo> getSimbolos() {
        return simbolos.values();
    }

    @Override
    public String toString() {
        return "Scope{"
                + "nombre='" + nombre + '\''
                + ", simbolos=" + simbolos.values()
                + '}';
    }
}
