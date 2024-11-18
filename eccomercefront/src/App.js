import React from 'react';
import './App.css';
import AdminPage from './AdminPage';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homecliente from './Homecliente';
import ViewProduto from './produtos/ViewProduto';
import { CarrinhoProvider } from './componentes/Carrinho/CarrinhoContext';
import Carrinho from './componentes/Carrinho/Carrinho';
function App() {
  return (
    <CarrinhoProvider>
      <Router>
        <Routes>

          <Route path="/" element={<Homecliente />} />
          <Route path="/produto/:id" element={<ViewProduto />} />
          <Route path="/carrinho" element={<Carrinho />} />
          <Route path="/admin/*" element={<AdminPage />} />
        </Routes>
      </Router>
    </CarrinhoProvider >
  );
}

export default App;
