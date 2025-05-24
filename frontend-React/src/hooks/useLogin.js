// src/hooks/useLogin.js
import { useState } from 'react';
import { loginCustomer } from '../api/customerApi';

export const useLogin = () => {
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const login = async (email, password) => {
    setLoading(true);
    setError(null);
    try {
      const user = await loginCustomer({email, password});
      setCustomer(user);
    } catch (err) {
      setError(err.response?.data || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return { customer, login, loading, error };
};
