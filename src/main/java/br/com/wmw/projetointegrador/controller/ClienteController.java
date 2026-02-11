package br.com.wmw.projetointegrador.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.wmw.projetointegrador.controller.form.AtualizacaoClienteForm;
import br.com.wmw.projetointegrador.controller.form.ClienteForm;
import br.com.wmw.projetointegrador.dto.ClienteDto;
import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.repository.ClienteRepository;
import br.com.wmw.projetointegrador.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	ClienteService clienteService;

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid ClienteForm form, UriComponentsBuilder uriBuilder) {
		Cliente cliente = clienteService.cadastrar(form);
		URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).body(new ClienteDto(cliente));
	}

	@GetMapping
	public ResponseEntity<List<ClienteDto>> listar() {
		List<ClienteDto> clientesDto = clienteService.listar();
		return ResponseEntity.ok(clientesDto);
	}

	@GetMapping("/{idCliente}")
	public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long idCliente) {
		return ResponseEntity.ok(clienteService.buscarPorId(idCliente));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteDto> atualizar(@PathVariable Long id,
			@RequestBody @Valid AtualizacaoClienteForm form) {
		return ResponseEntity.ok(clienteService.atualizar(id, form));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.ok().build();
	}

}
