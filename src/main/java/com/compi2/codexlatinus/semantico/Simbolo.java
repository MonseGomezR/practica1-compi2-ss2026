package com.compi2.codexlatinus.semantico;

import com.compi2.codexlatinus.Tipo;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Simbolo {

    private final String nombre;
    private final Tipo tipo;
    private final TipoSimbolo tipoSimbolo;
    private final int linea;
    private final int columna;

    // Arrays
    private Integer sizeArray;

    // Funciones
    private final List<Simbolo> parametros;

    // Estructuras
    private final List<Simbolo> atributos;

    public Simbolo(String nombre, Tipo tipo, TipoSimbolo tipoSimbolo, int linea, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tipoSimbolo = tipoSimbolo;
        this.linea = linea;
        this.columna = columna;

        this.sizeArray = null;
        this.parametros = new ArrayList<>();
        this.atributos = new ArrayList<>();
    }

    public void agregarParametro(Simbolo parametro) {
        parametros.add(parametro);
    }

    public void agregarAtributo(Simbolo atributo) {
        atributos.add(atributo);
    }

    public Simbolo buscarAtributo(String nombre) {
        for (Simbolo atributo : atributos) {
            if (atributo.getNombre().equals(nombre)) {
                return atributo;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder();

        resultado.append(nombre)
                .append(" : ")
                .append(tipo)
                .append(" [")
                .append(tipoSimbolo)
                .append("]");

        if (tipoSimbolo == TipoSimbolo.ARRAY) {
            resultado.append(" [size=")
                    .append(sizeArray)
                    .append("]");
        }

        if (tipoSimbolo == TipoSimbolo.FUNCION) {
            resultado.append(" (parametros=")
                    .append(parametros.size())
                    .append(")");
        }

        if (tipoSimbolo == TipoSimbolo.ESTRUCTURA) {
            resultado.append(" (atributos=")
                    .append(atributos.size())
                    .append(")");
        }

        resultado.append(" (linea ")
                .append(linea)
                .append(", columna ")
                .append(columna)
                .append(")");

        return resultado.toString();
    }
}
