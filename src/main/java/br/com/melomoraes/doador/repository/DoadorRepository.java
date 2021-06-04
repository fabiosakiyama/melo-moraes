package br.com.melomoraes.doador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.melomoraes.doador.model.Doador;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Long> {

	List<Doador> findByAtivoAndSemanaInAndEndereco_AreaIn(boolean ativo, List<Integer> semanas, List<String> bairros);

}
