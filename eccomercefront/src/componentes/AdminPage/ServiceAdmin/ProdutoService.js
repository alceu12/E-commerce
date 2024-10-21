import api from '../../../Service/Api';

export const createProduct = async (product) => {
  try {
    const response = await api.post('/produtos', product);
    return response.data;
  } catch (error) {
    console.error('Erro ao cadastrar produto:', error);
    throw error;
  }
};

export const getProducts = async () => {
  try {
    const response = await api.get('/produtos');
    return response.data;
  } catch (error) {
    console.error('Erro ao obter produtos:', error);
    throw error;
  }
};

export const updateProduct = async (id, updatedProduct) => {
  try {
    const response = await api.put(`/produtos/${id}`, updatedProduct);
    return response.data;
  } catch (error) {
    console.error('Erro ao atualizar produto:', error);
    throw error;
  }
};

export const deleteProduct = async (id) => {
  try {
    await api.delete(`/produtos/${id}`);
  } catch (error) {
    console.error('Erro ao excluir produto:', error);
    throw error;
  }
};
