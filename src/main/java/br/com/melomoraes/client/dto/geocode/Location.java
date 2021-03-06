package br.com.melomoraes.client.dto.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Location {
	
	@JsonProperty("lat")
	private double latitude;

	@JsonProperty("lng")
	private double longitude;
}
