import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { fetchAllItems, fetchItemsByCategory } from '../api/itemApi';
import SearchBar from '../components/SearchBar';
import ItemsList from '../components/ItemsList';
import { toast } from 'react-toastify';

function Items() {
  const [searchParams] = useSearchParams();
  const category = searchParams.get('category');
  
  const [items, setItems] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(category || 'all');
  const [categories, setCategories] = useState([]);

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
        
        setLoading(false);
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message || 'Failed to load items';
        setError(errorMessage);
        toast.error(errorMessage);
        setLoading(false);
      }
    };
    loadItems();
  }, [category]);

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

  const addToCart = (item) => {
    if (!item) return;
    // TODO: Implement cart functionality
    toast.success(`${item.name || 'Item'} added to cart`);
  };

  const formatCategory = (cat) => {
    if (!cat) return '';
    return cat.charAt(0).toUpperCase() + cat.slice(1);
  };

  return (
    <div className="min-h-screen w-full bg-background text-text p-4">
      <div className="min-w-[90%] mx-auto">
        <h1 className="text-3xl font-bold mb-6">
          {category ? `${formatCategory(category)} Items` : 'All Items'}
        </h1>
        
        <div className="mb-6">
          <SearchBar onSearch={handleSearch} />
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
              onAddToCart={addToCart}
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