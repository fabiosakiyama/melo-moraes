package br.com.melomoraes.doador;

import lombok.Data;

@Data
public class DoadorResponse {
	
	private String nome;
	
	private String contato;
	
	private EnderecoResponse endereco;

	public DoadorResponse(Doador doador) {
		this.nome = doador.getNome();
		this.contato = doador.getContato();
		this.endereco = new EnderecoResponse(doador.getEndereco());
	}
}
