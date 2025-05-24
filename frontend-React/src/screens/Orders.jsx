import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';

function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('current'); // 'current' or 'history'

  useEffect(() => {
    const loadOrders = async () => {
      try {
        // TODO: Implement actual API call to fetch orders
        // Mock data for demonstration
        const mockOrders = [
          {
            id: 1,
            orderNumber: 'ORD-001',
            date: '2024-03-15T10:30:00',
            status: 'preparing',
            items: [
              { name: 'Pizza', quantity: 2, price: 8000 },
              { name: 'Burger', quantity: 1, price: 5000 },
            ],
            total: 21000,
            deliveryAddress: '123 Main St, Kigali',
            vendor: 'Pizza Palace',
            estimatedDelivery: '2024-03-15T11:00:00',
          },
          {
            id: 2,
            orderNumber: 'ORD-002',
            date: '2024-03-14T15:45:00',
            status: 'delivered',
            items: [
              { name: 'Chicken Wings', quantity: 1, price: 12000 },
            ],
            total: 14000,
            deliveryAddress: '456 Side St, Kigali',
            vendor: 'Chicken Delight',
            deliveredAt: '2024-03-14T16:15:00',
          },
        ];
        setOrders(mockOrders);
        setLoading(false);
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message || 'Failed to load orders';
        setError(errorMessage);
        toast.error(errorMessage);
        setLoading(false);
      }
    };
    loadOrders();
  }, []);

  const getStatusColor = (status) => {
    switch (status) {
      case 'preparing':
        return 'bg-yellow-100 text-yellow-800';
      case 'on_the_way':
        return 'bg-blue-100 text-blue-800';
      case 'delivered':
        return 'bg-green-100 text-green-800';
      case 'cancelled':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const filteredOrders = orders.filter(order => {
    if (activeTab === 'current') {
      return ['preparing', 'on_the_way'].includes(order.status);
    }
    return ['delivered', 'cancelled'].includes(order.status);
  });

  if (loading) {
    return (
      <div className="min-h-screen bg-background text-text p-4 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
          <p className="mt-4">Loading orders...</p>
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

  return (
    <div className="min-h-screen bg-background text-text p-4">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-8">Your Orders</h1>

        {/* Tabs */}
        <div className="flex gap-4 mb-6">
          <button
            onClick={() => setActiveTab('current')}
            className={`px-4 py-2 rounded-md ${
              activeTab === 'current'
                ? 'bg-primary text-white'
                : 'bg-gray-200 hover:bg-gray-300'
            }`}
          >
            Current Orders
          </button>
          <button
            onClick={() => setActiveTab('history')}
            className={`px-4 py-2 rounded-md ${
              activeTab === 'history'
                ? 'bg-primary text-white'
                : 'bg-gray-200 hover:bg-gray-300'
            }`}
          >
            Order History
          </button>
        </div>

        {filteredOrders.length === 0 ? (
          <div className="text-center py-8">
            <p className="text-gray-600">
              {activeTab === 'current'
                ? 'No current orders'
                : 'No order history'}
            </p>
          </div>
        ) : (
          <div className="space-y-6">
            {filteredOrders.map(order => (
              <div key={order.id} className="bg-white rounded-lg shadow-md p-6">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h2 className="text-xl font-bold">{order.orderNumber}</h2>
                    <p className="text-gray-600">{formatDate(order.date)}</p>
                  </div>
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(
                      order.status
                    )}`}
                  >
                    {order.status.replace('_', ' ').toUpperCase()}
                  </span>
                </div>

                <div className="border-t border-b py-4 my-4">
                  <div className="mb-2">
                    <span className="font-semibold">Vendor: </span>
                    {order.vendor}
                  </div>
                  <div className="mb-2">
                    <span className="font-semibold">Delivery Address: </span>
                    {order.deliveryAddress}
                  </div>
                  {order.status === 'preparing' && order.estimatedDelivery && (
                    <div>
                      <span className="font-semibold">Estimated Delivery: </span>
                      {formatDate(order.estimatedDelivery)}
                    </div>
                  )}
                  {order.status === 'delivered' && order.deliveredAt && (
                    <div>
                      <span className="font-semibold">Delivered At: </span>
                      {formatDate(order.deliveredAt)}
                    </div>
                  )}
                </div>

                <div className="space-y-2">
                  {order.items.map((item, index) => (
                    <div key={index} className="flex justify-between">
                      <span>
                        {item.quantity}x {item.name}
                      </span>
                      <span>{item.price.toLocaleString()} RWF</span>
                    </div>
                  ))}
                </div>

                <div className="mt-4 pt-4 border-t">
                  <div className="flex justify-between font-bold">
                    <span>Total</span>
                    <span>{order.total.toLocaleString()} RWF</span>
                  </div>
                </div>

                {order.status === 'preparing' && (
                  <div className="mt-4">
                    <button
                      onClick={() => toast.info('Contacting vendor...')}
                      className="text-primary hover:text-primary-dark"
                    >
                      Contact Vendor
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default Orders; 