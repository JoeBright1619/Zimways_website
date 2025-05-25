/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { GiHamburgerMenu } from "react-icons/gi";
import { FaSignOutAlt } from "react-icons/fa";
import { Link, useNavigate } from 'react-router-dom';
import SearchBar from "../components/SearchBar";
import { fetchAllItems } from '../api/itemApi';
import { fetchAllVendors } from '../api/vendorApi';
import { getCustomerCart, addItemToCart } from '../api/cartApi';
import { toast } from 'react-toastify';
import ItemsList from '../components/ItemsList';
import VendorsList from '../components/VendorsList';

function Home({ logout, customerId }) {
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [showNav, setShowNav] = useState(false);
  const [items, setItems] = useState([]);
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [categories, setCategories] = useState([]);

  const loadCart = async () => {
    if (!customerId) return;
    
    try {
      const cartData = await getCustomerCart(customerId);
      setCart(cartData);
      console.log('Cart loaded in Home:', cartData);
    } catch (err) {
      // If cart doesn't exist, it will be created when first item is added
      console.log('No cart found for customer');
    }
  };

  useEffect(() => {
    const loadData = async () => {
      try {
        // Load items
        const itemsData = await fetchAllItems();
        setItems(itemsData);
        const uniqueCategories = [...new Set(itemsData.map(item => item.category))];
        setCategories(uniqueCategories);

        // Load vendors
        const vendorsData = await fetchAllVendors();
        setVendors(vendorsData);

        // Load cart if customer is logged in
        await loadCart();

        setLoading(false);
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message || 'Failed to load data';
        setError(errorMessage);
        toast.error(errorMessage);
        setLoading(false);
      }
    };
    loadData();
  }, [customerId]);

  // Close nav when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (showNav && !event.target.closest('.nav-menu') && !event.target.closest('.hamburger-button')) {
        setShowNav(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [showNav]);

  const addToCart = async (item) => {
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

  const removeFromCart = (itemId) => {
    setCart(prev => ({
      ...prev,
      cartItems: prev.cartItems.filter(item => item.item.id !== itemId)
    }));
  };

  const total = cart?.cartItems?.reduce((sum, item) => sum + item.totalPrice, 0) || 0;

  return (
    <div className="min-h-screen min-w-screen bg-background text-text">
      <header className="bg-primary text-white p-4 flex items-center justify-between relative">
        <h1 className="text-2xl font-bold flex items-center font-primary text-background">
          ZimWays
        </h1>
        <SearchBar />
        <div className="flex items-center gap-4">
          <button
            onClick={logout}
            className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-primary-dark transition-colors"
            title="Logout"
          >
            <FaSignOutAlt size={20} />
            <span className="hidden sm:inline">Logout</span>
          </button>
          <button 
            className="hamburger-button text-background hover:text-white transition-colors"
            onClick={() => setShowNav(!showNav)}
            aria-label="Toggle navigation menu"
          >
            <GiHamburgerMenu size={40} />
          </button>
        </div>

        {/* Navigation Menu */}
        {showNav && (
          <nav className="nav-menu absolute right-0 top-full mt-2 bg-white shadow-lg rounded-lg p-4 w-64 z-50">
            <ul className="space-y-2">
              <li key="all-items">
                <Link 
                  to="/items" 
                  className="block px-4 py-2 text-gray-800 hover:bg-primary hover:text-white rounded-md transition-colors"
                  onClick={() => setShowNav(false)}
                >
                  All Items
                </Link>
              </li>
              {categories.map(category => (
                <li key={`category-${category}`}>
                  <Link 
                    to={`/items?category=${category}`}
                    className="block px-4 py-2 text-gray-800 hover:bg-primary hover:text-white rounded-md transition-colors"
                    onClick={() => setShowNav(false)}
                  >
                    {category}
                  </Link>
                </li>
              ))}
              <li key="cart">
                <Link 
                  to="/cart" 
                  className="block px-4 py-2 text-gray-800 hover:bg-primary hover:text-white rounded-md transition-colors"
                  onClick={() => setShowNav(false)}
                >
                  Cart ({cart?.cartItems?.length || 0})
                </Link>
              </li>
              <li key="orders">
                <Link 
                  to="/orders" 
                  className="block px-4 py-2 text-gray-800 hover:bg-primary hover:text-white rounded-md transition-colors"
                  onClick={() => setShowNav(false)}
                >
                  Orders
                </Link>
              </li>
              <li key="settings">
                <Link 
                  to="/settings" 
                  className="block px-4 py-2 text-gray-800 hover:bg-primary hover:text-white rounded-md transition-colors"
                  onClick={() => setShowNav(false)}
                >
                  Settings
                </Link>
              </li>
            </ul>
          </nav>
        )}
      </header>

      <main className="p-4">
        {/* Featured Items Section */}
        <section id="featured-items" className="mb-8">
          <h2 className="text-xl font-bold mb-4">Featured Items</h2>
          <div className="relative">
            <div className="overflow-x-auto [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none] pb-4">
              <div className="flex space-x-4">
                <ItemsList 
                  items={items}
                  loading={loading}
                  error={error}
                  onAddToCart={addToCart}
                />
              </div>
            </div>
            <div className="absolute left-0 top-1/2 -translate-y-1/2 bg-gradient-to-r from-background to-transparent w-8 h-full"></div>
            <div className="absolute right-0 top-1/2 -translate-y-1/2 bg-gradient-to-l from-background to-transparent w-8 h-full"></div>
          </div>
        </section>

        <section id="restaurants" className="mb-8">
          <h2 className="text-xl font-bold mb-4">Vendors</h2>
          <div className="relative">
            <div className="overflow-x-auto [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none] pb-4">
              <div className="flex space-x-4">
                <VendorsList 
                  vendors={vendors}
                  loading={loading}
                  error={error}
                />
              </div>
            </div>
            <div className="absolute left-0 top-1/2 -translate-y-1/2 bg-gradient-to-r from-background to-transparent w-8 h-full"></div>
            <div className="absolute right-0 top-1/2 -translate-y-1/2 bg-gradient-to-l from-background to-transparent w-8 h-full"></div>
          </div>
        </section>

        
      </main>
    </div>
  );
}

export default Home;