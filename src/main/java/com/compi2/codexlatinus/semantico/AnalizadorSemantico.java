package com.compi2.codexlatinus.semantico;

import com.compi2.codexlatinus.Tipo;
import com.compi2.codexlatinus.errores.Error;
import com.compi2.codexlatinus.TipoEnum;
import com.compi2.codexlatinus.ast.Ast;
import com.compi2.codexlatinus.ast.expresiones.*;
import com.compi2.codexlatinus.ast.instrucciones.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@Getter
public class AnalizadorSemantico {

    private final Ast ast;
    private final TablaSimbolos tablaSimbolos;
    private final List<Error> errores;

    public AnalizadorSemantico(Ast ast, TablaSimbolos tablaSimbolos) {
        this.ast = ast;
        this.tablaSimbolos = tablaSimbolos;
        this.errores = new ArrayList<>();
    }

    // =========================================================
    // ANALISIS PRINCIPAL
    // =========================================================
    public void analizar() {

        // El análisis siempre comienza en GLOBAL.
        tablaSimbolos.entrarScopeExistente(tablaSimbolos.getGlobal());

        for (Instruccion instruccion : ast.getInstrucciones()) {
            analizarInstruccion(instruccion);
        }

        tablaSimbolos.entrarScopeExistente(tablaSimbolos.getGlobal());
    }

    private void analizarInstruccion(Instruccion instruccion) {
        if (instruccion != null) {
            switch (instruccion) {

                case Declaracion declaracion ->
                    analizarDeclaracion(declaracion);

                case DeclaracionArray declaracionArray ->
                    analizarDeclaracionArray(declaracionArray);

                case Asignacion asignacion ->
                    analizarAsignacion(asignacion);

                case Funcion funcion ->
                    analizarFuncion(funcion);

                case Condicional condicional ->
                    analizarCondicional(condicional);

                case CicloDum cicloDum ->
                    analizarCicloDum(cicloDum);

                case CicloFacere cicloFacere ->
                    analizarCicloFacere(cicloFacere);

                case CicloPer cicloPer ->
                    analizarCicloPer(cicloPer);

                case DeclaracionStructura decStructura ->
                    analizarDeclaracionEstructuraUso(decStructura);

                default -> {
                    // Instrucciones que todavía no tienen
                    // análisis semántico específico.
                }
            }
        }
    }

    // =========================================================
    // DECLARACIONES
    // =========================================================
    private void analizarDeclaracion(Declaracion declaracion) {

        if (declaracion.getExpresion() == null) {
            return;
        }

        Tipo tipoVariable
                = declaracion.getTipo();

        Tipo tipoExpresion
                = resolverTipo(declaracion.getExpresion());

        if (tipoExpresion == null) {
            return;
        }

        if (!tiposCompatibles(tipoVariable, tipoExpresion)) {
            agregarError("No se puede asignar un valor de tipo " + tipoExpresion + " a una variable de tipo " + tipoVariable, 0, 0);
        }
    }

    private void analizarDeclaracionArray(DeclaracionArray declaracionArray) {

        if (declaracionArray.getValores() == null) {
            return;
        }

        Tipo tipoArray
                = declaracionArray.getTipo();

        for (Expresion expresion
                : declaracionArray.getValores()) {

            Tipo tipoValor
                    = resolverTipo(expresion);

            if (tipoValor == null) {
                continue;
            }

            if (!tiposCompatibles(tipoArray, tipoValor)) {

                agregarError(
                        "El valor del arreglo no es compatible "
                        + "con el tipo "
                        + tipoArray,
                        0,
                        0
                );
            }
        }
    }

    private void analizarDeclaracionEstructuraUso(DeclaracionStructura declaracion) {
        String nombre = declaracion.getId();
        String nombreEstructura = declaracion.getNombreEstructura();

        // 1. Buscar la estructura
        Simbolo simboloEstructura = tablaSimbolos.buscar(nombreEstructura);

        if (simboloEstructura == null) {
            agregarError("La estructura '" + nombreEstructura + "' no existe.", 0, 0);
            return;
        }

        // 2. Verificar que realmente sea una estructura
        if (simboloEstructura.getTipoSimbolo() != TipoSimbolo.ESTRUCTURA) {
            agregarError(
                    "'" + nombreEstructura + "' no es una estructura.",
                    0,
                    0
            );
            return;
        }

        // 3. Buscar la variable que estamos declarando
        Simbolo variable = tablaSimbolos.buscar(nombre);

        if (variable == null) {
            agregarError("La variable '" + nombre + "' no existe en la tabla de símbolos.", 0, 0);
            return;
        }

        // 4. Validar cada atributo
        for (AsignacionAtributo atributo : declaracion.getAtributos()) {
            analizarAtributoEstructura(simboloEstructura, atributo);
        }
    }

    private void analizarAtributoEstructura(
            Simbolo estructura,
            AsignacionAtributo atributo) {

        Simbolo atributoDefinido
                = estructura.buscarAtributo(atributo.getId());

        // El atributo no existe en la estructura
        if (atributoDefinido == null) {
            agregarError(
                    "El atributo '" + atributo.getId()
                    + "' no existe en la structura '"
                    + estructura.getNombre() + "'.",
                    0,
                    0
            );
            return;
        }

        Tipo tipoDefinido = atributoDefinido.getTipo();

        Expresion valor = atributo.getValor();

        if (atributoDefinido.getTipoSimbolo() == TipoSimbolo.ARRAY
                && valor instanceof AccesoVariable acceso
                && acceso.getPartes().size() == 1) {

            ParteAcceso parte = acceso.getPartes().get(0);

            if (parte.getIndice() != null) {

                String nombreTipo = parte.getId();

                if (tipoDefinido.getNombreStructura() != null
                        && tipoDefinido.getNombreStructura()
                                .equals(nombreTipo)) {

                    Tipo tipoIndice
                            = resolverTipo(parte.getIndice());

                    if (tipoIndice == null
                            || tipoIndice.getTipo()
                            != TipoEnum.NUMERUS) {

                        agregarError(
                                "El tamaño de un arreglo debe ser de tipo NUMERUS.",
                                0,
                                0
                        );

                        return;
                    }

                    return;
                }
            }
        }

        // Caso normal
        Tipo tipoValor = resolverTipo(valor);

        if (!tiposCompatibles(tipoDefinido, tipoValor)) {

            agregarError(
                    "El atributo '"
                    + atributo.getId()
                    + "' esperaba tipo "
                    + atributoDefinido.getTipo()
                    + " pero recibio "
                    + tipoValor
                    + ".",
                    0,
                    0
            );
        }
    }

    // =========================================================
    // ASIGNACIONES
    // =========================================================
    private void analizarAsignacion(
            Asignacion asignacion) {

        Tipo tipoExpresion
                = resolverTipo(asignacion.getExpresion());

        Tipo tipoObjetivo
                = resolverTipo(asignacion.getObjetivo());

        if (tipoObjetivo == null || tipoExpresion == null) {
            return;
        }

        if (!tiposCompatibles(
                tipoObjetivo,
                tipoExpresion)) {

            agregarError(
                    "No se puede asignar un valor de tipo "
                    + tipoExpresion
                    + " a una variable de tipo "
                    + tipoObjetivo,
                    0,
                    0
            );
        }
    }

    private void analizarFuncion(Funcion funcion) {
        Scope scopeFuncion = funcion.getAmbito();

        if (scopeFuncion == null) {
            return;
        }

        tablaSimbolos.entrarScopeExistente(scopeFuncion);

        for (Instruccion instruccion : funcion.getInstrucciones()) {
            analizarInstruccionFuncion(instruccion, funcion);
        }

        tablaSimbolos.salirScope();
    }

    private void analizarInstruccionFuncion(Instruccion instruccion, Funcion funcion) {
        if (instruccion instanceof Retorno retorno) {
            analizarRetorno(retorno, funcion);
            return;
        }

        analizarInstruccion(instruccion);
    }

    private void analizarRetorno(
            Retorno retorno,
            Funcion funcion) {

        if (!funcion.isTieneRetorno()) {

            agregarError(
                    "La funcion '"
                    + funcion.getId()
                    + "' no puede contener "
                    + "una instruccion reddere.",
                    0,
                    0
            );

            return;
        }

        Tipo tipoRetornoEsperado
                = funcion.getTipoRetorno();

        Tipo tipoRetornoObtenido
                = resolverTipo(retorno.getExpresion());

        if (tipoRetornoObtenido == null) {
            return;
        }

        if (!tiposCompatibles(
                tipoRetornoEsperado,
                tipoRetornoObtenido)) {

            agregarError(
                    "La funcion '"
                    + funcion.getId()
                    + "' debe retornar "
                    + tipoRetornoEsperado
                    + " y se obtuvo "
                    + tipoRetornoObtenido,
                    0,
                    0
            );
        }
    }

    private void analizarCondicional(Condicional condicional) {

        for (RamaCondicional rama : condicional.getRamas()) {

            Scope scope = rama.getAmbito();

            if (scope == null) {
                continue;
            }

            tablaSimbolos.entrarScopeExistente(scope);

            if (rama.getCondicion() != null) {
                Tipo tipoCondicion = resolverTipo(rama.getCondicion());
                if (tipoCondicion == null
                        || tipoCondicion.getTipo() != TipoEnum.BOOL) {
                    agregarError("La condicion debe ser de tipo BOOL.", 0, 0);
                }
            }

            for (Instruccion instruccion : rama.getInstrucciones()) {
                analizarInstruccion(instruccion);
            }

            tablaSimbolos.salirScope();
        }
    }

    private void analizarCicloDum(CicloDum cicloDum) {
        Scope scope = cicloDum.getAmbito();

        if (scope == null) {
            return;
        }

        tablaSimbolos.entrarScopeExistente(scope);

        // La condicion se resuelve desde el scope del ciclo.
        Tipo tipoCondicion
                = resolverTipo(
                        cicloDum.getCondicion()
                );

        if (tipoCondicion == null
                || tipoCondicion.getTipo() != TipoEnum.BOOL) {

            agregarError(
                    "La condicion del ciclo DUM "
                    + "debe ser de tipo BOOL.",
                    0,
                    0
            );
        }

        for (Instruccion instruccion
                : cicloDum.getInstrucciones()) {

            analizarInstruccion(instruccion);
        }

        tablaSimbolos.salirScope();
    }

    private void analizarCicloFacere(CicloFacere cicloFacere) {
        Scope scope = cicloFacere.getAmbito();

        if (scope == null) {
            return;
        }

        tablaSimbolos.entrarScopeExistente(scope);

        for (Instruccion instruccion : cicloFacere.getInstrucciones()) {
            analizarInstruccion(instruccion);
        }

        Tipo tipoCondicion = resolverTipo(cicloFacere.getCondicion());

        if (tipoCondicion == null
                || tipoCondicion.getTipo() != TipoEnum.BOOL) {
            agregarError(
                    "La condicion del ciclo FACERE "
                    + "debe ser de tipo BOOL.",
                    0,
                    0
            );
        }

        tablaSimbolos.salirScope();
    }

    private void analizarCicloPer(CicloPer cicloPer) {
        Scope scope = cicloPer.getAmbito();
        if (scope == null) {
            agregarError("El ciclo PER no tiene un scope asociado.", 0, 0);
            return;
        }

        tablaSimbolos.entrarScopeExistente(scope);

        // Condición
        Tipo tipoCondicion = resolverTipo(cicloPer.getCondicion());

        if (tipoCondicion == null
                || tipoCondicion.getTipo() != TipoEnum.BOOL) {
            agregarError("La condición del ciclo PER debe ser de tipo BOOL.", 0, 0);
        }

        // Actualización
        analizarInstruccion(cicloPer.getActualizacion());

        // Instrucciones del ciclo
        for (Instruccion instruccion : cicloPer.getInstrucciones()) {
            analizarInstruccion(instruccion);
        }

        tablaSimbolos.salirScope();
    }

    private Tipo resolverTipo(Expresion expresion) {
        if (expresion == null) {
            return null;
        }
        if (expresion instanceof Literal literal) {
            return literal.getTipo();
        }
        if (expresion instanceof AccesoVariable acceso) {
            return resolverTipoAccesoVariable(acceso);
        }
        if (expresion instanceof LlamadaFuncion llamada) {
            Simbolo simbolo = tablaSimbolos.buscar(llamada.getId());
            if (simbolo == null) {
                agregarError("La funcion '" + llamada.getId() + "' no existe.", 0, 0);
                return null;
            }
            if (simbolo.getTipoSimbolo() != TipoSimbolo.FUNCION) {
                agregarError("'" + llamada.getId() + "' no es una funcion.", 0, 0);
                return null;
            }
            return simbolo.getTipo();
        }
        if (expresion instanceof Operacion operacion) {
            return resolverTipoOperacion(operacion);
        }
        return null;
    }

    private Tipo resolverTipoAccesoVariable(AccesoVariable acceso) {

        if (acceso == null
                || acceso.getPartes() == null
                || acceso.getPartes().isEmpty()) {
            return null;
        }

        List<ParteAcceso> partes = acceso.getPartes();

        ParteAcceso primeraParte = partes.get(0);

        String nombreVariable = primeraParte.getId();

        Simbolo simbolo = tablaSimbolos.buscar(nombreVariable);

        if (simbolo == null) {
            agregarError(
                    "La variable '" + nombreVariable + "' no existe.",
                    0,
                    0
            );
            return null;
        }

        Tipo tipoActual = simbolo.getTipo();

        if (primeraParte.getIndice() != null) {

            Tipo tipoIndice = resolverTipo(primeraParte.getIndice());

            if (tipoIndice == null
                    || tipoIndice.getTipo() != TipoEnum.NUMERUS) {

                agregarError(
                        "El indice de un arreglo debe ser de tipo NUMERUS.",
                        0,
                        0
                );

                return null;
            }

            if (simbolo.getTipoSimbolo() != TipoSimbolo.ARRAY) {
                if (simbolo.getTipo() != null && simbolo.getTipo().getNombreStructura() == null) {
                    agregarError(
                            "'" + simbolo.getNombre()
                            + "' no es un arreglo.",
                            0,
                            0
                    );

                    return null;
                }
            }

            tipoActual = simbolo.getTipo();
        }

        // =========================================================
        // ACCESOS .ATRIBUTO
        // =========================================================
        for (int i = 1; i < partes.size(); i++) {

            ParteAcceso parte = partes.get(i);

            String nombreAtributo = parte.getId();

            // -----------------------------------------------------
            // El tipo actual debe ser una estructura
            // -----------------------------------------------------
            if (tipoActual == null
                    || tipoActual.getNombreStructura() == null) {

                agregarError(
                        "'" + partes.get(i - 1).getId()
                        + "' no es una estructura y no puede accederse "
                        + "al atributo '" + nombreAtributo + "'.",
                        0,
                        0
                );

                return null;
            }

            String nombreEstructura = tipoActual.getNombreStructura();

            Simbolo estructura = tablaSimbolos.buscar(nombreEstructura);

            if (estructura == null
                    || estructura.getTipoSimbolo() != TipoSimbolo.ESTRUCTURA) {

                agregarError(
                        "La estructura '" + nombreEstructura
                        + "' no existe.",
                        0,
                        0
                );

                return null;
            }

            // -----------------------------------------------------
            // Buscar atributo
            // -----------------------------------------------------
            Simbolo atributo = estructura.buscarAtributo(nombreAtributo);

            if (atributo == null) {

                agregarError(
                        "El atributo '" + nombreAtributo
                        + "' no existe en la estructura '"
                        + nombreEstructura + "'.",
                        0,
                        0
                );

                return null;
            }

            tipoActual = atributo.getTipo();

            // -----------------------------------------------------
            // Índice del atributo
            // -----------------------------------------------------
            if (parte.getIndice() != null) {

                Tipo tipoIndice = resolverTipo(parte.getIndice());

                if (tipoIndice == null
                        || tipoIndice.getTipo() != TipoEnum.NUMERUS) {

                    agregarError(
                            "El indice de un arreglo debe ser de tipo NUMERUS.",
                            0,
                            0
                    );

                    return null;
                }

                if (atributo.getTipoSimbolo() != TipoSimbolo.ARRAY) {

                    agregarError(
                            "'" + nombreAtributo
                            + "' no es un arreglo.",
                            0,
                            0
                    );

                    return null;
                }

                /*
             * Al acceder al elemento del arreglo,
             * el tipo resultante es el tipo del atributo.
                 */
                tipoActual = atributo.getTipo();
            }
        }

        return tipoActual;
    }

    private Tipo resolverTipoOperacion(Operacion operacion) {
        Tipo izquierda = null;
        Tipo derecha = null;

        if (operacion.getIzquierda() != null) {
            izquierda = resolverTipo(operacion.getIzquierda());
        }

        if (operacion.getDerecha() != null) {
            derecha = resolverTipo(operacion.getDerecha());
        }

        String operador = operacion.getOperador();

        switch (operador) {

            case "+", "-", "*", "/" -> {
                return resolverTipoAritmetico(izquierda, derecha);
            }

            case "<", ">", "<=", ">=", "==", "!=" -> {
                return new Tipo(TipoEnum.BOOL);
            }

            case "&&", "||", "non" -> {
                return new Tipo(TipoEnum.BOOL);
            }

            case "++", "--" -> {
                return izquierda;
            }

            default -> {
                return null;
            }
        }
    }

    private Tipo resolverTipoAritmetico(
            Tipo izquierda,
            Tipo derecha) {

        if (izquierda == null || derecha == null) {
            return null;
        }

        // Una estructura no puede utilizarse
        // en una operación aritmética.
        if (izquierda.getTipo() == null
                || derecha.getTipo() == null) {
            return null;
        }

        if (izquierda.getTipo() == TipoEnum.TEXTUM
                || derecha.getTipo() == TipoEnum.TEXTUM) {

            return new Tipo(TipoEnum.TEXTUM);
        }

        if (izquierda.getTipo() == TipoEnum.DECIMALIS
                || derecha.getTipo() == TipoEnum.DECIMALIS) {

            return new Tipo(TipoEnum.DECIMALIS);
        }

        return new Tipo(TipoEnum.NUMERUS);
    }

    // =========================================================
    // COMPATIBILIDAD DE TIPOS
    // =========================================================
    private boolean tiposCompatibles(
            Tipo destino,
            Tipo origen) {

        if (destino == null || origen == null) {
            return false;
        }

        // ================================
        // ESTRUCTURAS
        // ================================
        if (destino.getNombreStructura() != null
                || origen.getNombreStructura() != null) {

            if (destino.getNombreStructura() == null
                    || origen.getNombreStructura() == null) {

                return false;
            }

            return destino.getNombreStructura()
                    .equals(origen.getNombreStructura());
        }

        // ================================
        // TIPOS PRIMITIVOS
        // ================================
        if (destino.getTipo() == null
                || origen.getTipo() == null) {

            return false;
        }

        if (destino.getTipo() == origen.getTipo()) {
            return true;
        }

        // NUMERUS -> DECIMALIS
        return destino.getTipo() == TipoEnum.DECIMALIS
                && origen.getTipo() == TipoEnum.NUMERUS;
    }

    // =========================================================
    // ERRORES
    // =========================================================
    private void agregarError(String mensaje, int linea, int columna) {
        errores.add(new Error(mensaje, linea, columna, "semantico"));
    }

}
