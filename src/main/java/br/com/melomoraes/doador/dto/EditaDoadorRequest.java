package br.com.melomoraes.doador.dto;

import org.springframework.util.Assert;

import br.com.melomoraes.doador.model.Doador;

public class EditaDoadorRequest extends NovoDoadorRequest{
	
	public Doador toModel(Long id, Doador doador, String placeId) {
		Assert.notNull(placeId, "PlaceId não pode ser nulo");
		return new Doador(id, getNome(), getContato(), getQuantidade(), getSemana(), getEndereco().toModel(placeId), getAtivo());
	}
}
