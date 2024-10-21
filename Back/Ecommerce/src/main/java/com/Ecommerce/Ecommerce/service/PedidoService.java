package com.Ecommerce.Ecommerce.service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.PedidoDTO;
import com.Ecommerce.Ecommerce.entity.Status;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.entity.Pedido;
import com.Ecommerce.Ecommerce.repository.StatusRepository;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.repository.PedidoRepository;
import com.Ecommerce.Ecommerce.util.PedidoMapper;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
        // Converter DTO para entidade pedido
        Pedido pedido = PedidoMapper.toEntity(pedidoDTO);

        // Buscar e associar o status usando o ID
        if (pedido.getStatusPedido() != null && pedido.getStatusPedido().getId() != null) {
            Optional<Status> statusOptional = statusRepository.findById(pedido.getStatusPedido().getId());
            if (statusOptional.isPresent()) {
                pedido.setStatusPeidido(statusOptional.get());
            } else {
                throw new RuntimeException("Status com ID " + pedido.getStatusPedido().getId() + " não encontrado.");
            }
        }

        if (pedido.getUsuario() != null && pedido.getUsuario().getId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(pedido.getUsuario().getId());
            if (usuarioOptional.isPresent()) {
                pedido.setUsuario(usuarioOptional.get());
            } else {
                throw new RuntimeException("Usuario com ID " + pedido.getUsuario().getId() + " não encontrado.");
            }
        }

        pedido = pedidoRepository.save(pedido);
        return PedidoMapper.toDTO(pedido);
    }

    public List<PedidoDTO> obterTodosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(PedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO obterPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(PedidoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public PedidoDTO atualizarPedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedidoExistente.setTotal(pedidoDTO.getTotal());

        if (pedidoDTO.getStatusDTO() != null && pedidoDTO.getStatusDTO().getId() != null) {
            Optional<Status> statusOptional = statusRepository.findById(pedidoDTO.getStatusDTO().getId());
            statusOptional.ifPresent(pedidoExistente::setStatusPedido);
        }

        if (pedidoDTO.getUsuarioDTO() != null && pedidoDTO.getUsuarioDTO().getId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(pedidoDTO.getUsuarioDTO().getId());
            usuarioOptional.ifPresent(pedidoExistente::setUsuario);
        }

        Pedido pedidoAtualizado = pedidoRepository.save(pedidoExistente);
        return PedidoMapper.toDTO(pedidoAtualizado);
    }

    public boolean deletarPedido(Long id) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        if(pedidoExistente.isPresent()){
            pedidoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
