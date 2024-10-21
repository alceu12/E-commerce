import React from "react";
import { Box, Grid, Typography, Card, CardContent } from "@mui/material";
import AppBar from "./componentes/appbar";

const Homecliente = () => {
  return (
    <div>
      {/* AppBar */}
      <AppBar />

      {/* Linha 1: Card logo abaixo do AppBar */}
      <Grid container columnSpacing={2} rowSpacing={2} sx={{ mt: 2 }}>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h5">Informações Importantes</Typography>
              <Typography variant="body1">
                Aqui você pode colocar um texto ou informações importantes para o cliente.
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Linha 2: Filtro fixo à esquerda com largura maior (xs={3}) */}
        <Grid item xs={3}>
          <Box sx={{ 
            position: 'sticky', 
            top: 100, // Define a distância do topo quando a página rola
            height: 'auto', 
            border: '1px solid red', 
            padding: 2 
          }}>
            <Typography variant="h6">Filtros</Typography>
            {/* Aqui você pode colocar os filtros */}
            <Typography variant="body1">Categoria</Typography>
            <Typography variant="body1">Preço</Typography>
            <Typography variant="body1">Marca</Typography>
          </Box>
        </Grid>

        {/* Linha 3: Produtos ao lado do filtro */}
        <Grid item xs={9}>
          <Box sx={{ border: '1px solid red', minHeight: '80vh', padding: 2 }}>
            <Typography variant="h6">Produtos</Typography>
            {/* Aqui você colocaria os componentes de produtos */}
            <Typography variant="body1">Produto 1</Typography>
            <Typography variant="body1">Produto 2</Typography>
            <Typography variant="body1">Produto 3</Typography>
            {/* Adicione mais produtos conforme necessário */}
          </Box>
        </Grid>

        {/* Linha 4: Footer no final da página */}
        <Grid item xs={12}>
          <Box sx={{ border: '1px solid red', height: 100, textAlign: 'center', marginTop: 2 }}>
            <Typography variant="h6">Footer</Typography>
            <Typography variant="body2">Todos os direitos reservados.</Typography>
          </Box>
        </Grid>
      </Grid>
    </div>
  );
};

export default Homecliente;
