package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "item_os")
public class ItemOS {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="valor_servico")
	private double valorServico;
	private String observacoes;

	@ManyToOne
	@JoinColumn(name = "id_servico", nullable = false)
	private Servico servico;

	@ManyToOne
	@JoinColumn(name = "id_ordemservico", nullable = false)
	private OrdemServico ordemServico;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getValorServico() {
		return valorServico;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public void setValorServico(double valorServico) {
		this.valorServico = valorServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ItemOS itemOS = (ItemOS) o;
		return id == itemOS.id;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
