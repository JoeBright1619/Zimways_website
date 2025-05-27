//import { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './screens/Login';
import Home from './screens/Home';
import Signup from './screens/Signup';
import AdminDashboard from './screens/AdminDashboard';
import VendorDashboard from './screens/VendorDashboard';
import VendorDetails from './screens/VendorDetails';
import { useUser } from './context/UserContext';
import { ToastContainer, toast } from 'react-toastify';
import Items from './screens/Items';
import Cart from './screens/Cart';
import ForgotPassword from './screens/ForgotPassword';
import Settings from './screens/Settings';

function App() {
  const { user, loging, logout } = useUser();

  const navigate = useNavigate();

 

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

  // Helper function to handle home route redirection
  const getHomeRouteElement = () => {
    if (!user) return <Navigate to="/" />;
    
    if (user.role === 'vendor') {
      return <Navigate to="/vendor-dashboard" />;
    }
    
    if (user.role === 'admin' ) {
      return <Navigate to="/admin-dashboard" />;
    }
    
    return <Home logout={handleLogout} customerId={user.id} />;
  };

  return (
    <div className="app">
      <Routes>
        <Route
          path="/"
          element={
            user ? <Navigate to="/home" /> : <Navigate to="/login" />
          }
        />
        <Route
          path="/login"
          element={<Login onLogin={handleLogin} />}
        />
        <Route
          path="/signup"
          element={<Signup />}
        />
        <Route
          path="/home"
          element={getHomeRouteElement()}
        />
        <Route
          path="/vendor-dashboard"
          element={user && user.role === 'vendor' ? <VendorDashboard /> : <Navigate to="/login" />}
        />
        <Route
          path="/vendor/:vendorId"
          element={user ? <VendorDetails /> : <Navigate to="/" />}
        />
        <Route
          path="/items"
          element={user ? <Items customerId={user.id} /> : <Navigate to="/" />}
        />
        <Route
          path="/cart"
          element={user ? <Cart customerId={user.id} /> : <Navigate to="/" />}
        />
        <Route
          path="/settings"
          element={user ? <Settings /> : <Navigate to="/" />}
        />
        <Route
          path="/admin-dashboard"
          element={user && user.role === 'admin' ? <AdminDashboard /> : <Navigate to="/" />}
        />
        <Route path="/forgot-password" element={<ForgotPassword />} />
      </Routes>
      <ToastContainer />
    </div>
  );
}

export default App;