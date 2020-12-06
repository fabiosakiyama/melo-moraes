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
	
	public Endereco toModel(String placeId) {
		return new Endereco(rua, bairro, numero, placeId);
	}
	
	public String toGoogleString() {
		return this.rua + ", " + this.numero + ", " + this.bairro + ", Pindamonhangaba";
	}

	
}
