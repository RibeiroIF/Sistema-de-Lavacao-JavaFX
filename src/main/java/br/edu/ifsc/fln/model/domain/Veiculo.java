package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "veiculo")
public class Veiculo implements IDados {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String placa, observacoes;

	@ManyToOne
	@JoinColumn(name = "id_modelo", nullable = false)
	private Modelo modelo;

	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "id_cor", nullable = false)
	private Cor cor;
	
	public Veiculo() {
		this.cor = new Cor();
	}

	public Veiculo(String placa, Modelo modelo) {
		super();
		this.placa = placa;
		this.modelo = modelo;
	}

	public Veiculo(int id, String placa, String observacoes, Modelo modelo, Cor cor) {
		super();
		this.id = id;
		this.placa = placa;
		this.observacoes = observacoes;
		this.modelo = modelo;
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return this.placa;
	}

	@Override
	public String getDados() {
		StringBuilder sb = new StringBuilder();
		sb.append("Placa..............: ").append(placa).append("\n");
		sb.append("Modelo.............: ").append(modelo.getDescricao()).append("\n");
		sb.append("Marca..............: ").append(modelo.getMarca().getNome()).append("\n");
		sb.append("Categoria..........: ").append(modelo.getCategoria()).append("\n");
		sb.append("Potência do motor..: ").append(modelo.getMotor().getPotencia());
		return sb.toString();
	}

}
