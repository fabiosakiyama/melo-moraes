package br.com.melomoraes.doador;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class NovoEnderecoRequest {
	
	@NotBlank
	private String rua;
	
	@NotBlank
	private String bairro;
	
	@NotBlank
	private String numero;

	public Endereco toModel() {
		return new Endereco(rua, bairro, numero);
	}

}
