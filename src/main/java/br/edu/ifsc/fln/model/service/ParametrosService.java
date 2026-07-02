package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.model.dao.ParametrosSistemaDAO;
import br.edu.ifsc.fln.model.domain.ParametrosSistema;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;

public class ParametrosService {

    private static ParametrosSistema config;

    private static final ParametrosSistemaDAO dao = new ParametrosSistemaDAO();

    // 🔥 carregar uma única vez
    public static void carregar() {
        config = dao.buscarPrimeiro();
    }

    public static int getPontos() {
        if (config == null) {
            carregar();
        }
        return config.getPontos();
    }

    public static void setPontos(int pontos) {
        config.setPontos(pontos);
        dao.atualizar(config);
    }

    public static double getPercentualPequeno() {
        if (config == null) {
            carregar();
        }
        return config.getPercentual_pequeno();
    }

    public static void setPercentualPequeno(double percentual_pequeno){
        config.setPercentual_pequeno(percentual_pequeno);
        dao.atualizar(config);
    }

    public static double getPercentualMedio() {
        if (config == null) {
            carregar();
        }
        return config.getPercentual_medio();
    }

    public static void setPercentualMedio(double percentual_medio){
        config.setPercentual_medio(percentual_medio);
        dao.atualizar(config);
    }

    public static double getPercentualGrande() {
        if (config == null) {
            carregar();
        }
        return config.getPercentual_grande();
    }

    public static void setPercentualGrande(double percentual_grande){
        config.setPercentual_grande(percentual_grande);
        dao.atualizar(config);
    }

    public static double getPercentualMoto() {
        if (config == null) {
            carregar();
        }
        return config.getPercentual_moto();
    }

    public static void setPercentualMoto(double percentual_moto){
        config.setPercentual_moto(percentual_moto);
        dao.atualizar(config);
    }

    public static double getPercentualPadrao() {
        if (config == null) {
            carregar();
        }
        return config.getPercentual_padrao();
    }

    public static void setPercentualPadrao(double percentual_padrao){
        config.setPercentual_padrao(percentual_padrao);
        dao.atualizar(config);
    }

    public static double calcularDesconto(Servico servico) {
        double valorFinal = servico.getValor();
        if (servico.getCategoria() == ECategoria.PEQUENO){
            valorFinal = servico.getValor() - (servico.getValor() * config.getPercentual_pequeno()/100);
        }
        if (servico.getCategoria() == ECategoria.MÉDIO){
            valorFinal = servico.getValor() - (servico.getValor() * config.getPercentual_medio()/100);
        }
        if (servico.getCategoria() == ECategoria.GRANDE){
            valorFinal = servico.getValor() - (servico.getValor() * config.getPercentual_grande()/100);
        }
        if (servico.getCategoria() == ECategoria.MOTO){
            valorFinal = servico.getValor() - (servico.getValor() * config.getPercentual_moto()/100);
        }
        if (servico.getCategoria() == ECategoria.PADRÃO){
            valorFinal = servico.getValor() - (servico.getValor() * config.getPercentual_padrao()/100);
        }
        return valorFinal;
    }
}
