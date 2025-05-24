/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getCustomerCart, removeItemFromCart, checkoutCart, addItemToCart } from '../api/cartApi';

function Cart({ customerId }) {
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('cash');
  const [loading, setLoading] = useState(true);
  const [checkoutLoading, setCheckoutLoading] = useState(false);

  useEffect(() => {
    const loadCart = async () => {
      if (!customerId) {
        toast.error('Please login to view your cart');
        navigate('/login');
        return;
      }

      try {
        const cartData = JSON.parse(JSON.stringify(await getCustomerCart(customerId)));
        
        setCart(
          cartData
        );

        console.log("typeof cartData:", typeof cartData);
        console.log('Cart loaded:', cart);
      } catch (error) {
        const errorMessage = error.response?.data?.message || error.message || 'Failed to load cart';
        toast.error(errorMessage);
      } finally {
        setLoading(false);
      }
    };

    loadCart();
  }, [customerId, navigate]);

  const updateQuantity = async (itemId, newQuantity) => {
    if (newQuantity < 1) return;
    
    try {
      // If new quantity is 0, remove the item
      if (newQuantity === 0) {
        await removeItem(itemId);
        return;
      }

      // Calculate the difference to add/remove
      const currentItem = cart.cartItems.find(item => item.item.id === itemId);
      const difference = newQuantity - currentItem.quantity;
      
      if (difference > 0) {
        // Add items
        const updatedCart = await addItemToCart(customerId, itemId, difference);
        setCart(updatedCart);
      } else if (difference < 0) {
        // Remove items
        const updatedCart = await removeItemFromCart(customerId, itemId, Math.abs(difference));
        setCart(updatedCart);
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to update quantity';
      toast.error(errorMessage);
    }
  };

  const removeItem = async (itemId) => {
    try {
      const currentItem = cart.cartItems.find(item => item.item.id === itemId);
      const updatedCart = await removeItemFromCart(customerId, itemId, currentItem.quantity);
      setCart(updatedCart);
      toast.success('Item removed from cart');
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to remove item';
      toast.error(errorMessage);
    }
  };

  const calculateSubtotal = () => {
    if (!cart?.cartItems) return 0;
    return cart.cartItems.reduce((sum, item) => sum + item.totalPrice, 0);
  };

  const calculateDeliveryFee = () => {
    return 2000; // Fixed delivery fee
  };

  const calculateTotal = () => {
    return calculateSubtotal() + calculateDeliveryFee();
  };

  const handleCheckout = async () => {
    if (!deliveryAddress.trim()) {
      toast.error('Please enter a delivery address');
      return;
    }

    setCheckoutLoading(true);
    try {
      await checkoutCart(customerId);
      toast.success('Order placed successfully!');
      setCart(null); // Clear cart after successful checkout
      navigate('/orders');
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to place order';
      toast.error(errorMessage);
    } finally {
      setCheckoutLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background text-text p-4">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-8">Your Cart</h1>

        {loading ? (
          <div className="text-center py-8">
            <p className="text-gray-600">Loading cart...</p>
          </div>
        ) : !cart || !cart.cartItems || cart.cartItems.length === 0 ? (
          <div className="text-center py-8">
            <p className="text-gray-600 mb-4">Your cart is empty</p>
            <button
              onClick={() => navigate('/items')}
              className="bg-primary text-white px-6 py-2 rounded-md hover:bg-primary-dark"
            >
              Browse Menu
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Cart Items */}
            <div className="md:col-span-2">
              <div className="bg-white rounded-lg shadow-md p-6">
                {cart.cartItems.map(cartItem => (
                  <div key={cartItem.id} className="flex items-center justify-between py-4 border-b last:border-b-0">
                    <div className="flex-1">
                      <h3 className="font-semibold">{cartItem.item.name}</h3>
                      <p className="text-gray-600">{cartItem.item.price.toLocaleString()} RWF</p>
                    </div>
                    <div className="flex items-center gap-4">
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => updateQuantity(cartItem.item.id, cartItem.quantity - 1)}
                          className="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300"
                        >
                          -
                        </button>
                        <span>{cartItem.quantity}</span>
                        <button
                          onClick={() => updateQuantity(cartItem.item.id, cartItem.quantity + 1)}
                          className="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300"
                        >
                          +
                        </button>
                      </div>
                      <button
                        onClick={() => removeItem(cartItem.item.id)}
                        className="text-red-500 hover:text-red-700"
                      >
                        Remove
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Order Summary */}
            <div className="md:col-span-1">
              <div className="bg-white rounded-lg shadow-md p-6">
                <h2 className="text-xl font-bold mb-4">Order Summary</h2>
                
                <div className="space-y-4 mb-6">
                  <div className="flex justify-between">
                    <span>Subtotal</span>
                    <span>{calculateSubtotal().toLocaleString()} RWF</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Delivery Fee</span>
                    <span>{calculateDeliveryFee().toLocaleString()} RWF</span>
                  </div>
                  <div className="border-t pt-4">
                    <div className="flex justify-between font-bold">
                      <span>Total</span>
                      <span>{calculateTotal().toLocaleString()} RWF</span>
                    </div>
                  </div>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">
                      Delivery Address
                    </label>
                    <textarea
                      value={deliveryAddress}
                      onChange={(e) => setDeliveryAddress(e.target.value)}
                      className="w-full p-2 border rounded-md"
                      rows="3"
                      placeholder="Enter your delivery address"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium mb-2">
                      Payment Method
                    </label>
                    <select
                      value={paymentMethod}
                      onChange={(e) => setPaymentMethod(e.target.value)}
                      className="w-full p-2 border rounded-md"
                    >
                      <option value="cash">Cash on Delivery</option>
                      <option value="mobile">Mobile Money</option>
                      <option value="card">Credit/Debit Card</option>
                    </select>
                  </div>

                  <button
                    onClick={handleCheckout}
                    disabled={checkoutLoading}
                    className="w-full bg-primary text-white py-3 rounded-md hover:bg-primary-dark disabled:opacity-50"
                  >
                    {checkoutLoading ? 'Processing...' : 'Place Order'}
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Cart; 