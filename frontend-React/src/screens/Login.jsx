/* eslint-disable no-unused-vars */
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {ToastContainer,toast} from 'react-toastify';
import AdminPrompt from "./AdminPrompt";
import axios from 'axios';

function Login({ onLogin }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPrompt, setShowPrompt] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async() => {
    try {
      const response = await axios.post('http://localhost:8080/api/customers/login', {
        email,
        password,
      });
      toast.success('Login successful!');
      sessionStorage.setItem('user', JSON.stringify(response.data)); // Store user data in sessionStorage
      onLogin(response.data); // Pass the logged-in customer data to the parent
      navigate('/home'); // Redirect to the home page
    } catch (error) {
      if(error.response && error.response.status === 401) {
      console.error('Login failed:', error);
      toast.error('Invalid email or password!');
      }
      else{
        console.error('Error during login:', error);
        toast.error('Failed to login. Please try again.');
      }
      
    }
  };

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
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full p-2 mb-4 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800 bg-gray-50"
        />
        <button
          onClick={handleLogin}
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition"
        >
          Login
        </button>
        <p className="mt-4 text-center text-gray-600">
          Don't have an account?{' '}
          <Link to="/signup" className="text-blue-500 hover:underline">
            Sign up
          </Link>
        </p>
      </div>
      <Link  onClick={() => setShowPrompt(true)} className="text-blue-500 hover:underline">
            Admin?
          </Link>
      <AdminPrompt
        show={showPrompt}
        onClose={() => setShowPrompt(false)}
        onSuccess={() => {
          setShowPrompt(false);
          navigate("/AdminDashboard");
        }}
      />
      <ToastContainer />
    </div>
  );
}

export default Login;