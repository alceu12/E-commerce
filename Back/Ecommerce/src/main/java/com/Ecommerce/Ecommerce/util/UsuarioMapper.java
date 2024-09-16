package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.dto.FuncaoDTO;
import com.Ecommerce.Ecommerce.dto.StatusDTO;
import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;
import com.Ecommerce.Ecommerce.entity.Funcao;
import com.Ecommerce.Ecommerce.entity.Status;
import com.Ecommerce.Ecommerce.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        EnderecoDTO enderecoDTO = EnderecoMapper.toDTO(usuario.getEndereco());
        FuncaoDTO funcaoDTO = FuncaoMapper.toDTO(usuario.getFuncao());
        StatusDTO statusDTO = StatusMapper.toDTO(usuario.getStatus());

        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPassword(),
            enderecoDTO,
            funcaoDTO,
            statusDTO

        );
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        if (dto.getEnderecoDTO() != null && dto.getEnderecoDTO().getId() != null) {
            Endereco endereco = new Endereco();
            endereco.setId(dto.getEnderecoDTO().getId());
            usuario.setEndereco(endereco);
        }

        if (dto.getFuncaoDTO() != null && dto.getFuncaoDTO().getId() != null) {
            Funcao funcao = new Funcao();
            funcao.setId(dto.getFuncaoDTO().getId());
            usuario.setFuncao(funcao);
        }

        if (dto.getStatusDTO() != null && dto.getStatusDTO().getId() != null) {
            Status status = new Status();
            status.setId(dto.getStatusDTO().getId());
            usuario.setStatus(status);
        }

        return usuario;
    }

}
