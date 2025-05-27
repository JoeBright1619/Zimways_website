import axios from './axios';

// Admin login
export const loginAdmin = async (adminId, password) => {
  try {
    const response = await axios.post('/admin/login', {
      identifier: adminId,
      password: password
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to login' };
  }
};

// Get admin profile
export const getAdminProfile = async (adminId) => {
  try {
    const response = await axios.get(`/admin/${adminId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch admin profile' };
  }
};

// Update admin profile
export const updateAdminProfile = async (adminId, updateData) => {
  try {
    const response = await axios.put(`/admin/${adminId}`, updateData);
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to update admin profile' };
  }
};

// Change admin password
export const changeAdminPassword = async (adminId, oldPassword, newPassword) => {
  try {
    const response = await axios.put(`/admin/${adminId}/password`, {
      oldPassword,
      newPassword
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to change password' };
  }
};

// Get dashboard statistics
export const getDashboardStats = async () => {
  try {
    const response = await axios.get('/admin/dashboard/stats');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch dashboard statistics' };
  }
};

// Get recent orders
export const getRecentOrders = async (limit = 10) => {
  try {
    const response = await axios.get(`/admin/orders/recent?limit=${limit}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch recent orders' };
  }
};

// Get revenue statistics
export const getRevenueStats = async (period = 'month') => {
  try {
    const response = await axios.get(`/admin/revenue?period=${period}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch revenue statistics' };
  }
};

// Get vendor performance
export const getVendorPerformance = async () => {
  try {
    const response = await axios.get('/admin/vendors/performance');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch vendor performance' };
  }
}; 