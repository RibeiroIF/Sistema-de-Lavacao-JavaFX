package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.service.ParametrosService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfServicoDescricao;

    @FXML
    private TextField tfServicoValor;

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarChoiceBoxCategorias();
        // TODO
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public void carregarChoiceBoxCategorias() {
        cbCategoria.getItems().setAll(ECategoria.values());
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        tfServicoDescricao.setText(servico.getDescricao());
        tfServicoValor.setText(String.valueOf(servico.getValor()));
        cbCategoria.getSelectionModel().select(servico.getCategoria());
    }


    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            servico.setDescricao(tfServicoDescricao.getText());
            servico.setValor(Double.parseDouble(tfServicoValor.getText()));
            servico.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            servico.setPontos(ParametrosService.getPontos());
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfServicoDescricao.getText() == null || this.tfServicoDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
        }

        if (this.tfServicoValor.getText() == null || this.tfServicoValor.getText().length() == 0) {
            errorMessage += "Valor inválido.\n";
        }

        if (this.cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Categoria inválida.\n";
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
