/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import { toast } from 'react-toastify';
import axios from 'axios';
import { ItemCategory } from '../enums/ItemCategory';

function VendorDashboard() {
  const { user, logout } = useUser();
  const navigate = useNavigate();
  const [items, setItems] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [categories, setCategories] = useState([]);
  const itemsPerPage = 5;

  // Column definitions for the items table
  const itemColumns = [
    { key: 'name', label: 'Item Name', editable: true },
    { key: 'price', label: 'Price (RWF)', editable: true },
    { key: 'description', label: 'Description', editable: true },
    { key: 'categoryNames', label: 'Categories', editable: true },
    { key: 'isAvailable', label: 'Available', editable: true },
    { key: 'discountPercentage', label: 'Discount %', editable: true },
    { key: 'actions', label: 'Actions', editable: false }
  ];

  useEffect(() => {
    if (!user || user.role !== 'vendor') {
      navigate('/login');
      return;
    }

    // Load vendor's items and categories
    const loadData = async () => {
      try {
        const [itemsResponse, categoriesResponse] = await Promise.all([
          axios.get(`http://localhost:8080/api/items/vendor/${user.id}`),
          axios.get('http://localhost:8080/api/categories')
        ]);

        console.log('Raw response from backend:', itemsResponse.data);

        const vendorItems = itemsResponse.data.map(item => {
          // Check both possible property names
          const available = item.available !== undefined ? item.available : 
                          item.isAvailable !== undefined ? item.isAvailable : true;
          
          console.log(`Processing item ${item.name}, availability:`, { 
            raw: item,
            available: item.available,
            isAvailable: item.isAvailable,
            final: available
          });

          return {
            ...item,
            categoryNames: item.categories ? item.categories.map(cat => cat.name) : [],
            price: parseFloat(item.price || 0).toFixed(2),
            isAvailable: available,
            discountPercentage: item.discountPercentage || 0
          };
        });
        
        console.log('Processed vendor items:', vendorItems);
        
        setItems(vendorItems);
        setFilteredItems(vendorItems);
        setTotalItems(vendorItems.length);
        setCategories(categoriesResponse.data || []);
        setLoading(false);
      } catch (error) {
        console.error('Error loading vendor data:', error);
        toast.error('Failed to load vendor items');
        setLoading(false);
      }
    };

    loadData();
  }, [user, navigate]);

  useEffect(() => {
    if (!searchTerm.trim()) {
      setFilteredItems(items);
      setTotalItems(items.length);
      return;
    }

    const searchTermLower = searchTerm.toLowerCase();
    const filtered = items.filter(item => {
      // Primary search by name
      const nameMatch = item.name.toLowerCase().includes(searchTermLower);
      if (nameMatch) return true;

      // Secondary search by description if no name match
      const descriptionMatch = item.description && item.description.toLowerCase().includes(searchTermLower);
      if (descriptionMatch) return true;

      // Search in categories
      const categoryMatch = (item.categoryNames || []).some(category => 
        category.toLowerCase().includes(searchTermLower)
      );
      
      return categoryMatch;
    });

    setFilteredItems(filtered);
    setTotalItems(filtered.length);
    setCurrentPage(1);
  }, [searchTerm, items]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  const handleAddItem = () => {
    const newItem = {
      id: 'temp-' + Date.now(),
      name: '',
      price: '0.00',
      description: '',
      categoryNames: [],
      isAvailable: true,
      discountPercentage: 0,
      isNew: true
    };
    setItems([newItem, ...items]);
    setFilteredItems([newItem, ...filteredItems]);
    setEditingItem(newItem.id);
  };

  const handleEditItem = (itemId) => {
    setEditingItem(itemId);
  };

  const handleCancelEdit = (itemId) => {
    if (items.find(item => item.id === itemId)?.isNew) {
      setItems(items.filter(item => item.id !== itemId));
      setFilteredItems(filteredItems.filter(item => item.id !== itemId));
    }
    setEditingItem(null);
  };

  const handleSaveItem = async (item) => {
    try {
      const itemData = {
        name: item.name,
        price: parseFloat(item.price),
        description: item.description,
        categoryNames: item.categoryNames,
        isAvailable: Boolean(item.isAvailable),
        discountPercentage: parseFloat(item.discountPercentage),
        vendorName: user.name
      };

      console.log('Saving item with data:', itemData);

      let response;
      if (item.isNew) {
        response = await axios.post('http://localhost:8080/api/items', itemData);
      } else {
        response = await axios.put(`http://localhost:8080/api/items/${item.id}`, itemData);
      }

      console.log('Raw response from save:', response.data);

      // Check both possible property names in response
      const available = response.data.available !== undefined ? response.data.available : 
                       response.data.isAvailable !== undefined ? response.data.isAvailable : true;

      console.log('Processed availability:', available);

      const updatedItem = {
        ...response.data,
        categoryNames: response.data.categories.map(cat => cat.name),
        price: parseFloat(response.data.price).toFixed(2),
        isAvailable: available
      };

      console.log('Final updated item:', updatedItem);

      setItems(items.map(i => i.id === item.id ? updatedItem : i));
      setFilteredItems(filteredItems.map(i => i.id === item.id ? updatedItem : i));
      setEditingItem(null);
      toast.success(item.isNew ? 'Item created successfully' : 'Item updated successfully');
    } catch (error) {
      console.error('Error saving item:', error);
      toast.error(error.response?.data?.message || 'Failed to save item');
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const getCurrentPageData = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return filteredItems.slice(startIndex, endIndex);
  };

  const renderCell = (item, column) => {
    const isEditing = editingItem === item.id;
    const available = item.isAvailable !== undefined ? item.isAvailable : true;
    const currentValue = item.isAvailable !== undefined ? item.isAvailable : true;

    if (!isEditing) {
      switch (column.key) {
        case 'categoryNames':
          return (
            <div className="relative group">
              <button className="px-2 py-1 bg-gray-100 rounded hover:bg-gray-200 focus:outline-none text-white">
                Categories ({(item[column.key] || []).length})
              </button>
              <div className="absolute z-10 hidden group-hover:block w-48 bg-white border rounded-md shadow-lg py-1">
                {(item[column.key] || []).map((category, index) => (
                  <div key={index} className="px-4 py-2 hover:bg-gray-100">
                    {category}
                  </div>
                ))}
                {(item[column.key] || []).length === 0 && (
                  <div className="px-4 py-2 text-gray-500 italic">No categories</div>
                )}
              </div>
            </div>
          );
        case 'isAvailable':
          console.log('Rendering availability for item:', item.name, 'isAvailable:', available);
          return available ? 'Yes' : 'No';
        case 'actions':
          return (
            <button
              onClick={() => handleEditItem(item.id)}
              className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition duration-200"
            >
              Edit
            </button>
          );
        default:
          return item[column.key] || '';
      }
    }

    switch (column.key) {
      case 'name':
      case 'description':
        return (
          <input
            type="text"
            value={item[column.key] || ''}
            onChange={(e) => {
              const updatedItem = { ...item, [column.key]: e.target.value };
              setItems(items.map(i => i.id === item.id ? updatedItem : i));
              setFilteredItems(filteredItems.map(i => i.id === item.id ? updatedItem : i));
            }}
            className="w-full px-2 py-1 border rounded"
          />
        );
      case 'price':
      case 'discountPercentage':
        return (
          <input
            type="number"
            step="0.01"
            min="0"
            value={item[column.key] || 0}
            onChange={(e) => {
              const updatedItem = { ...item, [column.key]: e.target.value };
              setItems(items.map(i => i.id === item.id ? updatedItem : i));
              setFilteredItems(filteredItems.map(i => i.id === item.id ? updatedItem : i));
            }}
            className="w-full px-2 py-1 border rounded"
          />
        );
      case 'categoryNames':
        return (
          <select
            multiple
            value={item.categoryNames || []}
            onChange={(e) => {
              const selectedCategories = Array.from(e.target.selectedOptions, option => option.value);
              const updatedItem = { ...item, categoryNames: selectedCategories };
              setItems(items.map(i => i.id === item.id ? updatedItem : i));
              setFilteredItems(filteredItems.map(i => i.id === item.id ? updatedItem : i));
            }}
            className="w-full px-2 py-1 border rounded min-h-[100px]"
          >
            {categories.map(category => (
              <option key={category.id} value={category.name}>
                {category.name}
              </option>
            ))}
          </select>
        );
      case 'isAvailable':
        return (
          <select
            value={String(currentValue)}
            onChange={(e) => {
              const isAvailable = e.target.value === 'true';
              console.log('Changing availability for', item.name, 'to:', isAvailable);
              const updatedItem = { ...item, isAvailable: isAvailable };
              console.log('Updated item:', updatedItem);
              setItems(items.map(i => i.id === item.id ? updatedItem : i));
              setFilteredItems(filteredItems.map(i => i.id === item.id ? updatedItem : i));
            }}
            className="w-full px-2 py-1 border rounded"
          >
            <option value="true">Yes</option>
            <option value="false">No</option>
          </select>
        );
      case 'actions':
        return (
          <div className="flex gap-2">
            <button
              onClick={() => handleSaveItem(item)}
              className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 transition duration-200"
            >
              Save
            </button>
            <button
              onClick={() => handleCancelEdit(item.id)}
              className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition duration-200"
            >
              Cancel
            </button>
          </div>
        );
      default:
        return item[column.key] || '';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen min-w-screen bg-gray-100 text-black">
      <nav className="bg-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <h1 className="text-xl font-semibold">Vendor Dashboard</h1>
            </div>
            <div className="flex items-center">
              <span className="mr-4 text-gray-700">{user?.name}</span>
              <button
                onClick={handleLogout}
                className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition duration-200"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="grid grid-cols-1 gap-6">
            {/* Items Management Section */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <div className="flex justify-between items-center mb-4">
                  <h2 className="text-lg font-medium text-gray-900">Items Management</h2>
                  <button 
                    onClick={handleAddItem}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-200"
                  >
                    Add New Item
                  </button>
                </div>

                {/* Search Input */}
                <div className="mb-4">
                  <div className="relative">
                    <input
                      type="text"
                      placeholder="Search items..."
                      value={searchTerm}
                      onChange={handleSearch}
                      className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                    />
                    <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                      <svg className="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                      </svg>
                    </div>
                  </div>
                </div>

                {/* Items Table */}
                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        {itemColumns.map(column => (
                          <th
                            key={column.key}
                            className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            {column.label}
                          </th>
                        ))}
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {getCurrentPageData().map((item) => (
                        <tr key={item.id}>
                          {itemColumns.map(column => (
                            <td key={`${item.id}-${column.key}`} className="px-6 py-4 whitespace-nowrap">
                              {renderCell(item, column)}
                            </td>
                          ))}
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                {/* Pagination */}
                <div className="flex items-center justify-between mt-4">
                  <div className="text-sm text-gray-700">
                    Showing {(currentPage - 1) * itemsPerPage + 1} to{' '}
                    {Math.min(currentPage * itemsPerPage, totalItems)} of {totalItems} items
                  </div>
                  <div className="flex gap-2">
                    <button
                      onClick={() => handlePageChange(currentPage - 1)}
                      disabled={currentPage === 1}
                      className="px-3 py-1 rounded border border-gray-300 disabled:opacity-50"
                    >
                      Previous
                    </button>
                    {Array.from({ length: Math.ceil(totalItems / itemsPerPage) }, (_, i) => (
                      <button
                        key={i + 1}
                        onClick={() => handlePageChange(i + 1)}
                        className={`px-3 py-1 rounded border ${
                          currentPage === i + 1
                            ? 'bg-blue-600 text-white'
                            : 'border-gray-300 hover:bg-gray-50'
                        }`}
                      >
                        {i + 1}
                      </button>
                    ))}
                    <button
                      onClick={() => handlePageChange(currentPage + 1)}
                      disabled={currentPage === Math.ceil(totalItems / itemsPerPage)}
                      className="px-3 py-1 rounded border border-gray-300 disabled:opacity-50"
                    >
                      Next
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default VendorDashboard; 