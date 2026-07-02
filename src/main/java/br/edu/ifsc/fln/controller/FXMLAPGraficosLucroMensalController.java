package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dto.LucroMensalDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author gabriel ribeiro
 */
public class FXMLAPGraficosLucroMensalController implements Initializable {

    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private CategoryAxis categoryAxis;

    private ObservableList<String> observableListMeses = FXCollections.observableArrayList();
    private final OrdemServicoDAO ordemDAO = new OrdemServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        observableListMeses.addAll(Arrays.asList(meses));
        categoryAxis.setCategories(observableListMeses);

        Map<Integer, ArrayList<Double>> dados = ordemDAO.listarLucroOrdensMensais();

        for (Map.Entry<Integer, ArrayList<Double>> dadosItem : dados.entrySet()) {
            XYChart.Series<String, Double> series = new XYChart.Series<>();
            series.setName(dadosItem.getKey().toString());

            for (int i = 0; i < dadosItem.getValue().size(); i += 2) {
                int numMes = dadosItem.getValue().get(i).intValue();
                String mesStr = retornaNomeMes(numMes);
                Double lucro = dadosItem.getValue().get(i + 1);

                series.getData().add(new XYChart.Data<>(mesStr, lucro));
            }

            barChart.getData().add(series);

            //HOVER
            for (XYChart.Data<String, Double> data : series.getData()) {
                javafx.scene.Node barra = data.getNode();

                if (barra != null) {
                    String valorFormatado = String.format("R$ %.2f", data.getYValue());
                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(valorFormatado);
                    tooltip.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
                    barra.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            tooltip.show(barra,
                                    javafx.stage.Window.getWindows().get(0).getX() + barra.getScene().getWindow().getX(),
                                    javafx.stage.Window.getWindows().get(0).getY() + barra.getScene().getWindow().getY()
                            );
                            javafx.scene.control.Tooltip.install(barra, tooltip);
                        } else {
                            tooltip.hide();
                            javafx.scene.control.Tooltip.uninstall(barra, tooltip);
                        }
                    });
                }
            }
        }
    }

    private String retornaNomeMes(int mes) {
        switch (mes) {
            case 1: return "Jan";
            case 2: return "Fev";
            case 3: return "Mar";
            case 4: return "Abr";
            case 5: return "Mai";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Ago";
            case 9: return "Set";
            case 10: return "Out";
            case 11: return "Nov";
            case 12: return "Dez";
            default: return null;
        }
    }
}