package com.autobots.automanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RmmIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("RMM Nível 1, 2 e 3: Listagem de clientes com status 200 e links HATEOAS")
	void deveObterColecaoClientesComLinks() throws Exception {
		mockMvc.perform(get("/clientes").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$._embedded.clienteList").isArray());
	}

	@Test
	@DisplayName("RMM Nível 2 e 3: Obter cliente por ID com status 200 e links HATEOAS detalhados")
	void deveObterClientePorIdComLinks() throws Exception {
		mockMvc.perform(get("/clientes/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$._links.clientes.href").exists())
				.andExpect(jsonPath("$._links.cliente_endereco.href").exists())
				.andExpect(jsonPath("$._links.cliente_documentos.href").exists())
				.andExpect(jsonPath("$._links.cliente_telefones.href").exists());
	}

	@Test
	@DisplayName("RMM Nível 2: Obter cliente inexistente deve retornar 404 Not Found")
	void deveRetornar404ParaClienteInexistente() throws Exception {
		mockMvc.perform(get("/clientes/99999").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("RMM Nível 2: Cadastro com POST deve retornar 201 Created e Header Location")
	void deveCadastrarClienteERetornar201ComLocation() throws Exception {
		String novoClienteJson = "{"
				+ "\"nome\": \"Santos Dumont\","
				+ "\"nomeSocial\": \"Pai da Aviação\","
				+ "\"endereco\": {\"cidade\": \"Petrópolis\", \"rua\": \"Rua do Encanto\", \"numero\": \"22\"}"
				+ "}";

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novoClienteJson))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("RMM Nível 2: Atualização com PUT deve retornar 200 OK")
	void deveAtualizarClienteComPut() throws Exception {
		String atualizacaoJson = "{"
				+ "\"nome\": \"Dom Pedro I Atualizado\""
				+ "}";

		mockMvc.perform(put("/clientes/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(atualizacaoJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Dom Pedro I Atualizado"))
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("RMM Nível 1 e 3: Sub-recursos de cliente com URIs únicas e HATEOAS")
	void deveObterSubrecursosCliente() throws Exception {
		mockMvc.perform(get("/clientes/1/endereco").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cidade").exists());

		mockMvc.perform(get("/clientes/1/documentos").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get("/clientes/1/telefones").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("RMM Nível 1, 2 e 3: Endpoints dedicados de Enderecos, Documentos e Telefones")
	void deveAcessarRecursosIndependentes() throws Exception {
		mockMvc.perform(get("/enderecos").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get("/documentos").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get("/telefones").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href").exists());
	}
}
