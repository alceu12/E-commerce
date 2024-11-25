package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.dto.FuncaoDTO;
import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;
import com.Ecommerce.Ecommerce.entity.Funcao;
import com.Ecommerce.Ecommerce.entity.StatusUser;
import com.Ecommerce.Ecommerce.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        EnderecoDTO enderecoDTO = EnderecoMapper.toDTO(usuario.getEndereco());
        FuncaoDTO funcaoDTO = FuncaoMapper.toDTO(usuario.getFuncao());


        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPassword(),
                enderecoDTO,
                funcaoDTO,
                // se o status for nullo defina como ativo
                usuario.getStatusUser() == null ? StatusUser.ACTIVE.name() : usuario.getStatusUser().name()

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

        if (dto.getStatusUser() != null) {
            usuario.setStatusUser(StatusUser.valueOf(dto.getStatusUser()));

            return usuario;
        }

        return usuario;
    }
}
