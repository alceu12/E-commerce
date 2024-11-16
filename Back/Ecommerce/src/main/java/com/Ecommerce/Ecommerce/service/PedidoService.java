package com.Ecommerce.Ecommerce.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.entity.*;
import com.Ecommerce.Ecommerce.repository.*;
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
            LocalDate dataAtual = LocalDate.now();
            if (dataAtual.isBefore(cupom.getDataInicio()) || dataAtual.isAfter(cupom.getDataFim())) {
                throw new RuntimeException("Cupom fora do prazo de validade.");
            }

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
        if(pedidoExistente.isPresent()){
            pedidoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
