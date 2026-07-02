package br.edu.ifsc.fln.model.dto;

public class LucroMensalDTO {
    private Integer ano;
    private Integer mes;
    private Double lucroTotal;

    public LucroMensalDTO(Integer ano, Integer mes, Double lucroTotal) {
        this.ano = ano;
        this.mes = mes;
        this.lucroTotal = lucroTotal;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Double getLucroTotal() {
        return lucroTotal;
    }

    public void setLucroTotal(Double lucroTotal) {
        this.lucroTotal = lucroTotal;
    }
}