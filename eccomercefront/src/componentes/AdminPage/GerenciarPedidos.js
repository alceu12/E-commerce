import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Button,
  CircularProgress,
  Alert,
  MenuItem,
  Select,
  FormControl,
} from '@mui/material';
import { getPedidos, atualizarStatusPedido } from './ServiceAdmin/PedidoService';
import { DataGrid } from '@mui/x-data-grid';
import { useNavigate } from 'react-router-dom'; // Importação do useNavigate
import { jwtDecode } from 'jwt-decode'; // Correção na importação
import { statusMap } from '../utils/statusMap';
import { DateTime } from 'luxon';
const GerenciarPedidos = () => {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [globalError, setGlobalError] = useState('');
  const [globalSuccess, setGlobalSuccess] = useState('');
  const [isAdmin, setIsAdmin] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const verificarRoleAdmin = () => {
      const storedToken = localStorage.getItem('userToken');

      if (!storedToken) {
        navigate('/login');
        return;
      }

      try {
        const decodedToken = jwtDecode(storedToken);

        const roles = decodedToken.role || [];
        if (roles.includes('ADMIN')) {
          setIsAdmin(true);
        } else {
          navigate('/');
        }
      } catch (error) {
        console.error('Erro ao decodificar o token:', error);
        navigate('/login');
      } finally {
        setLoading(false);
      }
    };

    verificarRoleAdmin();
  }, [navigate]);

  useEffect(() => {
    if (isAdmin) {
      const fetchPedidos = async () => {
        try {
          const pedidosData = await getPedidos();
          setPedidos(
            pedidosData.map((pedido) => ({
              ...pedido,
              novoStatus: pedido.statusPedido,
              actionLoading: false,
              actionError: '',
              actionSuccess: '',
              id: pedido.id,
              usuarioNome: pedido.usuarioDTO?.nome || 'Desconhecido',
              email: pedido.usuarioDTO?.email || 'Desconhecido',
            }))
          );
        } catch (error) {
          console.error('Erro ao buscar pedidos:', error);
          setGlobalError('Erro ao buscar pedidos. Tente novamente mais tarde.');
        } finally {
          setLoading(false);
        }
      };

      fetchPedidos();
    }
  }, [isAdmin]);

  const handleStatusChange = (pedidoId, novoStatus) => {
    setPedidos((prevPedidos) =>
      prevPedidos.map((pedido) =>
        pedido.id === pedidoId ? { ...pedido, novoStatus } : pedido
      )
    );
  };

  const handleSalvarStatus = async (pedidoId) => {
    const pedidoAtual = pedidos.find((pedido) => pedido.id === pedidoId);

    if (pedidoAtual.novoStatus === pedidoAtual.statusPedido) {
      setPedidos((prevPedidos) =>
        prevPedidos.map((pedido) =>
          pedido.id === pedidoId
            ? { ...pedido, actionError: 'O status já está atualizado.' }
            : pedido
        )
      );
      return;
    }

    setPedidos((prevPedidos) =>
      prevPedidos.map((pedido) =>
        pedido.id === pedidoId
          ? { ...pedido, actionLoading: true, actionError: '', actionSuccess: '' }
          : pedido
      )
    );

    try {
      const pedidoAtualizado = await atualizarStatusPedido(pedidoId, pedidoAtual.novoStatus);
      setPedidos((prevPedidos) =>
        prevPedidos.map((pedido) =>
          pedido.id === pedidoId
            ? {
              ...pedido,
              statusPedido: pedidoAtualizado.statusPedido,
              actionLoading: false,
              actionSuccess: 'Status atualizado com sucesso!',
            }
            : pedido
        )
      );
    } catch (error) {
      console.error('Erro ao atualizar status:', error);
      setPedidos((prevPedidos) =>
        prevPedidos.map((pedido) =>
          pedido.id === pedidoId
            ? { ...pedido, actionLoading: false, actionError: 'Erro ao atualizar status.' }
            : pedido
        )
      );
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (!isAdmin) {
    return null;
  }

  const columns = [
    { field: 'id', headerName: 'ID do Pedido', width: 130 },
    { field: 'usuarioNome', headerName: 'Usuário', width: 200 },
    { field: 'email', headerName: 'E-mail', width: 200 },
    {
      field: 'dataPedido',
      headerName: 'Data do Pedido',
      width: 150,
      renderCell: (params) => {
        if (!params.value) return '';
        const date = DateTime.fromFormat(params.value, 'yyyy-MM-dd', { zone: 'America/Sao_Paulo' });
        const formattedDate = date.toFormat('dd/MM/yyyy');
        return <span>{formattedDate}</span>;
      },
    },
    {
      field: 'total',
      headerName: 'Valor Total',
      width: 150,
      renderCell: (params) => (
        <span>
          {params.value.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL',
          })}
        </span>
      ),
    },
    {
      field: 'statusPedido',
      headerName: 'Status Atual',
      width: 180,
      renderCell: (params) => (
        <span>{statusMap[params.value] || params.value}</span>
      ),
    },
    // Coluna ajustada para mostrar o ID do produto
    {
      field: 'itens',
      headerName: 'Itens',
      width: 300,
      renderCell: (params) => {
        const items = params.row.itemPedidoDTO;
        return (
          <div>
            {items.map((item, index) => (
              <div key={index}>
                ID: {item.produtoDTO.id} - {item.produtoDTO.nome} x{item.quantidade}
              </div>
            ))}
          </div>
        );
      },
    },
    {
      field: 'novoStatus',
      headerName: 'Novo Status',
      width: 200,
      renderCell: (params) => (
        <FormControl fullWidth>
          <Select
            value={params.row.novoStatus}
            onChange={(e) => handleStatusChange(params.row.id, e.target.value)}
          >
            <MenuItem value="Processando">Processando</MenuItem>
            <MenuItem value="Enviado">Enviado</MenuItem>
            <MenuItem value="Cancelado">Cancelado</MenuItem>
            <MenuItem value="Devolução Solicitada">Devolução Solicitada</MenuItem>
          </Select>
        </FormControl>
      ),
    },
    {
      field: 'acoes',
      headerName: 'Ações',
      width: 200,
      renderCell: (params) => (
        <Box display="flex" flexDirection="column">
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleSalvarStatus(params.row.id)}
            disabled={params.row.actionLoading}
          >
            {params.row.actionLoading ? 'Atualizando...' : 'Salvar'}
          </Button>
          {params.row.actionError && (
            <Alert severity="error" sx={{ mt: 1 }}>
              {params.row.actionError}
            </Alert>
          )}
          {params.row.actionSuccess && (
            <Alert severity="success" sx={{ mt: 1 }}>
              {params.row.actionSuccess}
            </Alert>
          )}
        </Box>
      ),
    },
  ];

  return (
    <Box m={2}>
      <Typography variant="h4" gutterBottom>
        Gerenciar Pedidos
      </Typography>

      {/* Botão para voltar para /admin */}
      <Box display="flex" justifyContent="space-between" mb={2}>
        <Button variant="contained" onClick={() => navigate('/admin')}>
          Voltar para Admin
        </Button>
      </Box>

      {globalError && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {globalError}
        </Alert>
      )}
      {globalSuccess && (
        <Alert severity="success" sx={{ mb: 2 }}>
          {globalSuccess}
        </Alert>
      )}

      <Paper sx={{ height: 600, width: '100%', p: 2 }}>
        <DataGrid
          rows={pedidos}
          columns={columns}
          pageSize={10}
          rowsPerPageOptions={[10]}
          disableSelectionOnClick
          getRowHeight={() => 'auto'} // Ajusta a altura da linha para conteúdo variável
        />
      </Paper>
    </Box>
  );
};

export default GerenciarPedidos;
