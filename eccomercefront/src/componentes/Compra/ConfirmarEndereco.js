import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, Button, Paper, TextField, Alert } from '@mui/material';
import { getUsuarioById, updateAddressById } from './PedidoService';
import { jwtDecode } from 'jwt-decode'; // Ajuste na importação
import { faEdit, faSave } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { CarrinhoContext } from '../Carrinho/CarrinhoContext';

const ConfirmarEndereco = ({ avancarEtapa, voltarEtapa }) => {
    const [usuario, setUsuario] = useState(null);
    const [editAddress, setEditAddress] = useState(false);
    const [addressData, setAddressData] = useState({
        cep: '',
        rua: '',
        numero: '',
        complemento: '',
        bairro: '',
        cidade: '',
        estado: '',
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const navigate = useNavigate();
    const { finalizarCarrinho } = useContext(CarrinhoContext);

    useEffect(() => {
        const storedToken = localStorage.getItem('userToken');
        if (storedToken) {
            try {
                const decodedToken = jwtDecode(storedToken);
                const userId = decodedToken.id;

                if (!userId) {
                    throw new Error('ID do usuário não encontrado no token.');
                }

                getUsuarioById(userId)
                    .then((data) => {
                        setUsuario(data);
                        if (data.enderecoDTO) {
                            setAddressData({
                                cep: data.enderecoDTO.cep || '',
                                rua: data.enderecoDTO.rua || '',
                                numero: data.enderecoDTO.numero || '',
                                complemento: data.enderecoDTO.complemento || '',
                                bairro: data.enderecoDTO.bairro || '',
                                cidade: data.enderecoDTO.cidade || '',
                                estado: data.enderecoDTO.estado || '',
                            });
                        } else {
                            navigate('/profile', {
                                state: { message: 'Por favor, cadastre seu endereço antes de continuar.' },
                            });
                        }
                    })
                    .catch((error) => {
                        console.error('Erro ao buscar usuário:', error);
                        setError('Erro ao buscar dados do usuário.');
                    });
            } catch (error) {
                console.error('Erro ao decodificar o token:', error);
                setError('Erro ao decodificar o token.');
            }
        } else {
            setError('Token de autenticação não encontrado.');
        }
    }, [navigate]);

    useEffect(() => {
        const fetchAddress = async () => {
            const cep = addressData.cep.replace(/\D/g, '');
            if (cep.length === 8) {
                try {
                    const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
                    const data = await response.json();
                    if (data.erro) {
                        setError('CEP não encontrado.');
                        setAddressData((prevState) => ({
                            ...prevState,
                            rua: '',
                            bairro: '',
                            cidade: '',
                            estado: '',
                        }));
                    } else {
                        setError('');
                        setAddressData((prevState) => ({
                            ...prevState,
                            rua: data.logradouro,
                            bairro: data.bairro,
                            cidade: data.localidade,
                            estado: data.uf,
                        }));
                    }
                } catch (err) {
                    console.error('Erro ao buscar o CEP:', err);
                    setError('Erro ao buscar o CEP.');
                }
            }
        };

        if (addressData.cep) {
            fetchAddress();
        }
    }, [addressData.cep]);

    const handleAddressChange = (e) => {
        const { name, value } = e.target;
        setAddressData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSaveAddress = async () => {
        setError('');
        setSuccess('');

        if (!addressData.cep.trim()) {
            setError('CEP é obrigatório.');
            return;
        }
        if (!/^\d{5}-?\d{3}$/.test(addressData.cep)) {
            setError('CEP inválido. Deve ter 8 dígitos, opcionalmente com hífen (e.g., 12345-678).');
            return;
        }
        if (!addressData.numero.trim()) {
            setError('Número é obrigatório.');
            return;
        }

        try {
            const userId = usuario.id;
            await updateAddressById(userId, addressData);
            setSuccess('Endereço atualizado com sucesso!');
            setEditAddress(false);
            setUsuario((prevUser) => ({
                ...prevUser,
                enderecoDTO: { ...addressData },
            }));
        } catch (err) {
            console.error(err);
            setError('Falha ao atualizar o endereço. Tente novamente.');
        }
    };

    const handleConfirmAddress = () => {
        finalizarCarrinho();
        avancarEtapa();
    };

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
                        Bairro: {enderecoDTO.bairro}
                    </Typography>
                    <Typography variant="body2">
                        Cidade/Estado: {enderecoDTO.cidade} - {enderecoDTO.estado}
                    </Typography>
                    <Typography variant="body2">
                        CEP: {enderecoDTO.cep}
                    </Typography>
                </Box>
            ) : (
                <Typography variant="body2">Não informado</Typography>
            )}
            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}
            <Box sx={{ mt: 2 }}>
                {!editAddress ? (
                    <Button
                        variant="outlined"
                        size="small"
                        sx={{
                            mt: 2,
                            display: 'flex',
                            mb: 2,
                            width: '30%',
                        }}
                        onClick={() => setEditAddress(true)}
                        startIcon={<FontAwesomeIcon icon={faEdit} />}
                    >
                        Alterar Endereço
                    </Button>
                ) : (
                    <Box component="form" sx={{ mb: 2, maxWidth: "20%" }}>
                        <TextField
                            label="CEP"
                            variant="outlined"
                            fullWidth
                            name="cep"
                            value={addressData.cep}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                            required
                            error={addressData.cep.trim() === ''}
                            helperText={addressData.cep.trim() === '' ? 'CEP é obrigatório.' : ''}
                        />
                        <TextField
                            label="Rua"
                            variant="outlined"
                            fullWidth
                            name="rua"
                            value={addressData.rua}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                        <TextField
                            label="Número"
                            variant="outlined"
                            fullWidth
                            name="numero"
                            value={addressData.numero}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                            required
                            error={addressData.numero.trim() === ''}
                            helperText={addressData.numero.trim() === '' ? 'Número é obrigatório.' : ''}
                        />
                        <TextField
                            label="Complemento"
                            variant="outlined"
                            fullWidth
                            name="complemento"
                            value={addressData.complemento}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                        />
                        <TextField
                            label="Bairro"
                            variant="outlined"
                            fullWidth
                            name="bairro"
                            value={addressData.bairro}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                        <TextField
                            label="Cidade"
                            variant="outlined"
                            fullWidth
                            name="cidade"
                            value={addressData.cidade}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                        <TextField
                            label="Estado"
                            variant="outlined"
                            fullWidth
                            name="estado"
                            value={addressData.estado}
                            onChange={handleAddressChange}
                            sx={{ mb: 2 }}
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                        <Button
                            variant="contained"
                            color="primary"
                            size="small"
                            sx={{ fontSize: '0.8rem' }}
                            fullWidth
                            onClick={handleSaveAddress}
                            startIcon={<FontAwesomeIcon icon={faSave} />}
                        >
                            Salvar Endereço
                        </Button>
                        <Button
                            variant="outlined"
                            color="secondary"
                            size="small"
                            sx={{ mt: 1, fontSize: '0.8rem' }}
                            fullWidth
                            onClick={() => setEditAddress(false)}
                        >
                            Cancelar
                        </Button>
                    </Box>
                )}
                <Button
                    variant="contained"
                    color="primary"
                    size="small"
                    sx={{
                        mt: 2,
                        display: 'flex',
                        mb: 2,
                        width: '30%',
                    }}
                    onClick={handleConfirmAddress}
                >
                    Confirmar Endereço
                </Button>
                <Button
                    variant="text"
                    size="small"
                    sx={{
                        mt: 2,
                        display: 'flex',
                        mb: 2,
                        width: '30%',
                    }}
                    onClick={voltarEtapa}
                >
                    Voltar
                </Button>
            </Box>
        </Paper>
    );
};

export default ConfirmarEndereco;
