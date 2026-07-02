/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
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
public class FXMLAPCadastroClienteController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteId;
    @FXML
    private Label lbClienteNome;
    @FXML
    private Label lbClienteEmail;
    @FXML
    private Label lbClienteCelular;
    @FXML
    private Label lbClienteDataCadastro;
    @FXML
    private Label lbClienteTipo;
    @FXML
    private Label lbClientePais;
    @FXML
    private Label lbClientePontuacao;

    @FXML
    private Label lbClienteCPFouCNPJ;
    @FXML
    private Label lbClienteNascimentoOuInscricao;
    @FXML
    private Label tituloClienteCPFouCNPJ;
    @FXML
    private Label tituloClienteNascimentoOuInscricao;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCelular;
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;
    @FXML
    private TableView<Cliente> tableViewClientes;

    @FXML
    private TableColumn<Veiculo, String> tableColumnClienteVeiculoPlaca;
    @FXML
    private TableColumn<Veiculo, String> tableColumnClienteVeiculoModelo;
    @FXML
    private TableColumn<Veiculo, String> tableColumnClienteVeiculoCor;
    @FXML
    private TableView<Veiculo> tableViewClienteVeiculos;

    
    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;

    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewCliente();
        
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }     
    
    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        
        listaClientes = clienteDAO.listar();
        
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListClientes);
    }

    public void carregarTableViewClienteVeiculos(Cliente cliente) {
        tableColumnClienteVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tableColumnClienteVeiculoModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tableColumnClienteVeiculoCor.setCellValueFactory(new PropertyValueFactory<>("cor"));

        listaVeiculos = cliente.getListaDeVeiculos();

        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableViewClienteVeiculos.setItems(observableListVeiculos);
    }
    
    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            cliente = clienteDAO.buscarVeiculos(cliente.getId());
            carregarTableViewClienteVeiculos(cliente);

            lbClienteId.setText(String.valueOf(cliente.getId())); 
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(cliente.getDataCadastro().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            lbClientePontuacao.setText(String.valueOf(cliente.getPontuacao().verificarPontos()));
            if (cliente instanceof PessoaFisica) {
                lbClienteTipo.setText("Pessoa Física");
                lbClientePais.setText("Brasil");

                tituloClienteCPFouCNPJ.setText("CPF:");
                tituloClienteNascimentoOuInscricao.setText("Data de Nasc.:");

                lbClienteCPFouCNPJ.setText(((PessoaFisica)cliente).getCpf());
                lbClienteNascimentoOuInscricao.setText(((PessoaFisica)cliente).getDataNascimento()
                        .format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            } else {
                lbClienteTipo.setText("Pessoa Jurídica");
                lbClientePais.setText("Exterior");

                tituloClienteCPFouCNPJ.setText("CNPJ:");
                tituloClienteNascimentoOuInscricao.setText("Insc. Estadual:");

                lbClienteCPFouCNPJ.setText(((PessoaJuridica)cliente).getCnpj());
                lbClienteNascimentoOuInscricao.setText(((PessoaJuridica)cliente).getInscricaoEstadual());
            }
        } else {
            lbClienteId.setText("");
            lbClienteNome.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteDataCadastro.setText("");
            lbClientePontuacao.setText("");
            lbClienteTipo.setText("");
            lbClientePais.setText("");
            if (cliente instanceof PessoaFisica) {
                tituloClienteCPFouCNPJ.setText("CPF");
                tituloClienteNascimentoOuInscricao.setText("Data de Nasc.:");

                lbClienteCPFouCNPJ.setText("");
                lbClienteNascimentoOuInscricao.setText("");
            }
            else{
                tituloClienteCPFouCNPJ.setText("CNPJ");
                tituloClienteNascimentoOuInscricao.setText("Insc. Estadual:");
            }
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException, DAOException {
        Cliente cliente = getTipoCliente();
        if (cliente != null ) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.inserir(cliente);
                carregarTableViewCliente();
            }
        }
    }
    
    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Pessoa Física");
        opcoes.add("Pessoa Jurídica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pessoa Física", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("Pessoa Física"))
                return new PessoaFisica();
            else 
                return new PessoaJuridica();
        } else {
            return null;
        }
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException, DAOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
  }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException, DAOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o cliente " + cliente.getNome())) {
                clienteDAO.remover(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        if (cliente == null){
            return false;
        }
        if (cliente instanceof PessoaFisica){
            loader.setLocation(FXMLAPCadastroClienteController.class.getResource("/view/FXMLAPCadastroClienteDialogPF" +
                    ".fxml"));
        }
        if (cliente instanceof PessoaJuridica){
            loader.setLocation(FXMLAPCadastroClienteController.class.getResource("/view/FXMLAPCadastroClienteDialogPJ" +
                    ".fxml"));
        }
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        FXMLAPCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
