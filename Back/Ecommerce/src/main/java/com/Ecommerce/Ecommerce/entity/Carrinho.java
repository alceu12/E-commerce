package com.Ecommerce.Ecommerce.entity;

import java.util.ArrayList;
import java.util.List;

import com.Ecommerce.Ecommerce.entity.ItemPedido;

public class Carrinho {

    private Long usuarioId;
    private List<ItemPedido> itens;

    public Carrinho(Long usuarioId) {
        this.usuarioId = usuarioId;
        this.itens = new ArrayList<>();
    }

    // Getters e Setters

    public Long getUsuarioId() {
        return usuarioId;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    // Métodos para adicionar e remover itens

    public void adicionarItem(ItemPedido item) {
        // Verificar se o produto já está no carrinho
        boolean itemExistente = false;
        for (ItemPedido i : itens) {
            if (i.getProduto().getId().equals(item.getProduto().getId())) {
                // Atualizar a quantidade do item existente
                i.setQuantidade(i.getQuantidade() + item.getQuantidade());
                itemExistente = true;
                break;
            }
        }
        if (!itemExistente) {
            // Adicionar novo item ao carrinho
            itens.add(item);
        }
    }

    public void removerItem(Long produtoId) {
        itens.removeIf(item -> item.getProduto().getId().equals(produtoId));
    }


    public void limparCarrinho() {
        this.itens.clear();
    }
}
