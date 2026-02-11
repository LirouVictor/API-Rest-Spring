package br.com.wmw.projetointegrador.service;

import java.util.List;

import br.com.wmw.projetointegrador.controller.form.AtualizacaoClienteForm;
import br.com.wmw.projetointegrador.controller.form.ClienteForm;
import br.com.wmw.projetointegrador.dto.ClienteDto;
import br.com.wmw.projetointegrador.modelo.Cliente;

public interface ClienteService {

	Cliente cadastrar(ClienteForm form);

	List<ClienteDto> listar();

	ClienteDto buscarPorId(Long id);

	ClienteDto atualizar(Long id, AtualizacaoClienteForm form);

	void deletar(Long id);

}
