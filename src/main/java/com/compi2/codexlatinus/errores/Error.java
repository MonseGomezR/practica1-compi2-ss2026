package com.compi2.codexlatinus.errores;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Error {
    private final String mensaje;
    private final int linea;
    private final int columna;
    private final String tipoError;

    @Override
    public String toString() {
        return "Error " + tipoError + " [linea " + linea + ", columna " + columna + "]: " + mensaje;
    }
}