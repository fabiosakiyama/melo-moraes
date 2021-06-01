package br.com.melomoraes.doador.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;

import br.com.melomoraes.doador.model.Doador;
import lombok.Data;

@Data
public class NovoDoadorRequest {
	
	@NotBlank
	private String nome;
	
	private String contato;
	
	@Positive
	private double quantidade;
	
	@Max(4)
	@Min(1)
	private int semana;
	
	private boolean ativo;
	
	@NotNull
	private NovoEnderecoRequest endereco;

	public Doador toModel(String placeId) {
		Assert.notNull(placeId, "PlaceId não pode ser nulo");
		return new Doador(nome, contato, quantidade, semana, endereco.toModel(placeId), ativo);
	}
}
