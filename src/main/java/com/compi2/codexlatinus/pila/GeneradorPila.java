package com.compi2.codexlatinus.pila;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Escucha los eventos del parser mientras reconoce el código fuente
 * y construye una lista de "fotos" de la pila (EstadoPila), simulando
 * las operaciones shift/reduce/accept de un parser LR.
 */
public class GeneradorPila implements ParseTreeListener {

    private final Parser parser;
    private final List<String> pilaActual = new ArrayList<>();
    private final List<EstadoPila> estados = new ArrayList<>();

    public GeneradorPila(Parser parser) {
        this.parser = parser;
    }

    public List<EstadoPila> getEstados() {
        return estados;
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        String texto = node.getText();
        pilaActual.add(texto);
        registrarEstado("shift '" + texto + "'");
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        registrarEstado("error en '" + node.getText() + "'");
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        // No se apila nada al entrar; los terminales se apilan en visitTerminal
        // y la reducción ocurre en exitEveryRule.
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        String nombreRegla = parser.getRuleNames()[ctx.getRuleIndex()];
        int hijos = ctx.getChildCount();

        // Reduce: saca 'hijos' símbolos de la pila y mete el nombre de la regla
        for (int i = 0; i < hijos && !pilaActual.isEmpty(); i++) {
            pilaActual.remove(pilaActual.size() - 1);
        }
        pilaActual.add(nombreRegla.toUpperCase());

        registrarEstado("reduce " + nombreRegla + " -> " + hijos + " simbolo(s)");

        // Si es la regla raíz (no tiene padre) y quedó un solo símbolo, es accept
        if (ctx.getParent() == null && pilaActual.size() == 1) {
            registrarEstado("accept");
        }
    }

    private void registrarEstado(String operacion) {
        estados.add(new EstadoPila(new ArrayList<>(pilaActual), operacion));
    }
}