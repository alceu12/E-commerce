import React, { useEffect, useState } from 'react';
import { List, ListItem, ListItemText, CircularProgress, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode'; // Correção na importação do jwtDecode

const Sidebar = ({ onSelect }) => {
  const navigate = useNavigate();
  const [isAdmin, setIsAdmin] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const verificarRoleAdmin = () => {
      const storedToken = localStorage.getItem('userToken');

      if (!storedToken) {
        // Redireciona para a página de login caso não exista um token
        navigate('/login');
        return;
      }

      try {
        const decodedToken = jwtDecode(storedToken);

        // Verifica se o token contém a role de ADMIN
        const roles = decodedToken.role || [];
        if (roles.includes('ADMIN')) {
          setIsAdmin(true);
        } else {
          // Se o usuário não for admin, redireciona para uma página de acesso negado ou a página inicial
          navigate('/');
        }
      } catch (error) {
        console.error('Erro ao decodificar o token:', error);
        // Redireciona para a página de login em caso de erro ao processar o token
        navigate('/login');
      } finally {
        setLoading(false);
      }
    };

    verificarRoleAdmin();
  }, [navigate]);

  if (loading) {
    // Exibe um spinner de carregamento enquanto verifica a role
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (!isAdmin) {
    // Se o usuário não for admin, não renderiza o Sidebar
    return null;
  }

  return (
    <List>
      <ListItem button onClick={() => onSelect('products')}>
        <ListItemText primary="Produtos" />
      </ListItem>
      <ListItem button onClick={() => onSelect('categories')}>
        <ListItemText primary="Categorias" />
      </ListItem>
      <ListItem button onClick={() => onSelect('coupons')}>
        <ListItemText primary="Cupons" />
      </ListItem>
      <ListItem button onClick={() => onSelect('gerenciar-pedidos')}>
        <ListItemText primary="GerenciarPedidos" />
      </ListItem>
    </List>
  );
};

export default Sidebar;
