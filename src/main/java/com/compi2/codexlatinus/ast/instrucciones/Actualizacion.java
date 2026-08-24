/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.compi2.codexlatinus.ast.instrucciones;

import com.compi2.codexlatinus.TraductorPigLatin;
import com.compi2.codexlatinus.ast.expresiones.Expresion;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@AllArgsConstructor
@Getter
public class Actualizacion extends Instruccion {

    private String id;
    private String operador;
    private Expresion expresion;

    public Actualizacion(Expresion expresion) {
        this.expresion = expresion;
    }

    public Actualizacion(String id, String operador) {
        this.id = id;
        this.operador = operador;
        this.expresion = null;
    }

    @Override
    public void aPigLatin(StringBuffer sb) {
        TraductorPigLatin.convert(id);
    }

    @Override
    public String toString() {
        if (expresion != null) {
            return "Actualizacion(" + expresion + ")";
        }
        return "Actualizacion(" + id + operador + ")";
    }
}
