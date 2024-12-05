import api from '../../../Service/Api';

export const createCoupon = async (coupon) => {
    try {
        const response = await api.post('/cupons', coupon);
        return response.data;
    } catch (error) {
        console.error('Erro ao criar cupom:', error);
        throw error;
    }
};

export const getCoupons = async () => {
    try {
        const response = await api.get('/cupons');
        return response.data;
    } catch (error) {
        console.error('Erro ao obter cupons:', error);
        throw error;
    }
}