// CarrinhoService.js
import api from '../../Service/Api';

export const getCarrinhoFromAPI = (usuarioId) => {
    return api.get(`/carrinho/${usuarioId}`).then((response) => response.data);
};

export const adicionarItemNaAPI = (usuarioId, itemPedido) => {
    return api.post(`/carrinho/${usuarioId}/adicionar`, itemPedido).then((response) => response.data);
};

export const removerItemDaAPI = (usuarioId, itemId) => {
    return api.delete(`/carrinho/${usuarioId}/remover/${itemId}`).then((response) => response.data);
};

export const limparCarrinhoNaAPI = (usuarioId) => {
    return api.delete(`/carrinho/${usuarioId}/limpar`).then((response) => response.data);
};
