package com.compi2.codexlatinus.semantico;

import java.util.Collection;
import lombok.Getter;

@Getter
public class TablaSimbolos {

    private final Scope global;
    private Scope actual;

    public TablaSimbolos() {
        this.global = new Scope("GLOBAL", null);
        this.actual = global;
    }

    /**
     * Devuelve el scope global.
     *
     * @return
     */
    public Scope getGlobal() {
        return global;
    }

    /**
     * Devuelve el scope donde nos encontramos actualmente.
     *
     * @return
     */
    public Scope getActual() {
        return actual;
    }

    /**
     * Crea un nuevo scope hijo del scope actual y entra inmediatamente en él.
     *
     * @param nombre
     * @return
     */
    public Scope entrarScope(String nombre) {
        Scope nuevoScope = new Scope(nombre, actual);
        actual.agregarHijo(nuevoScope);
        actual = nuevoScope;
        return nuevoScope;
    }

    public void entrarScopeExistente(Scope scope) {
        if (scope != null) {
            actual = scope;
        }
    }

    /**
     * Sale del scope actual y vuelve al padre.
     *
     * El scope GLOBAL no tiene padre, por lo que nunca se puede salir de él.
     */
    public void salirScope() {
        if (actual.getPadre() != null) {
            actual = actual.getPadre();
        }
    }

    /**
     * Declara un símbolo en el scope actual.
     *
     * @param simbolo
     * @return true si se declaró correctamente. false si ya existía en este
     * scope.
     */
    public boolean declarar(Simbolo simbolo) {
        return actual.declarar(simbolo);
    }

    /**
     * Busca un símbolo desde el scope actual hacia sus scopes padres.
     *
     * @param nombre
     * @return
     */
    public Simbolo buscar(String nombre) {
        return actual.buscar(nombre);
    }

    public Scope buscarScopeHijo(String nombre) {
        for (Scope hijo : actual.getHijos()) {
            if (hijo.getNombre().equals(nombre)) {
                return hijo;
            }
        }

        return null;
    }

    /**
     * Busca solamente en el scope actual.
     *
     * @param nombre
     * @return
     */
    public Simbolo buscarLocal(String nombre) {
        return actual.buscarLocal(nombre);
    }

    /**
     * Verifica si existe un símbolo en el scope actual.
     *
     * @param nombre
     * @return
     */
    public boolean existeLocal(String nombre) {
        return actual.existeLocal(nombre);
    }

    /**
     * Devuelve los símbolos del scope actual.
     *
     * @return
     */
    public Collection<Simbolo> getSimbolosActuales() {
        return actual.getSimbolos();
    }

    /**
     * Verifica si actualmente estamos en el scope global.
     *
     * @return
     */
    public boolean estaEnGlobal() {
        return actual == global;
    }
}
