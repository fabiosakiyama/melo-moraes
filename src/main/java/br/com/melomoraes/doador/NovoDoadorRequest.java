package br.com.melomoraes.doador;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import lombok.Data;

@Data
public class NovoDoadorRequest {
	
	@NotBlank
	private String nome;
	
	private String contato;
	
	@NotNull
	private NovoEnderecoRequest endereco;

	public Doador toModel(String placeId) {
		Assert.notNull(placeId, "PlaceId não pode ser nulo");
		return new Doador(nome, contato, endereco.toModel(placeId));
	}
}
