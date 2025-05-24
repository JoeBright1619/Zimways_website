import api from './axios';

// Get all vendors
export const fetchAllVendors = async () => {
  const response = await api.get('/vendors');
  return response.data;
};

// Get vendor by ID
export const fetchVendorById = async (id) => {
  const response = await api.get(`/vendors/${id}`);
  return response.data;
};

// Search vendors by name
export const searchVendorsByName = async (keyword) => {
  const response = await api.get(`/vendors/search?keyword=${keyword}`);
  return response.data;
};

// Get vendors by status
export const fetchVendorsByStatus = async (status) => {
  const response = await api.get(`/vendors/status/${status}`);
  return response.data;
};

// Get vendors by location
export const fetchVendorsByLocation = async (location) => {
  const response = await api.get(`/vendors/location/${location}`);
  return response.data;
};

// Create new vendor
export const createVendor = async (vendorData) => {
  const response = await api.post('/vendors', vendorData);
  return response.data;
};

// Update existing vendor
export const updateVendor = async (id, vendorData) => {
  const response = await api.put(`/vendors/${id}`, vendorData);
  return response.data;
};

// Add rating to vendor
export const addVendorRating = async (id, rating) => {
  await api.post(`/vendors/${id}/rating?rating=${rating}`);
};

// Delete vendor by ID
export const deleteVendor = async (id) => {
  await api.delete(`/vendors/${id}`);
};

// Delete vendor by name
export const deleteVendorByName = async (name) => {
  await api.delete(`/vendors/name/${name}`);
};
