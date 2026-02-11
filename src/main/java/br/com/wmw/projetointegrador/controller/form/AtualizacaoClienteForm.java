package br.com.wmw.projetointegrador.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AtualizacaoClienteForm {

	@NotNull
	@NotEmpty
	@Pattern(regexp = "^(?:(?:\\+|00)55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})-?(\\d{4}))$", message = "Telefone inválido. Use o formato: (11) 99999-8888 ou 11999998888")
	private String telefone;

	@Email(message = "Formato inválido")
	@Pattern(regexp = ".+@.+\\..+", message = "O e-mail deve conter um domínio (ex: .com)")
	private String email;

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

}
