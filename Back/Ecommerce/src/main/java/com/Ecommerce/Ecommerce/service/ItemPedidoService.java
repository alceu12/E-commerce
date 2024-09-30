package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.entity.ItemPedido;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import com.Ecommerce.Ecommerce.repository.ItemPedidoRepository;
import com.Ecommerce.Ecommerce.util.ItemPedidoMapper;

@Service
public class ItemPedidoService {
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public ItemPedidoDTO criarItemPedido(ItemPedidoDTO itemPedidoDTO) {
        // Converter DTO para entidade itempedido
        ItemPedido itemPedido = ItemPedidoMapper.toEntity(itemPedidoDTO);

        // Buscar e associar o produto usando o ID
        if (itemPedido.getProduto() != null && itemPedido.getProduto().getId() != null) {
            Optional<Produto> produtoOptional = produtoRepository.findById(itemPedido.getProduto().getId());
            if (produtoOptional.isPresent()) {
                itemPedido.setProduto(produtoOptional.get());
            } else {
                throw new RuntimeException("Endereço com ID " + itemPedido.getProduto().getId() + " não encontrado.");
            }
        }
        itemPedido = itemPedidoRepository.save(itemPedido);
        return ItemPedidoMapper.toDTO(itemPedido);
    }

    public List<ItemPedidoDTO> obterTodosItemPedidos() {
        return itemPedidoRepository.findAll().stream()
                .map(ItemPedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ItemPedidoDTO obterItemPedidoPorId(Long id) {
        return itemPedidoRepository.findById(id)
                .map(ItemPedidoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("ItemPedido não encontrado"));
    }

    public ItemPedidoDTO atualizarItemPedido(Long id, ItemPedidoDTO itemPedidoDTO) {
        ItemPedido itemPedidoExistente = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemPedido não encontrado"));

        itemPedidoExistente.setQuantidade(itemPedidoDTO.getQuantidade());
        itemPedidoExistente.setPrecoUnitario(itemPedidoDTO.getPrecoUnitario());

        if (itemPedidoDTO.getProdutoDTO() != null && itemPedidoDTO.getProdutoDTO().getId() != null) {
            Optional<Produto> produtoOptional = produtoRepository.findById(itemPedidoDTO.getProdutoDTO().getId());
            produtoOptional.ifPresent(itemPedidoExistente::setProduto);
        }

        ItemPedido itemPedidoAtualizado = itemPedidoRepository.save(itemPedidoExistente);
        return ItemPedidoMapper.toDTO(itemPedidoAtualizado);
    }

    public boolean deletarItemPedido(Long id) {
        Optional<ItemPedido> itemPedidoExistente = itemPedidoRepository.findById(id);
        if(itemPedidoExistente.isPresent()){
            itemPedidoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
