// src/componentes/Compra/Pagamento.jsx

import React, { useState } from 'react';
import {
    Box,
    Typography,
    Button,
    Paper,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    TextField,
    CircularProgress,
} from '@mui/material';
import { atualizarStatusPedido } from './PedidoService'; // Ajuste o caminho conforme necessário
import { useNavigate } from 'react-router-dom';
import { statusMap } from '../utils/statusMap';

const Pagamento = ({ pedido, voltarEtapa }) => {
    const [formaPagamento, setFormaPagamento] = useState('');
    const [detalhesPagamento, setDetalhesPagamento] = useState({});
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handlePagamento = () => {
        if (!formaPagamento) {
            alert('Por favor, selecione uma forma de pagamento.');
            return;
        }

        setLoading(true);

        // Simular processamento de pagamento
        setTimeout(() => {
            const novoStatusDescricao = "Processando";

            atualizarStatusPedido(pedido.id, novoStatusDescricao)
                .then(() => {
                    setLoading(false);
                    navigate('/pedidos');
                })
                .catch(() => {
                    setLoading(false);
                    alert('Erro ao processar pagamento. Por favor, tente novamente.');
                });
        }, 2000);
    };

    return (
        <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom>
                Pagamento
            </Typography>
            <Typography variant="body1">
                Pedido ID: <strong>{pedido.id}</strong>
            </Typography>
            <Typography variant="body1">
                Total: {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(pedido.total)}
            </Typography>

            <Typography variant="body1">
                Status: {statusMap[pedido.statusPedido]}
            </Typography>
            <Typography variant="body1">
                Frete:{' '}
                <Typography
                    component="span"
                    variant="body1"
                    sx={{
                        textDecoration: 'line-through',
                        color: 'gray',
                    }}
                >
                    R$ 10,00
                </Typography>{' '}
                - Grátis
            </Typography>

            <Box
                sx={{
                    mt: 3,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <FormControl variant="outlined" sx={{ mb: 2, width: '40%' }}>
                    <InputLabel sx={{ fontSize: '0.8rem' }}>Forma de Pagamento</InputLabel>
                    <Select
                        value={formaPagamento}
                        onChange={(e) => setFormaPagamento(e.target.value)}
                        label="Forma de Pagamento"
                        size="small"
                        sx={{
                            fontSize: '0.8rem',
                        }}
                        MenuProps={{
                            PaperProps: {
                                sx: {
                                    fontSize: '0.8rem',
                                },
                            },
                        }}
                    >
                        <MenuItem value="cartao" sx={{ fontSize: '0.8rem' }}>
                            Cartão de Crédito
                        </MenuItem>
                        <MenuItem value="pix" sx={{ fontSize: '0.8rem' }}>
                            Pix
                        </MenuItem>
                    </Select>
                </FormControl>

                {formaPagamento === 'cartao' && (
                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <TextField
                            label="Número do Cartão"
                            variant="outlined"
                            size="small"
                            sx={{ mb: 2, width: '250%' }}
                            InputProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            InputLabelProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            value={detalhesPagamento.numeroCartao || ''}
                            onChange={(e) =>
                                setDetalhesPagamento({ ...detalhesPagamento, numeroCartao: e.target.value })
                            }
                        />
                        <TextField
                            label="Nome no Cartão"
                            variant="outlined"
                            size="small"
                            sx={{ mb: 2, width: '250%'  }}
                            InputProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            InputLabelProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            value={detalhesPagamento.nomeCartao || ''}
                            onChange={(e) =>
                                setDetalhesPagamento({ ...detalhesPagamento, nomeCartao: e.target.value })
                            }
                        />
                        <TextField
                            label="Validade"
                            variant="outlined"
                            size="small"
                            sx={{ mb: 2, width: '250%'  }}
                            placeholder="MM/AA"
                            InputProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            InputLabelProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            value={detalhesPagamento.validade || ''}
                            onChange={(e) =>
                                setDetalhesPagamento({ ...detalhesPagamento, validade: e.target.value })
                            }
                        />
                        <TextField
                            label="CVV"
                            variant="outlined"
                            size="small"
                            sx={{ mb: 2, width: '250%'  }}
                            InputProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            InputLabelProps={{
                                style: { fontSize: '0.8rem' },
                            }}
                            value={detalhesPagamento.cvv || ''}
                            onChange={(e) =>
                                setDetalhesPagamento({ ...detalhesPagamento, cvv: e.target.value })
                            }
                        />
                    </Box>
                )}

                {formaPagamento === 'pix' && (
                    <Box sx={{ textAlign: 'center' }}>
                        <Typography variant="body1" sx={{ mb: 2, fontSize: '0.8rem' }}>
                            Escaneie o QR Code abaixo ou utilize a chave Pix para efetuar o pagamento.
                        </Typography>
                        <Box display="flex" justifyContent="center" sx={{ mb: 2 }}>
                            <img
                                src="https://codigosdebarrasbrasil.com.br/wp-content/uploads/2019/09/codigo_qr-300x300.png"
                                alt="QR Code Pix"
                                style={{ width: '150px', height: '150px' }}
                            />
                        </Box>
                    </Box>
                )}

                <Box sx={{ mt: 3, width: '40%' }}>
                    <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        sx={{
                            mb: 2,
                            fontSize: '0.8rem',
                            width: '100%',
                        }}
                        onClick={handlePagamento}
                        disabled={loading}
                        startIcon={loading && <CircularProgress size={16} />}
                    >
                        {loading ? 'Processando...' : 'Finalizar Pagamento'}
                    </Button>
                </Box>

                <Box sx={{ width: '40%' }}>
                    <Button
                        variant="text"
                        size="small"
                        sx={{
                            mb: 2,
                            fontSize: '0.8rem',
                            width: '100%',
                        }}
                        onClick={voltarEtapa}
                    >
                        Voltar
                    </Button>
                </Box>
            </Box>
        </Paper>
    );
};

export default Pagamento;
