package com.Ecommerce.Ecommerce.DTO;

public class UsuarioDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private EnderecoDTO enderecoDTO;
    private FuncaoDTO funcaoDTO;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String name, String email, EnderecoDTO enderecoDTO, FuncaoDTO funcaoDTO) {
        this.name = name;
        this.email = email;
        this.enderecoDTO = enderecoDTO;
        this.funcaoDTO = funcaoDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
