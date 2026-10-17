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

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefones")
public class TelefoneControle {

	@Autowired
	private TelefoneRepositorio repositorio;

	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

	@GetMapping
	public ResponseEntity<CollectionModel<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		adicionadorLink.adicionarLink(telefones);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(telefones, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		Optional<Telefone> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Telefone telefone = opt.get();
		adicionadorLink.adicionarLink(telefone);
		return ResponseEntity.ok(telefone);
	}

	@PostMapping
	public ResponseEntity<Telefone> cadastrarTelefone(@RequestBody Telefone telefone) {
		telefone.setId(null);
		Telefone novoTelefone = repositorio.save(telefone);
		adicionadorLink.adicionarLink(novoTelefone);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoTelefone.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novoTelefone);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Telefone> atualizarTelefone(@PathVariable long id, @RequestBody Telefone atualizacao) {
		Optional<Telefone> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Telefone telefone = opt.get();
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);
		repositorio.save(telefone);
		adicionadorLink.adicionarLink(telefone);
		return ResponseEntity.ok(telefone);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirTelefone(@PathVariable long id) {
		Optional<Telefone> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
