package br.com.melomoraes.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.melomoraes.client.dto.direction.GoogleDirectionResponse;
import br.com.melomoraes.client.dto.geocode.GoogleGeocodeResponse;

@Service
public class GoogleClient {
	
	private String googleApiKey;
	
	private String googleGeocodeApiUrl;
	
	private String googleDirectionApiUrl;
	
	private String googleMapsUrl;
	
	private String origin;
	
	private String destination;
	
	private RestTemplate template;
	
	public GoogleClient(
			@Value("${google.key}") String googleApiKey, 
			@Value("${google.geocode-url}") String googleGeocodeApiUrl,
			@Value("${google.direction-url}") String googleDirectionApiUrl,
			@Value("${google.maps-url}") String googleMapsUrl,
			@Value("${google.origin}") String origin,
			@Value("${google.destination}") String destination) {
		this.googleApiKey = googleApiKey;
		this.googleGeocodeApiUrl = googleGeocodeApiUrl;
		this.googleDirectionApiUrl = googleDirectionApiUrl;
		this.googleMapsUrl = googleMapsUrl;
		this.origin = origin;
		this.destination = destination;
		this.template = new RestTemplate();
	}

	public String buscaPlaceIdDeUmEndereco(String endereco) {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(googleGeocodeApiUrl)
		        .queryParam("address", endereco)
		        .queryParam("key", googleApiKey)
		        .build();
		
		ResponseEntity<GoogleGeocodeResponse> response = template.getForEntity(builder.toString(), GoogleGeocodeResponse.class);
		Assert.notNull(response.getBody(), "Nenhum resultado encontrado para o endereco " + endereco);
		Assert.notEmpty(response.getBody().getResults(), "Nenhum resultado encontrado para o endereco " + endereco);
		Assert.isTrue(response.getBody().getResults().size() == 1, "Mais de um resultado encontrado para o endereco " + endereco + ", tente informar um endereço mais específico" );
		
		return response.getBody().getResults().get(0).getPlace_id();
	}
	
	public Integer[] gerarRotaOtimizada(List<String> waypointsId) {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(googleDirectionApiUrl)
		        .queryParam("origin", "place_id:" + origin)
		        .queryParam("destination", "place_id:" + destination)
		        .queryParam("key", googleApiKey)
		        .queryParam("waypoints", formatWaypoints(waypointsId))
		        .build();
		
		ResponseEntity<GoogleDirectionResponse> response = template.getForEntity(builder.toString(), GoogleDirectionResponse.class);
		Assert.notNull(response.getBody(), "Nenhum resultado encontrado para a rota " + waypointsId);
		Assert.notEmpty(response.getBody().getRoutes(), "Nenhum resultado encontrado para a rota " + waypointsId);
		Assert.isTrue(response.getBody().getRoutes().size() == 1, "Mais de um resultado encontrado para a rota " + waypointsId);
		
		return response.getBody().getRoutes().get(0).getWaypoint_order();
	}
	
	private String formatWaypoints(List<String> waypointsId) {
		StringBuilder sb = new StringBuilder();
		sb.append("optimize:true|place_id:");
		sb.append(String.join("|place_id:", waypointsId));
		return sb.toString();
	}

	public String getMapsUrl(List<String> placesIdOrdenados) {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(googleMapsUrl)
		        .queryParam("api", 1)
		        .queryParam("origin", "Pindamonhangaba")
		        .queryParam("origin_place_id", origin)
		        .queryParam("destination", "Pindamonhangaba")
		        .queryParam("destination_place_id", destination)
		        .queryParam("waypoints", generateWaypoint(placesIdOrdenados.size()))
		        .queryParam("waypoint_place_ids", formatMapsWaypoints(placesIdOrdenados))
		        .build();
		return builder.toString();
	}
	
	public String getSplitMapsUrl(List<String> placesIdOrdenados, String midOrigin, String midDestination) {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(googleMapsUrl)
		        .queryParam("api", 1)
		        .queryParam("origin", "Pindamonhangaba")
		        .queryParam("origin_place_id", (midOrigin == null ? origin : midOrigin))
		        .queryParam("destination", "Pindamonhangaba")
		        .queryParam("destination_place_id", (midDestination == null ? destination : midDestination))
		        .queryParam("waypoints", generateWaypoint(placesIdOrdenados.size()))
		        .queryParam("waypoint_place_ids", formatMapsWaypoints(placesIdOrdenados))
		        .build();
		return builder.toString();
	}

	private String formatMapsWaypoints(List<String> placesIdOrdenados) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.join("|", placesIdOrdenados));
		return sb.toString();
	}

	private String generateWaypoint(int size) {
		StringBuilder sb = new StringBuilder();
		sb.append("Pindamonhangaba|");
		for(int i = 0; i < size; i++) {
			if(i + 1 == size) {
				sb.append("Pindamonhangaba");
			} else {
				sb.append("Pindamonhangaba|");
			}
		}
		return sb.toString();
	}

}
