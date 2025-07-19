package com.pigtrack.pigtrack_api.api.controllers.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public class CerdoRequestDTO {

    @NotBlank(message = "El nombre del cerdo es obligatorio")
    private String nombre;

    @NotBlank(message = "La raza del cerdo es obligatoria")
    private String raza;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    private double peso;

    @NotNull(message = "El ID del usuario es obligatorio")
    private UUID usuarioId;

    // Getters y setters...

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
