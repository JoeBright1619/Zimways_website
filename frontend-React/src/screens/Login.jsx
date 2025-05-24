/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {ToastContainer,toast} from 'react-toastify';
import {useLogin} from '../hooks/useLogin';
import AdminPrompt from "./AdminPrompt";
import axios from 'axios';
import { useUser } from '../context/UserContext';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPrompt, setShowPrompt] = useState(false);
  const {customer, login, loading, error } = useLogin();
  const navigate = useNavigate();
  const { loging: contextLogin } = useUser(); // Rename login to avoid clash

  const handleLogin = async() => {
    if (!email || !password) {
      toast.error('Please fill in all fields');
      return;
    }
    await login(email, password);
  };

  useEffect(() => {
    if (customer) {
      toast.success('Login successful!');
      contextLogin(customer); // Store in global context
      navigate('/home');
    }
  }, [customer]);

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
  }, [error]);

  return (
    <div className="flex items-center justify-center min-h-screen min-w-screen bg-gray-100 flex-col">
      <div className="bg-white p-8 rounded shadow-md w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Login</h2>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full p-2 mb-4 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800 bg-gray-50"
          disabled={loading}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full p-2 mb-4 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800 bg-gray-50"
          disabled={loading}
        />
        <button
          onClick={handleLogin}
          disabled={loading}
          className={`w-full py-2 rounded transition ${
            loading
              ? 'bg-blue-300 cursor-not-allowed'
              : 'bg-blue-500 hover:bg-blue-600'
          } text-white`}
        >
          {loading ? 'Logging in...' : 'Login'}
        </button>
        <p className="mt-4 text-center text-gray-600">
          Don't have an account?{' '}
          <Link to="/signup" className="text-blue-500 hover:underline">
            Sign up
          </Link>
        </p>
      </div>
      <Link
        onClick={() => setShowPrompt(true)}
        className="text-blue-500 hover:underline mt-4"
      >
        Admin?
      </Link>
      <AdminPrompt
        show={showPrompt}
        onClose={() => setShowPrompt(false)}
        onSuccess={() => {
          setShowPrompt(false);
          navigate('/AdminDashboard');
        }}
      />
      <ToastContainer />
    </div>
  );
}

export default Login;