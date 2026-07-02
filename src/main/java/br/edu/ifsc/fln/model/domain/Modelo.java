package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "modelo")
public class Modelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "id_marca", nullable = false)
	private Marca marca;

	@Enumerated(EnumType.STRING)
	private ECategoria categoria;

	@OneToOne(mappedBy = "modelo", cascade = CascadeType.ALL, orphanRemoval = true)
	private Motor motor;
	
	public Modelo() {
		this.createMotor();
	}
	
	public Modelo(int id, String descricao, Marca marca, ECategoria categoria, int potencia, ETipoCombustivel combustivel) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.marca = marca;
		this.categoria = categoria;
		this.motor = new Motor(potencia, combustivel);
	}

	private void createMotor() {
		this.motor = new Motor(this);
	}
	
	public Motor getMotor() {
		return motor;
	}

	public ECategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(ECategoria categoria) {
		this.categoria = categoria;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
