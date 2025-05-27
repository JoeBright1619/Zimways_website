import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import {
  getDashboardStats,
  getRecentOrders,
  getVendorPerformance,
  getRevenueStats,
} from "../api/adminApi";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line, Bar, Pie } from 'react-chartjs-2';

// Register ChartJS components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

export default function AdminDashboard() {
  const navigate = useNavigate();
  const { logout } = useUser();
  const [stats, setStats] = useState(null);
  const [recentOrders, setRecentOrders] = useState([]);
  const [vendorPerformance, setVendorPerformance] = useState([]);
  const [revenueData, setRevenueData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [customers, setCustomers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [customersPerPage] = useState(3);

  useEffect(() => {
    fetchDashboardData();
    fetchCustomers();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const [statsData, ordersData, vendorsData, revenueStatsData] = await Promise.all([
        getDashboardStats(),
        getRecentOrders(),
        getVendorPerformance(),
        getRevenueStats(),
      ]);

      setStats(statsData);
      setRecentOrders(ordersData);
      setVendorPerformance(vendorsData);
      setRevenueData(revenueStatsData);
    } catch (error) {
      toast.error("Failed to fetch dashboard data");
      console.error("Dashboard data fetch error:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCustomers = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/customers");
      if (!res.ok) throw new Error("Failed to fetch customers");
      const data = await res.json();
      setCustomers(data);
    } catch (err) {
      console.error("Error fetching customers:", err);
      toast.error("Failed to fetch customers");
    }
  };

  // Get current customers for pagination
  const indexOfLastCustomer = currentPage * customersPerPage;
  const indexOfFirstCustomer = indexOfLastCustomer - customersPerPage;
  const currentCustomers = customers.slice(indexOfFirstCustomer, indexOfLastCustomer);
  const totalPages = Math.ceil(customers.length / customersPerPage);

  // Change page
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  // Chart configurations
  const revenueChartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Daily Revenue (Last 7 Days)',
      },
    },
  };

  const revenueChartData = {
    labels: revenueData?.labels || [],
    datasets: [
      {
        label: 'Revenue',
        data: revenueData?.data || [],
        borderColor: 'rgb(53, 162, 235)',
        backgroundColor: 'rgba(53, 162, 235, 0.5)',
      },
    ],
  };

  const orderStatusData = {
    labels: ['Completed', 'Pending', 'Cancelled'],
    datasets: [
      {
        data: [
          recentOrders.filter(order => order.status === 'COMPLETED').length,
          recentOrders.filter(order => order.status === 'PENDING').length,
          recentOrders.filter(order => order.status === 'CANCELLED').length,
        ],
        backgroundColor: [
          'rgba(75, 192, 192, 0.6)',
          'rgba(255, 206, 86, 0.6)',
          'rgba(255, 99, 132, 0.6)',
        ],
        borderColor: [
          'rgba(75, 192, 192, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(255, 99, 132, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  const vendorChartData = {
    labels: vendorPerformance.slice(0, 5).map(vendor => vendor.name),
    datasets: [
      {
        label: 'Total Sales',
        data: vendorPerformance.slice(0, 5).map(vendor => vendor.totalSales),
        backgroundColor: 'rgba(53, 162, 235, 0.5)',
      },
    ],
  };

  const vendorChartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Top 5 Vendors by Sales',
      },
    },
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen min-w-screen bg-gray-100 p-6 text-black">
      {/* Header with logout */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Admin Dashboard</h1>
        <button
          onClick={handleLogout}
          className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition flex items-center gap-2"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fillRule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V4a1 1 0 00-1-1H3zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clipRule="evenodd" />
          </svg>
          Logout
        </button>
      </div>

      {/* Overview Statistics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-600">Total Revenue</h3>
          <p className="text-3xl font-bold">{stats?.totalRevenue?.toFixed(2)}</p>
        </div>
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-600">Total Orders</h3>
          <p className="text-3xl font-bold">{stats?.totalOrders}</p>
        </div>
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-600">Active Customers</h3>
          <p className="text-3xl font-bold">{stats?.activeCustomers}</p>
        </div>
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-600">Active Vendors</h3>
          <p className="text-3xl font-bold">{stats?.activeVendors}</p>
        </div>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        {/* Revenue Trend Chart */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <Line options={revenueChartOptions} data={revenueChartData} />
        </div>

        {/* Top Vendors Chart */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <Bar options={vendorChartOptions} data={vendorChartData} />
        </div>

        {/* Order Status Distribution */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="h-[300px] flex items-center justify-center">
            <Pie 
              data={orderStatusData}
              options={{
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                  legend: {
                    position: 'top',
                  },
                  title: {
                    display: true,
                    text: 'Order Status Distribution',
                  },
                },
              }}
            />
          </div>
        </div>
      </div>

      {/* Recent Orders */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4">Recent Orders</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="bg-gray-50">
                <th className="p-3">Order ID</th>
                <th className="p-3">Customer</th>
                <th className="p-3">Total</th>
                <th className="p-3">Status</th>
                <th className="p-3">Date</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map((order) => (
                
                <tr key={order.id} className="border-t">
                  <td className="p-3">{order.id.substring(0, 8)}</td>
                  <td className="p-3">{order.customer?.name || "N/A"}</td>
                  <td className="p-3">${(order.total || 0).toFixed(2)}</td>
                  <td className="p-3">
                    <span className={`px-2 py-1 rounded-full text-xs ${
                      order.status === "COMPLETED"
                        ? "bg-green-100 text-green-800"
                        : order.status === "PENDING"
                        ? "bg-yellow-100 text-yellow-800"
                        : "bg-gray-100 text-gray-800"
                    }`}>
                      {order.status}
                    </span>
                  </td>
                  <td className="p-3">
                    {new Date(order.orderDate).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Vendor Performance */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4">Vendor Performance</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="bg-gray-50">
                <th className="p-3">Vendor</th>
                <th className="p-3">Total Sales</th>
                <th className="p-3">Orders</th>
                <th className="p-3">Rating</th>
              </tr>
            </thead>
            <tbody>
              {vendorPerformance.map((vendor) => (
                console.log(vendor),
                <tr key={vendor.id} className="border-t">
                  <td className="p-3">{vendor.name || 'N/A'}</td>
                  <td className="p-3">${(vendor.totalSales || 0).toFixed(2)}</td>
                  <td className="p-3">{vendor.totalOrders || 0}</td>
                  <td className="p-3">
                    <div className="flex items-center">
                      <span className="text-yellow-400">★</span>
                      <span className="ml-1">{(vendor.rating || 0).toFixed(1)}</span>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Customer Management */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4">Customer Management</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
                <thead>
              <tr className="bg-gray-50">
                <th className="p-3">Name</th>
                <th className="p-3">Email</th>
                <th className="p-3">Phone</th>
                <th className="p-3">Status</th>
                <th className="p-3">Password</th>
                  </tr>
                </thead>
                <tbody>
              {currentCustomers.map((customer) => (
                <tr key={customer.id} className="border-t">
                  <td className="p-3">{customer.name}</td>
                  <td className="p-3">{customer.email}</td>
                  <td className="p-3">{customer.phone}</td>
                  <td className="p-3">
                    <span className="px-2 py-1 rounded-full text-xs bg-green-100 text-green-800">
                      Active
                    </span>
                  </td>
                  <td className="p-3">{customer.password}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
        </div>

        {/* Pagination */}
        <div className="flex items-center justify-between mt-4">
          <div className="text-sm text-gray-600">
            Showing {indexOfFirstCustomer + 1} to {Math.min(indexOfLastCustomer, customers.length)} of {customers.length} customers
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => paginate(currentPage - 1)}
              disabled={currentPage === 1}
              className="px-3 py-1 rounded border border-gray-300 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              Previous
            </button>
            {[...Array(totalPages)].map((_, index) => (
              <button
                key={index + 1}
                onClick={() => paginate(index + 1)}
                className={`px-3 py-1 rounded border ${
                  currentPage === index + 1
                    ? 'bg-blue-600 text-white'
                    : 'border-gray-300 hover:bg-gray-50'
                }`}
              >
                {index + 1}
              </button>
            ))}
            <button
              onClick={() => paginate(currentPage + 1)}
              disabled={currentPage === totalPages}
              className="px-3 py-1 rounded border border-gray-300 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              Next
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
