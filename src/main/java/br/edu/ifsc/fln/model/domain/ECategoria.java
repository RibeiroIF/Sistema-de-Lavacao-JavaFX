package br.edu.ifsc.fln.model.domain;

public enum ECategoria {

	PEQUENO ("Carro de tamanho pequeno"), 
	MÉDIO ("Carro de tamanho médio"), 
	GRANDE ("Carro de tamanho grande"), 
	MOTO ("É uma moto"), 
	PADRÃO ("Veículo padrão registrado");
	
	private String descricao;

	private ECategoria(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
