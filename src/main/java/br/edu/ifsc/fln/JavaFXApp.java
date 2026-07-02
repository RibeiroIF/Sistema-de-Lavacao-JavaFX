package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.service.ParametrosService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Github: https://github.com/RibeiroIF/Sistema-de-Lavacao-JavaFX


        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/FXMLVBoxMainApp.fxml"));
        } catch (IOException ex) {
            System.out.println("Não foi possível carregar o formulário");
        }

        Scene scene = new Scene(root, 642, 532);
        primaryStage.getIcons().add(
                new Image(Main.class.getResource("/icon/IFSC_logo_vertical.png").toExternalForm())
        );
        ParametrosService.carregar();
        primaryStage.setTitle("Sistema de Lavação ");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
