package br.com.workingsafe.controller;

import br.com.workingsafe.dto.RecomendacaoIaDto;
import br.com.workingsafe.service.RecomendacaoIaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoIaController {

    private final RecomendacaoIaService recomendacaoIaService;

    public RecomendacaoIaController(RecomendacaoIaService recomendacaoIaService) {
        this.recomendacaoIaService = recomendacaoIaService;
    }

    /**
     * Lista recomendações.
     *
     * - Se vier usuarioId -> lista só desse usuário (paginado)
     * - Se não vier -> lista geral (paginado) — útil para dashboards/admin
     *
     * Exemplo:
     *   GET /api/recomendacoes?usuarioId=2&page=0&size=10
     *   GET /api/recomendacoes?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Page<RecomendacaoIaDto>> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dtCriacao"));

        Page<RecomendacaoIaDto> resultado;

        if (usuarioId != null) {
            resultado = recomendacaoIaService.listarPorUsuario(usuarioId, pageable);
        } else {
            resultado = recomendacaoIaService.listarTodas(pageable);
        }

        return ResponseEntity.ok(resultado);
    }

    /**
     * Busca uma recomendação específica pelo ID.
     *
     * GET /api/recomendacoes/15
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecomendacaoIaDto> buscarPorId(@PathVariable Long id) {
        RecomendacaoIaDto dto = recomendacaoIaService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Gera novas recomendações usando IA (Ollama) para um usuário específico.
     * Esse endpoint é "manual" — você pode chamar no Postman ou até
     * de um botão no front depois, se quiser.
     *
     * POST /api/recomendacoes/gerar/2
     */
    @PostMapping("/gerar/{usuarioId}")
    public ResponseEntity<List<RecomendacaoIaDto>> gerarParaUsuario(@PathVariable Long usuarioId) {

        // Usa exatamente o mesmo método que o CheckinService já está chamando:
        // recomendacaoIaService.gerarRecomendacoesGenericas(usuarioId)
        List<RecomendacaoIaDto> geradas =
                recomendacaoIaService.gerarRecomendacoesGenericas(usuarioId);

        return ResponseEntity.ok(geradas);
    }

    /**
     * Remove uma recomendação específica (admin/gestor).
     *
     * DELETE /api/recomendacoes/10
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        recomendacaoIaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
