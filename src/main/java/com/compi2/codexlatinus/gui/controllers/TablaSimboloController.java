package com.compi2.codexlatinus.gui.controllers;

import com.compi2.codexlatinus.semantico.Scope;
import com.compi2.codexlatinus.semantico.Simbolo;
import com.compi2.codexlatinus.semantico.TablaSimbolos;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Usuario
 */
public class TablaSimboloController {

    @FXML
    private TableView<FilaSimbolo> tabla;
    @FXML
    private TableColumn<FilaSimbolo, String> colNombre;
    @FXML
    private TableColumn<FilaSimbolo, String> colTipo;
    @FXML
    private TableColumn<FilaSimbolo, String> colCategoria;
    @FXML
    private TableColumn<FilaSimbolo, String> colAmbito;
    @FXML
    private TableColumn<FilaSimbolo, String> colLinea;
    @FXML
    private TableColumn<FilaSimbolo, String> colColumna;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre()));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipo()));
        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().categoria()));
        colAmbito.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().ambito()));
        colLinea.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().linea()));
        colColumna.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().columna()));
    }

    public void setTablaSimbolos(TablaSimbolos tablaSimbolos) {
        if (tablaSimbolos == null || tablaSimbolos.getGlobal() == null) {
            return;
        }

        List<FilaSimbolo> filas = new ArrayList<>();
        aplanar(tablaSimbolos.getGlobal(), "global", filas);

        ObservableList<FilaSimbolo> items = FXCollections.observableArrayList(filas);
        tabla.setItems(items);
    }

    private void aplanar(Scope scope, String rutaAmbito, List<FilaSimbolo> filas) {
        for (Simbolo simbolo : scope.getSimbolos()) {
            filas.add(new FilaSimbolo(
                    simbolo.getNombre(),
                    String.valueOf(simbolo.getTipo()),
                    String.valueOf(simbolo.getTipoSimbolo()),
                    rutaAmbito,
                    String.valueOf(simbolo.getLinea()),
                    String.valueOf(simbolo.getColumna())
            ));
        }

        for (Scope hijo : scope.getHijos()) {
            aplanar(hijo, rutaAmbito + " > " + hijo.getNombre(), filas);
        }
    }

    public record FilaSimbolo(String nombre, String tipo, String categoria,
            String ambito, String linea, String columna) {
    }
}
