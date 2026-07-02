/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.domain.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;
    
    @FXML
    private TextField tfVeiculoPlaca;

    @FXML
    private TextField tfVeiculoObservacoes;

    @FXML
    private ComboBox<Modelo> cbModelo;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private ComboBox<Cor> cbCor;

    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Veiculo veiculo;

    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final CorDAO corDAO = new CorDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarComboBoxClientes();
        try {
            carregarComboBoxModelos();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        carregarComboBoxCores();
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void carregarComboBoxModelos() throws DAOException {
        listaModelos = modeloDAO.listar();
        observableListModelos =
                FXCollections.observableArrayList(listaModelos);
        cbModelo.setItems(observableListModelos);
    }

    public void carregarComboBoxCores() {
        try {
            listaCores = corDAO.listar();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        observableListCores =
                FXCollections.observableArrayList(listaCores);
        cbCor.setItems(observableListCores);
    }

    public void carregarComboBoxClientes() {
        listaClientes = clienteDAO.listar();
        observableListClientes =
                FXCollections.observableArrayList(listaClientes);
        cbCliente.setItems(observableListClientes);
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        if (veiculo.getId() != 0) {
            tfVeiculoPlaca.setText(veiculo.getPlaca());
            tfVeiculoObservacoes.setText(veiculo.getObservacoes());
            cbModelo.getSelectionModel().select(veiculo.getModelo());
            cbCor.getSelectionModel().select(veiculo.getCor());
            cbCliente.getSelectionModel().select(veiculo.getCliente());
        }
        this.tfVeiculoPlaca.requestFocus();
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfVeiculoPlaca.getText());
            veiculo.setObservacoes(tfVeiculoObservacoes.getText());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            veiculo.setCliente(cbCliente.getSelectionModel().getSelectedItem());
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (this.tfVeiculoPlaca.getText() == null || this.tfVeiculoPlaca.getText().trim().length() == 0) {
            errorMessage += "Placa inválida.\n";
        } else {
            String placaDigitada = this.tfVeiculoPlaca.getText().trim();
            Veiculo veiculoExistente = veiculoDAO.buscarPorPlaca(placaDigitada);

            if (veiculoExistente != null) {
                if (this.veiculo == null || this.veiculo.getId() != veiculoExistente.getId()) {
                    errorMessage += "A placa '" + placaDigitada + "' já está cadastrada para o cliente: "
                            + veiculoExistente.getCliente().getNome() + ".\n";
                }
            }
        }

        if (this.tfVeiculoObservacoes.getText() == null || this.tfVeiculoObservacoes.getText().trim().length() == 0) {
            errorMessage += "Observação inválida.\n";
        }

        if (this.cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Modelo inválido.\n";
        }

        if (this.cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cor inválida.\n";
        }

        if (this.cbCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}