package br.com.melomoraes.client.dto;

import java.util.List;

import lombok.Data;

@Data
public class GoogleGeocodeResponse {
	
	private List<Results> results;
}
