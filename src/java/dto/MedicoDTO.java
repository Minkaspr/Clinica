
package dto;

import entity.Persona;

public class MedicoDTO extends Persona{
    private String numeroColegiado;
    private String especialidad;

    public MedicoDTO() {
    }

    public MedicoDTO(String numeroColegiado, String especialidad, Integer id, String nombres, String apellidos) {
        super(id, nombres, apellidos);
        this.numeroColegiado = numeroColegiado;
        this.especialidad = especialidad;
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
}
