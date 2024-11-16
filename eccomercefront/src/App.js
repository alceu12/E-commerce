import React from 'react';
import './App.css';
import AdminPage from './AdminPage';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homecliente from './Homecliente';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Homecliente />} />
        <Route path="/admin/*" element={<AdminPage />} />
      </Routes>
    </Router>
  );
}

export default App;
