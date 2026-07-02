package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "pontuacao")
public class Pontuacao {

	@Id
	@Column(name = "id_cliente")
	private int id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@Column(name = "quantidade")
	private int quantidade = 0;

	Pontuacao(Cliente cliente){
		this.cliente = cliente;
	}

	protected Pontuacao() {
		this.quantidade = 0;
	}
	
	public void somarPontos(int quantidade) {
		this.quantidade += quantidade;
	}

	public int verificarPontos() {
		return quantidade;
	}
}
