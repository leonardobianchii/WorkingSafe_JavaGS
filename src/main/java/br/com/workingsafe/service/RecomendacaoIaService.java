package br.com.workingsafe.service;

import br.com.workingsafe.dto.RecomendacaoIaDto;
import br.com.workingsafe.mapper.RecomendacaoIaMapper;
import br.com.workingsafe.model.Checkin;
import br.com.workingsafe.model.RecomendacaoIa;
import br.com.workingsafe.model.Usuario;
import br.com.workingsafe.repository.CheckinRepository;
import br.com.workingsafe.repository.RecomendacaoIaRepository;
import br.com.workingsafe.repository.UsuarioRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecomendacaoIaService {

    private final RecomendacaoIaRepository recomendacaoIaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CheckinRepository checkinRepository;
    private final RecomendacaoIaMapper mapper;
    private final ChatClient chatClient;

    public RecomendacaoIaService(RecomendacaoIaRepository recomendacaoIaRepository,
                                 UsuarioRepository usuarioRepository,
                                 CheckinRepository checkinRepository,
                                 RecomendacaoIaMapper mapper,
                                 ChatClient.Builder chatClientBuilder) {
        this.recomendacaoIaRepository = recomendacaoIaRepository;
        this.usuarioRepository = usuarioRepository;
        this.checkinRepository = checkinRepository;
        this.mapper = mapper;
        this.chatClient = chatClientBuilder.build();
    }

    // ============================================================
    // 1) LISTAGEM POR USUÁRIO
    // ============================================================
    @Transactional(readOnly = true)
    public Page<RecomendacaoIaDto> listarPorUsuario(Long usuarioId, Pageable pageable) {
        return recomendacaoIaRepository.findByUsuarioId(usuarioId, pageable)
                .map(mapper::toDto);
    }

    // ============================================================
    // 2) LISTAGEM GERAL (p/ gestor)
    // ============================================================
    @Transactional(readOnly = true)
    public Page<RecomendacaoIaDto> listarTodas(Pageable pageable) {
        return recomendacaoIaRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    // ============================================================
    // 3) BUSCAR POR ID
    // ============================================================
    @Transactional(readOnly = true)
    public RecomendacaoIaDto buscarPorId(Long id) {
        RecomendacaoIa rec = recomendacaoIaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recomendação não encontrada"));
        return mapper.toDto(rec);
    }

    // ============================================================
    // 4) EXCLUIR
    // ============================================================
    @Transactional
    public void excluir(Long id) {
        RecomendacaoIa rec = recomendacaoIaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recomendação não encontrada"));
        recomendacaoIaRepository.delete(rec);
    }

    // ============================================================
    // 5) GERAR RECOMENDAÇÕES (CHAMADO PELO CHECKIN)
    // ============================================================
    @Transactional
    public List<RecomendacaoIaDto> gerarRecomendacoesGenericas(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Optional<Checkin> ultimoCheckin = checkinRepository
                .findTopByUsuarioIdOrderByDataHoraDesc(usuarioId);

        String prompt = montarPrompt(usuario, ultimoCheckin.orElse(null));

        // ******** CHAMADA AO OLLAMA VIA SPRING AI ********
        String respostaIa = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        // cria entidade
        RecomendacaoIa nova = new RecomendacaoIa();
        nova.setUsuario(usuario);
        nova.setCategoria(sugerirCategoria(ultimoCheckin.orElse(null)));
        nova.setDescricao(respostaIa);
        nova.setDataCriacao(LocalDateTime.now());
        nova.setDataValidade(LocalDate.now().plusDays(7));
        nova.setOrigem("SPRING_AI_OLLAMA");

        recomendacaoIaRepository.save(nova);

        return List.of(mapper.toDto(nova));
    }

    // ===================== AUXILIARES ============================

    private String montarPrompt(Usuario usuario, Checkin checkin) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
                Você é um assistente de bem-estar corporativo.
                Gere uma recomendação curta (2–3 frases), empática e prática para o colaborador.
                Responda SEM mencionar números ou repetir valores.
                Responda em português brasileiro.
                
                Dados do colaborador:
                """);

        sb.append("\n- Nome: ").append(usuario.getNome());

        if (checkin != null) {
            sb.append("\n- Humor: ").append(checkin.getHumor());
            sb.append("\n- Foco: ").append(checkin.getFoco());
            sb.append("\n- Pausas: ").append(checkin.getMinutosPausas());
            sb.append("\n- Horas trabalhadas: ").append(checkin.getHorasTrabalhadas());
            sb.append("\n- Tags: ").append(checkin.getTags());
            sb.append("\n- Observações: ").append(checkin.getObservacoes());
        } else {
            sb.append("\n- Ainda sem check-ins. Crie uma recomendação genérica e motivadora.");
        }

        sb.append("""

                \nFormato desejado:
                - Um parágrafo curto
                - Sem listas, sem tópicos, sem emojis demais
                """);

        return sb.toString();
    }

    private String sugerirCategoria(Checkin c) {
        if (c == null) return "GERAL";

        if (c.getHorasTrabalhadas() != null && c.getHorasTrabalhadas() > 9)
            return "PAUSAS";

        if (c.getHumor() != null && c.getHumor() <= 2)
            return "MINDFULNESS";

        return "GERAL";
    }
}
