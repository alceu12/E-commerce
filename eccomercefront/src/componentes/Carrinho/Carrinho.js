// Carrinho.js
import React, { useContext } from 'react';
import { CarrinhoContext } from './CarrinhoContext';
import { Box, Typography, Button } from '@mui/material';

const Carrinho = () => {
    const { carrinho, removerItem, limparCarrinho } = useContext(CarrinhoContext);

    if (!carrinho || carrinho.itens.length === 0) {
        return (
            <Box>
                <Typography>Seu carrinho está vazio.</Typography>
            </Box>
        );
    }

    return (
        <Box>
            <Typography variant="h4">Carrinho</Typography>
            {carrinho.itens.map((item) => (
                <Box key={item.id} sx={{ display: 'flex', justifyContent: 'space-between', marginBottom: 2 }}>
                    <Typography>{item.produto.nome}</Typography>
                    <Typography>Quantidade: {item.quantidade}</Typography>
                    <Typography>Preço: R$ {item.precoUnitario}</Typography>
                    <Button variant="outlined" onClick={() => removerItem(item.id)}>
                        Remover
                    </Button>
                </Box>
            ))}
            <Button variant="contained" color="primary" onClick={limparCarrinho}>
                Limpar Carrinho
            </Button>
        </Box>
    );
};

export default Carrinho;
