/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.exception.ExceptionLavacao;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPProcessoOrdemServicoDialogController implements Initializable {

    @FXML
    private ComboBox<Veiculo> comboBoxVeiculos;
    @FXML
    private DatePicker datePickerAgenda;
    @FXML
    private TableView<ItemOS> tableViewItens;
    @FXML
    private TableColumn<ItemOS, Servico> tableColumnServico;
    @FXML
    private TableColumn<ItemOS, Double> tableColumnValor;
    @FXML
    private TextField tfValorTotal;
    @FXML
    private ComboBox<Servico> comboBoxServico;
    @FXML
    private TextField tfValorServico;
    @FXML
    private Button buttonAdicionar;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private ContextMenu contextMenuTableView;

    @FXML
    private MenuItem contextMenuItemRemoverItem;
    @FXML
    private ChoiceBox<EStatus> choiceBoxStatus;
    @FXML
    private TextField tfDesconto;

    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;
    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItens;

    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarComboBoxVeiculos();
        carregarComboBoxServicos();
        carregarChoiceBoxStatus();
        setFocusLostHandle();
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));

        choiceBoxStatus.getSelectionModel().selectedItemProperty().addListener
                ((observable, oldValue, newValue)
                        -> {if (this.ordemServico != null) {
                                this.conferirStatus();
                        }
                    });
    }

    private void carregarComboBoxVeiculos() {
        listaVeiculos = veiculoDAO.listar();
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        comboBoxVeiculos.setItems(observableListVeiculos);
    }

    private void carregarComboBoxServicos() {
        try {
            listaServicos = servicoDAO.listar();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        observableListServicos = FXCollections.observableArrayList(listaServicos);
        comboBoxServico.setItems(observableListServicos);
    }


    public void carregarChoiceBoxStatus() {
        choiceBoxStatus.setItems(FXCollections.observableArrayList(EStatus.values()));
        choiceBoxStatus.getSelectionModel().select(0);
    }

    public void conferirStatus(){
        if (buttonAdicionar == null || choiceBoxStatus == null) {
            return;
        }
        EStatus status = choiceBoxStatus.getSelectionModel().getSelectedItem();
        boolean desativar = (status == EStatus.FECHADA || status == EStatus.CANCELADA);

        comboBoxVeiculos.setDisable(desativar);
        comboBoxServico.setDisable(desativar);
        datePickerAgenda.setDisable(desativar);
        tableViewItens.setDisable(desativar);
        tfValorTotal.setDisable(desativar);
        tfDesconto.setDisable(desativar);
        tfValorServico.setDisable(desativar);
        buttonAdicionar.setDisable(desativar);
    }

    private void setFocusLostHandle() {
        tfDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) {
                if (tfDesconto.getText() != null && !tfDesconto.getText().isEmpty()) {
                    try {
                        ordemServico.setDesconto(Double.parseDouble(tfDesconto.getText()));
                    } catch (ExceptionLavacao e) {
                        throw new RuntimeException(e);
                    }
                    tfValorTotal.setText(String.valueOf(ordemServico.getTotal()));

                }
            }
        });
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
     * @return the ordemServico
     */
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    /**
     * @param ordemServico the ordemServico to set
     */
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico.getId() != 0) {
            choiceBoxStatus.getSelectionModel().select(ordemServico.getStatus());
            comboBoxVeiculos.getSelectionModel().select(this.ordemServico.getVeiculo());
            datePickerAgenda.setValue(this.ordemServico.getAgenda());
            observableListItens = FXCollections.observableArrayList(
                    this.ordemServico.getItens());
            tableViewItens.setItems(observableListItens);
            tfValorTotal.setText(String.format("%.2f", this.ordemServico.getTotal()));
            tfDesconto.setText(String.format("%.2f", this.ordemServico.getDesconto()));
            this.conferirStatus();
        }
    }

    @FXML
    public void handleButtonAdicionar() {
        Servico servico;
        ItemOS itemOS = new ItemOS();
        if (comboBoxServico.getSelectionModel().getSelectedItem() != null) {
            servico = comboBoxServico.getSelectionModel().getSelectedItem();
            if (servico.getId() != 0) {
                itemOS.setServico(servico);
                if (tfValorServico.getText() == null || tfValorServico.getText().length() == 0) {
                    itemOS.setValorServico(itemOS.getServico().getValor());
                }
                else {
                    itemOS.setValorServico(Double.parseDouble(tfValorServico.getText()));
                }
                itemOS.getValorServico();
                itemOS.setOrdemServico(ordemServico);
                ordemServico.getItens().add(itemOS);
                ordemServico.calcularTotal();
                observableListItens = FXCollections.observableArrayList(ordemServico.getItens());
                tableViewItens.setItems(observableListItens);
                tfValorTotal.setText(String.format("%.2f", ordemServico.getTotal()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do servico");
                alert.setContentText("Não existe quantidade suficiente de servicos para a ordem.");
                alert.show();
            }
        }
    }

    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            ordemServico.setVeiculo(comboBoxVeiculos.getSelectionModel().getSelectedItem());
            ordemServico.setAgenda(datePickerAgenda.getValue());
            ordemServico.setStatus(choiceBoxStatus.getSelectionModel().getSelectedItem());
            try {
                ordemServico.setDesconto(Double.parseDouble(tfDesconto.getText()));
            } catch (ExceptionLavacao e) {
                throw new RuntimeException(e);
            }
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleTableViewMouseClicked() {
        ItemOS itemOS = tableViewItens.getSelectionModel().getSelectedItem();
        if (itemOS == null) {
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemRemoverItem.setDisable(false);
        }

    }

    @FXML
    private void handleContextMenuItemRemoverItem() {
        int index = tableViewItens.getSelectionModel().getSelectedIndex();
        ordemServico.getItens().remove(index);
        observableListItens = FXCollections.observableArrayList(ordemServico.getItens());
        tableViewItens.setItems(observableListItens);
        tfValorTotal.setText(String.format("%.2f", ordemServico.getTotal()));
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (comboBoxVeiculos.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Veiculo inválido!\n";
        }

        if (datePickerAgenda.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }

        if (observableListItens == null) {
            errorMessage += "Itens de ordemServico inválidos!\n";
        }

        DecimalFormat df = new DecimalFormat("0.00");
        try {
            tfDesconto.setText(df.parse(tfDesconto.getText()).toString());
        } catch (ParseException ex) {
            errorMessage += "A taxa de desconto está incorreta! Use \",\" como ponto decimal.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}