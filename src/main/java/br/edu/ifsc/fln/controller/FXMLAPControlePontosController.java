package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.ParametrosSistema;
import br.edu.ifsc.fln.model.service.ParametrosService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPControlePontosController implements Initializable {

    @FXML
    private Button btSalvar;

    @FXML
    private Button btRestaurar;

    @FXML
    private Spinner<Integer> spPontosGerais;

    private ParametrosSistema parametros;

    private Stage dialogStage;
    private boolean btSalvarClicked = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int pontosGerais = ParametrosService.getPontos();

        inicializarSpinner(spPontosGerais, 0, 100, pontosGerais);
    }

    private void inicializarSpinner(Spinner<Integer> spinner, int min, int max, int initial){
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial);
        spinner.setValueFactory(factory);
    }

    public boolean isBtSalvarClicked() {
        return btSalvarClicked;
    }

    public void setBtSalvarClicked(boolean btSalvarClicked) {
        this.btSalvarClicked = btSalvarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public ParametrosSistema getParametrosSistema(){
        return parametros;
    }

    public void setParametrosSistema(ParametrosSistema parametros) {
        this.parametros = parametros;
        spPontosGerais.getValueFactory().setValue(ParametrosService.getPontos());
    }

    @FXML
    public void handleBtSalvar() {
        ParametrosService.setPontos(spPontosGerais.getValue());
        btSalvarClicked = true;
    }

    @FXML
    public void handleBtRestaurar() throws IOException {
        ParametrosSistema parametros = new ParametrosSistema();
        this.setParametrosSistema(parametros);
    }

}