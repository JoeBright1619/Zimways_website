/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { fetchAllItems, fetchItemsByCategory } from '../api/itemApi';
import { getCustomerCart, addItemToCart } from '../api/cartApi';
import SearchBar from '../components/SearchBar';
import ItemsList from '../components/ItemsList';
import { toast } from 'react-toastify';

function Items({ customerId }) {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const category = searchParams.get('category');
  
  const [items, setItems] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(category || 'all');
  const [categories, setCategories] = useState([]);
  const [cart, setCart] = useState(null);

  const loadCart = async () => {
    if (!customerId) return;
    
    try {
      const cartData = await getCustomerCart(customerId);
      setCart(cartData);
      console.log('Cart loaded in Items:', cartData);
    } catch (err) {
      // If cart doesn't exist, it will be created when first item is added
      console.log('No cart found for customer');
    }
  };

  useEffect(() => {
    const loadItems = async () => {
      try {
        let data;
        if (category) {
          data = await fetchItemsByCategory(category);
        } else {
          data = await fetchAllItems();
        }
        setItems(data || []);
        setFilteredItems(data || []);
        
        // Extract unique categories if we're loading all items
        if (!category && Array.isArray(data)) {
          const uniqueCategories = [...new Set(data
            .filter(item => item && item.category)
            .map(item => item.category))];
          setCategories(uniqueCategories);
        }
        
        // Load cart if customer is logged in
        await loadCart();
        
        setLoading(false);
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message || 'Failed to load items';
        setError(errorMessage);
        toast.error(errorMessage);
        setLoading(false);
      }
    };
    loadItems();
  }, [category, customerId]);

  const handleSearch = (searchTerm) => {
    if (!Array.isArray(items)) return;
    
    const filtered = items.filter(item => 
      item && item.name && item.description &&
      (item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
       item.description.toLowerCase().includes(searchTerm.toLowerCase()))
    );
    setFilteredItems(filtered);
  };

  const handleCategoryFilter = (category) => {
    setSelectedCategory(category);
    if (!Array.isArray(items)) return;
    
    if (category === 'all') {
      setFilteredItems(items);
    } else {
      const filtered = items.filter(item => item && item.category === category);
      setFilteredItems(filtered);
    }
  };

  const addToCartHandler = async (item) => {
    if (!customerId) {
      toast.error('Please login to add items to cart');
      navigate('/login');
      return;
    }

    try {
      await addItemToCart(customerId, item.id, 1);
      // Refresh cart data after adding item
      await loadCart();
      toast.success(`${item.name} added to cart`);
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to add item to cart';
      toast.error(errorMessage);
    }
  };

  const formatCategory = (cat) => {
    if (!cat) return '';
    return cat.charAt(0).toUpperCase() + cat.slice(1);
  };

  return (
    <div className="min-h-screen min-w-screen w-full bg-background text-text p-4">
      <div className="min-w-[90%] mx-auto">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold">
            {category ? `${formatCategory(category)} Items` : 'All Items'}
          </h1>
          <button
            onClick={() => navigate('/')}
            className="bg-primary text-white px-4 py-2 rounded-lg hover:bg-primary/90 transition duration-200 flex items-center gap-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z" />
            </svg>
            Back to Home
          </button>
        </div>
        
        <div className="mb-6">
          <SearchBar onSearch={handleSearch} context='items'/>
        </div>

        {!category && categories.length > 0 && (
          <div className="mb-6">
            <div className="flex flex-wrap gap-2">
              <button
                onClick={() => handleCategoryFilter('all')}
                className={`px-4 py-2 rounded-full ${
                  selectedCategory === 'all'
                    ? 'bg-primary text-white'
                    : 'bg-gray-200 hover:bg-gray-300'
                }`}
              >
                All Items
              </button>
              {categories.map(cat => (
                <button
                  key={cat || 'unknown'}
                  onClick={() => handleCategoryFilter(cat)}
                  className={`px-4 py-2 rounded-full ${
                    selectedCategory === cat
                      ? 'bg-primary text-white'
                      : 'bg-gray-200 hover:bg-gray-300'
                  }`}
                >
                  {formatCategory(cat)}
                </button>
              ))}
            </div>
          </div>
        )}

        <div className="w-full">
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            <ItemsList
              items={filteredItems}
              loading={loading}
              error={error}
              onAddToCart={addToCartHandler}
            />
          </div>
        </div>

        {filteredItems.length === 0 && !loading && (
          <div className="text-center py-8">
            <p className="text-gray-600">No items found matching your criteria</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default Items; 