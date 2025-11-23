package br.com.workingsafe.rabbit.events;

import java.time.LocalDateTime;

public class CheckinEvent {

    private Long idCheckin;
    private Long idUsuario;
    private LocalDateTime dataHora;
    private Integer humor;
    private Integer foco;
    private String origem; // MOBILE / WEB / API

    public CheckinEvent() {
    }

    public CheckinEvent(Long idCheckin, Long idUsuario, LocalDateTime dataHora,
                        Integer humor, Integer foco, String origem) {
        this.idCheckin = idCheckin;
        this.idUsuario = idUsuario;
        this.dataHora = dataHora;
        this.humor = humor;
        this.foco = foco;
        this.origem = origem;
    }

    public Long getIdCheckin() {
        return idCheckin;
    }

    public void setIdCheckin(Long idCheckin) {
        this.idCheckin = idCheckin;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

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

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    @Override
    public String toString() {
        return "CheckinEvent{" +
                "idCheckin=" + idCheckin +
                ", idUsuario=" + idUsuario +
                ", dataHora=" + dataHora +
                ", humor=" + humor +
                ", foco=" + foco +
                ", origem='" + origem + '\'' +
                '}';
    }
}
