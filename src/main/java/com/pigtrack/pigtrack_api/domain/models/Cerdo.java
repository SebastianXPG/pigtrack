package com.pigtrack.pigtrack_api.domain.models;

import java.time.LocalDate;
import java.util.UUID;

public class Cerdo {

    private UUID id;
    private String nombre;
    private String raza;
    private LocalDate fechaNacimiento;
    private double peso;
    private UUID usuarioId;

    public Cerdo(UUID id, String nombre, String raza, LocalDate fechaNacimiento, double peso, UUID usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.usuarioId = usuarioId;
    }

    // Getters y setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
}
