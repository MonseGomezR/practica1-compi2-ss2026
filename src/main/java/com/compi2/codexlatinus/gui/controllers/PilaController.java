package com.compi2.codexlatinus.gui.controllers;

import com.compi2.codexlatinus.pila.EstadoPila;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PilaController {

    @FXML private VBox contenedorPila;
    @FXML private ListView<String> listaLog;
    @FXML private Label lblEstado;

    private List<EstadoPila> estados = new ArrayList<>();
    private int indiceActual = -1;

    public void setEstados(List<EstadoPila> estados) {
        this.estados = estados;
        this.indiceActual = estados.isEmpty() ? -1 : 0;
        actualizarVista();
    }

    @FXML
    private void siguiente() {
        if (indiceActual < estados.size() - 1) {
            indiceActual++;
            actualizarVista();
        }
    }

    @FXML
    private void anterior() {
        if (indiceActual > 0) {
            indiceActual--;
            actualizarVista();
        }
    }

    private void actualizarVista() {
        contenedorPila.getChildren().clear();
        listaLog.getItems().clear();

        if (indiceActual < 0) {
            lblEstado.setText("0 / 0");
            return;
        }

        EstadoPila estado = estados.get(indiceActual);
        for (String elemento : estado.getPila()) {
            Label caja = new Label(elemento);
            caja.setMinWidth(80);
            caja.setStyle(
                "-fx-background-color: " + colorAleatorioPastel() + ";" +
                "-fx-border-color: #333; -fx-border-radius: 6; -fx-background-radius: 6;" +
                "-fx-alignment: center; -fx-padding: 8; -fx-font-weight: bold;"
            );
            contenedorPila.getChildren().add(0, caja); // tope arriba visualmente
        }

        for (int i = 0; i <= indiceActual; i++) {
            listaLog.getItems().add(estados.get(i).getOperacion());
        }
        listaLog.scrollTo(listaLog.getItems().size() - 1);

        lblEstado.setText((indiceActual + 1) + " / " + estados.size());
    }

    private String colorAleatorioPastel() {
        String[] colores = {"#dceeff", "#ffe0d6", "#e6ffe0", "#fff2cc"};
        return colores[indiceActual % colores.length];
    }
}