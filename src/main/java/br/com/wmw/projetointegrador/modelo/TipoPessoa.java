package br.com.wmw.projetointegrador.modelo;

public enum TipoPessoa {
	
	FISICA, JURIDICA;

	@com.fasterxml.jackson.annotation.JsonCreator
	public static TipoPessoa fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			return TipoPessoa.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
