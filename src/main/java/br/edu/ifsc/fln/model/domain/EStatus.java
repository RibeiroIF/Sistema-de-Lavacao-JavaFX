package br.edu.ifsc.fln.model.domain;

public enum EStatus {

	ABERTA ("Ordem aberta, podendo fazer alterações"), 
	FECHADA ("Ordem fechada, impossibilitada de fazer alterações"), 
	CANCELADA ("Ordem cancelada pelo usuário");
	
	private String descricao;

	private EStatus(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
}
