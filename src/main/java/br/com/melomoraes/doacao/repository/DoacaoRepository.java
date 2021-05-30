package br.com.melomoraes.doacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.melomoraes.doacao.model.Doacao;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long>{

}
