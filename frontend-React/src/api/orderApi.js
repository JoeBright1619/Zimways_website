import axios from './axios';

export const createOrder = async (customerId, driverId, orderData) => {
    try {
        const response = await axios.post(`/orders/customer/${customerId}/driver/${driverId}`, orderData);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const updateOrderStatus = async (orderId, status) => {
    try {
        const response = await axios.put(`/orders/${orderId}/status`, null, {
            params: { status }
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const getOrderById = async (orderId) => {
    try {
        const response = await axios.get(`/orders/${orderId}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const getCustomerOrders = async (customerId) => {
    try {
        const response = await axios.get(`/orders/customer/${customerId}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

export const cancelOrder = async (orderId) => {
    try {
        const response = await axios.post(`/orders/${orderId}/cancel`, null, {
            params: { cancellationType: 'CANCELLED_BY_CUSTOMER' }
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
}; 