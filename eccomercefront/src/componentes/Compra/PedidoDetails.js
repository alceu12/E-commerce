import React, { useEffect, useState } from 'react';
import {
    Box,
    Typography,
    Button,
    CircularProgress,
    Divider,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Alert,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { useParams, useNavigate } from 'react-router-dom';
import { getPedidosByUsuarioId, atualizarStatusPedido } from './PedidoService';
import AppBarComponent from '../appbar';
import FooterComponent from '../Footer';
import { statusMap } from '../utils/statusMap';
import { jwtDecode } from 'jwt-decode'; // Correção na importação
import { DateTime } from 'luxon';
// Importações das bibliotecas para gerar PDF
import jsPDF from 'jspdf';
import 'jspdf-autotable';

// Estilização para a seção de Endereço de Entrega
const AddressBox = styled(Box)(({ theme }) => ({
    padding: theme.spacing(2),
    backgroundColor: '#e0f7fa',
    borderRadius: theme.shape.borderRadius,
    marginBottom: theme.spacing(2),
}));

// Estilização para o Histórico de Status
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
    const [error, setError] = useState('');
    const [actionLoading, setActionLoading] = useState(false);
    const [actionError, setActionError] = useState('');
    const [actionSuccess, setActionSuccess] = useState('');

    useEffect(() => {
        const storedToken = localStorage.getItem('userToken');
        if (storedToken) {
            try {
                const decodedToken = jwtDecode(storedToken);
                const usuarioId = decodedToken.id;

                if (!usuarioId) {
                    throw new Error('ID do usuário não encontrado no token.');
                }

                const fetchPedido = async () => {
                    try {
                        const pedidos = await getPedidosByUsuarioId(usuarioId);
                        const encontrado = pedidos.find((p) => p.id === parseInt(pedidoId));
                        if (encontrado) {
                            setPedido(encontrado);
                        } else {
                            setError('Pedido não encontrado.');
                        }
                    } catch (error) {
                        console.error('Erro ao buscar pedido:', error);
                        setError('Erro ao buscar pedido. Tente novamente mais tarde.');
                    } finally {
                        setLoading(false);
                    }
                };

                fetchPedido();
            } catch (error) {
                console.error('Erro ao decodificar o token:', error);
                navigate('/login');
            }
        } else {
            navigate('/login');
        }
    }, [pedidoId, navigate]);

    const formatarMoeda = (valor) => {
        return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    };

    const handleCancelarPedido = async () => {
        setActionLoading(true);
        setActionError('');
        setActionSuccess('');
        try {
            const updatedPedido = await atualizarStatusPedido(pedido.id, 'Cancelado');
            setPedido((prevPedido) => ({
                ...prevPedido,
                statusPedido: updatedPedido.statusPedido,
            }));
            setActionSuccess('Pedido cancelado com sucesso.');
        } catch (error) {
            console.error('Erro ao cancelar pedido:', error);
            setActionError('Erro ao cancelar pedido. Tente novamente mais tarde.');
        } finally {
            setActionLoading(false);
        }
    };

    const handleSolicitarDevolucao = async () => {
        setActionLoading(true);
        setActionError('');
        setActionSuccess('');
        try {
            const updatedPedido = await atualizarStatusPedido(pedido.id, 'Devolução Solicitada');
            setPedido((prevPedido) => ({
                ...prevPedido,
                statusPedido: updatedPedido.statusPedido,
            }));
            setActionSuccess('Solicitação de devolução enviada com sucesso.');
        } catch (error) {
            console.error('Erro ao solicitar devolução:', error);
            setActionError('Erro ao solicitar devolução. Tente novamente mais tarde.');
        } finally {
            setActionLoading(false);
        }
    };

    const handlePagarPedido = async () => {
        setActionLoading(true);
        setActionError('');
        setActionSuccess('');
        try {
            const updatedPedido = await atualizarStatusPedido(pedido.id, 'Processando');
            setPedido((prevPedido) => ({
                ...prevPedido,
                statusPedido: updatedPedido.statusPedido,
            }));
            setActionSuccess('Pagamento realizado com sucesso. Seu pedido está sendo processado.');
        } catch (error) {
            console.error('Erro ao processar pagamento:', error);
            setActionError('Erro ao processar pagamento. Tente novamente mais tarde.');
        } finally {
            setActionLoading(false);
        }
    };

    // Função para baixar a nota fiscal
    const handleDownloadInvoice = () => {
        const doc = new jsPDF();

        // Título
        doc.setFontSize(18);
        doc.text('Nota Fiscal', 14, 22);

        // Informações do pedido
        doc.setFontSize(12);
        doc.text(`Pedido #: ${pedido.id}`, 14, 30);
        doc.text(`Data do Pedido: ${new Date(pedido.dataPedido).toLocaleDateString()}`, 14, 36);
        doc.text(`Status: ${statusMap[pedido.statusPedido] || pedido.statusPedido}`, 14, 42);

        // Informações do cliente
        doc.text('Dados do Cliente:', 14, 50);
        doc.text(`Nome: ${pedido.usuarioDTO.nome}`, 14, 56);
        doc.text(`Email: ${pedido.usuarioDTO.email}`, 14, 62);

        // Endereço de entrega
        doc.text('Endereço de Entrega:', 14, 70);
        const endereco = pedido.enderecoEntrega;
        const addressLines = [
            `CEP: ${endereco.cep}`,
            `Rua: ${endereco.rua}, Nº ${endereco.numero}`,
            `Complemento: ${endereco.complemento || 'N/A'}`,
            `Bairro: ${endereco.bairro}`,
            `Cidade: ${endereco.cidade} - ${endereco.estado}`,
        ];
        let addressY = 76;
        addressLines.forEach((line) => {
            doc.text(line, 14, addressY);
            addressY += 6;
        });

        // Itens do pedido
        const itemColumns = ['Descrição', 'Quantidade', 'Preço Unitário', 'Total'];
        const itemRows = pedido.itemPedidoDTO.map((item) => [
            item.produtoDTO.nome,
            item.quantidade,
            formatarMoeda(item.precoUnitario),
            formatarMoeda(item.quantidade * item.precoUnitario),
        ]);

        // Adiciona a tabela de itens
        doc.autoTable({
            startY: addressY + 6,
            head: [itemColumns],
            body: itemRows,
        });

        // Calcula o subtotal
        const subtotal = pedido.itemPedidoDTO.reduce(
            (acc, item) => acc + item.quantidade * item.precoUnitario,
            0
        );

        // Posição vertical após a tabela
        let finalY = doc.lastAutoTable.finalY || addressY + 6;
        finalY += 10;

        // Exibe o subtotal
        doc.text(`Subtotal: ${formatarMoeda(subtotal)}`, 14, finalY);
        finalY += 6;

        // Verifica se há cupom aplicado
        let descontoValor = 0;
        if (pedido.cupomAplicado) {
            descontoValor = subtotal * pedido.cupomAplicado.valorDesconto;
            doc.text(`Cupom Aplicado: ${pedido.cupomAplicado.codigo}`, 14, finalY);
            finalY += 6;
            doc.text(`Desconto: -${formatarMoeda(descontoValor)}`, 14, finalY);
            finalY += 6;
        }

        // Exibe o total final
        doc.text(`Total: ${formatarMoeda(pedido.total)}`, 14, finalY);

        // Baixa o PDF
        doc.save(`NotaFiscal_Pedido_${pedido.id}.pdf`);
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
                        {error || 'Pedido não encontrado.'}
                    </Typography>
                    <Box display="flex" justifyContent="center" sx={{ mt: 2 }}>
                        <Button variant="contained" color="primary" onClick={() => navigate('/pedidos')}>
                            Voltar para Meus Pedidos
                        </Button>
                    </Box>
                </Box>
                <FooterComponent />
            </Box>
        );
    }

    const {
        itemPedidoDTO,
        historicoStatus,
        statusPedido,
        total,
        dataPedido,
        cupomAplicado,
        usuarioDTO, 
        enderecoEntrega,
    } = pedido;
    const date = DateTime.fromFormat(dataPedido, 'yyyy-MM-dd', { zone: 'America/Sao_Paulo' });
    const formattedDate = date.toFormat('dd/MM/yyyy');
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
                    <Button variant="outlined" color="primary" onClick={() => navigate('/pedidos')}>
                        Voltar para Meus Pedidos
                    </Button>
                </Box>

                {/* Mensagens de Ação */}
                {actionError && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        {actionError}
                    </Alert>
                )}
                {actionSuccess && (
                    <Alert severity="success" sx={{ mb: 2 }}>
                        {actionSuccess}
                    </Alert>
                )}

                {/* Informações Básicas */}
                <Typography variant="subtitle1" gutterBottom>
                    <strong>Status:</strong> {statusMap[statusPedido] || statusPedido}
                </Typography>
                <Typography variant="subtitle1" gutterBottom>
                    <strong>Total:</strong> {formatarMoeda(total)}
                </Typography>
                <Typography variant="subtitle1" gutterBottom>
                    <strong>Data do Pedido:</strong> {formattedDate}
                </Typography>

                {/* Botões Condicionais Baseados no Status */}
                <Box sx={{ mt: 2, mb: 2 }}>
                    {statusPedido === 'PENDING_PAYMENT' && (
                        <Box>
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={handlePagarPedido}
                                disabled={actionLoading}
                            >
                                {actionLoading ? 'Processando pagamento...' : 'Pagar Agora'}
                            </Button>

                            <Button
                                variant="contained"
                                color="error"
                                onClick={handleCancelarPedido}
                                disabled={actionLoading}
                                sx={{ paddingLeft: 2, paddingRight: 2, marginLeft: 2 }}
                            >
                                {actionLoading ? 'Cancelando...' : 'Cancelar Pedido'}
                            </Button>
                        </Box>
                    )}

                    {statusPedido === 'PROCESSING' && (
                        <Button
                            variant="contained"
                            color="error"
                            onClick={handleCancelarPedido}
                            disabled={actionLoading}
                        >
                            {actionLoading ? 'Cancelando...' : 'Cancelar Pedido'}
                        </Button>
                    )}
                    {statusPedido === 'SHIPPED' && (
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={handleSolicitarDevolucao}
                            disabled={actionLoading}
                        >
                            {actionLoading ? 'Solicitando...' : 'Solicitar Devolução'}
                        </Button>
                    )}
                </Box>

                {/* Botão para Baixar a Nota Fiscal */}
                <Box sx={{ mb: 2 }}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleDownloadInvoice}
                    >
                        Baixar Nota Fiscal
                    </Button>
                </Box>

                {/* Endereço de Entrega */}
                {enderecoEntrega && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Endereço de Entrega
                        </Typography>
                        <AddressBox>
                            <Typography variant="body1">
                                <strong>CEP:</strong> {enderecoEntrega.cep}
                            </Typography>
                            <Typography variant="body1">
                                <strong>Rua:</strong> {enderecoEntrega.rua}
                            </Typography>
                            <Typography variant="body1">
                                <strong>Número:</strong> {enderecoEntrega.numero}
                            </Typography>
                            <Typography variant="body1">
                                <strong>Complemento:</strong> {enderecoEntrega.complemento || 'N/A'}
                            </Typography>
                            <Typography variant="body1">
                                <strong>Bairro:</strong> {enderecoEntrega.bairro}
                            </Typography>
                            <Typography variant="body1">
                                <strong>Cidade:</strong> {enderecoEntrega.cidade}
                            </Typography>
                            <Typography variant="body1">
                                <strong>Estado:</strong> {enderecoEntrega.estado}
                            </Typography>
                        </AddressBox>
                    </>
                )}

                {/* Cupom Aplicado */}
                {cupomAplicado && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Cupom Aplicado
                        </Typography>
                        <Typography variant="body1" color="secondary">
                            <strong>Código:</strong> {cupomAplicado.codigo} - <strong>Desconto:</strong>{' '}
                            {cupomAplicado.valorDesconto * 100}%
                        </Typography>
                    </>
                )}

                {/* Itens do Pedido */}
                <Divider sx={{ my: 2 }} />
                <Typography variant="h6" gutterBottom>
                    Itens do Pedido
                </Typography>
                <TableContainer component={Paper} sx={{ mb: 4 }}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>
                                    <strong>Descrição</strong>
                                </TableCell>
                                <TableCell align="right">
                                    <strong>Quantidade</strong>
                                </TableCell>
                                <TableCell align="right">
                                    <strong>Preço Unitário</strong>
                                </TableCell>
                                <TableCell align="right">
                                    <strong>Total</strong>
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {itemPedidoDTO.map((item) => (
                                <TableRow key={item.id}>
                                    <TableCell>{item.produtoDTO.nome}</TableCell>
                                    <TableCell align="right">{item.quantidade}</TableCell>
                                    <TableCell align="right">{formatarMoeda(item.precoUnitario)}</TableCell>
                                    <TableCell align="right">
                                        {formatarMoeda(item.quantidade * item.precoUnitario)}
                                    </TableCell>
                                </TableRow>
                            ))}

                            {cupomAplicado &&
                                itemPedidoDTO.map((item) => (
                                    <TableRow key={`cupom-${item.id}`}>
                                        <TableCell colSpan={1}>
                                            <strong>Cupom Aplicado</strong>
                                        </TableCell>
                                        <TableCell align="right">
                                            <strong>{cupomAplicado.codigo}</strong>
                                        </TableCell>
                                        <TableCell align="right">
                                            <strong>Desconto:</strong>
                                        </TableCell>
                                        <TableCell align="right">
                                            <strong>
                                                {formatarMoeda(
                                                    item.precoUnitario * cupomAplicado.valorDesconto
                                                )}
                                            </strong>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            <TableRow>
                                <TableCell colSpan={3} align="right">
                                    <strong>Subtotal:</strong>
                                </TableCell>
                                <TableCell align="right">{formatarMoeda(total)}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell rowSpan={3} />
                                <TableCell colSpan={2} align="right">
                                    <strong>Total:</strong>
                                </TableCell>
                                <TableCell align="right">
                                    <strong>{formatarMoeda(total)}</strong>
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </TableContainer>

                {/* Histórico de Status */}
                {historicoStatus && historicoStatus.length > 0 && (
                    <>
                        <Divider sx={{ my: 2 }} />
                        <Typography variant="h6" gutterBottom>
                            Histórico de Status
                        </Typography>
                        <StatusHistoryBox>
                            {historicoStatus.map((status, index) => (
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
            </Box>
            <FooterComponent />
        </Box>
    );
};

export default PedidoDetails;
