
import { useState, useEffect } from 'react';
import { fetchAllCustomers } from '../api/customerApi';

export const useCustomers = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAllCustomers()
      .then(data => setCustomers(data))
      .catch(err => setError(err.message || 'Something went wrong'))
      .finally(() => setLoading(false));
  }, []);

  return { customers, loading, error };
};
