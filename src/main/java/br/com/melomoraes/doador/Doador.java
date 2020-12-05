package br.com.melomoraes.doador;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Entity
@Getter
public class Doador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String contato;
	
	@Embedded
	private Endereco endereco;
	
	@Deprecated
	public Doador() {}

	public Doador(String nome, String contato, Endereco endereco) {
		this.nome = nome;
		this.contato = contato;
		this.endereco = endereco;
	}
	
	public Doador(String nome, Endereco endereco) {
		this.nome = nome;
		this.endereco = endereco;
	}
}