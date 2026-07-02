/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPCadastroVeiculoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbVeiculoId;

    @FXML
    private Label lbVeiculoPlaca;

    @FXML
    private Label lbVeiculoCor;

    @FXML
    private Label lbVeiculoCliente;

    @FXML
    private Label lbModeloDescricao;

    @FXML
    private Label lbModeloCategoria;

    @FXML
    private Label lbMotorPotencia;

    @FXML
    private Label lbMotorCombustivel;

    @FXML
    private TableColumn<Veiculo, String> tableColumnCliente;

    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculoPlaca;

    @FXML
    private TableView<Veiculo> tableViewVeiculos;


    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewVeiculo();

        tableViewVeiculos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewVeiculos(newValue));
    }

    public void carregarTableViewVeiculo() {
        tableColumnVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        listaVeiculos = veiculoDAO.listar();

        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableViewVeiculos.setItems(observableListVeiculos);
    }

    public void selecionarItemTableViewVeiculos(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoId.setText(String.valueOf(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());

            if (veiculo.getCor() != null) {
                lbVeiculoCor.setText(veiculo.getCor().getNome());
            } else {lbVeiculoCor.setText("Sem Cor / Removida");}

            if (veiculo.getCliente() != null) {
                lbVeiculoCliente.setText(veiculo.getCliente().getNome());
            } else {lbVeiculoCliente.setText("Sem Cliente");}

            if (veiculo.getModelo() != null) {
                lbModeloDescricao.setText(veiculo.getModelo().getDescricao());

                if (veiculo.getModelo().getCategoria() != null) {
                    lbModeloCategoria.setText(veiculo.getModelo().getCategoria().getDescricao());
                } else {lbModeloCategoria.setText("");}

                if (veiculo.getModelo().getMotor() != null) {
                    lbMotorPotencia.setText(String.valueOf(veiculo.getModelo().getMotor().getPotencia()));

                    if (veiculo.getModelo().getMotor().getTipoCombustivel() != null) {
                        lbMotorCombustivel.setText(veiculo.getModelo().getMotor().getTipoCombustivel().getDescricao());
                    } else {
                        lbMotorCombustivel.setText("");
                    }
                } else {
                    lbMotorPotencia.setText("N/A");
                    lbMotorCombustivel.setText("N/A");
                }
            } else {
                lbModeloDescricao.setText("Modelo Removido");
                lbModeloCategoria.setText("Sem categoria");
                lbMotorPotencia.setText("Sem potência");
                lbMotorCombustivel.setText("Sem combustível");
            }

        } else {
            lbVeiculoId.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoCor.setText("");
            lbVeiculoCliente.setText("");
            lbModeloDescricao.setText("");
            lbModeloCategoria.setText("");
            lbMotorPotencia.setText("");
            lbMotorCombustivel.setText("");
        }
    }

    @FXML
    public void handleBtInserir() throws IOException, DAOException {
        Veiculo veiculo = new Veiculo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btConfirmarClicked) {
                veiculoDAO.inserir(veiculo);
                carregarTableViewVeiculo();
            }
    }

    @FXML
    public void handleBtAlterar() throws IOException, DAOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableViewVeiculo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Veiculo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException, DAOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o veiculo " + veiculo.getPlaca())) {
                veiculoDAO.remover(veiculo);
                carregarTableViewVeiculo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Veiculo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAPCadastroVeiculoController.class.getResource("/view/FXMLAPCadastroVeiculoDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Veiculo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAPCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}