package com.compi2.codexlatinus.gui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Crea ventanas secundarias (AST, tabla de símbolos, pila, PigLatin, etc.)
 * sin la decoración nativa del sistema operativo, reemplazándola por una
 * barra de título temática consistente con el resto de la aplicación.
 */
public class VentanaUtil {

    public static class VentanaCreada {
        public final Stage stage;
        public final FXMLLoader loader;

        public VentanaCreada(Stage stage, FXMLLoader loader) {
            this.stage = stage;
            this.loader = loader;
        }

        @SuppressWarnings("unchecked")
        public <T> T getController() {
            return (T) loader.getController();
        }
    }

    public static VentanaCreada crear(String rutaFxml, String titulo, double ancho, double alto) throws IOException {
        FXMLLoader loader = new FXMLLoader(VentanaUtil.class.getResource(rutaFxml));
        Parent contenido = loader.load();

        VBox raiz = new VBox();
        raiz.getStyleClass().add("ventana-secundaria");

        HBox barraTitulo = construirBarraTitulo(titulo, raiz);

        VBox.setVgrow(contenido, Priority.ALWAYS);
        raiz.getChildren().addAll(barraTitulo, contenido);

        Scene scene = new Scene(raiz, ancho, alto);
        EstilosController.aplicar(scene);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        return new VentanaCreada(stage, loader);
    }

    private static HBox construirBarraTitulo(String titulo, VBox raizVentana) {
        HBox barra = new HBox();
        barra.getStyleClass().add("barra-titulo-custom");
        barra.setSpacing(10);
        barra.setPadding(new Insets(6, 10, 6, 14));
        barra.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("titulo-app");

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Button btnCerrar = new Button("✕");
        btnCerrar.getStyleClass().addAll("boton-ventana", "boton-cerrar");

        barra.getChildren().addAll(lblTitulo, espaciador, btnCerrar);

        // Arrastre de ventana
        double[] offset = new double[2];
        barra.setOnMousePressed(e -> {
            offset[0] = e.getSceneX();
            offset[1] = e.getSceneY();
        });
        barra.setOnMouseDragged(e -> {
            Stage stage = (Stage) raizVentana.getScene().getWindow();
            stage.setX(e.getScreenX() - offset[0]);
            stage.setY(e.getScreenY() - offset[1]);
        });

        // Cerrar - se conecta al stage real una vez la escena esté asignada
        btnCerrar.setOnAction(e -> {
            Stage stage = (Stage) raizVentana.getScene().getWindow();
            stage.close();
        });

        return barra;
    }
}