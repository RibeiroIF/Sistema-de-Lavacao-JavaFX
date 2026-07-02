package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.domain.Marca;
import java.io.IOException;
import java.net.URL;
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
public class FXMLAPCadastroMarcaController implements Initializable {


    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbMarcaNome;

    @FXML
    private Label lbMarcaId;

    @FXML
    private TableColumn<Marca, String> tableColumnMarcaNome;

    @FXML
    private TableView<Marca> tableViewMarcas;

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;

    private final MarcaDAO marcaDAO = new MarcaDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewMarca();

        tableViewMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewMarcas(newValue));
    }

    public void carregarTableViewMarca() {
        tableColumnMarcaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        try {
            listaMarcas = marcaDAO.listar();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        tableViewMarcas.setItems(observableListMarcas);
    }

    public void selecionarItemTableViewMarcas(Marca marca) {
        if (marca != null) {
            lbMarcaId.setText(String.valueOf(marca.getId()));
            lbMarcaNome.setText(marca.getNome());
        } else {
            lbMarcaId.setText("");
            lbMarcaNome.setText("");
        }

    }

    @FXML
    public void handleBtInserir() throws IOException {
        Marca marca = new Marca();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
        if (btConfirmarClicked) {
            try {
                marcaDAO.inserir(marca);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            carregarTableViewMarca();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
            if (btConfirmarClicked) {
                try {
                    marcaDAO.alterar(marca);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            try {
                marcaDAO.remover(marca);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            carregarTableViewMarca();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroMarcaDialog(Marca marca) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAPCadastroMarcaController.class.getResource("/view/FXMLAPCadastroMarcaDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Marca");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAPCadastroMarcaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(marca);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}