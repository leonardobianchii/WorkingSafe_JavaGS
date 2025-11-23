package br.com.workingsafe.web;

import br.com.workingsafe.dto.CheckinDto;
import br.com.workingsafe.dto.RecomendacaoIaDto;
import br.com.workingsafe.dto.UsuarioDto;
import br.com.workingsafe.service.CheckinService;
import br.com.workingsafe.service.RecomendacaoIaService;
import br.com.workingsafe.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeWebController {

    private final UsuarioService usuarioService;
    private final CheckinService checkinService;
    private final RecomendacaoIaService recomendacaoIaService;

    public HomeWebController(UsuarioService usuarioService,
                             CheckinService checkinService,
                             RecomendacaoIaService recomendacaoIaService) {
        this.usuarioService = usuarioService;
        this.checkinService = checkinService;
        this.recomendacaoIaService = recomendacaoIaService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        // Usuário logado (email vem do Spring Security)
        String email = authentication.getName();
        UsuarioDto usuario = usuarioService.buscarPorEmail(email);

        model.addAttribute("usuario", usuario);

        // Form de check-in (view model padrão)
        CheckinFormViewModel form = new CheckinFormViewModel();
        form.setHumor(3);
        form.setFoco(3);
        form.setMinutosPausas(0);
        form.setHorasTrabalhadas(8.0);
        form.setTags("");
        form.setObservacoes("");
        model.addAttribute("checkinForm", form);

        // Últimos check-ins do usuário (limite 5)
        List<CheckinDto> checkinsRecentes =
                checkinService.listarUltimosPorUsuario(usuario.id(), 5);
        model.addAttribute("checkinsRecentes", checkinsRecentes);

        // (Opcional, mas bom para demo)
        // Gera uma nova recomendação com base no último check-in
        recomendacaoIaService.gerarRecomendacoesGenericas(usuario.id());

        // Recomendacoes (usando o service atual: listarPorUsuario)
        Page<RecomendacaoIaDto> paginaRecs =
                recomendacaoIaService.listarPorUsuario(usuario.id(), PageRequest.of(0, 10));

        List<RecomendacaoIaDto> recomendacoesAtuais = paginaRecs.getContent();
        model.addAttribute("recomendacoesAtuais", recomendacoesAtuais);

        return "home/index";
    }

    /**
     * ViewModel só para o formulário do colaborador.
     * Os nomes dos campos DEVEM bater com o th:field do HTML.
     */
    public static class CheckinFormViewModel {

        private Integer humor;
        private Integer foco;
        private Integer minutosPausas;
        private Double horasTrabalhadas;
        private String tags;
        private String observacoes;

        public Integer getHumor() {
            return humor;
        }

        public void setHumor(Integer humor) {
            this.humor = humor;
        }

        public Integer getFoco() {
            return foco;
        }

        public void setFoco(Integer foco) {
            this.foco = foco;
        }

        public Integer getMinutosPausas() {
            return minutosPausas;
        }

        public void setMinutosPausas(Integer minutosPausas) {
            this.minutosPausas = minutosPausas;
        }

        public Double getHorasTrabalhadas() {
            return horasTrabalhadas;
        }

        public void setHorasTrabalhadas(Double horasTrabalhadas) {
            this.horasTrabalhadas = horasTrabalhadas;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getObservacoes() {
            return observacoes;
        }

        public void setObservacoes(String observacoes) {
            this.observacoes = observacoes;
        }
    }
}
