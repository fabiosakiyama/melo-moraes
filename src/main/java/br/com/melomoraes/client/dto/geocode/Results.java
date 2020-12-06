package br.com.melomoraes.client.dto.geocode;

import lombok.Data;

@Data
public class Results {
	
	private Geometry geometry;
	
	private String place_id;

}
