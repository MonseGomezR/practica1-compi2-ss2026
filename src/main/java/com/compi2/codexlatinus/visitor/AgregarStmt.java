package com.compi2.codexlatinus.visitor;

import com.compi2.codexlatinus.ast.instrucciones.Instruccion;

/**
 *
 * @author Usuario
 */
public interface AgregarStmt {
    void addStatement(Instruccion statement);
}
