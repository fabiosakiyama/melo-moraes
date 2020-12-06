package br.com.melomoraes.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.melomoraes.client.dto.GoogleGeocodeResponse;

@Service
public class GoogleClient {
	
	private String googleApiKey;
	
	private String googleGeocodeApiUrl;
	
	private RestTemplate template;
	
	public GoogleClient(@Value("${google.key}") String googleApiKey, @Value("${google.geocode-url}") String googleGeocodeApiUrl) {
		this.googleApiKey = googleApiKey;
		this.googleGeocodeApiUrl = googleGeocodeApiUrl;
		this.template = new RestTemplate();
	}

	public String buscaPlaceIdDeUmEndereco(String endereco) {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(googleGeocodeApiUrl)
		        .queryParam("address", endereco)
		        .queryParam("key", googleApiKey)
		        .build();
		
		ResponseEntity<GoogleGeocodeResponse> response = template.getForEntity(builder.toString(), GoogleGeocodeResponse.class);
		
		return response.getBody().getResults().get(0).getPlace_id();
	}

}