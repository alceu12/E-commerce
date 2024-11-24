// src/components/Pedidos/PedidoDetails.jsx
import React, { useEffect, useState } from 'react';
import {
    Box,
    Typography,
    Grid,
    Card,
    CardContent,
    CardMedia,
    Button,
    CircularProgress,
    Divider,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { useParams, useNavigate } from 'react-router-dom';
import { getPedidosByUsuarioId } from './PedidoService';
import AppBarComponent from '../appbar';
import FooterComponent from '../Footer';

const AddressBox = styled(Box)(({ theme }) => ({
    padding: theme.spacing(2),
    backgroundColor: '#e0f7fa',
    borderRadius: theme.shape.borderRadius,
    marginBottom: theme.spacing(2),
}));

const StatusHistoryBox = styled(Box)(({ theme }) => ({
    padding: theme.spacing(2),
    backgroundColor: '#fff3e0',
    borderRadius: theme.shape.borderRadius,
    marginTop: theme.spacing(2),
}));

const PedidoDetails = () => {
    const { pedidoId } = useParams();
    const [pedido, setPedido] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const usuarioId = 7; // ID fixo do usuário, substitua conforme necessário

    useEffect(() => {
        const fetchPedido = async () => {
            try {
                const pedidos = await getPedidosByUsuarioId(usuarioId);
                const encontrado = pedidos.find((p) => p.id === parseInt(pedidoId));
                if (encontrado) {
                    // Geração aleatória da nota fiscal se ainda não existir
                    if (!encontrado.notaFiscalUrl) {
                        encontrado.notaFiscalUrl = gerarNotaFiscalUrl(encontrado);
                    }
                    setPedido(encontrado);
                } else {
                    console.error('Pedido não encontrado');
                }
            } catch (error) {
                console.error('Erro ao buscar pedido:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPedido();
    }, [pedidoId, usuarioId]);

    const gerarNotaFiscalUrl = (pedido) => {
        // Gera uma URL aleatória simulando uma nota fiscal
        const baseUrl = 'https://via.placeholder.com/600x800?text=Nota+Fiscal+Pedido+' + pedido.id;
        return baseUrl;
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
                <CircularProgress />
            </Box>
        );
    }

    if (!pedido) {
        return (
            <Box display="flex" flexDirection="column" minHeight="100vh">
                <AppBarComponent />
                <Box component="main" sx={{ flexGrow: 1, p: { xs: 2, sm: 4 }, backgroundColor: '#f5f5f5' }}>
                    <Typography variant="h6" sx={{ textAlign: 'center', mt: 4 }}>
                        Pedido não encontrado.
                    </Typography>
                    <Box display="flex" justifyContent="center" sx={{ mt: 2 }}>
                        <Button variant="contained" color="primary" onClick={() => navigate('/meus-pedidos')}>
                            Voltar para Meus Pedidos
                        </Button>
                    </Box>
                </Box>
                <FooterComponent />
            </Box>
        );
    }

    return (
        <Box display="flex" flexDirection="column" minHeight="100vh">
            {/* AppBar */}
            <AppBarComponent />

            {/* Conteúdo Principal */}
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: { xs: 2, sm: 4 },
                    backgroundColor: '#f5f5f5',
                }}
            >
                <Typography variant="h4" gutterBottom sx={{ textAlign: 'center', mb: 4 }}>
                    Detalhes do Pedido #{pedido.id}
                </Typography>

                {/* Botão de Voltar */}
                <Box sx={{ mb: 3 }}>
                    <Button variant="outlined" color="primary" onClick={() => navigate('/meus-pedidos')}>
                        Voltar para Meus Pedidos
                    </Button>
                </Box>

                {/* Informações Básicas */}
                <Typography variant="subtitle1" gutterBottom>
                    <strong>Status:</strong> {pedido.statusPedido}
                </Typography>
                <Typography variant="subtitle1" gutterBottom>
                    <strong>Total:</strong> R$ {pedido.total.toFixed(2)}
                </Typography>
                <Typography variant="subtitle1" gutterBottom>
                    <strong>Previsão de Entrega:</strong>{' '}
                    {new Date(pedido.previsaoEntrega).toLocaleDateString()} -{' '}
                </Typography>

                {/* Endereço de Entrega */}
                {pedido.enderecoEntrega && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Endereço de Entrega
                        </Typography>
                        <AddressBox>
                            <Typography variant="body1">
                                <strong>Nome:</strong> {pedido.enderecoEntrega.nome}
                            </Typography>
                            <Typography variant="body2">
                                <strong>Endereço:</strong> {pedido.enderecoEntrega.logradouro}, {pedido.enderecoEntrega.numero}
                            </Typography>
                            <Typography variant="body2">
                                <strong>Bairro:</strong> {pedido.enderecoEntrega.bairro}, {pedido.enderecoEntrega.cidade} - {pedido.enderecoEntrega.estado}, CEP: {pedido.enderecoEntrega.cep}
                            </Typography>
                        </AddressBox>
                    </>
                )}

                {/* Itens do Pedido */}
                <Divider sx={{ my: 2 }} />
                <Typography variant="h6" gutterBottom>
                    Itens do Pedido
                </Typography>
                <Grid container spacing={2}>
                    {pedido.itemPedidoDTO.map((item, index) => (
                        <Grid item xs={12} key={index}>
                            <Card sx={{ display: 'flex', alignItems: 'center', mb: 2, boxShadow: 1 }}>
                                <CardMedia
                                    component="img"
                                    sx={{ width: 100, height: 100, objectFit: 'contain', p: 1 }}
                                    image={item.produtoDTO?.imagem || 'https://via.placeholder.com/100'}
                                    alt={item.produtoDTO?.nome || 'Produto'}
                                />
                                <CardContent sx={{ flexGrow: 1 }}>
                                    <Typography variant="body1">
                                        <strong>Produto:</strong> {item.produtoDTO?.nome || 'Produto'}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        <strong>Quantidade:</strong> {item.quantidade}
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                    ))}
                </Grid>

                {/* Cupom Aplicado */}
                {pedido.cupomAplicado && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Cupom Aplicado
                        </Typography>
                        <Typography variant="body1" color="secondary">
                            <strong>Código:</strong> {pedido.cupomAplicado.codigo} - <strong>Desconto:</strong> {pedido.cupomAplicado.valorDesconto * 100}%
                        </Typography>
                    </>
                )}

                {/* Histórico de Status */}
                {pedido.historicoStatus && pedido.historicoStatus.length > 0 && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Histórico de Status
                        </Typography>
                        <StatusHistoryBox>
                            {pedido.historicoStatus.map((status, index) => (
                                <Box key={index} sx={{ mb: 1 }}>
                                    <Typography variant="body2">
                                        <strong>Data:</strong> {new Date(status.data).toLocaleString()}
                                    </Typography>
                                    <Typography variant="body2">
                                        <strong>Status:</strong> {status.descricao}
                                    </Typography>
                                    <Divider sx={{ my: 1 }} />
                                </Box>
                            ))}
                        </StatusHistoryBox>
                    </>
                )}

                {/* Nota Fiscal */}
                {pedido.notaFiscalUrl && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Nota Fiscal
                        </Typography>
                        <Box display="flex" justifyContent="center" sx={{ mt: 2 }}>
                            <a href={pedido.notaFiscalUrl} target="_blank" rel="noopener noreferrer">
                                <Button variant="contained" color="primary">
                                    Ver Nota Fiscal
                                </Button>
                            </a>
                        </Box>
                    </>
                )}
            </Box>

            {/* Footer */}
            <FooterComponent />
        </Box>
    );
};

export default PedidoDetails;
