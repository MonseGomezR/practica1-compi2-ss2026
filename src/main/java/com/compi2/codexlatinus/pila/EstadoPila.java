package com.compi2.codexlatinus.pila;

import java.util.List;

public class EstadoPila {
    private final List<String> pila;
    private final String operacion;

    public EstadoPila(List<String> pila, String operacion) {
        this.pila = pila;
        this.operacion = operacion;
    }

    public List<String> getPila() {
        return pila;
    }

    public String getOperacion() {
        return operacion;
    }
}