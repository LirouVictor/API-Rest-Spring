package br.com.wmw.projetointegrador.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wmw.projetointegrador.controller.form.AtualizacaoClienteForm;
import br.com.wmw.projetointegrador.controller.form.ClienteForm;
import br.com.wmw.projetointegrador.dto.ClienteDto;
import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.modelo.TipoPessoa;
import br.com.wmw.projetointegrador.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	ClienteRepository clienteRepository;

	@Override
	public Cliente cadastrar(ClienteForm form) {

		String apenasNumeros = form.getCpfCnpj().replaceAll("[^0-9]", "");
		if (clienteRepository.existsBycpfCnpj(apenasNumeros)) {
			throw new IllegalArgumentException("CPF/CNPJ já cadastrado!");
		}

		validaCpfCnpjTipoPessoa(form, apenasNumeros);
		Cliente cliente = form.toCliente();
		return clienteRepository.save(cliente);
	}

	private void validaCpfCnpjTipoPessoa(ClienteForm form, String apenasNumeros) {
		if (form.getTipoPessoa() == TipoPessoa.FISICA && apenasNumeros.length() != 11) {
			throw new IllegalArgumentException("CPF/CNPJ incompatível com o tipo de pessoa");
		}

		if (form.getTipoPessoa() == TipoPessoa.JURIDICA && apenasNumeros.length() != 14) {
			throw new IllegalArgumentException("CPF/CNPJ incompatível com o tipo de pessoa");
		}
	}

	@Override
	public List<ClienteDto> listar() {
		List<Cliente> clientes = clienteRepository.findAll();
		List<ClienteDto> clientesDto = clientes.stream().map(ClienteDto::new).collect(Collectors.toList());
		return clientesDto;
	}

	@Override
	public ClienteDto buscarPorId(Long id) {
		return clienteRepository.findById(id).map(cliente -> (new ClienteDto(cliente)))
				.orElseThrow(() -> new NoSuchElementException("Nenhum cliente encontrado com o Id enviado"));
	}

	@Override
	public ClienteDto atualizar(Long id, AtualizacaoClienteForm form) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Nenhum cliente encontrado com o Id enviado"));
		cliente.setTelefone(form.getTelefone());

		if (form.getEmail() == null || form.getEmail() == "") {
			cliente.setEmail(cliente.getEmail());
		} else {
			cliente.setEmail(form.getEmail());
		}
		clienteRepository.save(cliente);
		return new ClienteDto(cliente);
	}

	@Override
	public void deletar(Long id) {
		clienteRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Nenhum cliente encontrado com o Id enviado"));
		clienteRepository.deleteById(id);
	}

}
