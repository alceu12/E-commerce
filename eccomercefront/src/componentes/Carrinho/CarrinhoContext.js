// CarrinhoContext.js
import React, { createContext, useState, useEffect } from 'react';
import { getCarrinhoFromAPI, adicionarItemNaAPI, removerItemDaAPI, limparCarrinhoNaAPI } from './CarrinhoService';

export const CarrinhoContext = createContext();

export const CarrinhoProvider = ({ children }) => {
    const [carrinho, setCarrinho] = useState(null);

    const usuarioId = 1; // Substitua pelo ID real do usuário

    useEffect(() => {
        // Obter o carrinho do backend quando o componente for montado
        getCarrinhoFromAPI(usuarioId).then((data) => {
            setCarrinho(data);
        });
    }, []);

    const adicionarItem = (itemPedido) => {
        adicionarItemNaAPI(usuarioId, itemPedido).then((data) => {
            setCarrinho(data);
        });
    };

    const removerItem = (itemId) => {
        removerItemDaAPI(usuarioId, itemId).then((data) => {
            setCarrinho(data);
        });
    };

    const limparCarrinho = () => {
        limparCarrinhoNaAPI(usuarioId).then(() => {
            setCarrinho(null);
        });
    };

    return (
        <CarrinhoContext.Provider value={{ carrinho, adicionarItem, removerItem, limparCarrinho }}>
            {children}
        </CarrinhoContext.Provider>
    );
};
