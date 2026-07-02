/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPCadastroClienteDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfCelular;

    @FXML
    private TextField tfCpf;
    @FXML
    private DatePicker dpDataNascimento;

    @FXML
    private TextField tfCnpj;
    @FXML
    private TextField tfInscricaoEstadual;


    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente.getId() != 0) {
            this.tfNome.setText(this.cliente.getNome());
            this.tfEmail.setText(this.cliente.getEmail());
            this.tfCelular.setText(this.cliente.getCelular());
            if (cliente instanceof PessoaFisica) {
                tfInscricaoEstadual.setText("BRASIL");
                tfInscricaoEstadual.setDisable(true);
            } else {
                tfInscricaoEstadual.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
                tfInscricaoEstadual.setDisable(false);
            }
        }
        this.tfNome.requestFocus();
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setCelular(tfCelular.getText());
            cliente.setDataCadastro(LocalDate.now());
            if (this.cliente instanceof PessoaFisica){
                ((PessoaFisica)cliente).setDataNascimento(dpDataNascimento.getValue());
                ((PessoaFisica)cliente).setCpf(tfCpf.getText());
            }
            if (this.cliente instanceof PessoaJuridica) {
                ((PessoaJuridica)cliente).setCnpj(tfCnpj.getText());
                ((PessoaJuridica)cliente).setInscricaoEstadual(tfInscricaoEstadual.getText());
            }
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
        ClienteDAO dao = new ClienteDAO();

        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome inválido.\n";
        }

        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Telecelular inválido.\n";
        }

        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0 || !this.tfEmail.getText().contains("@")) {
            errorMessage += "Email inválido.\n";
        }

        // CORREÇÃO: Usando 'instanceof' em vez de '== new'
        if (this.cliente instanceof PessoaFisica) {
            if (this.dpDataNascimento.getValue() == null) {
                errorMessage += "Data de Nascimento inválida.\n";
            }
            if (this.tfCpf.getText() == null || this.tfCpf.getText().trim().length() == 0){
                errorMessage += "CPF inválido.\n";
            } else {
                // VALIDAÇÃO DE DUPLICIDADE (Apenas para novos registros)
                if (this.cliente.getId() == 0) {
                    PessoaFisica pfExistente = dao.buscarPorCpf(this.tfCpf.getText().trim());
                    if (pfExistente != null) {
                        errorMessage += "Já existe um cliente cadastrado com este CPF.\n";
                    }
                }
            }
        }

        if (this.cliente instanceof PessoaJuridica) {
            if (this.tfCnpj.getText() == null || this.tfCnpj.getText().trim().length() == 0) {
                errorMessage += "CNPJ inválido.\n";
            } else {
                // VALIDAÇÃO DE DUPLICIDADE DE CNPJ (Apenas para novos registros)
                if (this.cliente.getId() == 0) {
                    PessoaJuridica pjExistente = dao.buscarPorCnpj(this.tfCnpj.getText().trim());
                    if (pjExistente != null) {
                        errorMessage += "Já existe um cliente cadastrado com este CNPJ.\n";
                    }
                }
            }

            if (this.tfInscricaoEstadual.getText() == null || this.tfInscricaoEstadual.getText().length() == 0) {
                errorMessage += "Inscrição Estadual inválida.\n"; // Ajustado o texto do seu esqueleto original
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Exibindo a mensagem de erro consolidada (Campos vazios + Duplicidades)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os problemas encontrados!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
