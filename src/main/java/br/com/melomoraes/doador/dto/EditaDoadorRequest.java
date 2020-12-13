package br.com.melomoraes.doador.dto;

import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import br.com.melomoraes.doador.model.Doador;

public class EditaDoadorRequest extends NovoDoadorRequest{
	
	@NotNull
	private Long id;

	public Doador toModel(Doador doador, String placeId) {
		Assert.notNull(placeId, "PlaceId não pode ser nulo");
		return new Doador(getNome(), getContato(), getQuantidade(), getSemana(), getEndereco().toModel(placeId));
	}

	public Long getId() {
		return id;
	}
}
