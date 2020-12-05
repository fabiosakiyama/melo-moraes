package br.com.melomoraes.doador;

import lombok.Data;

@Data
public class EnderecoResponse {
	
	private String rua;
	
	private String bairro;
	
	private String numero;

	public EnderecoResponse(Endereco endereco) {
		this.rua = endereco.getRua();
		this.bairro = endereco.getBairro();
		this.numero = endereco.getNumero();
	}
}
