import React, { useState } from "react";
import { Box, Container } from "@mui/material";
import Sidebar from "./componentes/AdminPage/Sidebar";
import Product from "./componentes/AdminPage/Produto";
import Category from "./componentes/AdminPage/Categoria";
import Coupon from "./componentes/AdminPage/Cupom";

const AdminPage = () => {
  const [selectedSection, setSelectedSection] = useState("products");

  const renderContent = () => {
    switch (selectedSection) {
      case "products":
        return <Product />;
      case "categories":
        return <Category />;
      case "coupons":
        return <Coupon />;
      default:
        return <Product />;
    }
  };

  return (
    <Box sx={{ display: "flex" }}>
      <Sidebar onSelect={setSelectedSection} />
      <Container sx={{ mt: 4, ml: 3, flexGrow: 1 }}>
        {renderContent()}
      </Container>
    </Box>
  );
};

export default AdminPage;
