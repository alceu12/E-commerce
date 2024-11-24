import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, Paper } from '@mui/material';
import { getUsuarioById } from './PedidoService';

const ConfirmarEndereco = ({ avancarEtapa, voltarEtapa }) => {
    const [usuario, setUsuario] = useState(null);

    useEffect(() => {
        getUsuarioById(7) // Ajustar ID do usuário conforme necessário
            .then((data) => setUsuario(data))
            .catch((error) => console.error('Erro ao buscar usuário:', error));
    }, []);

    if (!usuario) return <Typography>Carregando endereço...</Typography>;

    const { enderecoDTO } = usuario;

    return (
        <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom>
                Confirmação do Endereço
            </Typography>
            <Typography variant="body1" gutterBottom>
                <strong>Endereço Atual:</strong>
            </Typography>
            {enderecoDTO ? (
                <Box sx={{ pl: 2, mb: 2 }}>
                    <Typography variant="body2">
                        Rua: {enderecoDTO.rua}, {enderecoDTO.numero}
                    </Typography>
                    <Typography variant="body2">
                        Complemento: {enderecoDTO.complemento || 'N/A'}
                    </Typography>
                    <Typography variant="body2">
                        CEP: {enderecoDTO.cep}
                    </Typography>
                </Box>
            ) : (
                <Typography variant="body2">Não informado</Typography>
            )}
            <Box sx={{ mt: 2 }}>
                <Button
                    variant="outlined"
                    fullWidth
                    onClick={() => alert('Alterar endereço não implementado')}
                >
                    Alterar Endereço
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ mt: 2 }}
                    onClick={() => avancarEtapa()}
                >
                    Confirmar Endereço
                </Button>
                <Button
                    variant="text"
                    fullWidth
                    sx={{ mt: 2 }}
                    onClick={voltarEtapa}
                >
                    Voltar
                </Button>
            </Box>
        </Paper>
    );
};

export default ConfirmarEndereco;
