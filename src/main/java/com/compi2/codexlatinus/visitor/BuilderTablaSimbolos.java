package com.compi2.codexlatinus.visitor;

import com.compi2.codexlatinus.Tipo;
import com.compi2.codexlatinus.TipoEnum;
import com.compi2.codexlatinus.ast.instrucciones.AsignacionAtributo;
import com.compi2.codexlatinus.ast.instrucciones.Atributo;
import com.compi2.codexlatinus.semantico.Scope;
import com.compi2.codexlatinus.semantico.Simbolo;
import com.compi2.codexlatinus.semantico.TablaSimbolos;
import com.compi2.codexlatinus.semantico.TipoSimbolo;
import java.util.List;

public class BuilderTablaSimbolos {

    private final TablaSimbolos tabla;

    public BuilderTablaSimbolos() {
        this.tabla = new TablaSimbolos();
    }

    public TablaSimbolos getTabla() {
        return tabla;
    }

    private void declarar(Simbolo simbolo) {
        boolean agregado = tabla.declarar(simbolo);
        if (!agregado) {
            throw new RuntimeException("Error semántico en línea " + simbolo.getLinea() + ", columna " + simbolo.getColumna() + ": el identificador '" + simbolo.getNombre() + "' ya fue declarado en este alcance.");
        }
    }

    public void symVariable(String nombre, Tipo tipo, int linea, int columna) {
        TipoSimbolo tipoSimbolo;

        if (tipo.getNombreStructura() != null) {
            tipoSimbolo = TipoSimbolo.ESTRUCTURA_USO;
        } else {
            tipoSimbolo = TipoSimbolo.VARIABLE;
        }

        Simbolo simbolo = new Simbolo(
                nombre,
                tipo,
                tipoSimbolo,
                linea,
                columna
        );

        declarar(simbolo);
    }

    public void symArray(String nombre, Tipo tipo, int size, int linea, int columna) {
        Simbolo simbolo = new Simbolo(nombre, tipo, TipoSimbolo.ARRAY, linea, columna);
        simbolo.setSizeArray(size);
        declarar(simbolo);
    }

    public void symFuncionSR(String nombre, int linea, int columna) {
        Simbolo funcion = new Simbolo(nombre, null, TipoSimbolo.FUNCION, linea, columna);
        declarar(funcion);
    }

    public void symFuncionCR(String nombre, Tipo tipo, int linea, int columna) {
        Simbolo funcion = new Simbolo(nombre, tipo, TipoSimbolo.FUNCION, linea, columna);
        declarar(funcion);
    }

    public void symParametro(String nombre, Tipo tipo, int linea, int columna, String padre) {
        Simbolo parametro = new Simbolo(nombre, tipo, TipoSimbolo.PARAMETRO, linea, columna);
        Simbolo funcionPadre = tabla.buscar(padre);
        funcionPadre.agregarParametro(parametro);
        declarar(parametro);
    }

    public void symStructura(String nombre, List<Atributo> atributos, int linea, int columna) {
        Simbolo simbolo = new Simbolo(nombre, null, TipoSimbolo.ESTRUCTURA, linea, columna);
        for (Atributo atributo : atributos) {
            Tipo tipo = atributo.getTipo();
            TipoSimbolo tipoSimbolo;
            if (tipo.getNombreStructura() != null) {
                tipoSimbolo = TipoSimbolo.ESTRUCTURA_USO;
            } else {
                tipoSimbolo = TipoSimbolo.VARIABLE;
            }
            Simbolo atributoSimbolo = new Simbolo(atributo.getNombre(), tipo, tipoSimbolo, linea, columna);
            simbolo.agregarAtributo(atributoSimbolo);
        }
        declarar(simbolo);
    }

    public void symStructuraUso(String nombre, String nombreStructura, int linea, int columna) {
        Tipo tipo = new Tipo();
        tipo.setNombreStructura(nombreStructura);

        Simbolo simbolo = new Simbolo(nombre, tipo, TipoSimbolo.ESTRUCTURA_USO, linea, columna);

        declarar(simbolo);
    }

    public Scope entrarNuevoScope(String nombre) {
        tabla.entrarScope(nombre);
        return tabla.getActual();
    }

    public void salirScopeActual() {
        tabla.salirScope();
    }
}
