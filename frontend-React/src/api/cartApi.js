import axios from './axios';

/**
 * Get cart for a specific customer
 * @param {string} customerId - UUID of the customer
 * @returns {Promise<Object>} Cart object
 */
export const getCustomerCart = async (customerId) => {
    try {
        const response = await axios.get(`/carts/customer/${customerId}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Create a new cart for a customer
 * @param {string} customerId - UUID of the customer
 * @returns {Promise<Object>} Created cart object
 */
export const createCustomerCart = async (customerId) => {
    try {
        const response = await axios.post(`/carts/customer/${customerId}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Add an item to customer's cart
 * @param {string} customerId - UUID of the customer
 * @param {string} itemId - UUID of the item to add
 * @param {number} quantity - Quantity of the item to add
 * @returns {Promise<Object>} Updated cart object
 */
export const addItemToCart = async (customerId, itemId, quantity) => {
    try {
        const response = await axios.post(`/carts/customer/${customerId}/add-item`, {
            itemId,
            quantity
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Remove an item from customer's cart
 * @param {string} customerId - UUID of the customer
 * @param {string} itemId - UUID of the item to remove
 * @param {number} quantity - Quantity of the item to remove
 * @returns {Promise<Object>} Updated cart object
 */
export const removeItemFromCart = async (customerId, itemId, quantity) => {
    try {
        const response = await axios.post(`/carts/customer/${customerId}/remove-item`, {
            itemId,
            quantity
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Checkout customer's cart (clears the cart after successful checkout)
 * @param {string} customerId - UUID of the customer
 * @returns {Promise<string>} Success message
 */
export const checkoutCart = async (customerId) => {
    try {
        const response = await axios.post(`/carts/customer/${customerId}/checkout`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Delete a cart (Admin only)
 * @param {string} cartId - UUID of the cart to delete
 * @returns {Promise<void>}
 */
export const deleteCart = async (cartId) => {
    try {
        await axios.delete(`/carts/${cartId}`);
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Delete a cart item completely
 * @param {string} customerId - UUID of the customer
 * @param {string} itemId - UUID of the item to delete
 * @returns {Promise<Object>} Updated cart object
 */
export const deleteCartItem = async (customerId, itemId) => {
    try {
        const response = await axios.delete(`/carts/customer/${customerId}/items/${itemId}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
}; 