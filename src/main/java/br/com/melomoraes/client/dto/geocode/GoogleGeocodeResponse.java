package br.com.melomoraes.client.dto.geocode;

import java.util.List;

import lombok.Data;

@Data
public class GoogleGeocodeResponse {
	
	private List<Results> results;
}
