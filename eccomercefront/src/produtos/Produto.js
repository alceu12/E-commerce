import React from "react";
import { Grid, Typography, Card, CardContent, CardMedia, Button, Box } from "@mui/material";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";

// Lista de produtos replicados para chegar a 30
const produtos = [
    ...Array(30).fill({
        nome: "Camisa Azul",
        descricao: "Camisa Azul Royal 100% Poliéster.",
        imagem: "https://images.tcdn.com.br/img/img_prod/275189/camisa_azul_royal_100_poliester_para_sublimacao_m_2701_1_20200722153204.jpg",
        preco: 49.90,
    }).map((produto, index) => ({ ...produto, id: index + 1 })),
    ...Array(10).fill({
        nome: "Tênis Feminino Casual",
        descricao: "Tênis Feminino Casual Elôa Branco e Preto.",
        imagem: "https://lojavittal.fbitsstatic.net/img/p/tenis-feminino-casual-eloa-branco-e-preto-vittal-150815/339657.jpg?w=1000&h=1000&v=no-value",
        preco: 129.90,
    }).map((produto, index) => ({ ...produto, id: index + 11 })),
    ...Array(10).fill({
        nome: "Smart TV 40''",
        descricao: "Smart TV LED, Compatível com Normas de TV Digital.",
        imagem: "https://tvlar.vtexassets.com/arquivos/ids/19906918/TV-Suba-40-Polegadas-LED-Compativel-com-as-Normas-de-TV-Digital-SB-TV3201-Preta-110V.png?v=637922964213730000.jpg",
        preco: 1899.99,
    }).map((produto, index) => ({ ...produto, id: index + 21 })),
];

const Produtos = () => {
    const handleAddToCart = (produtoId) => {
        console.log(`Produto ${produtoId} adicionado ao carrinho.`);
    };

    return (
        <Grid container spacing={3} sx={{ mt: 2 }}>
            {produtos.map((produto) => (
                <Grid item xs={12} sm={6} md={4} key={produto.id}>
                    <Card sx={{ maxWidth: 345 }}>
                        <CardMedia
                            component="img"
                            height="180"
                            image={produto.imagem}
                            alt={produto.nome}
                            sx={{ cursor: "pointer" }}
                            onClick={() => console.log(`Visualizar detalhes do produto ${produto.id}`)}
                        />
                        <CardContent>
                            <Typography gutterBottom variant="h6" component="div">
                                {produto.nome}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {produto.descricao}
                            </Typography>
                            <Box sx={{ mt: 2 }}>
                                <Typography variant="h5" color="primary">
                                    R$ {produto.preco.toFixed(2)}
                                </Typography>
                            </Box>
                            <Button
                                variant="contained"
                                color="primary"
                                sx={{ mt: 2 }}
                                startIcon={<ShoppingCartIcon />}
                                onClick={() => handleAddToCart(produto.id)}
                            >
                                Adicionar ao Carrinho
                            </Button>
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
};

export default Produtos;
