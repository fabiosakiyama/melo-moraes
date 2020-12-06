package br.com.melomoraes.doador.dto;

import br.com.melomoraes.doador.model.Doador;
import lombok.Data;

@Data
public class DoadorResponse {
	
	private String nome;
	
	private String contato;
	
	private double quantidade;
	
	private int semana;
	
	private EnderecoResponse endereco;

	public DoadorResponse(Doador doador) {
		this.nome = doador.getNome();
		this.contato = doador.getContato();
		this.quantidade = doador.getQuantidade();
		this.semana = doador.getSemana();
		this.endereco = new EnderecoResponse(doador.getEndereco());
	}
}
