package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

	@Autowired(required = false)
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired(required = false)
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Autowired(required = false)
	private AdicionadorLinkDocumento adicionadorLinkDocumento;

	@Override
	public void adicionarLink(List<Cliente> lista) {
		for (Cliente cliente : lista) {
			long id = cliente.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ClienteControle.class)
							.obterCliente(id))
					.withSelfRel();
			cliente.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Cliente objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterCliente(objeto.getId()))
				.withSelfRel();
		Link linkColecao = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterClientes())
				.withRel("clientes");
		objeto.add(linkProprio, linkColecao);

		Link linkSubEndereco = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterEnderecoCliente(objeto.getId()))
				.withRel("cliente_endereco");
		Link linkSubDocumentos = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterDocumentosCliente(objeto.getId()))
				.withRel("cliente_documentos");
		Link linkSubTelefones = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterTelefonesCliente(objeto.getId()))
				.withRel("cliente_telefones");
		objeto.add(linkSubEndereco, linkSubDocumentos, linkSubTelefones);

		if (objeto.getEndereco() != null) {
			if (objeto.getEndereco().getId() != null) {
				Link linkEndereco = WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder
								.methodOn(EnderecoControle.class)
								.obterEndereco(objeto.getEndereco().getId()))
						.withRel("endereco");
				objeto.add(linkEndereco);
			}
			if (adicionadorLinkEndereco != null) {
				adicionadorLinkEndereco.adicionarLink(objeto.getEndereco());
			}
		}

		if (objeto.getDocumentos() != null && !objeto.getDocumentos().isEmpty()) {
			Link linkDocs = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(DocumentoControle.class)
							.obterDocumentos())
					.withRel("todos_documentos");
			objeto.add(linkDocs);
			if (adicionadorLinkDocumento != null) {
				for (Documento doc : objeto.getDocumentos()) {
					if (doc.getId() != null) {
						adicionadorLinkDocumento.adicionarLink(doc);
					}
				}
			}
		}

		if (objeto.getTelefones() != null && !objeto.getTelefones().isEmpty()) {
			Link linkTels = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(TelefoneControle.class)
							.obterTelefones())
					.withRel("todos_telefones");
			objeto.add(linkTels);
			if (adicionadorLinkTelefone != null) {
				for (Telefone tel : objeto.getTelefones()) {
					if (tel.getId() != null) {
						adicionadorLinkTelefone.adicionarLink(tel);
					}
				}
			}
		}
	}
}