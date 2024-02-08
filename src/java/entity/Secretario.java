
package entity;

import java.time.LocalTime;

public class Secretario extends Persona{
    private Double salario;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private Usuario usuario;

    public Secretario() {
    }

    public Secretario(Double salario, LocalTime horaEntrada, LocalTime horaSalida, Usuario usuario, Integer id, String nombres, String apellidos) {
        super(id, nombres, apellidos);
        this.salario = salario;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.usuario = usuario;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
