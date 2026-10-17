package com.autobots.automanager.controles;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class TratadorExcecao {

	@ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class, EmptyResultDataAccessException.class})
	public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(Exception ex) {
		Map<String, Object> corpo = new HashMap<>();
		corpo.put("status", HttpStatus.NOT_FOUND.value());
		corpo.put("erro", "Recurso não encontrado");
		corpo.put("mensagem", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
	public ResponseEntity<Map<String, Object>> tratarRequisicaoInvalida(Exception ex) {
		Map<String, Object> corpo = new HashMap<>();
		corpo.put("status", HttpStatus.BAD_REQUEST.value());
		corpo.put("erro", "Requisição inválida");
		corpo.put("mensagem", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, Object>> tratarResponseStatus(ResponseStatusException ex) {
		Map<String, Object> corpo = new HashMap<>();
		corpo.put("status", ex.getStatus().value());
		corpo.put("erro", ex.getReason());
		corpo.put("mensagem", ex.getMessage());
		return ResponseEntity.status(ex.getStatus()).body(corpo);
	}
}
