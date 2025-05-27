import axios from './axios';

export const createPayment = async (orderId, paymentMethod) => {
    try {
        const response = await axios.post(`/payments/order/${orderId}`, null, {
            params: { paymentMethod }
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const processPayment = async (paymentId) => {
    try {
        const response = await axios.post(`/payments/${paymentId}/process`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const getPaymentById = async (paymentId) => {
    try {
        const response = await axios.get(`/payments/${paymentId}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const refundPayment = async (paymentId) => {
    try {
        const response = await axios.post(`/payments/${paymentId}/refund`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const cancelPayment = async (paymentId) => {
    try {
        const response = await axios.post(`/payments/${paymentId}/cancel`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
}; 