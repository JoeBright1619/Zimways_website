import axios from './axios';

/**
 * Fetch all available categories
 * @returns {Promise<Array>} Array of category objects
 */
export const fetchAllCategories = async () => {
    try {
        const response = await axios.get('/api/categories');
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Fetch a specific category by name
 * @param {string} name - The name of the category to fetch
 * @returns {Promise<Object>} Category object
 */
export const fetchCategoryByName = async (name) => {
    try {
        const response = await axios.get(`/api/categories/${name}`);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Create a new category
 * @param {Object} category - The category object to create
 * @returns {Promise<Object>} Created category object
 */
export const createCategory = async (category) => {
    try {
        const response = await axios.post('/api/categories', category);
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};

/**
 * Delete a category by name
 * @param {string} name - The name of the category to delete
 * @returns {Promise<void>}
 */
export const deleteCategory = async (name) => {
    try {
        await axios.delete(`/api/categories/${name}`);
    } catch (error) {
        throw error.response?.data || error;
    }
}; 