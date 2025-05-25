/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {ToastContainer,toast} from 'react-toastify';
import {useLogin} from '../hooks/useLogin';
import axios from 'axios';
import { useUser } from '../context/UserContext';
import TwoFactorVerify from '../components/TwoFactorVerify';

function Login() {
  const [role, setRole] = useState('customer');
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [show2FA, setShow2FA] = useState(false);
  const [tempUser, setTempUser] = useState(null);
  const {customer, login, loading, error } = useLogin();
  const navigate = useNavigate();
  const { loging: contextLogin } = useUser();

  const handleLogin = async(e) => {
    e.preventDefault();
    if (!identifier || !password) {
      toast.error('Please fill in all fields');
      return;
    }
    
    try {
      let endpoint = '';
      let payload = {};
      
      switch(role) {
        case 'customer':
          endpoint = 'http://localhost:8080/api/customers/login';
          payload = { email: identifier, password };
          break;
        case 'vendor':
          endpoint = 'http://localhost:8080/api/vendors/login';
          payload = { identifier: identifier, password };
          break;
        case 'admin':
          endpoint = 'http://localhost:8080/api/admin/login';
          payload = { adminId: identifier, password };
          break;
        default:
          toast.error('Invalid role selected');
          return;
      }

      const response = await axios.post(endpoint, payload);
      const loggedInUser = response.data;
      
      // Only check 2FA for customers
      if (role === 'customer' && loggedInUser.tfaEnabled) {
        setTempUser(loggedInUser);
        setShow2FA(true);
      } else {
        // Handle successful login
        contextLogin({ ...loggedInUser, role });
        toast.success('Login successful!');
        
        // Navigate based on role
        switch(role) {
          case 'vendor':
            navigate('/vendor-dashboard');
            break;
          case 'admin':
            navigate('/admin-dashboard');
            break;
          default:
            navigate('/home');
        }
      }
    } catch (error) {
      toast.error(error.response?.data || `${role} login failed`);
    }
  };

  const handle2FAVerification = (success) => {
    if (success) {
      contextLogin({ ...tempUser, role });
      toast.success('Login successful!');
      navigate('/home');
    }
  };

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
  }, [error]);

  // Reset identifier when role changes
  useEffect(() => {
    setIdentifier('');
  }, [role]);

  if (show2FA) {
    return (
      <div className="min-h-screen min-w-screen bg-gray-100 flex flex-col items-center justify-center px-4">
        <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-8">
          <TwoFactorVerify
            customerId={tempUser.id}
            onVerificationComplete={handle2FAVerification}
          />
        </div>
      </div>
    );
  }

  const getIdentifierLabel = () => {
    switch(role) {
      case 'customer':
        return 'Email';
      case 'vendor':
        return 'Vendor ID';
      case 'admin':
        return 'Admin ID';
      default:
        return 'Identifier';
    }
  };

  const getIdentifierType = () => {
    return role === 'customer' ? 'email' : 'text';
  };

  return (
    <div className="min-h-screen min-w-screen bg-gray-100 flex flex-col items-center justify-center px-4 text-black">
      <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-8">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">Login</h2>
        <form onSubmit={handleLogin} className="space-y-6">
          <div className="space-y-2">
            <label htmlFor="role" className="text-sm font-medium text-gray-700">
              Login as
            </label>
            <select
              id="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              disabled={loading}
            >
              <option value="customer">Customer</option>
              <option value="vendor">Vendor</option>
              <option value="admin">Admin</option>
            </select>
          </div>

          <div className="space-y-2">
            <label htmlFor="identifier" className="text-sm font-medium text-gray-700">
              {getIdentifierLabel()}
            </label>
            <input
              id="identifier"
              type={getIdentifierType()}
              placeholder={`Enter your ${getIdentifierLabel().toLowerCase()}`}
              value={identifier}
              onChange={(e) => setIdentifier(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              disabled={loading}
            />
          </div>

          <div className="space-y-2">
            <label htmlFor="password" className="text-sm font-medium text-gray-700">
              Password
            </label>
            <input
              id="password"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              disabled={loading}
            />
          </div>

          <div className="space-y-4">
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-600 text-white py-3 rounded-md hover:bg-blue-700 transition duration-200 disabled:opacity-50 font-medium"
            >
              {loading ? 'Logging in...' : 'Login'}
            </button>

            {role === 'customer' && (
              <div className="flex items-center justify-between flex-col">
                <Link
                  to="/forgot-password"
                  className="text-sm text-blue-600 hover:text-blue-800 font-medium"
                >
                  Forgot Password?
                </Link>
                <p className="text-center text-gray-600">
                  Don't have an account?{' '}
                  <Link to="/signup" className="text-sm text-blue-600 hover:text-blue-800 font-medium">
                    Sign up
                  </Link>
                </p>
              </div>
            )}
          </div>
        </form>
      </div>
      <ToastContainer />
    </div>
  );
}

export default Login;