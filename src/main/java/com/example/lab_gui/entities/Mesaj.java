package com.example.lab_gui.entities;

public class Mesaj extends Entity<Long>{
    private String mesaj;
    private Long from;
    private Long to;

    public Mesaj(String mesaj, Long from, Long to) {
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
