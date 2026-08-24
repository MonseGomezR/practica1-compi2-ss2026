package com.compi2.codexlatinus.gui;

import javafx.scene.Scene;

public class EstilosController {
    private static final String TEMA = "/styles/theme.css";

    public static void aplicar(Scene scene) {
        scene.getStylesheets().add(
                EstilosController.class.getResource(TEMA).toExternalForm());
    }
}
