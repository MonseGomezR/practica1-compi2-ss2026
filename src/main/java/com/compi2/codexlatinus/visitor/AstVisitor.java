package com.compi2.codexlatinus.visitor;

import com.compi2.CodexLatinusBaseVisitor;
import com.compi2.CodexLatinusParser;
import com.compi2.codexlatinus.*;
import com.compi2.codexlatinus.ast.Ast;
import com.compi2.codexlatinus.ast.NodoAst;
import com.compi2.codexlatinus.ast.expresiones.*;
import com.compi2.codexlatinus.ast.instrucciones.*;
import com.compi2.codexlatinus.semantico.Scope;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author Usuario
 */
@Getter
public class AstVisitor extends CodexLatinusBaseVisitor<NodoAst> {

    private final BuilderTablaSimbolos builderTabla;

    public AstVisitor() {
        this.builderTabla = new BuilderTablaSimbolos();
    }

    // --------- 1. Programa y Secciones Principales ---------
    @Override
    public Ast visitPrograma(CodexLatinusParser.ProgramaContext ctx) {
        Ast ast = new Ast();

        // VARIABLES
        if (ctx.seccionVariables() != null) {
            for (CodexLatinusParser.DeclaracionContext d : ctx.seccionVariables().declaracion()) {
                ast.addStatement((Instruccion) visit(d));
            }
        }

        // FUNCIONES
        if (ctx.seccionFunciones() != null) {
            for (CodexLatinusParser.FuncionContext f : ctx.seccionFunciones().funcion()) {
                ast.addStatement((Instruccion) visit(f));
            }
        }

        // MAIOR
        if (ctx.seccionPrincipal() != null) {
            for (CodexLatinusParser.InstruccionContext i : ctx.seccionPrincipal().instruccion()) {
                ast.addStatement((Instruccion) visit(i));
            }
        }

        return ast;
    }

    @Override
    public Instruccion visitDeclaracion(CodexLatinusParser.DeclaracionContext ctx) {
        return (Instruccion) visitChildren(ctx);
    }

    @Override
    public Instruccion visitInstruccion(CodexLatinusParser.InstruccionContext ctx) {
        if (ctx.PERGE() != null) {
            return new Perge();
        }

        if (ctx.INTERRUMPE() != null) {
            return new Interrumpe();
        }
        return (Instruccion) visitChildren(ctx);
    }
    // ----------------------------------------------------------------------

    // --------- 2. Declaraciones ---------
    @Override
    public Instruccion visitDeclaracionVariable(CodexLatinusParser.DeclaracionVariableContext ctx) {
        String id = ctx.ID().getText();
        Tipo tipo;
        Expresion expresion = null;

        if (ctx.tipo() != null) {
            tipo = (Tipo) visit(ctx.tipo());
            if (ctx.expresion() != null) {
                expresion = (Expresion) visit(ctx.expresion());
            }

        } else {
            if (ctx.VERUM() != null) {
                tipo = new Tipo(TipoEnum.BOOL);
            } else {
                tipo = new Tipo(TipoEnum.BOOL);
            }
        }
        //builderTabla.visitDeclaracionVariable(ctx);
        builderTabla.symVariable(id, tipo, ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine());
        return new Declaracion(id, tipo, expresion);
    }

    @Override
    public DeclaracionArray visitDeclaracionArray(CodexLatinusParser.DeclaracionArrayContext ctx) {
        String id = ctx.ID().getText();
        Expresion size = (Expresion) visit(ctx.expresion());
        Tipo tipo = (Tipo) visit(ctx.tipo());
        List<Expresion> valores = null;

        if (ctx.listaExpresiones() != null) {
            valores = new ArrayList<>();

            for (CodexLatinusParser.ExpresionContext expresion : ctx.listaExpresiones().expresion()) {
                valores.add((Expresion) visit(expresion));
            }
        }

        int tempSize = 0;

        if (ctx.expresion().getText().matches("[0-9]+")) {
            tempSize = Integer.parseInt(ctx.expresion().getText());
        }

        builderTabla.symArray(id, tipo, tempSize, ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine());
        return new DeclaracionArray(id, size, tipo, valores);
    }
    // ----------------------------------------------------------------------

    // --------- 3. Funciones ---------
    @Override
    public Funcion visitFuncion(CodexLatinusParser.FuncionContext ctx) {
        return (Funcion) visitChildren(ctx);
    }

    @Override
    public Funcion visitFuncionSinRetorno(CodexLatinusParser.FuncionSinRetornoContext ctx) {
        String id = ctx.ID().getText();
        List<Parametro> parametros = new ArrayList<>();

        builderTabla.symFuncionSR(id, ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine());
        Scope ambito = builderTabla.entrarNuevoScope("funcion_" + id);

        if (ctx.listaParametros() != null) {
            for (CodexLatinusParser.ParametroContext parametro : ctx.listaParametros().parametro()) {
                Parametro param = (Parametro) visit(parametro);
                parametros.add(param);
                builderTabla.symParametro(param.getId(), param.getTipo(), ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine(), id);
            }
        }

        List<Instruccion> instrucciones = new ArrayList<>();

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        return new Funcion(id, parametros, instrucciones, ambito, false, null);
    }

    @Override
    public Funcion visitFuncionConRetorno(CodexLatinusParser.FuncionConRetornoContext ctx) {
        Tipo tipoRetorno = (Tipo) visit(ctx.tipo());
        String id = ctx.ID().getText();
        List<Parametro> parametros = new ArrayList<>();

        builderTabla.symFuncionCR(id, tipoRetorno, ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine());

        Scope ambito = builderTabla.entrarNuevoScope("funcion_" + id);

        if (ctx.listaParametros() != null) {
            for (CodexLatinusParser.ParametroContext parametro : ctx.listaParametros().parametro()) {
                Parametro param = (Parametro) visit(parametro);
                parametros.add(param);
                builderTabla.symParametro(param.getId(), param.getTipo(), ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine(), id);
            }
        }

        List<Instruccion> instrucciones = new ArrayList<>();
        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        return new Funcion(id, parametros, instrucciones, ambito, true, tipoRetorno);
    }

    @Override
    public Parametro visitParametro(CodexLatinusParser.ParametroContext ctx) {
        String id = ctx.ID().getText();
        Tipo tipo = (Tipo) visit(ctx.tipo());
        return new Parametro(id, tipo);
    }
    // ----------------------------------------------------------------------

    // --------- 4. Instrucciones ---------
    @Override
    public Asignacion visitAsignacion(
            CodexLatinusParser.AsignacionContext ctx) {

        CodexLatinusParser.ObjetivoAsignacionContext objetivoCtx
                = ctx.objetivoAsignacion();

        List<ParteAcceso> partes = new ArrayList<>();

        // Primer ID
        String primerId = objetivoCtx.ID().getText();

        Expresion primerIndice = null;

        if (objetivoCtx.expresion() != null) {
            primerIndice = (Expresion) visit(objetivoCtx.expresion());
        }

        partes.add(new ParteAcceso(primerId, primerIndice));

        // Segmentos .id, .id[indice], etc.
        for (CodexLatinusParser.SegmentoAccesoContext segmentoCtx
                : objetivoCtx.segmentoAcceso()) {

            String id = segmentoCtx.ID().getText();

            Expresion indice = null;

            if (segmentoCtx.expresion() != null) {
                indice = (Expresion) visit(segmentoCtx.expresion());
            }

            partes.add(new ParteAcceso(id, indice));
        }

        AccesoVariable objetivo = new AccesoVariable(partes);

        // Asignación normal
        if (ctx.expresion() != null) {

            Expresion expresion = (Expresion) visit(ctx.expresion());

            return new Asignacion(objetivo, expresion);
        }

        // Asignación de estructura
        List<AsignacionAtributo> atributos = new ArrayList<>();

        for (CodexLatinusParser.AsignacionAtributoContext atributoCtx
                : ctx.asignacionAtributo()) {

            atributos.add((AsignacionAtributo) visit(atributoCtx));
        }

        return new Asignacion(objetivo, atributos);
    }

    @Override
    public AsignacionAtributo visitAsignacionAtributo(CodexLatinusParser.AsignacionAtributoContext ctx) {
        String id = ctx.ID().getText();
        Expresion valor = (Expresion) visit(ctx.valorAtributo());

        return new AsignacionAtributo(id, valor);
    }

    @Override
    public LlamadaFuncionInst visitLlamadaFuncionStmt(CodexLatinusParser.LlamadaFuncionStmtContext ctx) {

        LlamadaFuncion llamada = (LlamadaFuncion) visit(ctx.llamadaFuncion());

        return new LlamadaFuncionInst(llamada);
    }

    @Override
    public Retorno visitInstruccionRetorno(CodexLatinusParser.InstruccionRetornoContext ctx) {
        Expresion expresion = (Expresion) visit(ctx.expresion());
        return new Retorno(expresion);
    }

    @Override
    public Escritura visitInstruccionEscritura(CodexLatinusParser.InstruccionEscrituraContext ctx) {
        List<Expresion> expresiones = new ArrayList<>();
        for (CodexLatinusParser.ExpresionContext expresion : ctx.expresion()) {
            expresiones.add((Expresion) visit(expresion));
        }

        return new Escritura(expresiones);
    }

    @Override
    public Lectura visitInstruccionLectura(CodexLatinusParser.InstruccionLecturaContext ctx) {
        String id = null;

        if (ctx.ID() != null) {
            id = ctx.ID().getText();
        }

        return new Lectura(id);
    }
    // ----------------------------------------------------------------------

    // --------- 5. Condicionales ---------
    @Override
    public Condicional visitCondicional(CodexLatinusParser.CondicionalContext ctx) {

        List<RamaCondicional> ramas = new ArrayList<>();
        ramas.add((RamaCondicional) visit(ctx.ramaSi()));

        for (CodexLatinusParser.RamaAliterContext rama : ctx.ramaAliter()) {
            ramas.add((RamaCondicional) visit(rama));
        }

        if (ctx.ramaElse() != null) {
            ramas.add((RamaCondicional) visit(ctx.ramaElse()));
        }

        return new Condicional(ramas);
    }

    @Override
    public RamaCondicional visitRamaSi(CodexLatinusParser.RamaSiContext ctx) {
        Expresion condicion = (Expresion) visit(ctx.expresion());
        List<Instruccion> instrucciones = new ArrayList<>();

        Scope scope = builderTabla.entrarNuevoScope("cond_si");

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        RamaCondicional rc = new RamaCondicional(condicion, instrucciones);
        rc.setAmbito(scope);

        return rc;
    }

    @Override
    public RamaCondicional visitRamaAliter(CodexLatinusParser.RamaAliterContext ctx) {
        Expresion condicion = (Expresion) visit(ctx.expresion());
        List<Instruccion> instrucciones = new ArrayList<>();

        Scope scope = builderTabla.entrarNuevoScope("cond_aliter");

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        RamaCondicional rc = new RamaCondicional(condicion, instrucciones);
        rc.setAmbito(scope);

        return rc;
    }

    @Override
    public RamaCondicional visitRamaElse(CodexLatinusParser.RamaElseContext ctx) {
        List<Instruccion> instrucciones = new ArrayList<>();

        Scope scope = builderTabla.entrarNuevoScope("cond_aliter");

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        RamaCondicional rc = new RamaCondicional(null, instrucciones);
        rc.setAmbito(scope);

        return rc;
    }
    // ----------------------------------------------------------------------

    // --------- 6. Ciclos ---------
    @Override
    public CicloDum visitCicloDum(CodexLatinusParser.CicloDumContext ctx) {
        Expresion condicion = (Expresion) visit(ctx.expresion());
        List<Instruccion> instrucciones = new ArrayList<>();

        Scope scope = builderTabla.entrarNuevoScope("ciclo_dum");

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        CicloDum cd = new CicloDum(condicion, instrucciones);
        cd.setAmbito(scope);

        return cd;
    }

    @Override
    public CicloFacere visitCicloFacere(CodexLatinusParser.CicloFacereContext ctx) {
        List<Instruccion> instrucciones = new ArrayList<>();

        Scope scope = builderTabla.entrarNuevoScope("ciclo_facere");

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }
        Expresion condicion = (Expresion) visit(ctx.expresion());

        builderTabla.salirScopeActual();

        CicloFacere cf = new CicloFacere(instrucciones, condicion);
        cf.setAmbito(scope);
        return cf;
    }

    @Override
    public CicloPer visitCicloPer(CodexLatinusParser.CicloPerContext ctx) {
        List<Instruccion> instrucciones = new ArrayList<>();

        Scope scope = builderTabla.entrarNuevoScope("ciclo_per");

        Instruccion inicializacion = (Instruccion) visit(ctx.cicloPerInit());
        Expresion condicion = (Expresion) visit(ctx.expresion());
        Instruccion actualizacion = (Instruccion) visit(ctx.cicloPerActualizacion());

        for (CodexLatinusParser.InstruccionContext instruccion : ctx.instruccion()) {
            instrucciones.add((Instruccion) visit(instruccion));
        }

        builderTabla.salirScopeActual();

        CicloPer cp = new CicloPer(inicializacion, condicion, actualizacion, instrucciones);
        cp.setAmbito(scope);

        return cp;
    }

    @Override
    public Instruccion visitCicloPerInit(CodexLatinusParser.CicloPerInitContext ctx) {
        return (Instruccion) visitChildren(ctx);
    }

    @Override
    public Declaracion visitDeclaracionVariableSinPuntoComa(CodexLatinusParser.DeclaracionVariableSinPuntoComaContext ctx) {
        String id = ctx.ID().getText();
        Tipo tipo;
        Expresion expresion = null;

        if (ctx.tipo() != null) {
            tipo = (Tipo) visit(ctx.tipo());
            if (ctx.expresion() != null) {
                expresion = (Expresion) visit(ctx.expresion());
            }
        } else {
            tipo = new Tipo(TipoEnum.BOOL);
        }

        builderTabla.symVariable(id, tipo, ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine());

        return new Declaracion(id, tipo, expresion);
    }

    @Override
    public Asignacion visitAsignacionSinPuntoComa(CodexLatinusParser.AsignacionSinPuntoComaContext ctx) {

        if (ctx == null) {
            return null;
        }

        CodexLatinusParser.ObjetivoAsignacionContext objetivoCtx
                = ctx.objetivoAsignacion();

        List<ParteAcceso> partes = new ArrayList<>();

        // Primer ID
        String primerId = objetivoCtx.ID().getText();

        Expresion primerIndice = null;

        if (objetivoCtx.expresion() != null) {
            primerIndice = (Expresion) visit(objetivoCtx.expresion());
        }

        partes.add(new ParteAcceso(primerId, primerIndice));

        // Segmentos .id, .id[indice], etc.
        for (CodexLatinusParser.SegmentoAccesoContext segmentoCtx
                : objetivoCtx.segmentoAcceso()) {

            String id = segmentoCtx.ID().getText();

            Expresion indice = null;

            if (segmentoCtx.expresion() != null) {
                indice = (Expresion) visit(segmentoCtx.expresion());
            }

            partes.add(new ParteAcceso(id, indice));
        }

        AccesoVariable objetivo = new AccesoVariable(partes);

        Expresion expresion = (Expresion) visit(ctx.expresion());

        return new Asignacion(objetivo, expresion);
    }

    @Override
    public Instruccion visitCicloPerActualizacion(
            CodexLatinusParser.CicloPerActualizacionContext ctx) {

        if (ctx.asignacionSinPuntoComa() != null) {
            return (Instruccion) visit(ctx.asignacionSinPuntoComa());
        }

        AccesoVariable variable = new AccesoVariable(
                List.of(
                        new ParteAcceso(
                                ctx.ID().getText(),
                                null
                        )
                )
        );

        String operador;

        if (ctx.INCREMENTO() != null) {
            operador = "++";
        } else {
            operador = "--";
        }

        return new Actualizacion(
                new Operacion(variable, null, operador)
        );
    }

    @Override
    public Actualizacion visitActualizacion(CodexLatinusParser.ActualizacionContext ctx) {
        String id = ctx.ID().getText();
        String operador;
        if (ctx.INCREMENTO() != null) {
            operador = "++";
        } else {
            operador = "--";
        }
        return new Actualizacion(id, operador);
    }

    @Override
    public Structura visitDeclaracionEstructuraDef(CodexLatinusParser.DeclaracionEstructuraDefContext ctx) {
        String nombre = ctx.ID().getText();
        List<Atributo> atributos = new ArrayList<>();

        for (CodexLatinusParser.AtributoEstructuraContext atributoCtx : ctx.atributoEstructura()) {
            String id = atributoCtx.ID().getText();
            Tipo tipo = (Tipo) visit(atributoCtx.tipo());

            Atributo atributo = new Atributo(id);
            atributo.setTipo(tipo);
            atributo.setEsArray(atributoCtx.SERIES() != null);
            atributos.add(atributo);
        }

        builderTabla.symStructura(nombre, atributos, ctx.ID().getSymbol().getLine(), ctx.ID().getSymbol().getCharPositionInLine());
        return new Structura(nombre, atributos);
    }

    @Override
    public NodoAst visitDeclaracionEstructuraUso(CodexLatinusParser.DeclaracionEstructuraUsoContext ctx) {
        String id = ctx.ID(0).getText();
        String nombreEstructura = ctx.ID(1).getText();
        List<AsignacionAtributo> atributos = new ArrayList<>();

        for (CodexLatinusParser.AsignacionAtributoContext atributoCtx : ctx.asignacionAtributo()) {
            atributos.add((AsignacionAtributo) visit(atributoCtx));
        }

        builderTabla.symStructuraUso(id, nombreEstructura, ctx.ID(0).getSymbol().getLine(), ctx.ID(0).getSymbol().getCharPositionInLine());
        return new DeclaracionStructura(id, nombreEstructura, atributos);
    }

    // ----------------------------------------------------------------------
    // --------- 7. Expresiones ---------
    @Override
    public Expresion visitExpresion(CodexLatinusParser.ExpresionContext ctx) {
        return (Expresion) visit(ctx.expresionOr());
    }

    @Override
    public Expresion visitExpresionOr(CodexLatinusParser.ExpresionOrContext ctx) {
        Expresion izquierda = (Expresion) visit(ctx.expresionAnd(0));

        for (int i = 1; i < ctx.expresionAnd().size(); i++) {
            Expresion derecha = (Expresion) visit(ctx.expresionAnd(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            izquierda = new Operacion(izquierda, derecha, operador);
        }

        return izquierda;
    }

    @Override
    public Expresion visitExpresionAnd(CodexLatinusParser.ExpresionAndContext ctx) {
        Expresion izquierda = (Expresion) visit(ctx.expresionIgualdad(0));

        for (int i = 1; i < ctx.expresionIgualdad().size(); i++) {
            Expresion derecha = (Expresion) visit(ctx.expresionIgualdad(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            izquierda = new Operacion(izquierda, derecha, operador);
        }

        return izquierda;
    }

    @Override
    public Expresion visitExpresionIgualdad(CodexLatinusParser.ExpresionIgualdadContext ctx) {
        Expresion izquierda = (Expresion) visit(ctx.expresionRelacional(0));

        for (int i = 1; i < ctx.expresionRelacional().size(); i++) {
            Expresion derecha = (Expresion) visit(ctx.expresionRelacional(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            izquierda = new Operacion(izquierda, derecha, operador);
        }

        return izquierda;
    }

    @Override
    public Expresion visitExpresionRelacional(CodexLatinusParser.ExpresionRelacionalContext ctx) {
        Expresion izquierda = (Expresion) visit(ctx.expresionAditiva(0));

        for (int i = 1; i < ctx.expresionAditiva().size(); i++) {
            Expresion derecha = (Expresion) visit(ctx.expresionAditiva(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            izquierda = new Operacion(izquierda, derecha, operador);
        }

        return izquierda;
    }

    @Override
    public Expresion visitExpresionAditiva(CodexLatinusParser.ExpresionAditivaContext ctx) {
        Expresion izquierda = (Expresion) visit(ctx.expresionMultiplicativa(0));

        for (int i = 1; i < ctx.expresionMultiplicativa().size(); i++) {
            Expresion derecha = (Expresion) visit(ctx.expresionMultiplicativa(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            izquierda = new Operacion(izquierda, derecha, operador);
        }

        return izquierda;
    }

    @Override
    public Expresion visitExpresionMultiplicativa(CodexLatinusParser.ExpresionMultiplicativaContext ctx) {
        Expresion izquierda = (Expresion) visit(ctx.expresionUnaria(0));

        for (int i = 1; i < ctx.expresionUnaria().size(); i++) {
            Expresion derecha = (Expresion) visit(ctx.expresionUnaria(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            izquierda = new Operacion(izquierda, derecha, operador);
        }

        return izquierda;
    }

    @Override
    public Expresion visitExpresionUnaria(CodexLatinusParser.ExpresionUnariaContext ctx) {
        if (ctx.NON() != null) {
            Expresion expresion = (Expresion) visit(ctx.expresionUnaria());
            return new Operacion(null, expresion, "non");
        }

        if (ctx.MENOS() != null) {
            Expresion expresion = (Expresion) visit(ctx.expresionUnaria());
            return new Operacion(null, expresion, "-");
        }

        return (Expresion) visit(ctx.expresionPostfija());
    }

    @Override
    public Expresion visitExpresionPostfija(CodexLatinusParser.ExpresionPostfijaContext ctx) {
        Expresion expresion = (Expresion) visit(ctx.expresionPrimaria());

        if (ctx.INCREMENTO() != null) {
            return new Operacion(expresion, null, "++");
        }

        if (ctx.DECREMENTO() != null) {
            return new Operacion(expresion, null, "--");
        }

        return expresion;
    }

    @Override
    public Expresion visitExpresionPrimaria(CodexLatinusParser.ExpresionPrimariaContext ctx) {
        return (Expresion) visitChildren(ctx);
    }

    @Override
    public NodoAst visitExp_nativa(CodexLatinusParser.Exp_nativaContext ctx) {
        String texto = ctx.getText();

        if (ctx.NUMERO() != null) {
            return new Literal(new Tipo(TipoEnum.NUMERUS), Integer.valueOf(texto));
        }

        if (ctx.DECIMAL() != null) {
            return new Literal(new Tipo(TipoEnum.DECIMALIS), Double.valueOf(texto));
        }

        if (ctx.CADENA() != null) {
            return new Literal(new Tipo(TipoEnum.TEXTUM), texto.substring(1, texto.length() - 1));
        }

        if (ctx.CARACTER() != null) {
            return new Literal(new Tipo(TipoEnum.LITTERA), texto.charAt(1));
        }

        if (ctx.VERUM() != null) {
            return new Literal(new Tipo(TipoEnum.BOOL), true);
        }

        if (ctx.FALSUS() != null) {
            return new Literal(new Tipo(TipoEnum.BOOL), false);
        }

        return null;
    }

    @Override
    public AccesoVariable visitAccesoVariable(
            CodexLatinusParser.AccesoVariableContext ctx) {

        List<ParteAcceso> partes = new ArrayList<>();

        String primerId = ctx.ID().getText();

        Expresion primerIndice = null;

        if (ctx.expresion() != null) {
            primerIndice = (Expresion) visit(ctx.expresion());
        }

        partes.add(new ParteAcceso(primerId, primerIndice));

        for (CodexLatinusParser.SegmentoAccesoContext puntoCtx : ctx.segmentoAcceso()) {

            String id = puntoCtx.ID().getText();

            Expresion indice = null;

            if (puntoCtx.expresion() != null) {
                indice = (Expresion) visit(puntoCtx.expresion());
            }

            partes.add(new ParteAcceso(id, indice));
        }

        return new AccesoVariable(partes);

    }

    @Override
    public LlamadaFuncion visitLlamadaFuncion(
            CodexLatinusParser.LlamadaFuncionContext ctx
    ) {

        String id = ctx.ID().getText();

        List<Expresion> argumentos = null;

        if (ctx.listaExpresiones() != null) {
            argumentos = new ArrayList<>();

            for (CodexLatinusParser.ExpresionContext expresion
                    : ctx.listaExpresiones().expresion()) {

                argumentos.add((Expresion) visit(expresion));
            }
        }

        return new LlamadaFuncion(id, argumentos);
    }

    @Override
    public Tipo visitTipo(CodexLatinusParser.TipoContext ctx
    ) {
        return switch (ctx.getText()) {
            case "numerus" -> {
                yield new Tipo(TipoEnum.NUMERUS);
            }
            case "decimalis" -> {
                yield new Tipo(TipoEnum.DECIMALIS);
            }
            case "textum" -> {
                yield new Tipo(TipoEnum.TEXTUM);
            }
            case "littera" -> {
                yield new Tipo(TipoEnum.LITTERA);
            }
            case "bool" -> {
                yield new Tipo(TipoEnum.BOOL);
            }
            default -> {
                Tipo t = new Tipo();
                t.setNombreStructura(ctx.ID().getText());
                yield t;
            }
        };
    }
}
