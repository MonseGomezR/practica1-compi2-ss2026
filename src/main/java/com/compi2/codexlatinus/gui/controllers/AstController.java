package com.compi2.codexlatinus.gui.controllers;

import com.compi2.codexlatinus.ArbolBuilder;
import com.compi2.codexlatinus.NodoGrafico;
import com.compi2.codexlatinus.ast.Ast;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class AstController {

    @FXML private Pane lienzo;

    private static final double ANCHO_NODO = 150;
    private static final double ALTO_LINEA = 14;
    private static final double ALTO_BASE = 30;
    private static final double ESPACIO_H = 20;
    private static final double ESPACIO_V = 90;

    public void setRaiz(Ast ast) {
        lienzo.getChildren().clear();
        if (ast == null) return;
        NodoGrafico grafico = ArbolBuilder.construir(ast);
        dibujar(grafico, 20, 20);
    }

    private double dibujar(NodoGrafico nodo, double x, double y) {
        double altoNodo = calcularAlto(nodo.etiqueta);

        if (nodo.hijos.isEmpty()) {
            dibujarCaja(nodo.etiqueta, x, y, altoNodo);
            return ANCHO_NODO;
        }

        double xHijo = x;
        double anchoTotal = 0;
        List<Double> centrosHijos = new ArrayList<>();

        for (NodoGrafico hijo : nodo.hijos) {
            double anchoSubarbol = dibujar(hijo, xHijo, y + ESPACIO_V);
            centrosHijos.add(xHijo + anchoSubarbol / 2);
            xHijo += anchoSubarbol + ESPACIO_H;
            anchoTotal += anchoSubarbol + ESPACIO_H;
        }
        anchoTotal -= ESPACIO_H;

        double centroPropio = x + anchoTotal / 2 - ANCHO_NODO / 2;
        dibujarCaja(nodo.etiqueta, centroPropio, y, altoNodo);

        for (double centroHijo : centrosHijos) {
            Line linea = new Line(
                    centroPropio + ANCHO_NODO / 2, y + altoNodo,
                    centroHijo + ANCHO_NODO / 2, y + ESPACIO_V
            );
            linea.setStroke(Color.GRAY);
            lienzo.getChildren().add(0, linea);
        }

        return anchoTotal;
    }

    private double calcularAlto(String etiqueta) {
        int lineas = etiqueta.split("\n").length;
        return ALTO_BASE + (lineas - 1) * ALTO_LINEA;
    }

    private void dibujarCaja(String etiqueta, double x, double y, double alto) {
        Rectangle caja = new Rectangle(x, y, ANCHO_NODO, alto);
        caja.setArcWidth(10);
        caja.setArcHeight(10);
        caja.setFill(Color.web("#dceeff"));
        caja.setStroke(Color.web("#3070b0"));

        Label etiquetaLbl = new Label(etiqueta);
        etiquetaLbl.setLayoutX(x + 4);
        etiquetaLbl.setLayoutY(y + 4);
        etiquetaLbl.setMaxWidth(ANCHO_NODO - 8);
        etiquetaLbl.setWrapText(true);
        etiquetaLbl.setStyle("-fx-font-size: 10px;");

        lienzo.getChildren().addAll(caja, etiquetaLbl);
    }
}
