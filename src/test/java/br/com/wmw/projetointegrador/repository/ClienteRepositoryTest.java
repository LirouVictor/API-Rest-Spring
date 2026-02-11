package br.com.wmw.projetointegrador.repository;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.wmw.projetointegrador.modelo.Cliente;
import br.com.wmw.projetointegrador.modelo.TipoPessoa;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClienteRepositoryTest  {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void deveriaSalvarEBuscarUmClientePeloId() {
		Cliente cliente = new Cliente("João Silva", TipoPessoa.FISICA, "12345678900", "4799999999", "joao@email.com");
		entityManager.persist(cliente);
		Cliente clienteEncontrado = repository.findById(cliente.getId()).orElse(null);
		Assert.assertNotNull(clienteEncontrado);
		Assert.assertEquals("João Silva", clienteEncontrado.getNome());
		Assert.assertEquals(TipoPessoa.FISICA, clienteEncontrado.getTipoPessoa());
	}

	@Test
	public void deveriaRetornarTodosOsClientesAoListar() {
		Cliente c1 = new Cliente("Ana", TipoPessoa.FISICA, "111", "111", "ana@email.com");
		Cliente c2 = new Cliente("Beto", TipoPessoa.JURIDICA, "222", "222", "beto@email.com");
		entityManager.persist(c1);
		entityManager.persist(c2);
		List<Cliente> lista = repository.findAll();
		Assert.assertEquals(6, lista.size()); //Dois criados agora e 4 do arquivo data.sql
		Assert.assertTrue(lista.stream().anyMatch(c -> c.getNome().equals("Ana")));
		Assert.assertTrue(lista.stream().anyMatch(c -> c.getNome().equals("Beto")));
	}

	@Test
	public void deveriaEncontrarClientePorId() {
		Cliente cliente = new Cliente("Luis", TipoPessoa.FISICA, "333", "333", "z@z.com");
		entityManager.persist(cliente);
		Optional<Cliente> encontrado = repository.findById(cliente.getId());
		Assert.assertTrue(encontrado.isPresent());
		Assert.assertEquals("Luis", encontrado.get().getNome());
	}

	@Test
	public void deveriaRetornarVazioAoBuscarIdInexistente() {
		Optional<Cliente> encontrado = repository.findById(999L);
		Assert.assertFalse(encontrado.isPresent());
	}

	@Test
	public void deveriaDeletarClienteExistente() {
		Cliente cliente = new Cliente("Ana", TipoPessoa.FISICA, "12345678900", "47999999999", "ana@email.com");
		entityManager.persist(cliente);
		Long id = cliente.getId();
		repository.deleteById(id);
		Assert.assertFalse(repository.findById(id).isPresent());
	}

	@Test
	public void deveriaVerificarSeExisteCpfCadastrado() {
		Cliente cliente = new Cliente("João", TipoPessoa.FISICA, "12345678900", "47999999999", "joao@email.com");
		entityManager.persist(cliente);
		boolean existe = repository.existsBycpfCnpj("12345678900");
		Assert.assertTrue(existe);
	}

	@Test
	public void deveriaVerificarSeNaoExisteCpfNaoCadastrado() {
		boolean existe = repository.existsBycpfCnpj("99999999999");
		Assert.assertFalse(existe);
	}

}
