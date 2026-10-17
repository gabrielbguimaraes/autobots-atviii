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

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documentos")
public class DocumentoControle {

	@Autowired
	private DocumentoRepositorio repositorio;

	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping
	public ResponseEntity<CollectionModel<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		adicionadorLink.adicionarLink(documentos);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(documentos, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		Optional<Documento> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Documento documento = opt.get();
		adicionadorLink.adicionarLink(documento);
		return ResponseEntity.ok(documento);
	}

	@PostMapping
	public ResponseEntity<Documento> cadastrarDocumento(@RequestBody Documento documento) {
		documento.setId(null);
		Documento novoDocumento = repositorio.save(documento);
		adicionadorLink.adicionarLink(novoDocumento);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoDocumento.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novoDocumento);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Documento> atualizarDocumento(@PathVariable long id, @RequestBody Documento atualizacao) {
		Optional<Documento> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Documento documento = opt.get();
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, atualizacao);
		repositorio.save(documento);
		adicionadorLink.adicionarLink(documento);
		return ResponseEntity.ok(documento);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirDocumento(@PathVariable long id) {
		Optional<Documento> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
