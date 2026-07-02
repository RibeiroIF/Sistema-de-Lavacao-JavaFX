    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLVBoxMainAppController implements Initializable {

    @FXML
    private MenuItem menuItemCadastroCliente;

    @FXML
    private MenuItem menuItemCadastroServico;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MenuBar menuBarPrincipal;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBarPrincipal.setMaxWidth(Double.MAX_VALUE);
        try {
            handleRetornarMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRetornarMain() throws IOException {
        AnchorPane home = FXMLLoader.load(getClass().getResource("/view/FXMLAPHome.fxml"));
        anchorPane.getChildren().setAll(home);
    }

    @FXML
    public void handleMenuItemCadastroCor() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPCadastroCor.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroMarca() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPCadastroMarca.fxml"));
        anchorPane.getChildren().setAll(a);
    }


    @FXML
    public void handleMenuItemCadastroModelo() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPCadastroModelo.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroVeiculo() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPCadastroVeiculo.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroCliente() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPCadastroCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroServico() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPCadastroServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemProcessoOrdemServico() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPProcessoOrdemServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemGraficosOrdensMensais() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPGraficosOrdensMensais.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemGraficosLucroMensal() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPGraficosLucroMensal.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemRelatorioServico() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPRelatorioServicos.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemControleDescontos() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPControleDescontos.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemControlePontos() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/view/FXMLAPControlePontos.fxml"));
        anchorPane.getChildren().setAll(a);
    }

}
