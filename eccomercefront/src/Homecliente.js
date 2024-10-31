import React from "react";
import { Box, Grid, Typography, Card, CardContent } from "@mui/material";
import AppBar from "./componentes/appbar";
import Produtos from "./produtos/Produto";  // Importe o novo componente de produtos

const Homecliente = () => {
  return (
      <div>
        {/* AppBar */}
        <AppBar />

        {/* Layout da página */}
        <Grid container columnSpacing={2} rowSpacing={2} sx={{ mt: 2 }}>
          {/* Linha 1: Card logo abaixo do AppBar */}
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

          {/* Linha 2: Filtro fixo à esquerda */}
          <Grid item xs={3}>
            <Box sx={{
              position: 'sticky',
              top: 100,
              height: 'auto',
              border: '1px solid red',
              padding: 2
            }}>
              <Typography variant="h6">Filtros</Typography>
              <Typography variant="body1">Categoria</Typography>
              <Typography variant="body1">Preço</Typography>
              <Typography variant="body1">Marca</Typography>
            </Box>
          </Grid>

          {/* Linha 3: Produtos usando o componente separado */}
          <Grid item xs={9}>
            <Box sx={{ border: '1px solid red', minHeight: '80vh', padding: 2 }}>
              <Typography variant="h6">Produtos</Typography>
              <Produtos />  {/* Exibe os produtos como cards */}
            </Box>
          </Grid>

          {/* Linha 4: Footer */}
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
