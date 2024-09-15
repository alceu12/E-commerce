package com.Ecommerce.Ecommerce.dto;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String password;
    private EnderecoDTO enderecoDTO;
    private FuncaoDTO funcaoDTO;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nome, String email, String password, EnderecoDTO enderecoDTO,
            FuncaoDTO funcaoDTO) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.enderecoDTO = enderecoDTO;
        this.funcaoDTO = funcaoDTO;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EnderecoDTO getEnderecoDTO() {
        return enderecoDTO;
    }

    public void setEnderecoDTO(EnderecoDTO enderecoDTO) {
        this.enderecoDTO = enderecoDTO;
    }

    public FuncaoDTO getFuncaoDTO() {
        return funcaoDTO;
    }

    public void setFuncaoDTO(FuncaoDTO funcaoDTO) {
        this.funcaoDTO = funcaoDTO;
    }
}
