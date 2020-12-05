package br.com.melomoraes.doador;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Embeddable
@Getter
public class Endereco {
	
	@NotBlank
	private String rua;
	
	@NotBlank
	private String bairro;
	
	@NotBlank
	private String numero;
	
	@Deprecated
	public Endereco() {}

	public Endereco(String rua, String bairro, String numero) {
		this.rua = rua;
		this.bairro = bairro;
		this.numero = numero;
	}
}
