import React, { useContext, useState } from 'react';
import {
    Box,
    Typography,
    TextField,
    Button,
    Grid,
    Paper,
    Card,
    CardMedia,
    CardContent,
    CircularProgress,
} from '@mui/material';
import { CarrinhoContext } from '../Carrinho/CarrinhoContext';
import { validarCupom, finalizarPedido } from './PedidoService';
import { jwtDecode } from 'jwt-decode';
import { Link } from 'react-router-dom';
import { Navigate } from 'react-router-dom';
const ResumoCarrinho = ({ avancarEtapa }) => {
    const { carrinho } = useContext(CarrinhoContext);
    const [cupom, setCupom] = useState('');
    const [mensagem, setMensagem] = useState('');
    const [totalComDesconto, setTotalComDesconto] = useState(carrinho.total || 0);
    const [cupomValido, setCupomValido] = useState(null);
    const [loading, setLoading] = useState(false);
    const [cupomId, setCupomId] = useState(null);

    const storedToken = localStorage.getItem('userToken');
    const decodedToken = jwtDecode(storedToken);
    const userId = decodedToken.id;

    if (decodedToken === null) {
        return <Navigate to="/login" />;
    }

    const handleValidarCupom = () => {
        validarCupom(cupom)
            .then((data) => {
                if (data.valorMinimo <= carrinho.total) {
                    setCupomValido(data);
                    const desconto = (carrinho.total || 0) * data.valorDesconto;
                    setTotalComDesconto((carrinho.total || 0) - desconto);
                    setMensagem(`Cupom "${data.codigo}" aplicado!`);
                    setCupomId(data.id);
                } else {
                    setMensagem(`Cupom requer valor mínimo de R$ ${data.valorMinimo.toFixed(2)}`);
                    setCupomId(null);
                }
            })
            .catch(() => {
                setMensagem('Cupom inválido ou fora do prazo.');
                setCupomId(null);
            });
    };

    const handleContinuar = () => {
        setLoading(true);

        const pedidoDTO = {
            usuarioDTO: {
                id: userId,
            },
            itemPedidoDTO: carrinho.itens.map((item) => ({
                id: item.id,
            })),
            ...(cupomValido && { cupomAplicado: { id: cupomId } }),
        };

        finalizarPedido(pedidoDTO)
            .then((dadosPedido) => {
                setLoading(false);
                avancarEtapa(dadosPedido);
            })
            .catch(() => {
                setLoading(false);
                alert('Erro ao finalizar pedido. Por favor, tente novamente.');
            });
    };

    return (
        <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom>
                Resumo do Carrinho
            </Typography>
            <Grid container spacing={2}>
                {carrinho.itens.map((item, index) => (
                    <Grid item xs={12} md={6} key={index}>
                        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                            <Box sx={{ height: 100, overflow: 'hidden' }}>
                                <Link to={`/produto/${item.produtoDTO.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                    <CardMedia
                                        component="img"
                                        image={
                                            item.produtoDTO.imagens?.[0]?.dados
                                                ? `data:image/jpeg;base64,${item.produtoDTO.imagens[0].dados}`
                                                : 'https://via.placeholder.com/200'
                                        }
                                        alt={item.produtoDTO?.nome || 'Produto'}
                                        sx={{
                                            width: '100%',
                                            height: '100%',
                                            objectFit: 'contain',
                                            objectPosition: 'center',
                                        }}
                                    />
                                </Link>
                            </Box>
                            <CardContent sx={{ flexGrow: 1 }}>
                                <Link to={`/produto/${item.produtoDTO.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                    <Typography variant="h6" noWrap>
                                        {item.produtoDTO?.nome || 'Produto indisponível'}
                                    </Typography>
                                </Link>
                                <Typography variant="body2">Quantidade: {item.quantidade}</Typography>
                                <Typography variant="body2">
                                    Preço unitário:{' '}
                                    {new Intl.NumberFormat('pt-BR', {
                                        style: 'currency',
                                        currency: 'BRL',
                                    }).format(item.precoUnitario)}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
            <Box sx={{ mt: 3 }}>
                <Typography variant="h5" color="primary">
                    Total:{' '}
                    {new Intl.NumberFormat('pt-BR', {
                        style: 'currency',
                        currency: 'BRL',
                    }).format(totalComDesconto)}
                </Typography>
            </Box>
            <Box sx={{ mt: 2 }}>
                <TextField
                    label="Cupom de Desconto"
                    variant="outlined"
                    sx={{
                        width: '40%',
                        mr: 2, 
                        display: 'flex-center',
                        mt: 2
                    }}

                    value={cupom}
                    onChange={(e) => setCupom(e.target.value)}
                />
                <Button variant="contained" color="primary" onClick={handleValidarCupom} sx={{ mt: 2 }}>
                    Aplicar Cupom
                </Button>
                {mensagem && (
                    <Typography sx={{ mt: 2 }} color={cupomValido ? 'primary' : 'error'}>
                        {mensagem}
                    </Typography>
                )}
            </Box>
            <Button
                variant="contained"
                color="primary"
                fullWidth

                sx={{
                    mt: 2
                    ,
                    mb: 2,
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    margin: '0 auto',
                    width: '40%',
                }}
                onClick={handleContinuar}
                disabled={loading}
                startIcon={loading && <CircularProgress size={20} />}
            >
                {loading ? 'Finalizando Pedido...' : 'Continuar'}
            </Button>
        </Paper >
    );
};

export default ResumoCarrinho;
