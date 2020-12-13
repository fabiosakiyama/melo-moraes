package br.com.melomoraes.doador.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Embeddable
@Getter
public class Endereco {
	
	@NotBlank
	@Column(nullable = false)
	private String rua;
	
	@NotBlank
	@Column(nullable = false)
	private String bairro;
	
	@NotBlank
	@Column(nullable = false)
	private String numero;
	
	private String complemento;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String placeId;
	
	private String obs;
	
	@Deprecated
	public Endereco() {}

	public Endereco(String rua, String bairro, String numero, String placeId) {
		this(rua, bairro, numero, placeId, null, null);
	}
	
	public Endereco(String rua, String bairro, String numero, String placeId, String complemento, String obs) {
		this.rua = rua;
		this.bairro = bairro;
		this.numero = numero;
		this.placeId = placeId;
		this.complemento = complemento;
		this.obs = obs;
	}
}
