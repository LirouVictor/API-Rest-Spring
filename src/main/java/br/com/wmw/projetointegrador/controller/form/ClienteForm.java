package br.com.wmw.projetointegrador.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.modelo.TipoPessoa;

public class ClienteForm {

	@NotNull
	@NotEmpty
	private String nome;

	@NotNull(message = "Campo tipo de pessoa é obrigatório e só aceita os seguintes valores: FISICA/JURIDICA")
	private TipoPessoa tipoPessoa;

	@NotNull
	@NotEmpty
	private String cpfCnpj;

	@NotNull
	@NotEmpty
	@Pattern(regexp = "^(?:(?:\\+|00)55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})-?(\\d{4}))$", message = "Telefone inválido. Use o formato: (11) 99999-8888 ou 11999998888")
	private String telefone;

	@Email(message = "Formato inválido")
	@Pattern(regexp = ".+@.+\\..+", message = "O e-mail deve conter um domínio (ex: .com)")
	private String email;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cliente toCliente() {
		String apenasNumeros = cpfCnpj.replaceAll("[^0-9]", "");

		// Verifica se há dígitos repetidos
		if (apenasNumeros.matches("(\\d)\\1{10}") || apenasNumeros.matches("(\\d)\\1{13}")) {
			throw new IllegalArgumentException("CPF/CNPJ inválido, números repetidos");
		}

		if (apenasNumeros.length() == 11) {
			if (validaCpf(apenasNumeros)) {
				return new Cliente(nome, tipoPessoa, cpfCnpj.replaceAll("[^0-9]", ""),
						telefone.replaceAll("[^0-9]", ""), email);
			}
		}
		else if (apenasNumeros.length() == 14) {
			if (validaCnpj(apenasNumeros)) {
				return new Cliente(nome, tipoPessoa, cpfCnpj.replaceAll("[^0-9]", ""),
						telefone.replaceAll("[^0-9]", ""), email);
			}
		}

		throw new IllegalArgumentException("CPF/CNPJ inválido");
	}

	private boolean validaCnpj(String apenasNumeros) {
		// Calculando primeiro dígito
		int soma = 0;
		int[] pesoPrimeiro = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

		for (int i = 0; i < 12; i++) {
			soma += Character.getNumericValue(apenasNumeros.charAt(i)) * pesoPrimeiro[i];
		}

		int resto = soma % 11;
		int digito1 = (resto < 2) ? 0 : (11 - resto);

		// Calculando segundo dígito
		soma = 0;
		int[] pesoSegundo = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

		for (int i = 0; i < 13; i++) {
			soma += Character.getNumericValue(apenasNumeros.charAt(i)) * pesoSegundo[i];
		}

		resto = soma % 11;
		int digito2 = (resto < 2) ? 0 : (11 - resto);

		// Validando valor dos digitos com suas posições
		if (digito1 == Character.getNumericValue(apenasNumeros.charAt(12))
				&& digito2 == Character.getNumericValue(apenasNumeros.charAt(13))) {
			return true;
		}
		throw new IllegalArgumentException("CNPJ inválido");
	}

	private boolean validaCpf(String apenasNumeros) {
		// Calculando primeiro dígito
		int soma = 0;
		int peso = 10;
		for (int i = 0; i < 9; i++) {
			soma += Character.getNumericValue(apenasNumeros.charAt(i)) * peso;
			peso--;
		}
		int resto = (soma * 10) % 11;
		int digito1 = (resto == 10) ? 0 : resto;

		// Calculando segundo dígito
		soma = 0;
		peso = 11;
		for (int i = 0; i < 10; i++) {
			soma += Character.getNumericValue(apenasNumeros.charAt(i)) * peso;
			peso--;
		}
		resto = (soma * 10) % 11;
		int digito2 = (resto == 10) ? 0 : resto;

		// Validando valor dos digitos com suas posições
		if (digito1 == Character.getNumericValue(apenasNumeros.charAt(9))
				&& digito2 == Character.getNumericValue(apenasNumeros.charAt(10))) {
			return true;
		}
		throw new IllegalArgumentException("CPF inválido");
	}

}
