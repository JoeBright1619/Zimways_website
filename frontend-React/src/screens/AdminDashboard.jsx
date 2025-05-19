import { useState } from "react";

export default function AdminDashboard() {
  const [showPrompt, setShowPrompt] = useState(false);
  const [customers, setCustomers] = useState([]);

  const fetchCustomers = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/customers");
      if (!res.ok) throw new Error("Failed to fetch customers");
      const data = await res.json();
      setCustomers(data);
    } catch (err) {
      console.error("Error fetching customers:", err);
    }
  };

  const handleOpenPrompt = () => {
    fetchCustomers();
    setShowPrompt(true);
  };

  const handleClosePrompt = () => {
    setShowPrompt(false);
    setCustomers([]); // Clear data on close if needed
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 min-w-screen text-black">
      <h1 className="text-3xl font-bold mb-4">Admin Dashboard</h1>
      <p className="text-lg mb-8">Manage your restaurant's operations here.</p>

      <div className="bg-white shadow-md rounded-lg p-6 w-full max-w-2xl mb-4">
        <h2 className="text-xl font-semibold mb-4">Menu Management</h2>
        {/* Your menu management components */}
      </div>

      <button
        onClick={handleOpenPrompt}
        className="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 transition"
      >
        View All Customers
      </button>

      {/* Modal Prompt */}
      {showPrompt && (
        <div className="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex items-center justify-center z-50 px-4">
          <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-2xl relative max-h-[80vh] overflow-y-auto">
            <button
              onClick={handleClosePrompt}
              className="absolute top-3 right-4 text-red-600 text-xl hover:scale-110"
            >
              ✖
            </button>
            <h3 className="text-2xl font-bold text-center text-[var(--color-primary)] mb-4">
              Customer List
            </h3>

            {customers.length > 0 ? (
              <table className="w-full text-left text-sm border">
                <thead>
                  <tr className="bg-gray-200">
                    <th className="p-2 border">Name</th>
                    <th className="p-2 border">Email</th>
                    <th className="p-2 border">Phone</th>
                  </tr>
                </thead>
                <tbody>
                  {customers.map((customer) => (
                    <tr key={customer.id} className="hover:bg-gray-100">
                      <td className="p-2 border">{customer.name}</td>
                      <td className="p-2 border">{customer.email}</td>
                      <td className="p-2 border">{customer.phone}</td>
                      <td className="p-2 border">{customer.password}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p className="text-center">No customers found.</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
