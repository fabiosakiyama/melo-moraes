package br.com.melomoraes.doacao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.melomoraes.doador.model.Doador;
import lombok.Getter;

@Entity
@Getter
public class Doacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Doador doador;
	
	@NotNull
	private TipoDoacaoEnum tipo;
	
	@Positive
	private BigDecimal valor;
	
	@NotNull
	private LocalDate data;
	
	@Deprecated
	public Doacao() {}

	public Doacao(Doador doador, TipoDoacaoEnum tipo, BigDecimal valor, LocalDate data) {
		this.doador = doador;
		this.tipo = tipo;
		this.valor = valor;
		this.data = data;
	}
}
