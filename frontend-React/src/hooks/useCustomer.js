// src/hooks/useCustomer.js
import { useState, useEffect } from 'react';
import { fetchCustomerById } from '../api/customerApi';

export const useCustomer = (id) => {
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id) return;

    fetchCustomerById(id)
      .then(data => setCustomer(data))
      .catch(err => setError(err.message || 'Failed to fetch customer'))
      .finally(() => setLoading(false));
  }, [id]);

  return { customer, loading, error };
};
