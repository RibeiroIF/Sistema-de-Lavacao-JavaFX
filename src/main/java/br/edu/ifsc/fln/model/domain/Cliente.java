package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cliente implements IDados {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	protected String nome;
	protected String email;
	protected String celular;
	protected LocalDate dataCadastro;

	@OneToMany(mappedBy = "cliente")
	protected List<Veiculo> ListaDeVeiculos = new ArrayList<Veiculo>();

	@OneToOne(mappedBy = "cliente", cascade=CascadeType.ALL, orphanRemoval = true)
	protected Pontuacao pontuacao;
	
	public Cliente() {
		this.createPontuacao();
	}

	public Cliente(int id, String nome, String celular, String email, LocalDate dataCadastro) {
		super();
		this.id = id;
		this.nome = nome;
		this.celular = celular;
		this.email = email;
		this.dataCadastro = dataCadastro;
		this.pontuacao = new Pontuacao();
	}

	private void createPontuacao(){
		this.pontuacao = new Pontuacao(this);
	}

	public Pontuacao getPontuacao() {
		return pontuacao;
	}

	public List<Veiculo> getListaDeVeiculos(){
		return ListaDeVeiculos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	//PARTE COMENTADA PARA SER USADA NO FUTURO
	@Override
	public String getDados() {
		DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("Nome................: ").append(nome).append("\n");
		sb.append("Celular.............: ").append(celular).append("\n");
		sb.append("E-mail..............: ").append(email).append("\n");
		sb.append("Data de Cadastro....: ").append(dataCadastro.format(formatadorData)).append("\n");
		sb.append("Pontuação...........: ").append(pontuacao.verificarPontos()).append("\n");
		return sb.toString();
	}


}

