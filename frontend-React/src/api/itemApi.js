import api from './axios';

// Get all items
export const fetchAllItems = async () => {
  const res = await api.get('/items');
  return res.data;
};

// Get item by ID
export const fetchItemById = async (id) => {
  const res = await api.get(`/items/${id}`);
  return res.data;
};

// Get item by name
export const fetchItemByName = async (name) => {
  const res = await api.get(`/items/name/${name}`);
  return res.data;
};

// Get items by category
export const fetchItemsByCategory = async (categoryName) => {
  const res = await api.get(`/items/category/${categoryName}`);
  return res.data;
};

// Get items cheaper than price
export const fetchItemsCheaperThan = async (price) => {
  const res = await api.get(`/items/cheaper-than/${price}`);
  return res.data;
};

// Get items more expensive than price
export const fetchItemsMoreExpensiveThan = async (price) => {
  const res = await api.get(`/items/more-expensive-than/${price}`);
  return res.data;
};

// Search items by keyword
export const searchItemsByKeyword = async (keyword) => {
  const res = await api.get(`/items/search/${keyword}`);
  return res.data;
};

// Create new item
export const createItem = async (itemData) => {
  const res = await api.post('/items', itemData);
  return res.data;
};

// Update existing item
export const updateItem = async (id, itemData) => {
  const res = await api.put(`/items/${id}`, itemData);
  return res.data;
};

// Delete item by ID
export const deleteItem = async (id) => {
  await api.delete(`/items/${id}`);
};

// Delete item by name
export const deleteItemByName = async (name) => {
  await api.delete(`/items/name/${name}`);
};

// Get items by vendor ID
export const fetchItemsByVendor = async (vendorId) => {
  const res = await api.get(`/items/vendor/${vendorId}`);
  return res.data;
};

