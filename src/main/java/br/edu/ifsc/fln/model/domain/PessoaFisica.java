package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "pessoa_fisica")
@PrimaryKeyJoinColumn(name = "id_cliente")
public class PessoaFisica extends Cliente {

	private String cpf;
	private LocalDate dataNascimento;
	
	public PessoaFisica() {
		super();
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

//	PARTE COMENTADA PARA SER USADA APENAS NO FINAL
	@Override
	public String getDados() {
		DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append(super.getDados());
		sb.append("CPF.................: ").append(cpf).append("\n");
		sb.append("Data de Nascimento..: ").append(dataNascimento.format(formatadorData));
		return sb.toString();
	}


}
