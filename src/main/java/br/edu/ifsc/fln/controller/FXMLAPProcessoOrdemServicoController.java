package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.domain.*;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPProcessoOrdemServicoController implements Initializable {

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonRemover;

    @FXML
    private Label lbOrdemServicoVeiculo;

    @FXML
    private Label lbOrdemServicoAgenda;

    @FXML
    private Label lbOrdemServicoDesconto;

    @FXML
    private Label lbOrdemServicoId;

    @FXML
    private Label lbOrdemServicoStatus;

    @FXML
    private Label lbOrdemServicoTotal;

    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnOrdemServicoStatus;
    @FXML
    private TableColumn<OrdemServico, LocalDate> tableColumnOrdemServicoAgenda;
    @FXML
    private TableColumn<OrdemServico, OrdemServico> tableColumnOrdemServicoVeiculo;
    @FXML
    private TableView<OrdemServico> tableViewOrdemServico;

    @FXML
    private TableColumn<ItemOS, String> tableColumnServicosOrdemNome;
    @FXML
    private TableColumn<ItemOS, String> tableColumnServicosOrdemValor;
    @FXML
    private TableView<ItemOS> tableViewServicosOrdem;

    private List<OrdemServico> listaOrdemServicos;
    private ObservableList<OrdemServico> observableListOrdemServicos;

    private List<ItemOS> listaServicosOrdem;
    private ObservableList<ItemOS> observableListServicosOrdem;

    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewOrdemServico();

        tableViewOrdemServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableViewOrdemServico() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableColumnOrdemServicoAgenda.setCellFactory(column -> {
            return new TableCell<OrdemServico, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });

        tableColumnOrdemServicoAgenda.setCellValueFactory(new PropertyValueFactory<>("agenda"));
        tableColumnOrdemServicoVeiculo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        tableColumnOrdemServicoStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        listaOrdemServicos = ordemServicoDAO.listar();

        observableListOrdemServicos = FXCollections.observableArrayList(listaOrdemServicos);
        tableViewOrdemServico.setItems(observableListOrdemServicos);
    }

    public void carregarTableViewServicosOrdem(OrdemServico ordem) {
        tableColumnServicosOrdemNome.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnServicosOrdemValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));

        listaServicosOrdem = ordem.getItens();

        observableListServicosOrdem = FXCollections.observableArrayList(listaServicosOrdem);
        tableViewServicosOrdem.setItems(observableListServicosOrdem);
    }

    public void selecionarItemTableView(OrdemServico ordem) {
        if (ordem != null) {
            ordem = ordemServicoDAO.buscarServicosPorOrdem(ordem.getId());
            carregarTableViewServicosOrdem(ordem);

            lbOrdemServicoId.setText(Integer.toString(ordem.getId()));
            lbOrdemServicoAgenda.setText(ordem.getAgenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lbOrdemServicoTotal.setText(String.format("%.2f", ordem.getTotal()));
            lbOrdemServicoDesconto.setText((String.format("%.2f", ordem.getDesconto())) + "%");
            lbOrdemServicoStatus.setText(ordem.getStatus().name());
            lbOrdemServicoVeiculo.setText(ordem.getVeiculo().getPlaca());
        } else {
            lbOrdemServicoId.setText("");
            lbOrdemServicoAgenda.setText("");
            lbOrdemServicoTotal.setText("");
            lbOrdemServicoDesconto.setText("");
            lbOrdemServicoStatus.setText("");
            lbOrdemServicoVeiculo.setText("");
        }
    }

    @FXML
    private void handleButtonInserir(ActionEvent event) throws IOException {
        OrdemServico ordem = new OrdemServico();
        List<ItemOS> itens = new ArrayList<>();
        ordem.setItens(itens);
        boolean buttonConfirmarClicked = showFXMLAPProcessoOrdemServicoDialog(ordem);
        if (buttonConfirmarClicked) {
            try {
                ordemServicoDAO.inserir(ordem);
            } catch (Exception e) {
                AlertDialog.exceptionMessage(e);
            }
            carregarTableViewOrdemServico();
        }
    }

    @FXML
    private void handleButtonAlterar(ActionEvent event)  {
        OrdemServico ordem = tableViewOrdemServico.getSelectionModel().getSelectedItem();
        if (ordem != null) {
            boolean buttonConfirmarClicked = false;
            try {
                buttonConfirmarClicked = showFXMLAPProcessoOrdemServicoDialog(ordem);
            } catch (IOException e) {
                AlertDialog.exceptionMessage(e);
            }
            if (buttonConfirmarClicked) {
                try {
                    ordemServicoDAO.alterar(ordem);
                    carregarTableViewOrdemServico();
                } catch (Exception e) {
                    AlertDialog.exceptionMessage(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um ordem na Tabela.");
            alert.show();
        }
    }

    @FXML
    private void handleButtonRemover(ActionEvent event) {
        OrdemServico ordem = tableViewOrdemServico.getSelectionModel().getSelectedItem();
        if (ordem != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a ordem " + ordem.getId())) {
                try {
                    ordemServicoDAO.remover(ordem);
                    carregarTableViewOrdemServico();
                } catch (Exception e) {
                    AlertDialog.exceptionMessage(e);
                }

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma ordem na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonImprimir(ActionEvent event) {
        OrdemServico ordemSelecionada = tableViewOrdemServico.getSelectionModel().getSelectedItem();

        if (ordemSelecionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhuma Ordem Selecionada");
            alert.setContentText("Por favor, selecione uma ordem de serviço na tabela para poder imprimir.");
            alert.showAndWait();
            return;
        }

        try {
            URL url = getClass().getResource("/report/RelatorioOrdemServico.jasper");
            if (url == null) {
                throw new JRException("Arquivo do relatório 'RelatorioOrdemServico.jasper' não foi encontrado na pasta /report/.");
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("PAR_ID_OS", ordemSelecionada.getId());

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.doWork(connection -> {
                    try {
                        JasperPrint jasperPrint = JasperFillManager.fillReport(
                                jasperReport,
                                parametros,
                                connection
                        );
                        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                        jasperViewer.setVisible(true);

                    } catch (JRException e) {
                        throw new RuntimeException("Erro ao processar o relatório via JasperReports.", e);
                    }
                });
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na Emissão");
            alert.setHeaderText("Não foi possível gerar a Ordem de Serviço.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public boolean showFXMLAPProcessoOrdemServicoDialog(OrdemServico ordem) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAPProcessoOrdemServicoDialogController.class.getResource(
                "/view/FXMLAPProcessoOrdemServicoDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de ordems");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(scene);
        dialogStage.sizeToScene();
        dialogStage.setResizable(false);

        FXMLAPProcessoOrdemServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(ordem);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}