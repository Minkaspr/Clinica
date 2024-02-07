
package entity;

import java.time.LocalTime;

public class Medico extends Persona{
    private String numeroColegiado;
    private String especialidad;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private Usuario usuario;

    public Medico() {
    }

    public Medico(String numeroColegiado, String especialidad, LocalTime horaEntrada, LocalTime horaSalida, Usuario usuario, Integer id, String nombres, String apellidos) {
        super(id, nombres, apellidos);
        this.numeroColegiado = numeroColegiado;
        this.especialidad = especialidad;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.usuario = usuario;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
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
