package br.com.melomoraes.doacao;

import java.util.ArrayList;
import java.util.List;

import br.com.melomoraes.doador.dto.DoadorResponse;
import lombok.Data;

@Data
public class DoadoresSemanaResponse {
	
	public List<DoadorResponse> rota = new ArrayList<>();
	
	public String googleMapsUrl;
	
}
