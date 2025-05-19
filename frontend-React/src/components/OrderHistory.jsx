import { useEffect, useState } from 'react';

function OrderHistory() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    // Mock data for orders (replace with API call later)
    const mockOrders = [
      { id: 1, date: '2025-04-30', total: 15000, status: 'Delivered' },
      { id: 2, date: '2025-04-28', total: 8000, status: 'Pending' },
    ];
    setOrders(mockOrders);
  }, []);

  return (
    <div className="order-history">
      <h2>Order History</h2>
      <ul>
        {orders.map(order => (
          <li key={order.id}>
            <p>Order #{order.id}</p>
            <p>Date: {order.date}</p>
            <p>Total: {order.total.toLocaleString()} RWF</p>
            <p>Status: {order.status}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default OrderHistory;