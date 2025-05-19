import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './screens/Login';
import Home from './screens/Home';
import Signup from './screens/Signup';
import Admin from './screens/Admin';
import AdminDashboard from './screens/AdminDashboard';

import {ToastContainer, toast} from 'react-toastify';


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const user = sessionStorage.getItem('user');
    if (user) {
      setIsLoggedIn(true); // User is logged in
    }
  }, []); 

  useEffect(() => {
    const admin = sessionStorage.getItem('admin');
    if (admin) {
      setIsAdmin(true); // User is admin
    }
  }, []);

  const handleLogin = (customerData) => {
    if (customerData) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false); // Show error snackbar
      toast.error("Invalid email or password");
    }
  };

  const handleLogout = () => {
    sessionStorage.removeItem('user'); // Clear user data from sessionStorage
    setIsLoggedIn(false); // Update the state
    navigate('/'); // Redirect to the login page
  };



  return (
      <div className="app">
        <Routes>
          <Route
            path="/"
            element={
              isLoggedIn ? <Navigate to="/home" /> : <Login onLogin={handleLogin} />
            }
          />
          <Route
            path="/signup"
            element={<Signup />}
          />
          <Route
            path="/home"
            //element={isLoggedIn ? <Home /> : <Navigate to="/" />}
            element={<Home logout={handleLogout}/>}
          />
          <Route
            path="/AdminDashboard"
            element={isAdmin ? <AdminDashboard /> : <Navigate to="/" />}
          />
         
        </Routes>

      </div>
  );
}

export default App;