package com.compi2.codexlatinus.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PigLatinController {

    @FXML private TextArea areaPigLatin;

    public void setTexto(String texto) {
        areaPigLatin.setText(texto);
    }

    @FXML
    private void guardarComoPig() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PigLatin", "*.pig"));
        File f = fc.showSaveDialog(areaPigLatin.getScene().getWindow());
        if (f != null) {
            try {
                Files.writeString(f.toPath(), areaPigLatin.getText());
            } catch (IOException e) {
                // podrías mostrar una alerta aquí
                e.printStackTrace();
            }
        }
    }
}