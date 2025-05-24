import { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './screens/Login';
import Home from './screens/Home';
import Signup from './screens/Signup';
import AdminDashboard from './screens/AdminDashboard';
import VendorDetails from './screens/VendorDetails';
import { useUser } from './context/UserContext';
import { ToastContainer, toast } from 'react-toastify';
import Items from './screens/Items';
import Cart from './screens/Cart';
function App() {
  const { user, loging, logout } = useUser();
  const [isAdmin, setIsAdmin] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const admin = sessionStorage.getItem('admin');
    if (admin) {
      setIsAdmin(true);
    }
  }, []);

  const handleLogin = (customerData) => {
    if (customerData) {
      loging(customerData);
      navigate('/home');
    } else {
      toast.error("Invalid email or password");
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="app">
      <Routes>
        <Route
          path="/"
          element={
            user ? <Navigate to="/home" /> : <Login onLogin={handleLogin} />
          }
        />
        <Route
          path="/signup"
          element={<Signup />}
        />
        <Route
          path="/home"
          element={user ? <Home logout={handleLogout} customerId={user.id} /> : <Navigate to="/" />}
        />
        <Route
          path="/vendor/:vendorId"
          element={user ? <VendorDetails /> : <Navigate to="/" />}
        />
        <Route
          path="/items"
          element={user ? <Items /> : <Navigate to="/" />}
        />
        <Route
          path="/cart"
          element={user ? <Cart customerId={user.id} /> : <Navigate to="/" />}
        />
        <Route
          path="/AdminDashboard"
          element={isAdmin ? <AdminDashboard /> : <Navigate to="/" />}
        />
      </Routes>
      <ToastContainer />
    </div>
  );
}

export default App;