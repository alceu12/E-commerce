import React from "react";
import { Box, List, ListItem, ListItemText, Typography } from "@mui/material";

const Sidebar = ({ onSelect }) => {
  const menuItems = [
    { text: "Produtos", value: "products" },
    { text: "Categorias", value: "categories" },
    { text: "Cupons", value: "coupons" },
  ];

  return (
    <Box sx={{ width: 200, bgcolor: "#f0f0f0", height: "100vh", p: 2 }}>
      <Typography variant="h6" gutterBottom>
        Administração
      </Typography>
      <List>
        {menuItems.map((item) => (
          <ListItem button key={item.value} onClick={() => onSelect(item.value)}>
            <ListItemText primary={item.text} />
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export default Sidebar;
