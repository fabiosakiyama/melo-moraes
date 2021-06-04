package br.com.melomoraes.doador.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.melomoraes.client.GoogleClient;
import br.com.melomoraes.doador.dto.DoadorResponse;
import br.com.melomoraes.doador.dto.EditaDoadorRequest;
import br.com.melomoraes.doador.dto.NovoDoadorRequest;
import br.com.melomoraes.doador.model.Doador;
import br.com.melomoraes.doador.model.Endereco;
import br.com.melomoraes.doador.repository.DoadorRepository;
import br.com.melomoraes.doador.service.ImportadorDeDoadoresPlanilhaService;

@RestController
@RequestMapping("/doadores")
public class DoadorController {
	
	private DoadorRepository repository;
	
	private ImportadorDeDoadoresPlanilhaService importadorDeDoadoresPlanilhaService;
	
	private GoogleClient googleClient;
	
	public DoadorController(DoadorRepository repository, GoogleClient googleClient, ImportadorDeDoadoresPlanilhaService importadorDeDoadoresPlanilhaService) {
		this.repository = repository;
		this.googleClient = googleClient;
		this.importadorDeDoadoresPlanilhaService = importadorDeDoadoresPlanilhaService;
	}

	@PostMapping
	public void criaDoador(@Valid @RequestBody NovoDoadorRequest request) {
		String placeId = googleClient.buscaPlaceIdDeUmEndereco(request.getEndereco().toGoogleString());
		Assert.hasLength(placeId, "Não foi possível encontrar um placeId para o endereco " + request.getEndereco().toGoogleString());
		repository.save(request.toModel(placeId));
	}
	
	@PutMapping("/{id}")
	public void editaDoador(@PathVariable Long id, @Valid @RequestBody EditaDoadorRequest request) {
		Optional<Doador> possivelDoador = repository.findById(id);
		if(!possivelDoador.isPresent()) throw new IllegalArgumentException("Nenhum doador encontrado com o id " + id);
		Doador doador = possivelDoador.get();
		String novoPlaceId = googleClient.buscaPlaceIdDeUmEndereco(request.getEndereco().toGoogleString());
		Assert.hasLength(novoPlaceId, "Não foi possível encontrar um placeId para o endereco " + request.getEndereco().toGoogleString());
		repository.save(request.toModel(id, doador, novoPlaceId));
	}
	
	@DeleteMapping("/{id}")
	public void deletaDoador(@PathVariable Long id) {
		Optional<Doador> possivelDoador = repository.findById(id);
		if(!possivelDoador.isPresent()) throw new IllegalArgumentException("Nenhum doador encontrado com o id " + id);
		repository.deleteById(id);
	}
	
	@GetMapping
	public ResponseEntity<List<DoadorResponse>> listaDoadores(
			@RequestParam(required = false) String nome, 
			@RequestParam(required = false) String bairro, 
			@RequestParam(required = false) Integer semana,
			@RequestParam(required = false, defaultValue = "true") boolean ativo) {
		Endereco endereco = new Endereco(null, bairro, null, null);
		Doador doador = new Doador(nome, null, null, semana, endereco, ativo);                         
		Example<Doador> example = Example.of(doador);
		List<Doador> doadores = repository.findAll(example);
		List<DoadorResponse> doadoresResponse = new ArrayList<>();
		doadores.forEach(d -> doadoresResponse.add(new DoadorResponse(d)));
		return ResponseEntity.ok(doadoresResponse);
	}
	
	@PostMapping("/planilha")
	public ResponseEntity<String> importaDoadoresViaPlanilha(@RequestParam MultipartFile file) throws IOException {
		String[] splittedFileName = file.getOriginalFilename().split("\\.");
		if(!splittedFileName[splittedFileName.length - 1].equalsIgnoreCase("xlsx")) {
			return ResponseEntity.badRequest().body("Somente arquivos .xlsx podem ser importados");
		}
		InputStream inputStream =  new BufferedInputStream(file.getInputStream());
		long doadoresSalvos = importadorDeDoadoresPlanilhaService.importaDoadoresViaPlanilha(inputStream);
		return ResponseEntity.ok("Foram salvos " + doadoresSalvos + " doadores");
	}
}
