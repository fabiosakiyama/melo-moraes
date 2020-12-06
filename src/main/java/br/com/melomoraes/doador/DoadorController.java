package br.com.melomoraes.doador;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.melomoraes.client.GoogleClient;

@RestController
@RequestMapping("/doadores")
public class DoadorController {
	
	private DoadorRepository repository;
	
	private GoogleClient googleClient;
	
	public DoadorController(DoadorRepository repository, GoogleClient googleClient) {
		this.repository = repository;
		this.googleClient = googleClient;
	}

	@PostMapping
	public void criaDoador(@Valid @RequestBody NovoDoadorRequest request) {
		String placeId = googleClient.buscaPlaceIdDeUmEndereco(request.getEndereco().toGoogleString());
		repository.save(request.toModel(placeId));
	}
	
	@GetMapping
	public ResponseEntity<List<DoadorResponse>> listaDoadores(
			@RequestParam(required = false) String nome, 
			@RequestParam(required = false) String bairro, 
			Pageable pageable) {
		Endereco endereco = new Endereco(null, bairro, null, null);
		Doador doador = new Doador(nome, null, endereco);                         
		Example<Doador> example = Example.of(doador);
		Page<Doador> page = repository.findAll(example, pageable);
		List<Doador> doadores = page.getContent();
		List<DoadorResponse> doadoresResponse = new ArrayList<>();
		doadores.forEach(d -> doadoresResponse.add(new DoadorResponse(d)));
		return ResponseEntity.ok(doadoresResponse);
	}
}
