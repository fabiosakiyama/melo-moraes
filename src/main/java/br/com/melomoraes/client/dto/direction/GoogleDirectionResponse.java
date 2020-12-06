package br.com.melomoraes.client.dto.direction;

import java.util.List;

import lombok.Data;

@Data
public class GoogleDirectionResponse {
	
	public List<Routes> routes;

}
