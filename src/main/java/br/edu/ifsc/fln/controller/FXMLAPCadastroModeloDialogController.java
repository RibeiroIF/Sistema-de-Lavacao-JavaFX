/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPCadastroModeloDialogController implements Initializable {
    
    @FXML
    private TextField tfModeloDescricao;

    @FXML
    private TextField tfMotorPotencia;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private ChoiceBox<ETipoCombustivel> cbCombustivel;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;

    private final MarcaDAO marcaDAO = new MarcaDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarComboBoxMarcas();
        carregarChoiceBoxCategorias();
        carregarChoiceBoxCombustiveis();

        setFocusLostHandle();
    }

    private void setFocusLostHandle() {
        tfModeloDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) {
                if (tfModeloDescricao.getText() == null || tfModeloDescricao.getText().isEmpty()) {
                    tfModeloDescricao.requestFocus();
                }
            }
        });
    }

    public void carregarComboBoxMarcas() {
        try {
            listaMarcas = marcaDAO.listar();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        observableListMarcas =
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }

    public void carregarChoiceBoxCategorias() {
        cbCategoria.getItems().setAll(ECategoria.values());
    }

    public void carregarChoiceBoxCombustiveis() {
        cbCombustivel.getItems().setAll(ETipoCombustivel.values());
    }

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        tfModeloDescricao.setText(modelo.getDescricao());
        cbMarca.getSelectionModel().select(modelo.getMarca());
        cbCategoria.getSelectionModel().select(modelo.getCategoria());
        tfMotorPotencia.setText(String.valueOf(modelo.getMotor().getPotencia()));
        cbCombustivel.getSelectionModel().select(modelo.getMotor().getTipoCombustivel());
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfModeloDescricao.getText());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(Integer.parseInt(tfMotorPotencia.getText()));
            modelo.getMotor().setTipoCombustivel(cbCombustivel.getSelectionModel().getSelectedItem());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (tfModeloDescricao.getText() == null || tfModeloDescricao.getText().isEmpty()) {
            errorMessage += "Descricao inválido!\n";
        }

        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca!\n";
        }

        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
        }

        if (tfMotorPotencia.getText() == null || tfMotorPotencia.getText().isEmpty()) {
            errorMessage += "Potência inválida!\n";
        }

        if (cbCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um tipo de combustível!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
