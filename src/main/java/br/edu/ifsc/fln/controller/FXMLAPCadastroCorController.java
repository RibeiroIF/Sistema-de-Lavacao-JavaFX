package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.domain.Cor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPCadastroCorController implements Initializable {


    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbCorNome;

    @FXML
    private Label lbCorId;

    @FXML
    private TableColumn<Cor, String> tableColumnCorNome;

    @FXML
    private TableView<Cor> tableViewCores;

    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;

    private final CorDAO corDAO = new CorDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            carregarTableViewCor();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        tableViewCores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCores(newValue));
    }

    public void carregarTableViewCor() throws DAOException {
        tableColumnCorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        listaCores = corDAO.listar();

        observableListCores = FXCollections.observableArrayList(listaCores);
        tableViewCores.setItems(observableListCores);
    }

    public void selecionarItemTableViewCores(Cor cor) {
        if (cor != null) {
            lbCorId.setText(String.valueOf(cor.getId()));
            lbCorNome.setText(cor.getNome());
        } else {
            lbCorId.setText("");
            lbCorNome.setText("");
        }

    }

    @FXML
    public void handleBtInserir() throws IOException {
        Cor cor = new Cor();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
        if (btConfirmarClicked) {
            try {
                corDAO.inserir(cor);
                carregarTableViewCor();
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
            if (btConfirmarClicked) {
                try {
                    corDAO.alterar(cor);
                    carregarTableViewCor();
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            try {
                corDAO.remover(cor);
                carregarTableViewCor();
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroCorDialog(Cor cor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAPCadastroCorController.class.getResource("/view/FXMLAPCadastroCorDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cor");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAPCadastroCorDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCor(cor);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}