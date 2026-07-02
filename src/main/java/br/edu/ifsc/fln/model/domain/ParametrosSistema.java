package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "parametros")
public class ParametrosSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pontos", nullable = false)
    private int pontos;
    private double percentual_pequeno;
    private double percentual_medio;
    private double percentual_grande;
    private double percentual_moto;
    private double percentual_padrao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public double getPercentual_pequeno() {
        return percentual_pequeno;
    }

    public void setPercentual_pequeno(double percentual_pequeno) {
        this.percentual_pequeno = percentual_pequeno;
    }

    public double getPercentual_medio() {
        return percentual_medio;
    }

    public void setPercentual_medio(double percentual_medio) {
        this.percentual_medio = percentual_medio;
    }

    public double getPercentual_grande() {
        return percentual_grande;
    }

    public void setPercentual_grande(double percentual_grande) {
        this.percentual_grande = percentual_grande;
    }

    public double getPercentual_moto() {
        return percentual_moto;
    }

    public void setPercentual_moto(double percentual_moto) {
        this.percentual_moto = percentual_moto;
    }

    public double getPercentual_padrao() {
        return percentual_padrao;
    }

    public void setPercentual_padrao(double percentual_padrao) {
        this.percentual_padrao = percentual_padrao;
    }

    @Override
    public String toString() {
        return String.valueOf(this.pontos);
    }


}
