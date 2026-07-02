package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;
@Entity
@Table(name = "motor")
public class Motor {

	@Id
	@Column(name = "id_modelo")
	private Integer id;

	private int potencia;

	@Enumerated(EnumType.STRING)
	private ETipoCombustivel tipoCombustivel;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id_modelo")
	private Modelo modelo;

	Motor(Modelo modelo){
		this.modelo = modelo;
	}
	
	protected Motor() {}

	public Motor(int potencia, ETipoCombustivel tipoCombustivel) {
		super();
		this.potencia = potencia;
		this.tipoCombustivel = tipoCombustivel;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public ETipoCombustivel getTipoCombustivel() {
		return tipoCombustivel;
	}

	public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
		this.tipoCombustivel = tipoCombustivel;
	}
	
	
}
