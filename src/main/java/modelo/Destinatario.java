package modelo;

public class Destinatario {
    private String direccion;

    public Destinatario() { }

    public Destinatario(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
