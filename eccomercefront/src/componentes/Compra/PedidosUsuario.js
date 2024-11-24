// src/components/Pedidos/PedidosUsuarios.jsx
import React, { useEffect, useState } from 'react';
import {
    Box,
    Typography,
    List,
    ListItem,
    ListItemText,
    ListItemSecondaryAction,
    IconButton,
    CircularProgress,
    Chip,
    Divider,
    Paper,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import { getPedidosByUsuarioId } from './PedidoService';
import AppBarComponent from '../appbar'; // Certifique-se de que o caminho está correto e case-sensitive
import FooterComponent from '../Footer'; // Certifique-se de que o caminho está correto e case-sensitive
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { statusMap } from '../utils/statusMap';

const StatusChip = styled(Chip)(({ theme, status }) => ({
    backgroundColor:
        status === 'Entregue'
            ? theme.palette.success.main
            : status === 'Processando'
            ? theme.palette.info.main
            : status === 'Cancelado'
            ? theme.palette.error.main
            : theme.palette.warning.main,
    color: theme.palette.common.white,
    fontWeight: 600,
    marginBottom: theme.spacing(1),
}));

const PedidoListItem = styled(Paper)(({ theme }) => ({
    padding: theme.spacing(2),
    marginBottom: theme.spacing(2),
    position: 'relative',
    cursor: 'pointer',
    transition: 'box-shadow 0.3s',
    '&:hover': {
        boxShadow: theme.shadows[4],
    },
}));

const PedidosUsuarios = () => {
    const [pedidos, setPedidos] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const usuarioId = 7; // ID fixo do usuário, substitua conforme necessário

    useEffect(() => {
        const fetchPedidos = async () => {
            try {
                const data = await getPedidosByUsuarioId(usuarioId);
                setPedidos(data);
            } catch (error) {
                console.error('Erro ao buscar pedidos:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPedidos();
    }, [usuarioId]);

    const handleViewDetails = (pedidoId) => {
        navigate(`/pedidos/${pedidoId}`);
    };

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
                    Meus Pedidos
                </Typography>

                {loading ? (
                    <Box display="flex" justifyContent="center" alignItems="center" sx={{ mt: 4 }}>
                        <CircularProgress />
                    </Box>
                ) : pedidos.length > 0 ? (
                    <List>
                        {pedidos.map((pedido) => (
                            <React.Fragment key={pedido.id}>
                                <PedidoListItem onClick={() => handleViewDetails(pedido.id)}>
                                    {/* Status do Pedido */}
                                    <StatusChip
                                        label={statusMap[pedido.statusPedido] || pedido.statusPedido}
                                        status={statusMap[pedido.statusPedido] || pedido.statusPedido}
                                        sx={{
                                            position: 'absolute',
                                            top: 16,
                                            right: 16,
                                        }}
                                    />
                                    <ListItemText
                                        primary={
                                            <Typography variant="h6">
                                                Pedido #{pedido.id}
                                            </Typography>
                                        }
                                        secondary={
                                            <>
                                                <Typography variant="body2" color="text.secondary">
                                                    Data do Pedido: {new Date(pedido.dataPedido).toLocaleDateString()}
                                                </Typography>
                                            
                                                <Box sx={{ mt: 1 }}>
                                                    {pedido.itemPedidoDTO.map((item, index) => (
                                                        <Typography variant="body2" key={index}>
                                                            • {item.produtoDTO.nome} - Quantidade: {item.quantidade}
                                                        </Typography>
                                                    ))}
                                                </Box>
                                            </>
                                        }
                                    />
                                    <ListItemSecondaryAction>
                                        <IconButton edge="end" aria-label="details" onClick={() => handleViewDetails(pedido.id)}>
                                            <ArrowForwardIosIcon />
                                        </IconButton>
                                    </ListItemSecondaryAction>
                                </PedidoListItem>
                                <Divider />
                            </React.Fragment>
                        ))}
                    </List>
                ) : (
                    <Typography variant="h6" sx={{ textAlign: 'center', mt: 4 }}>
                        Você não possui nenhum pedido.
                    </Typography>
                )}
            </Box>

            {/* Footer */}
            <FooterComponent />
        </Box>
    );
};

export default PedidosUsuarios;
