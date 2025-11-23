package br.com.workingsafe.rabbit.events;

import java.time.LocalDateTime;

public class UsuarioEvent {

    private Long id;
    private String email;
    private String nome;
    private String tipoEvento; // CREATED / UPDATED / DELETED
    private LocalDateTime dataHora;

    public UsuarioEvent() {
    }

    public UsuarioEvent(Long id, String email, String nome, String tipoEvento, LocalDateTime dataHora) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.tipoEvento = tipoEvento;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "UsuarioEvent{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
