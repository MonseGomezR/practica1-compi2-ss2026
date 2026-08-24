package com.compi2.codexlatinus.gui.controllers;

import com.compi2.codexlatinus.Compilador;
import com.compi2.codexlatinus.ast.Ast;
import com.compi2.codexlatinus.gui.VentanaUtil;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.compi2.codexlatinus.pila.EstadoPila;
import com.compi2.codexlatinus.semantico.TablaSimbolos;
import java.util.List;

public class MainController {

    @FXML
    private StackPane contenedorEditor;
    @FXML
    private ListView<String> listaErrores;
    @FXML
    private Label lblEstado;
    @FXML
    private HBox barraTitulo;

    private CodeArea codeArea;
    private File archivoActual;
    private Ast ultimoAst;
    private TablaSimbolos ultimaTablaSimbolos;

    private List<EstadoPila> ultimaPila;
    private String ultimoCodigoPigLatin;

    private double offsetX, offsetY;

    @FXML
    public void initialize() {
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.getStylesheets().add(
                getClass().getResource(Rutas.CSS_EDITOR).toExternalForm());

        codeArea.multiPlainChanges()
                .successionEnds(java.time.Duration.ofMillis(100))
                .subscribe(ignore -> {
                    codeArea.setStyleSpans(0, Resaltador.calcularEstilos(codeArea.getText()));
                });

        contenedorEditor.getChildren().add(new VirtualizedScrollPane<>(codeArea));
        barraTitulo.setOnMousePressed(e -> {
            offsetX = e.getSceneX();
            offsetY = e.getSceneY();
        });
        barraTitulo.setOnMouseDragged(e -> {
            Stage stage = (Stage) barraTitulo.getScene().getWindow();
            stage.setX(e.getScreenX() - offsetX);
            stage.setY(e.getScreenY() - offsetY);
        });
    }

    @FXML
    private void minimizarVentana() {
        Stage stage = (Stage) barraTitulo.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) barraTitulo.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void nuevoArchivo() {
        codeArea.clear();
        archivoActual = null;
    }

    @FXML
    private void abrirArchivo() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Latín", "*.lat"));
        File f = fc.showOpenDialog(contenedorEditor.getScene().getWindow());
        if (f != null) {
            try {
                String contenido = Files.readString(f.toPath());
                codeArea.replaceText(contenido);
                archivoActual = f;
            } catch (IOException e) {
                mostrarError("No se pudo abrir el archivo: " + e.getMessage());
            }
        }
    }

    @FXML
    private void guardarArchivo() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Latín", "*.lat"));
        if (archivoActual != null) {
            fc.setInitialFileName(archivoActual.getName());
        }
        File f = fc.showSaveDialog(contenedorEditor.getScene().getWindow());
        if (f != null) {
            try {
                Files.writeString(f.toPath(), codeArea.getText());
                archivoActual = f;
            } catch (IOException e) {
                mostrarError("No se pudo guardar: " + e.getMessage());
            }
        }
    }

    @FXML
    private void salir() {
        ((Stage) contenedorEditor.getScene().getWindow()).close();
    }

    @FXML
    private void analizarCodigo() {
        listaErrores.getItems().clear();
        String fuente = codeArea.getText();

        Compilador.ResultadoAnalisis resultado = Compilador.analizar(fuente);

        this.ultimoAst = resultado.ast;
        this.ultimaTablaSimbolos = resultado.tabla;
        this.ultimaPila = resultado.estadosPila;
        this.ultimoCodigoPigLatin = resultado.codigoPigLatin; //

        if (!resultado.errores.isEmpty()) {
            listaErrores.getItems().addAll(resultado.errores);
            lblEstado.setText("Análisis con errores (" + resultado.errores.size() + ")");
        } else {
            lblEstado.setText("Análisis válido ✓");
        }
    }

    @FXML
    private void verAst() {
        try {
            var ventana = VentanaUtil.crear(Rutas.FXML_AST, "Árbol de Sintaxis Abstracta (AST)",
                    1000, 700);
            AstController ctrl = ventana.getController();
            ctrl.setRaiz(ultimoAst);
            ventana.stage.show();
        } catch (IOException e) {
            mostrarError("No se pudo abrir la vista AST: " + e.getMessage());
        }
    }

    @FXML
    private void verTablaSimbolos() {
        try {
            var ventana = VentanaUtil.crear(Rutas.FXML_TABLA, "Tabla de Símbolos", 800,
                    600);
            TablaSimboloController ctrl = ventana.getController();
            ctrl.setTablaSimbolos(ultimaTablaSimbolos);
            ventana.stage.show();
        } catch (IOException e) {
            mostrarError("No se pudo abrir la tabla de símbolos: " + e.getMessage());
        }
    }

    @FXML
    private void verPila() {
        try {
            var ventana = VentanaUtil.crear(Rutas.FXML_PILA, "Pila de Análisis Sintáctico", 900,
                    600);
            PilaController ctrl = ventana.getController();
            if (ultimaPila != null) {
                ctrl.setEstados(ultimaPila);
            }
            ventana.stage.show();
        } catch (IOException e) {
            mostrarError("No se pudo abrir la pila: " + e.getMessage());
        }
    }

    @FXML
    private void verPigLatin() {
        try {
            var ventana = VentanaUtil.crear(Rutas.FXML_PIGLATIN, "Traducción a PigLatin", 700, 600);
            PigLatinController ctrl = ventana.getController();
            ctrl.setTexto(ultimoCodigoPigLatin != null ? ultimoCodigoPigLatin : "");
            ventana.stage.show();
        } catch (IOException e) {
            mostrarError("No se pudo abrir la vista PigLatin: " + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        listaErrores.getItems().add(msg);
    }
}
