import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Alert,
} from "@mui/material";
import { createCoupon, getCoupons } from "./ServiceAdmin/CupomService"; // Ajustar o caminho conforme necessário

const Coupon = () => {
  const [couponList, setCouponList] = useState([]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // Estados para os campos do formulário
  const [codigo, setCodigo] = useState("");
  const [valorDesconto, setValorDesconto] = useState("");
  const [valorMinimo, setValorMinimo] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [ativo, setAtivo] = useState(true);
  const [editMode, setEditMode] = useState(false); // Controle de modo edição
  const [editingCouponId, setEditingCouponId] = useState(null); // ID do cupom em edição

  useEffect(() => {
    // Busca a lista de cupons ao carregar o componente
    const fetchCoupons = async () => {
      try {
        const coupons = await getCoupons();
        setCouponList(coupons);
      } catch (error) {
        console.error("Erro ao obter cupons:", error);
        setError("Erro ao obter cupons. Tente novamente mais tarde.");
      }
    };

    fetchCoupons();
  }, []);

  const handleAddOrEditCoupon = async () => {
    setError("");
    setSuccess("");

    // Validações básicas
    if (!codigo.trim()) {
      setError("O código do cupom é obrigatório.");
      return;
    }
    if (!valorDesconto) {
      setError("O valor de desconto é obrigatório.");
      return;
    }
    if (!valorMinimo) {
      setError("O valor mínimo é obrigatório.");
      return;
    }
    if (!dataInicio) {
      setError("A data de início é obrigatória.");
      return;
    }

    const novoCupom = {
      codigo,
      valorDesconto: parseFloat(valorDesconto),
      valorMinimo: parseFloat(valorMinimo),
      dataInicio,
      dataFim: dataFim || null,
      ativo,
    };

    try {
      if (editMode) {
        // Se estamos em modo de edição, atualizar o cupom
        await createCoupon({ ...novoCupom, id: editingCouponId }); // Substitua por uma função de atualização real
        setSuccess("Cupom atualizado com sucesso!");
      } else {
        // Caso contrário, criar um novo cupom
        await createCoupon(novoCupom);
        setSuccess("Cupom criado com sucesso!");
      }

      // Resetar formulário e atualizar lista
      setCodigo("");
      setValorDesconto("");
      setValorMinimo("");
      setDataInicio("");
      setDataFim("");
      setAtivo(true);
      setEditMode(false);
      setEditingCouponId(null);

      const coupons = await getCoupons();
      setCouponList(coupons);
    } catch (error) {
      console.error("Erro ao salvar cupom:", error);
      setError("Erro ao salvar cupom. Verifique os dados e tente novamente.");
    }
  };

  const handleEditCoupon = (coupon) => {
    // Preenche o formulário com os dados do cupom selecionado para edição
    setCodigo(coupon.codigo);
    setValorDesconto(coupon.valorDesconto);
    setValorMinimo(coupon.valorMinimo);
    setDataInicio(coupon.dataInicio);
    setDataFim(coupon.dataFim || "");
    setAtivo(coupon.ativo);
    setEditMode(true);
    setEditingCouponId(coupon.id);
  };

  return (
    <Paper elevation={3} sx={{ p: 3 }}>
      <Typography variant="h6" gutterBottom>
        Gerenciar Cupons
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      {success && (
        <Alert severity="success" sx={{ mb: 2 }}>
          {success}
        </Alert>
      )}

      {/* Formulário para criar/editar cupom */}
      <Box component="form" sx={{ mb: 4 }}>
        <TextField
          label="Código do Cupom"
          fullWidth
          value={codigo}
          onChange={(e) => setCodigo(e.target.value)}
          sx={{ mb: 2 }}
          required
        />
        <TextField
          label="Valor de Desconto (%)"
          type="number"
          fullWidth
          value={valorDesconto}
          onChange={(e) => setValorDesconto(e.target.value)}
          sx={{ mb: 2 }}
          required
        />
        <TextField
          label="Valor Mínimo"
          type="number"
          fullWidth
          value={valorMinimo}
          onChange={(e) => setValorMinimo(e.target.value)}
          sx={{ mb: 2 }}
          required
        />
        <TextField
          label="Data de Início"
          type="date"
          fullWidth
          value={dataInicio}
          onChange={(e) => setDataInicio(e.target.value)}
          sx={{ mb: 2 }}
          InputLabelProps={{
            shrink: true,
          }}
          required
        />
        <TextField
          label="Data de Fim"
          type="date"
          fullWidth
          value={dataFim}
          onChange={(e) => setDataFim(e.target.value)}
          sx={{ mb: 2 }}
          InputLabelProps={{
            shrink: true,
          }}
        />
        <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
          <Typography variant="body1" sx={{ mr: 2 }}>
            Ativo:
          </Typography>
          <Button
            variant={ativo ? "contained" : "outlined"}
            color={ativo ? "primary" : "default"}
            onClick={() => setAtivo(!ativo)}
          >
            {ativo ? "Sim" : "Não"}
          </Button>
        </Box>
        <Button
          variant="contained"
          color="primary"
          onClick={handleAddOrEditCoupon}
          sx={{ mb: 2 }}
        >
          {editMode ? "Salvar Alterações" : "Adicionar Cupom"}
        </Button>
        {editMode && (
          <Button
            variant="outlined"
            color="secondary"
            onClick={() => {
              setEditMode(false);
              setCodigo("");
              setValorDesconto("");
              setValorMinimo("");
              setDataInicio("");
              setDataFim("");
              setAtivo(true);
              setEditingCouponId(null);
            }}
          >
            Cancelar
          </Button>
        )}
      </Box>

      {/* Tabela com a lista de cupons */}
      <Box>
        <Typography variant="subtitle1" sx={{ mb: 2 }}>
          Cupons Cadastrados:
        </Typography>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Código</TableCell>
              <TableCell>Desconto (%)</TableCell>
              <TableCell>Valor Mínimo</TableCell>
              <TableCell>Data Início</TableCell>
              <TableCell>Data Fim</TableCell>
              <TableCell>Ativo</TableCell>
              <TableCell>Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {couponList.map((coupon) => (
              <TableRow key={coupon.id}>
                <TableCell>{coupon.codigo}</TableCell>
                <TableCell>{coupon.valorDesconto * 100}%</TableCell>
                <TableCell>R$ {coupon.valorMinimo.toFixed(2)}</TableCell>
                <TableCell>
                  {new Date(coupon.dataInicio).toLocaleDateString()}
                </TableCell>
                <TableCell>
                  {coupon.dataFim
                    ? new Date(coupon.dataFim).toLocaleDateString()
                    : "N/A"}
                </TableCell>
                <TableCell>{coupon.ativo ? "Sim" : "Não"}</TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    color="primary"
                    size="small"
                    onClick={() => handleEditCoupon(coupon)}
                  >
                    Editar
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Box>
    </Paper>
  );
};

export default Coupon;
