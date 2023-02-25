package jacques.raul.uv.es;

public class Camping {

    private String nombre, categoria, municipio;

    public Camping(String nombre, String categoria, String municipio){
        this.nombre = nombre;
        this.categoria = categoria;
        this.municipio = municipio;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
