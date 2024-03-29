package br.com.melomoraes.doacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.melomoraes.client.GoogleClient;
import br.com.melomoraes.doador.dto.DoadorResponse;
import br.com.melomoraes.doador.model.Doador;
import br.com.melomoraes.doador.model.Endereco;
import br.com.melomoraes.doador.repository.DoadorRepository;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {
	
	private DoadorRepository doadorRepository;
	
	private GoogleClient client;
	
	private static Logger LOGGER = LoggerFactory.getLogger(DoacaoController.class);
	
	public DoacaoController(DoadorRepository doadorRepository, GoogleClient client) {
		this.doadorRepository = doadorRepository;
		this.client = client;
	}
	
	@GetMapping("/semana")
	public ResponseEntity<?> doadoresDaSemana(
			@RequestParam List<Integer> semanas, 
			@RequestParam List<String> areas) {
		List<Doador> doadoresDaSemana = doadorRepository.findByAtivoAndSemanaInAndEndereco_AreaIn(true, semanas, areas);
		if(doadoresDaSemana.size() > 25) {
		//	return ResponseEntity.badRequest()
		//			.body("Foram encontrados mais de 25 doadores com esses filtros, infelizmente a API do Google tem um limite de 25 rotas");
		}
		List<DoadorResponse> response = new ArrayList<>();
		doadoresDaSemana.forEach(d -> response.add(new DoadorResponse(d)));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/gera-rota")
	public ResponseEntity<?> geraRota(@RequestParam List<Long> doadoresIds) {
		if(doadoresIds.size() > 25) {
			return ResponseEntity.badRequest()
					.body("Mais de 25 ids foram informados, infelizmente a API do Google tem um limite de 25 rotas");
		}
		List<Doador> doadoresDaSemana = doadorRepository.findAllById(doadoresIds);
		DoadoresSemanaResponse doadoresResponse = new DoadoresSemanaResponse();
		List<String> waypointsId = new ArrayList<>();
		doadoresDaSemana.forEach(d -> waypointsId.add(d.getEndereco().getPlaceId()));
		Integer[] gerarRotaOtimizada = client.gerarRotaOtimizada(waypointsId);
		List<String> placesIdOrdenados = new ArrayList<>();
		for(Integer index : gerarRotaOtimizada) {
			String placeId = waypointsId.get(index);
			Doador doador = doadoresDaSemana.stream()
					.filter(d -> d.getEndereco().getPlaceId().equalsIgnoreCase(placeId)).findFirst().get();
			doadoresResponse.getRota().add(new DoadorResponse(doador));
			placesIdOrdenados.add(doador.getEndereco().getPlaceId());
		}
		if(placesIdOrdenados.size() > 9) {
			int count = 0;
			List<String> splitPlacesIdOrdenados = new ArrayList<>();
			String primeiroEnd = null;
			String ultimoEnd = null;
			for(String place : placesIdOrdenados) {
				splitPlacesIdOrdenados.add(place);
				count++;
				if(count >= 8) {
					if(doadoresResponse.getGoogleMapsUrls().isEmpty()) {
						ultimoEnd = splitPlacesIdOrdenados.get(splitPlacesIdOrdenados.size() - 1);
						splitPlacesIdOrdenados.remove(splitPlacesIdOrdenados.size() - 1);
						doadoresResponse.getGoogleMapsUrls().add(client.getSplitMapsUrl(splitPlacesIdOrdenados, null, ultimoEnd));
						primeiroEnd = ultimoEnd;
					} else {
						if(placesIdOrdenados.get(placesIdOrdenados.size() - 1) == place) {
							doadoresResponse.getGoogleMapsUrls().add(client.getSplitMapsUrl(splitPlacesIdOrdenados, primeiroEnd, null));
						} else {
							ultimoEnd = splitPlacesIdOrdenados.get(splitPlacesIdOrdenados.size() - 1);
							splitPlacesIdOrdenados.remove(splitPlacesIdOrdenados.size() - 1);
							doadoresResponse.getGoogleMapsUrls().add(client.getSplitMapsUrl(splitPlacesIdOrdenados, primeiroEnd, ultimoEnd));
							primeiroEnd = ultimoEnd;
						}
						
					}
					count = 0;
					splitPlacesIdOrdenados.clear();
				}
			}
			if(count > 0) {
				doadoresResponse.getGoogleMapsUrls().add(client.getSplitMapsUrl(splitPlacesIdOrdenados, primeiroEnd, null));
			}
		} else {
			doadoresResponse.getGoogleMapsUrls().add(client.getMapsUrl(placesIdOrdenados));
		}
		doadoresResponse.setNumeroDoadores(doadoresDaSemana.size());
		if(!CollectionUtils.isEmpty(doadoresDaSemana)) {
			try {
				Set<Integer> semanas = doadoresDaSemana.stream().map(d -> d.getSemana()).collect(Collectors.toSet());
				Endereco endereco = doadoresDaSemana.get(0).getEndereco();
				LOGGER.info("Rota gerada para bairro " + endereco.getArea() + ", total de " + doadoresDaSemana.size() + " doadores para as semanas " + semanas);
			} catch (Exception e) {
				// nothing
			}
		}
		return ResponseEntity.ok(doadoresResponse);
	}
}
