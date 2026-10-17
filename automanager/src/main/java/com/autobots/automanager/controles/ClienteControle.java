package com.autobots.automanager.controles;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkCliente;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.ClienteAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {

	@Autowired
	private ClienteRepositorio repositorio;

	@Autowired
	private AdicionadorLinkCliente adicionadorLink;

	@Autowired(required = false)
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired(required = false)
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Autowired(required = false)
	private AdicionadorLinkDocumento adicionadorLinkDocumento;

	@GetMapping
	public ResponseEntity<CollectionModel<Cliente>> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		adicionadorLink.adicionarLink(clientes);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(clientes, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> obterCliente(@PathVariable long id) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Cliente cliente = opt.get();
		adicionadorLink.adicionarLink(cliente);
		return ResponseEntity.ok(cliente);
	}

	@PostMapping
	public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
		cliente.setId(null);
		Cliente novoCliente = repositorio.save(cliente);
		adicionadorLink.adicionarLink(novoCliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoCliente.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novoCliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizarCliente(@PathVariable long id, @RequestBody Cliente atualizacao) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Cliente cliente = opt.get();
		ClienteAtualizador atualizador = new ClienteAtualizador();
		atualizador.atualizar(cliente, atualizacao);
		repositorio.save(cliente);
		adicionadorLink.adicionarLink(cliente);
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirCliente(@PathVariable long id) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/endereco")
	public ResponseEntity<Endereco> obterEnderecoCliente(@PathVariable long id) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty() || opt.get().getEndereco() == null) {
			return ResponseEntity.notFound().build();
		}
		Endereco endereco = opt.get().getEndereco();
		if (adicionadorLinkEndereco != null) {
			adicionadorLinkEndereco.adicionarLink(endereco);
		}
		return ResponseEntity.ok(endereco);
	}

	@GetMapping("/{id}/documentos")
	public ResponseEntity<CollectionModel<Documento>> obterDocumentosCliente(@PathVariable long id) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<Documento> docs = opt.get().getDocumentos();
		if (adicionadorLinkDocumento != null) {
			adicionadorLinkDocumento.adicionarLink(docs);
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterDocumentosCliente(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(docs, linkProprio));
	}

	@GetMapping("/{id}/telefones")
	public ResponseEntity<CollectionModel<Telefone>> obterTelefonesCliente(@PathVariable long id) {
		Optional<Cliente> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<Telefone> tels = opt.get().getTelefones();
		if (adicionadorLinkTelefone != null) {
			adicionadorLinkTelefone.adicionarLink(tels);
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterTelefonesCliente(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(tels, linkProprio));
	}
}
