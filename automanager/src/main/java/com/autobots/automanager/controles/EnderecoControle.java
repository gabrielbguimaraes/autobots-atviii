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

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/enderecos")
public class EnderecoControle {

	@Autowired
	private EnderecoRepositorio repositorio;

	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;

	@GetMapping
	public ResponseEntity<CollectionModel<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		adicionadorLink.adicionarLink(enderecos);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(enderecos, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		Optional<Endereco> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Endereco endereco = opt.get();
		adicionadorLink.adicionarLink(endereco);
		return ResponseEntity.ok(endereco);
	}

	@PostMapping
	public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody Endereco endereco) {
		endereco.setId(null);
		Endereco novoEndereco = repositorio.save(endereco);
		adicionadorLink.adicionarLink(novoEndereco);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoEndereco.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novoEndereco);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Endereco> atualizarEndereco(@PathVariable long id, @RequestBody Endereco atualizacao) {
		Optional<Endereco> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Endereco endereco = opt.get();
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(endereco, atualizacao);
		repositorio.save(endereco);
		adicionadorLink.adicionarLink(endereco);
		return ResponseEntity.ok(endereco);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirEndereco(@PathVariable long id) {
		Optional<Endereco> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
