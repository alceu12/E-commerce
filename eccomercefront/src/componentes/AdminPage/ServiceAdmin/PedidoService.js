import api from '../../../Service/Api';

export const atualizarStatusPedido = async (pedidoId, novoStatusDescricao) => {
    try {
        const response = await api.put(`/pedidos/${pedidoId}/status`, { statusPedido: novoStatusDescricao }, {
            headers: {
                'Content-Type': 'application/json', // Enviando como JSON
            },
        });
        return response.data;
    } catch (error) {
        console.error('Erro ao atualizar o status do pedido:', error);
        throw error;
    }
};
export const getPedidos = () => {
    return api
        .get(`/pedidos`)
        .then((response) => response.data)
        .catch((error) => {
            console.error(`Erro ao obter pedidos.`, error);
            throw error;
        });
};