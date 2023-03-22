package jacques.raul.uv.es;

import java.util.List;

public class Camping {

    private int id;
    private String nombre, categoria, municipio, estado, provincia, cp, direccion, email, web, numParcelas, plazasParcela, plazasLibreAcampada, periodo;

    public Camping(String nombre, String categoria, String municipio, String estado, String provincia, String cp, String direccion, String email, String web, String numParcelas, String plazasParcela, String plazasLibreAcampada, String periodo) {

        this.nombre = nombre;
        this.categoria = categoria;
        this.municipio = municipio;
        this.estado = estado;
        this.provincia = provincia;
        this.cp = cp;
        this.direccion = direccion;
        this.email = email;
        this.web = web;
        this.numParcelas = numParcelas;
        this.plazasParcela = plazasParcela;
        this.plazasLibreAcampada = plazasLibreAcampada;
        this.periodo = periodo;
    }

    public Camping(String nombre, String categoria, String municipio, String estado, String provincia, String cp, String direccion, String email, String web, String numParcelas) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.municipio = municipio;
        this.estado = estado;
        this.provincia = provincia;
        this.cp = cp;
        this.direccion = direccion;
        this.email = email;
        this.web = web;
        this.numParcelas = numParcelas;
    }

    public Camping(int id, String nombre, String categoria, String municipio, String estado, String provincia, String cp, String direccion, String email, String web, String numParcelas, String plazasParcela, String plazasLibreAcampada, String periodo) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.municipio = municipio;
        this.estado = estado;
        this.provincia = provincia;
        this.cp = cp;
        this.direccion = direccion;
        this.email = email;
        this.web = web;
        this.numParcelas = numParcelas;
        this.plazasParcela = plazasParcela;
        this.plazasLibreAcampada = plazasLibreAcampada;
        this.periodo = periodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getNumParcelas() {
        return numParcelas;
    }

    public void setNumParcelas(String numParcelas) {
        this.numParcelas = numParcelas;
    }

    public String getPlazasParcela() {
        return plazasParcela;
    }

    public void setPlazasParcela(String plazasParcela) {
        this.plazasParcela = plazasParcela;
    }

    public String getPlazasLibreAcampada() {
        return plazasLibreAcampada;
    }

    public void setPlazasLibreAcampada(String plazasLibreAcampada) {
        this.plazasLibreAcampada = plazasLibreAcampada;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}