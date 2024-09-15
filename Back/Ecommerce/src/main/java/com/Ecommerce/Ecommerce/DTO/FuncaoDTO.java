package com.Ecommerce.Ecommerce.DTO;

public class FuncaoDTO {

    private Long id;
    private String nome;
    
    public FuncaoDTO() {
    }

    public FuncaoDTO( String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
