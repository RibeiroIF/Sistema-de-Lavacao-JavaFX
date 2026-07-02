package br.edu.ifsc.fln.model.domain;

public enum ETipoCombustivel {

	GASOLINA ("Veículo a base de gasolina"), 
	ETANOL ("Veículo a base de etanol"), 
	FLEX ("Veículo a base flex"), 
	DIESEL ("Veículo a base de diesel"), 
	GNV ("Veículo a base de GNV"), 
	OUTRO ("Outro tipo de combustível atribuído");
	
	private String descricao;
	
	private ETipoCombustivel(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
