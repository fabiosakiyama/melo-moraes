package br.com.melomoraes.doacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.melomoraes.doacao.model.Doacao;
import br.com.melomoraes.doacao.model.TipoDoacaoEnum;
import br.com.melomoraes.doador.dto.DoadorResponse;
import lombok.Data;

@Data
public class DoacaoResponse {
	
	private Long id;
	
	private DoadorResponse doador;
	
	private TipoDoacaoEnum tipo;
	
	private BigDecimal valor;
	
	private LocalDate data;

	public DoacaoResponse(Doacao doacao) {
		this.id = doacao.getId();
		this.doador = new DoadorResponse(doacao.getDoador());
		this.tipo = doacao.getTipo();
		this.valor = doacao.getValor();
		this.data = doacao.getData();
	}

}
