package br.com.melomoraes.doacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.melomoraes.doacao.model.Doacao;
import br.com.melomoraes.doacao.model.TipoDoacaoEnum;
import br.com.melomoraes.doador.model.Doador;
import lombok.Data;

@Data
public class NovaDoacaoRequest {
	
	@NotNull
	private Long doadorId;
	
	@NotNull
	private TipoDoacaoEnum tipo;
	
	@NotNull
	@Positive
	private BigDecimal valor;
	
	@NotNull
	private LocalDate data;

	public Doacao toModel(Doador doador) {
		return new Doacao(doador, tipo, valor, data);
	}

}
