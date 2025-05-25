/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import { toast } from 'react-toastify';
import PaginatedTable from '../components/PaginatedTable';
import axios from 'axios';

function VendorDashboard() {
  const { user, logout } = useUser();
  const navigate = useNavigate();
  const [items, setItems] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const itemsPerPage = 5;

  // Column definitions for the items table
  const itemColumns = [
    { key: 'name', label: 'Item Name' },
    { key: 'price', label: 'Price ($)' },
    { key: 'category', label: 'Category' },
    { key: 'quantity', label: 'Stock' },
    { key: 'status', label: 'Status' }
  ];

  useEffect(() => {
    // Redirect if not logged in as vendor
    if (!user || user.role !== 'vendor') {
      navigate('/login');
      return;
    }

    // Load vendor's items and orders
    const loadData = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/items/vendor/${user.id}`);
        const vendorItems = response.data.map(item => ({
          ...item,
          status: item.quantity > 0 ? 'Available' : 'Out of Stock',
          price: parseFloat(item.price).toFixed(2) // Format price to 2 decimal places
        }));
        
        setItems(vendorItems);
        setFilteredItems(vendorItems);
        setTotalItems(vendorItems.length);
        setLoading(false);
      } catch (error) {
        console.error('Error loading vendor data:', error);
        toast.error('Failed to load vendor items');
        setLoading(false);
      }
    };

    loadData();
  }, [user, navigate]);

  // Search functionality
  useEffect(() => {
    if (!searchTerm.trim()) {
      setFilteredItems(items);
      setTotalItems(items.length);
      return;
    }

    const searchTermLower = searchTerm.toLowerCase();
    const filtered = items.filter(item => 
      Object.entries(item).some(([key, value]) => {
        // Only search through specified columns
        if (!itemColumns.find(col => col.key === key)) return false;
        return String(value).toLowerCase().includes(searchTermLower);
      })
    );

    setFilteredItems(filtered);
    setTotalItems(filtered.length);
    setCurrentPage(1); // Reset to first page when searching
  }, [searchTerm, items]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  const handleAddItem = () => {
    // TODO: Implement add item functionality
    toast.info('Add item functionality coming soon!');
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  // Calculate the current page's data
  const getCurrentPageData = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return filteredItems.slice(startIndex, endIndex);
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
                      placeholder="Search items by any field..."
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

                {filteredItems.length === 0 ? (
                  <div className="text-center py-4">
                    <p className="text-gray-500">
                      {searchTerm ? 'No items found matching your search.' : 'No items found. Add some items to get started!'}
                    </p>
                  </div>
                ) : (
                  <PaginatedTable
                    data={getCurrentPageData()}
                    columns={itemColumns}
                    itemsPerPage={itemsPerPage}
                    currentPage={currentPage}
                    onPageChange={handlePageChange}
                    totalItems={totalItems}
                  />
                )}
              </div>
            </div>

            {/* Orders Management Section */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <h2 className="text-lg font-medium text-gray-900 mb-4">Orders Management</h2>
                {/* TODO: Add orders management UI */}
                <p className="text-gray-500">Coming soon...</p>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default VendorDashboard; 