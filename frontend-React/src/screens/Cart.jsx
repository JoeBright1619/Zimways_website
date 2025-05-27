/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getCustomerCart, deleteCartItem, addItemToCart, removeItemFromCart } from '../api/cartApi';
import { createOrder, updateOrderStatus } from '../api/orderApi';
import { createPayment, processPayment } from '../api/paymentApi';
import axios from '../api/axios';

function Cart({ customerId }) {
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('CASH');
  const [loading, setLoading] = useState(true);
  const [checkoutLoading, setCheckoutLoading] = useState(false);
  const [updatingItems, setUpdatingItems] = useState(new Set());
  const [orderId, setOrderId] = useState(null);
  const [orderStatus, setOrderStatus] = useState(null);
  const [paymentStatus, setPaymentStatus] = useState(null);

  useEffect(() => {
    const loadCart = async () => {
      if (!customerId) {
        toast.error('Please login to view your cart');
        navigate('/login');
        return;
      }

      try {
        const cartData = await getCustomerCart(customerId);
        if (cartData?.cartItems) {
          cartData.cartItems.sort((a, b) => {
            if (a.item.id < b.item.id) return -1;
            if (a.item.id > b.item.id) return 1;
            return 0;
          });
        }
        setCart(cartData);
      } catch (error) {
        const errorMessage = error.response?.data?.message || error.message || 'Failed to load cart';
        toast.error(errorMessage);
      } finally {
        setLoading(false);
      }
    };

    loadCart();
  }, [customerId, navigate]);

  const removeItem = async (itemId) => {
    if (updatingItems.has(itemId)) return;
    
    setUpdatingItems(prev => new Set([...prev, itemId]));
    
    try {
      setCart(prevCart => {
        const updatedItems = prevCart.cartItems.filter(item => item.item.id !== itemId);
        updatedItems.sort((a, b) => {
          if (a.item.id < b.item.id) return -1;
          if (a.item.id > b.item.id) return 1;
          return 0;
        });
        return {
        ...prevCart,
          cartItems: updatedItems
        };
      });
      
      await deleteCartItem(customerId, itemId);
      toast.success('Item removed from cart');
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to remove item';
      toast.error(errorMessage);
      const cartData = await getCustomerCart(customerId);
      if (cartData?.cartItems) {
        cartData.cartItems.sort((a, b) => {
          if (a.item.id < b.item.id) return -1;
          if (a.item.id > b.item.id) return 1;
          return 0;
        });
      }
      setCart(cartData);
    } finally {
      setUpdatingItems(prev => {
        const next = new Set(prev);
        next.delete(itemId);
        return next;
      });
    }
  };

  const updateQuantity = async (itemId, newQuantity) => {
    if (newQuantity < 1 || updatingItems.has(itemId)) return;
    
    setUpdatingItems(prev => new Set([...prev, itemId]));
    
    try {
      if (newQuantity === 0) {
        await removeItem(itemId);
        return;
      }

      const currentItem = cart.cartItems.find(item => item.item.id === itemId);
      const difference = newQuantity - currentItem.quantity;
      
      setCart(prevCart => {
        const updatedItems = prevCart.cartItems.map(item => 
          item.item.id === itemId 
            ? { ...item, quantity: newQuantity, totalPrice: item.item.price * newQuantity }
            : item
        );
        updatedItems.sort((a, b) => {
          if (a.item.id < b.item.id) return -1;
          if (a.item.id > b.item.id) return 1;
          return 0;
        });
        return {
          ...prevCart,
          cartItems: updatedItems
        };
      });

      if (difference > 0) {
        await addItemToCart(customerId, itemId, difference);
      } else {
        await removeItemFromCart(customerId, itemId, Math.abs(difference));
      }
      
      const updatedCart = await getCustomerCart(customerId);
      if (updatedCart?.cartItems) {
        updatedCart.cartItems.sort((a, b) => {
          if (a.item.id < b.item.id) return -1;
          if (a.item.id > b.item.id) return 1;
          return 0;
        });
      }
      setCart(updatedCart);
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to update quantity';
      toast.error(errorMessage);
      const cartData = await getCustomerCart(customerId);
      if (cartData?.cartItems) {
        cartData.cartItems.sort((a, b) => {
          if (a.item.id < b.item.id) return -1;
          if (a.item.id > b.item.id) return 1;
          return 0;
        });
      }
      setCart(cartData);
    } finally {
      setUpdatingItems(prev => {
        const next = new Set(prev);
        next.delete(itemId);
        return next;
      });
    }
  };

  const calculateSubtotal = () => {
    if (!cart?.cartItems) return 0;
    return cart.cartItems.reduce((sum, item) => sum + item.totalPrice, 0);
  };

  const calculateDeliveryFee = () => {
    return 2000;
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
      const orderData = {
        deliveryAddress: deliveryAddress,
        items: cart.cartItems.map(item => ({
          itemId: item.item.id,
          quantity: item.quantity
        }))
      };
      
      const newOrder = await createOrder(customerId, '00000000-0000-0000-0000-000000000000', orderData);
      setOrderId(newOrder.id);
      setOrderStatus('PENDING');

      const payment = await createPayment(newOrder.id, paymentMethod);
      setPaymentStatus(payment.status);

      if (payment.id) {
        const processedPayment = await processPayment(payment.id);
        setPaymentStatus('COMPLETED');
      }

      await updateOrderStatus(newOrder.id, 'PAID');
      setOrderStatus('PAID');

      await axios.post(`/carts/customer/${customerId}/checkout`, {
        deliveryAddress,
        paymentMethod
      });

      toast.success('Order placed and payment processed successfully!');
      setCart(null);
      navigate('/orders');
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to process order';
      toast.error(errorMessage);
      
      if (orderId) {
        try {
          await updateOrderStatus(orderId, 'PAYMENT_FAILED');
          setOrderStatus('PAYMENT_FAILED');
        } catch (updateError) {
          console.error('Failed to update order status:', updateError);
        }
      }
    } finally {
      setCheckoutLoading(false);
    }
  };

  return (
    <div className="min-h-screen min-w-screen bg-background text-text p-4">
      <div className="max-w-4xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">Your Cart</h1>
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
            <div className="md:col-span-2">
              <div className="bg-white rounded-lg shadow-md p-6">
                {cart.cartItems.map(cartItem => (
                  <div 
                    key={cartItem.item.id}
                    className="flex items-center justify-between py-4 border-b last:border-b-0"
                  >
                    <div className="flex-1">
                      <h3 className="font-semibold">{cartItem.item.name}</h3>
                      <p className="text-gray-600">{cartItem.item.price.toLocaleString()} RWF</p>
                    </div>
                    <div className="flex items-center gap-4">
                      <div className="flex items-center gap-2 ">
                        <button
                          onClick={() => updateQuantity(cartItem.item.id, cartItem.quantity - 1)}
                          disabled={updatingItems.has(cartItem.item.id)}
                          className={`px-2 py-1 bg-gray-200 rounded hover:bg-gray-300 text-white ${updatingItems.has(cartItem.item.id) ? 'opacity-50 cursor-not-allowed' : ''}`}
                        >
                          -
                        </button>
                        <span>{cartItem.quantity}</span>
                        <button
                          onClick={() => updateQuantity(cartItem.item.id, cartItem.quantity + 1)}
                          disabled={updatingItems.has(cartItem.item.id)}
                          className={`px-2 py-1 bg-gray-200 rounded hover:bg-gray-300 text-white ${updatingItems.has(cartItem.item.id) ? 'opacity-50 cursor-not-allowed' : ''}`}
                        >
                          +
                        </button>
                      </div>
                      <button
                        onClick={() => removeItem(cartItem.item.id)}
                        disabled={updatingItems.has(cartItem.item.id)}
                        className={`text-red-500 hover:text-red-700 ${updatingItems.has(cartItem.item.id) ? 'opacity-50 cursor-not-allowed' : ''}`}
                      >
                        {updatingItems.has(cartItem.item.id) ? 'Removing...' : 'Remove'}
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>

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

                {orderStatus && (
                  <div className={`mb-4 p-4 rounded-lg ${
                    orderStatus === 'PAID' ? 'bg-green-100 text-green-800' : 
                    orderStatus === 'PAYMENT_FAILED' ? 'bg-red-100 text-red-800' : 
                    'bg-blue-100 text-blue-800'
                  }`}>
                    <p className="font-semibold">Order Status: {orderStatus}</p>
                    {paymentStatus && <p>Payment Status: {paymentStatus}</p>}
                  </div>
                )}

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
                      <option value="CASH">Cash on Delivery</option>
                      <option value="MOBILE_MONEY">Mobile Money</option>
                      <option value="CARD">Credit/Debit Card</option>
                    </select>
                  </div>

                  <button
                    onClick={handleCheckout}
                    disabled={checkoutLoading}
                    className="w-full bg-primary text-white py-3 rounded-md hover:bg-primary-dark disabled:opacity-50"
                  >
                    {checkoutLoading ? 'Processing Order...' : 'Place Order'}
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