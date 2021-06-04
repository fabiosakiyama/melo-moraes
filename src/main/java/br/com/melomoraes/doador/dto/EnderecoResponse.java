package br.com.melomoraes.doador.dto;

import br.com.melomoraes.doador.model.Endereco;
import lombok.Data;

@Data
public class EnderecoResponse {
	
	private String rua;
	
	private String bairro;
	
	private String numero;
	
	private String complemento;
	
	private String obs;
	
	private String area;

	public EnderecoResponse(Endereco endereco) {
		this.rua = endereco.getRua();
		this.bairro = endereco.getBairro();
		this.numero = endereco.getNumero();
		this.complemento = endereco.getComplemento();
		this.obs = endereco.getObs();
		this.area = endereco.getArea();
	}
}
