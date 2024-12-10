// Footer.js
import React from 'react';
import { Box, Typography } from '@mui/material';

const Footer = () => {
  return (
    <Box sx={{ height: 100, textAlign: 'center', marginTop: 2, backgroundColor: '#f5f5f5', padding: 2 }}>
      <br />
      <Typography variant="body2">E-ccomerce</Typography>
      <Typography variant="body2">Todos os direitos reservados.</Typography>
    </Box>
  );
};

export default Footer;
