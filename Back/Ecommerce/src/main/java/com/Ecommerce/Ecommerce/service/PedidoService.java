package com.Ecommerce.Ecommerce.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.dto.UpdateStatusDTO;
import com.Ecommerce.Ecommerce.entity.*;
import com.Ecommerce.Ecommerce.repository.*;
import com.Ecommerce.Ecommerce.util.EnderecoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.PedidoDTO;
import com.Ecommerce.Ecommerce.util.PedidoMapper;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;


    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();

        // Definir o usuário
        Usuario usuario = usuarioRepository.findById(pedidoDTO.getUsuarioDTO().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        pedido.setUsuario(usuario);

        // Definir o status como PENDING_PAYMENT
        pedido.setStatusPedido(StatusPedido.PENDING_PAYMENT);

        // Buscar e associar os ItemPedido existentes
        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoDTO itemDTO : pedidoDTO.getItemPedidoDTO()) {
            ItemPedido item = itemPedidoRepository.findById(itemDTO.getId())
                    .orElseThrow(() -> new RuntimeException("ItemPedido com ID " + itemDTO.getId() + " não encontrado."));
            itens.add(item);
        }
        pedido.setItemPedido(itens);

        // Calcular o total do pedido
        double total = itens.stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                .sum();

        // Aplicar desconto do cupom se houver
        if (pedidoDTO.getCupomAplicado() != null && pedidoDTO.getCupomAplicado().getId() != null) {
            Cupom cupom = cupomRepository.findById(pedidoDTO.getCupomAplicado().getId())
                    .orElseThrow(() -> new RuntimeException("Cupom não encontrado."));

            // Validar data do cupom
            // Aplicar desconto se o valor mínimo for atingido
            if (cupom.getValorMinimo() <= total) {
                double desconto = total * cupom.getValorDesconto();
                total -= desconto;
            }

            pedido.setCupomAplicado(cupom);
        } else {
            pedido.setCupomAplicado(null); // Define o cupom como nulo se não houver
        }

        pedido.setTotal(total);
        pedido.setDataPedido(LocalDate.now());
        pedido.setDataEntrega(null);

        Endereco enderecoUsuario = usuario.getEndereco();
        if (enderecoUsuario == null) {
            throw new RuntimeException("Usuário não possui um endereço cadastrado.");
        }

        // Clonar o endereço do usuário
        Endereco novoEndereco = new Endereco();
        novoEndereco.setRua(enderecoUsuario.getRua());
        novoEndereco.setNumero(enderecoUsuario.getNumero());
        novoEndereco.setComplemento(enderecoUsuario.getComplemento());
        novoEndereco.setBairro(enderecoUsuario.getBairro());
        novoEndereco.setCidade(enderecoUsuario.getCidade());
        novoEndereco.setEstado(enderecoUsuario.getEstado());
        novoEndereco.setCep(enderecoUsuario.getCep());
        // Se houver outros campos, copie-os também

        // Salvar o novo endereço
        enderecoRepository.save(novoEndereco);

        // Definir o novo endereço de entrega no pedido
        pedido.setEnderecoEntrega(novoEndereco);
        // Salvar o pedido
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return PedidoMapper.toDTO(pedidoSalvo);
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


        if (pedidoDTO.getUsuarioDTO() != null && pedidoDTO.getUsuarioDTO().getId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(pedidoDTO.getUsuarioDTO().getId());
            usuarioOptional.ifPresent(pedidoExistente::setUsuario);
        }

        Pedido pedidoAtualizado = pedidoRepository.save(pedidoExistente);
        return PedidoMapper.toDTO(pedidoAtualizado);
    }

    public boolean deletarPedido(Long id) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        if (pedidoExistente.isPresent()) {
            pedidoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public PedidoDTO atualizarStatusPedido(Long id, UpdateStatusDTO updateStatusDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        // Força o carregamento dos ItemPedido para evitar que sejam removidos
        pedidoExistente.getItemPedido().size();

        // Atualiza o status usando o método do mapper
        PedidoMapper.updateStatusFromDTO(pedidoExistente, updateStatusDTO);

        // Salva o pedido atualizado
        Pedido pedidoAtualizado = pedidoRepository.save(pedidoExistente);

        // Converte a entidade atualizada para DTO e retorna
        return PedidoMapper.toDTO(pedidoAtualizado);
    }


    public List<PedidoDTO> obterPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findAllByUsuarioIdOrderByIdDesc(usuarioId).stream()
                .map(PedidoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
