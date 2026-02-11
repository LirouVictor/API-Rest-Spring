package br.com.wmw.projetointegrador.dto;

import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.modelo.TipoPessoa;

public class ClienteDto {

	private Long id;
	private String nome;
	private TipoPessoa tipoPessoa;
	private String cpfCnpj;
	private String telefone;
	private String email;

	public ClienteDto(Cliente cliente) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.tipoPessoa = cliente.getTipoPessoa();
		this.cpfCnpj = cliente.getCpfCnpj();
		this.telefone = cliente.getTelefone();
		this.email = cliente.getEmail();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

}
