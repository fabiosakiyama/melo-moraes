package br.com.melomoraes.doador.dto;

import javax.validation.constraints.NotBlank;

import br.com.melomoraes.doador.model.Endereco;
import lombok.Data;

@Data
public class NovoEnderecoRequest {
	
	@NotBlank
	private String rua;
	
	@NotBlank
	private String bairro;
	
	@NotBlank
	private String numero;
	
	private String complemento;
	
	private String obs;
	
	public Endereco toModel(String placeId) {
		return new Endereco(rua, bairro, numero, placeId, complemento, obs);
	}
	
	public String toGoogleString() {
		return this.rua + ", " + this.numero + ", " + this.bairro + ", Pindamonhangaba";
	}
}
