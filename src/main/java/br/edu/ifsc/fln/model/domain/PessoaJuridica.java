package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "id_cliente")
public class PessoaJuridica extends Cliente {

	private String cnpj;
	private String inscricaoEstadual;
	
	public PessoaJuridica() {
		super();
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	@Override
	public String getDados() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getDados());
		sb.append("CNPJ................: ").append(cnpj).append("\n");
		sb.append("Inscrição Estadual..: ").append(inscricaoEstadual);
		return sb.toString();
	}

}
