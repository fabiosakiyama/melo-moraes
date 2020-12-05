package br.com.melomoraes.doador;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NovoDoadorRequest {
	
	@NotBlank
	private String nome;
	
	private String contato;
	
	@NotNull
	private NovoEnderecoRequest endereco;

	public Doador toModel() {
		return new Doador(nome, contato, endereco.toModel());
	}
}
