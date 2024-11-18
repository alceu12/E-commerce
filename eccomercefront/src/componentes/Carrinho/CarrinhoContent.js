// CarrinhoContent.js
import React, { useContext } from 'react';
import { CarrinhoContext } from './CarrinhoContext';
import { Box, Typography, List, ListItem, ListItemText, IconButton, Divider, Button, ListItemAvatar, Avatar } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const CarrinhoContent = () => {
    const { carrinho, removerItem, limparCarrinho } = useContext(CarrinhoContext);

    if (!carrinho || carrinho.itens.length === 0) {
        return (
            <Box sx={{ p: 2 }}>
                <Typography variant="h6">Seu carrinho está vazio.</Typography>
            </Box>
        );
    }

    const calcularTotal = () => {
        return carrinho.itens.reduce((total, item) => total + item.precoUnitario * item.quantidade, 0).toFixed(2);
    };

    return (
        <Box sx={{ p: 2 }}>
            <Typography variant="h5" sx={{ mb: 2 }}>Carrinho</Typography>
            <List>
                {carrinho.itens.map((item) => (
                    <React.Fragment key={item.produto.id}>
                        <ListItem alignItems="flex-start"
                            secondaryAction={
                                <IconButton edge="end" aria-label="delete" onClick={() => removerItem(item.produto.id)}>
                                    <DeleteIcon />
                                </IconButton>
                            }
                        >
                            <ListItemAvatar>
                                <Avatar
                                    variant="square"
                                    src={`data:image/jpeg;base64,${item.produto.imagens && item.produto.imagens[0]?.dados}`}
                                    alt={item.produto.nome}
                                    sx={{ width: 56, height: 56 }}
                                />
                            </ListItemAvatar>
                            <ListItemText
                                primary={item.produto.nome}
                                secondary={
                                    <>
                                        <Typography variant="body2" color="text.secondary">
                                            Quantidade: {item.quantidade}
                                        </Typography>
                                        <Typography variant="body2" color="text.secondary">
                                            Preço unitário: R$ {parseFloat(item.precoUnitario).toFixed(2)}
                                        </Typography>
                                    </>
                                }
                            />
                        </ListItem>
                        <Divider />
                    </React.Fragment>
                ))}
            </List>
            <Box sx={{ mt: 2 }}>
                <Typography variant="h6">Total: R$ {calcularTotal()}</Typography>
                <Button variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
                    Finalizar Compra
                </Button>
                <Button variant="text" color="secondary" fullWidth onClick={limparCarrinho}>
                    Limpar Carrinho
                </Button>
            </Box>
        </Box>
    );
};

export default CarrinhoContent;
