package com.Ecommerce.Ecommerce.dto;

public class EnderecoDTO {
    private Long id;
    private String cep;
    private String numero;
    private String complemento;

    public EnderecoDTO() {
    }
    
    public EnderecoDTO(Long id, String cep, String numero, String complemento) {
        this.id = id;
        this.cep = cep;
        this.numero = numero;
        this.complemento = complemento;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getComplemento() {
        return complemento;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
