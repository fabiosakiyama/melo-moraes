package br.com.melomoraes.doacao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.melomoraes.doacao.dto.DoacaoResponse;
import br.com.melomoraes.doacao.dto.NovaDoacaoRequest;
import br.com.melomoraes.doacao.model.Doacao;
import br.com.melomoraes.doacao.repository.DoacaoRepository;
import br.com.melomoraes.doador.model.Doador;
import br.com.melomoraes.doador.repository.DoadorRepository;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {
	
	private DoadorRepository doadorRepository;
	
	private DoacaoRepository doacaoRepository;
	
	public DoacaoController(DoadorRepository doadorRepository, DoacaoRepository doacaoRepository) {
		this.doadorRepository = doadorRepository;
		this.doacaoRepository = doacaoRepository;
	}
	
	@PostMapping
	public void salvaDoacao(@RequestBody NovaDoacaoRequest request) {
		Optional<Doador> possivelDoador = doadorRepository.findById(request.getDoadorId());
		if(!possivelDoador.isPresent()) throw new IllegalArgumentException("Nenhum doador encontrado com o id " + request.getDoadorId());
		Doador doador = possivelDoador.get();
		doacaoRepository.save(request.toModel(doador));
	}
	
	@GetMapping
	public ResponseEntity<?> listaDoacoes() {
		List<Doacao> doacoes = doacaoRepository.findAll();
		List<DoacaoResponse> response = new ArrayList<>();
		doacoes.forEach(d -> response.add(new DoacaoResponse(d)));
		return ResponseEntity.ok(response);
	}
}
