package br.com.wmw.projetointegrador.controller;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.modelo.TipoPessoa;
import br.com.wmw.projetointegrador.repository.ClienteRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ClienteRepository clienteRepository;

	@Test
	public void deveriaRetornar201AoCadastrarClienteValido() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"Empresa X\",\"tipoPessoa\":\"JURIDICA\",\"cpfCnpj\":\"04.252.011/0001-10\",\"telefone\":\"(47) 3333-4444\",\"email\":\"contato@empresa.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Empresa X"));
	}

	@Test
	public void deveriaRetornar400CasoDadosEstejamIncompletos() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"Empresa X\",\"cpfCnpj\":\"04.252.011/0001-10\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarCpfDuplicado() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"Joao\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"12345678909\",\"telefone\":\"(47) 3333-4444\",\"email\":\"a@a.com\"}";
		clienteRepository.save(new Cliente("Joao", TipoPessoa.FISICA, "12345678909", "(47) 3333-4444", "a@a.com"));

		// Segundo cadastro com o mesmo JSON - Deve retornar 400
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarCnpjInvalido() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"Joao\",\"tipoPessoa\":\"JURIDICA\",\"cpfCnpj\":\"99.766.317/0001-00\",\"telefone\":\"(47) 3333-4444\",\"email\":\"a@a.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarCpfComDigitosRepetidos() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"111.111.111-11\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void deveriaRetornarListaDeClientes() throws Exception {
		clienteRepository.save(new Cliente("Teste", TipoPessoa.FISICA, "12345678909", "(47) 3333-4444", "a@a.com"));
		clienteRepository.save(new Cliente("Teste2", TipoPessoa.FISICA, "113.934.780-26", "(47) 3333-4444", "a@a.com"));
		URI uri = new URI("/clientes");
		mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void deveriaRetornarDetalhesDeUmClienteExistente() throws Exception {
		Cliente cliente = clienteRepository
				.save(new Cliente("Joao", TipoPessoa.FISICA, "12345678909", "(47) 3333-4444", "j@j.com"));
		URI uri = new URI("/clientes/" + cliente.getId());
		mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void deveriaRetornar404CasoIdNaoExista() throws Exception {
		clienteRepository.save(new Cliente("Joao", TipoPessoa.FISICA, "12345678909", "(47) 3333-4444", "j@j.com"));
		URI uri = new URI("/clientes/999999");
		mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void deveriaAtualizarAsInformacoesDeUmClienteExistente() throws Exception {
		Cliente original = new Cliente("Nome Antigo", TipoPessoa.FISICA, "12345678909", "(47) 3333-4444",
				"antigo@email.com");
		clienteRepository.save(original);
		URI uri = new URI("/clientes/" + original.getId());
		String jsonAtualizacao = "{\"telefone\":\"(47) 3333-4444\",\"email\":\"novo@email.com\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.put(uri).content(jsonAtualizacao).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void deveriaRetornar404AoTentarAtualizarIdInexistente() throws Exception {
		URI uri = new URI("/clientes/99999");
		String json = "{\"telefone\":\"(47) 3333-4444\",\"email\":\"novo@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.put(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void deveriaRemoverUmClienteExistente() throws Exception {
		Cliente cliente = new Cliente("Para Deletar", TipoPessoa.FISICA, "12345678909", "(47) 3333-4444",
				"del@email.com");
		clienteRepository.save(cliente);
		URI uri = new URI("/clientes/" + cliente.getId());
		mockMvc.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void deveriaRetornar404AoTentarDeletarIdInexistente() throws Exception {
		URI uri = new URI("/clientes/888888");
		mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void naoDeveriaCadastrarPessoaFisicaComCnpj() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"04.252.011/0001-10\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarPessoaJuridicaComCpf() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"Empresa\",\"tipoPessoa\":\"JURIDICA\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"(47) 3333-4444\",\"email\":\"empresa@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarTelefoneInvalido() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"123\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void deveriaCadastrarTelefoneSemFormatacao() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"Maria\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"47999998888\",\"email\":\"maria@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Maria"));
	}

	@Test
	public void naoDeveriaCadastrarEmailSemDominio() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joao@email\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarEmailSemArroba() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joaoemail.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarSemNome() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarSemTipoPessoa() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"cpfCnpj\":\"113.934.780-26\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarSemCpfCnpj() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"telefone\":\"(47) 99999-8888\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveriaCadastrarSemTelefone() throws Exception {
		URI uri = new URI("/clientes");
		String json = "{\"nome\":\"João\",\"tipoPessoa\":\"FISICA\",\"cpfCnpj\":\"113.934.780-26\",\"email\":\"joao@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveAtualizarComTelefoneInvalido() throws Exception {
		URI uri = new URI("/clientes/1");
		String json = "{\"telefone\":\"123\",\"email\":\"novo@email.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.put(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void naoDeveAtualizarComEmailInvalido() throws Exception {
		URI uri = new URI("/clientes/1");
		String json = "{\"telefone\":\"(47) 99999-8888\",\"email\":\"emailinvalido\"}";
		mockMvc.perform(MockMvcRequestBuilders.put(uri).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
