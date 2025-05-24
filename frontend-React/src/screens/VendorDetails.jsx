import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { fetchVendorById } from '../api/vendorApi';
import { fetchItemsByVendor } from '../api/itemApi';
import ItemsList from '../components/ItemsList';
import { toast } from 'react-toastify';

function VendorDetails() {
  const { vendorId } = useParams();
  const [vendor, setVendor] = useState(null);
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadVendorData = async () => {
      try {
        const [vendorData, vendorItems] = await Promise.all([
          fetchVendorById(vendorId),
          fetchItemsByVendor(vendorId)
        ]);
        setVendor(vendorData);
        setItems(vendorItems);
        setLoading(false);
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message || 'Failed to load vendor details';
        setError(errorMessage);
        toast.error(errorMessage);
        setLoading(false);
      }
    };
    loadVendorData();
  }, [vendorId]);

  const addToCart = (item) => {
    // TODO: Implement cart functionality
    toast.success(`${item.name} added to cart`);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background text-text p-4 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
          <p className="mt-4">Loading vendor details...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-background text-text p-4 flex items-center justify-center">
        <div className="text-center text-red-500">
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (!vendor) {
    return (
      <div className="min-h-screen bg-background text-text p-4 flex items-center justify-center">
        <div className="text-center">
          <p>Vendor not found</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background text-text p-4">
      <div className="max-w-7xl mx-auto">
        {/* Vendor Header */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <div className="flex flex-col md:flex-row gap-6">
            <div className="w-full md:w-1/3">
              <img
                src={vendor.imageUrl || 'https://via.placeholder.com/300x200'}
                alt={vendor.name}
                className="w-full h-48 object-cover rounded-lg"
              />
            </div>
            <div className="w-full md:w-2/3">
              <h1 className="text-3xl font-bold mb-2">{vendor.name}</h1>
              <p className="text-gray-600 mb-4">{vendor.description}</p>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="font-semibold">Location</p>
                  <p className="text-gray-600">{vendor.location}</p>
                </div>
                <div>
                  <p className="font-semibold">Rating</p>
                  <p className="text-gray-600">{vendor.rating || 'N/A'}</p>
                </div>
                <div>
                  <p className="font-semibold">Opening Hours</p>
                  <p className="text-gray-600">{vendor.openingHours || 'N/A'}</p>
                </div>
                <div>
                  <p className="font-semibold">Contact</p>
                  <p className="text-gray-600">{vendor.contact || 'N/A'}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Menu Items */}
        <div className="mb-8">
          <h2 className="text-2xl font-bold mb-4">Menu Items</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <ItemsList
              items={items}
              loading={loading}
              error={error}
              onAddToCart={addToCart}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default VendorDetails; 