package br.com.workingsafe.repository;

import br.com.workingsafe.model.RecomendacaoIa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecomendacaoIaRepository extends JpaRepository<RecomendacaoIa, Long> {

    // lista recomendações de um usuário com paginação
    Page<RecomendacaoIa> findByUsuarioId(Long usuarioId, Pageable pageable);

    // pega a recomendação mais recente de um usuário
    Optional<RecomendacaoIa> findTopByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);
}
