import React from 'react';
import './App.css';
import AdminPage from './AdminPage';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homecliente from './Homecliente';
import ViewProduto from './produtos/ViewProduto';
import { CarrinhoProvider } from './componentes/Carrinho/CarrinhoContext';
import Carrinho from './componentes/Carrinho/Carrinho';
import FinalizarCompra from './componentes/Compra/FinalizarCompra';
import PedidosUsuario from './componentes/Compra/PedidosUsuario';
import { ThemeProvider } from '@mui/material/styles';
import theme from './theme';
import PedidoDetails from './componentes/Compra/PedidoDetails';
function App() {
  return (
    <ThemeProvider theme={theme}>
      <CarrinhoProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Homecliente />} />
            <Route path="/produto/:id" element={<ViewProduto />} />
            <Route path="/carrinho" element={<Carrinho />} />
            <Route path="/admin/*" element={<AdminPage />} />
            <Route path="/finalizar-compra" element={<FinalizarCompra />} />
            <Route path="/meus-pedidos" element={<PedidosUsuario />} />
            <Route path="/pedidos/:pedidoId" element={<PedidoDetails />} />
          </Routes>
        </Router>
      </CarrinhoProvider >
    </ThemeProvider>
  );
}

export default App;
