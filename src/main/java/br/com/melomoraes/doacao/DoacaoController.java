package br.com.melomoraes.doacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.melomoraes.client.GoogleClient;
import br.com.melomoraes.doador.dto.DoadorResponse;
import br.com.melomoraes.doador.model.Doador;
import br.com.melomoraes.doador.repository.DoadorRepository;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {
	
	private DoadorRepository doadorRepository;
	
	private GoogleClient client;
	
	public DoacaoController(DoadorRepository doadorRepository, GoogleClient client) {
		this.doadorRepository = doadorRepository;
		this.client = client;
	}

	@GetMapping("/semana")
	public ResponseEntity<DoadoresSemanaResponse> doadoresDaSemana(
			@RequestParam List<Integer> semanas, 
			@RequestParam(required = false) List<Integer> semanasPS, 
			@RequestParam List<String> bairros) {
		List<Doador> doadoresDaSemana = doadorRepository.findBySemanaInAndEndereco_BairroIn(semanas, bairros);
		DoadoresSemanaResponse doadoresResponse = new DoadoresSemanaResponse();
		List<String> waypointsId = new ArrayList<>();
		doadoresDaSemana.forEach(d -> waypointsId.add(d.getEndereco().getPlaceId()));
		Integer[] gerarRotaOtimizada = client.gerarRotaOtimizada(waypointsId);
		List<String> placesIdOrdenados = new ArrayList<>();
		for(Integer index : gerarRotaOtimizada) {
			String placeId = waypointsId.get(index);
			long count = doadoresDaSemana.stream().filter(d -> d.getEndereco().getPlaceId().equalsIgnoreCase(placeId)).count();
			Assert.isTrue(count == 1, "Erro filtrando um endereço pelo placeId " + placeId);
			Doador doador = doadoresDaSemana.stream()
					.filter(d -> d.getEndereco().getPlaceId().equalsIgnoreCase(placeId)).findFirst().get();
			doadoresResponse.getRota().add(new DoadorResponse(doador));
			placesIdOrdenados.add(doador.getEndereco().getPlaceId());
		}
		doadoresResponse.setGoogleMapsUrl(client.getMapsUrl(placesIdOrdenados));
		return ResponseEntity.ok(doadoresResponse);
	}
}
