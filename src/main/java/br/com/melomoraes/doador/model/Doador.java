package br.com.melomoraes.doador.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

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

	@Positive
	private Double quantidade;

	@Max(4)
	@Min(1)
	private Integer semana;

	@Embedded
	private Endereco endereco;

	private boolean ativo = true;

	@Deprecated
	public Doador() {
	}

	public Doador(String nome, String contato, Double quantidade, Integer semana, Endereco endereco, boolean ativo) {
		this.nome = nome;
		this.contato = contato;
		this.quantidade = quantidade;
		this.semana = semana;
		this.ativo = ativo;
		this.endereco = endereco;
	}

	public Doador(Long id, String nome, String contato, Double quantidade, Integer semana, Endereco endereco,
			boolean ativo) {
		this(nome, contato, quantidade, semana, endereco, ativo);
		this.id = id;
	}
}