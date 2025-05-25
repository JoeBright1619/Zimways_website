import api from './axios';

export const fetchAllCustomers = async () => {
  const res = await api.get('/customers');
  return res.data;
};

export const fetchCustomerById = async (id) => {
  const res = await api.get(`/customers/${id}`);
  return res.data;
};

export const createCustomer = async (customer) => {
  const res = await api.post('/customers', customer);
  return res.data;
}
export const updateCustomer = async (id, customer) => {
  const res = await api.put(`/customers/${id}`, customer);
  return res.data;
}
export const deleteCustomer = async (id) => {
  const res = await api.delete(`/customers/${id}`);
  return res.data;
}
export const loginCustomer = async (customer) => {
  const res = await api.post('/customers/login', customer);
  return res.data;
}

export const requestPasswordReset = async (email) => {
  const res = await api.post('/customers/forgot-password', { email });
  return res.data;
}

export const resetPassword = async (token, newPassword) => {
  const res = await api.post('/customers/reset-password', { token, newPassword });
  return res.data;
}
