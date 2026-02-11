package br.com.wmw.projetointegrador.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.wmw.projetointegrador.controller.form.AtualizacaoClienteForm;
import br.com.wmw.projetointegrador.controller.form.ClienteForm;
import br.com.wmw.projetointegrador.dto.ClienteDto;
import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.modelo.TipoPessoa;
import br.com.wmw.projetointegrador.repository.ClienteRepository;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceImplTest {

	@InjectMocks
	private ClienteServiceImpl clienteService;

	@Mock
	private ClienteRepository clienteRepository;

	private ClienteForm formPessoaFisica;
	private ClienteForm formPessoaJuridica;
	private Cliente clienteSalvo;

	@Before
	public void setup() {
		formPessoaFisica = new ClienteForm();
		formPessoaFisica.setNome("João Silva");
		formPessoaFisica.setTipoPessoa(TipoPessoa.FISICA);
		formPessoaFisica.setCpfCnpj("113.934.780-26");
		formPessoaFisica.setTelefone("(47) 99999-8888");
		formPessoaFisica.setEmail("joao@email.com");

		formPessoaJuridica = new ClienteForm();
		formPessoaJuridica.setNome("Empresa XYZ");
		formPessoaJuridica.setTipoPessoa(TipoPessoa.JURIDICA);
		formPessoaJuridica.setCpfCnpj("04.252.011/0001-10");
		formPessoaJuridica.setTelefone("(47) 3333-4444");
		formPessoaJuridica.setEmail("contato@empresa.com");

		clienteSalvo = new Cliente("João Silva", TipoPessoa.FISICA, "11393478026", "47999998888", "joao@email.com");
		clienteSalvo.setId(1L);
	}

	@Test
	public void deveriaCadastrarClientePessoaFisicaComSucesso() {
		when(clienteRepository.existsBycpfCnpj(anyString())).thenReturn(false);
		when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);
		Cliente resultado = clienteService.cadastrar(formPessoaFisica);
		assertNotNull(resultado);
		assertEquals("João Silva", resultado.getNome());
		verify(clienteRepository, times(1)).save(any(Cliente.class));
	}

	@Test
	public void deveriaCadastrarClientePessoaJuridicaComSucesso() {
		Cliente empresaSalva = new Cliente("Empresa XYZ", TipoPessoa.JURIDICA, "04252011000110", "4733334444",
				"contato@empresa.com");
		empresaSalva.setId(2L);
		when(clienteRepository.existsBycpfCnpj(anyString())).thenReturn(false);
		when(clienteRepository.save(any(Cliente.class))).thenReturn(empresaSalva);
		Cliente resultado = clienteService.cadastrar(formPessoaJuridica);
		assertNotNull(resultado);
		assertEquals("Empresa XYZ", resultado.getNome());
		assertEquals(TipoPessoa.JURIDICA, resultado.getTipoPessoa());
		verify(clienteRepository, times(1)).save(any(Cliente.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveriaCadastrarCpfDuplicado() {
		when(clienteRepository.existsBycpfCnpj("11393478026")).thenReturn(true);
		clienteService.cadastrar(formPessoaFisica);
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveriaCadastrarCnpjDuplicado() {
		when(clienteRepository.existsBycpfCnpj("04252011000110")).thenReturn(true);
		clienteService.cadastrar(formPessoaJuridica);
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveriaCadastrarPessoaFisicaComCnpj() {
		formPessoaFisica.setCpfCnpj("04.252.011/0001-10"); // CNPJ com 14 dígitos
		when(clienteRepository.existsBycpfCnpj(anyString())).thenReturn(false);
		clienteService.cadastrar(formPessoaFisica);
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveriaCadastrarPessoaJuridicaComCpf() {
		formPessoaJuridica.setCpfCnpj("113.934.780-26"); // CPF com 11 dígitos
		when(clienteRepository.existsBycpfCnpj(anyString())).thenReturn(false);
		clienteService.cadastrar(formPessoaJuridica);
	}

	@Test
	public void deveriaListarTodosOsClientes() {
		List<Cliente> clientes = new ArrayList<>();
		clientes.add(clienteSalvo);
		clientes.add(new Cliente("Maria", TipoPessoa.FISICA, "12345678900", "47888888888", "maria@email.com"));
		when(clienteRepository.findAll()).thenReturn(clientes);
		List<ClienteDto> resultado = clienteService.listar();
		assertEquals(2, resultado.size());
		verify(clienteRepository, times(1)).findAll();
	}

	@Test
	public void deveriaRetornarListaVaziaQuandoNaoHouverClientes() {
		when(clienteRepository.findAll()).thenReturn(new ArrayList<>());
		List<ClienteDto> resultado = clienteService.listar();
		assertEquals(0, resultado.size());
		assertTrue(resultado.isEmpty());
	}

	@Test
	public void deveriaBuscarClientePorId() {
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSalvo));
		ClienteDto resultado = clienteService.buscarPorId(1L);
		assertNotNull(resultado);
		assertEquals("João Silva", resultado.getNome());
		assertEquals(Long.valueOf(1L), resultado.getId());
		verify(clienteRepository, times(1)).findById(1L);
	}

	@Test(expected = NoSuchElementException.class)
	public void deveriaLancarExcecaoAoBuscarClienteInexistente() {
		when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
		clienteService.buscarPorId(999L);
	}

	@Test
	public void deveriaAtualizarTelefoneEEmailDoCliente() {
		AtualizacaoClienteForm form = new AtualizacaoClienteForm();
		form.setTelefone("(47) 98888-7777");
		form.setEmail("novoemail@email.com");
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSalvo));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);
		ClienteDto resultado = clienteService.atualizar(1L, form);
		assertNotNull(resultado);
		verify(clienteRepository, times(1)).save(any(Cliente.class));
	}

	@Test
	public void deveriaManterEmailAntigoQuandoNovoEmailForNull() {
		AtualizacaoClienteForm form = new AtualizacaoClienteForm();
		form.setTelefone("(47) 98888-7777");
		form.setEmail(null);
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSalvo));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);
		ClienteDto resultado = clienteService.atualizar(1L, form);
		assertNotNull(resultado);
		assertEquals("joao@email.com", clienteSalvo.getEmail());
	}

	@Test
	public void deveriaManterEmailAntigoQuandoNovoEmailForVazio() {
		AtualizacaoClienteForm form = new AtualizacaoClienteForm();
		form.setTelefone("(47) 98888-7777");
		form.setEmail("");
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSalvo));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);
		ClienteDto resultado = clienteService.atualizar(1L, form);
		assertNotNull(resultado);
		assertEquals("joao@email.com", clienteSalvo.getEmail());
	}

	@Test(expected = NoSuchElementException.class)
	public void naoDeveAtualizarClienteInexistente() {
		AtualizacaoClienteForm form = new AtualizacaoClienteForm();
		form.setTelefone("(47) 98888-7777");
		form.setEmail("novoemail@email.com");
		when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
		clienteService.atualizar(999L, form);
	}

	@Test
	public void deveriaDeletarClienteExistente() {
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSalvo));
		doNothing().when(clienteRepository).deleteById(1L);
		clienteService.deletar(1L);
		verify(clienteRepository, times(1)).findById(1L);
		verify(clienteRepository, times(1)).deleteById(1L);
	}

	@Test(expected = NoSuchElementException.class)
	public void naoDeveDeletarClienteInexistente() {
		when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
		clienteService.deletar(999L);
	}
}
