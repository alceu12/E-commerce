// Logo.js

import React from 'react';
import { Box } from '@mui/material';
import logoImage from './logo.png'; // Certifique-se de ajustar o caminho conforme a localização do seu arquivo

const Logo = ({ width = '100px', height = '100px' }) => {
  return (
    <Box
      component="img"
      src={logoImage}
      alt="Logo"
      sx={{
        width: width,
        height: height,
      }}
    />
  );
};

export default Logo;
