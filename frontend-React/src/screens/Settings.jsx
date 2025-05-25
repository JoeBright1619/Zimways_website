/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { useUser } from '../context/UserContext';
import TwoFactorSetup from '../components/TwoFactorSetup';
import axios from 'axios';
import { toast } from 'react-toastify';

function Settings() {
    const { user } = useUser();
    const [showSetup, setShowSetup] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleDisable2FA = async () => {
        const code = prompt('Please enter your 2FA code to disable two-factor authentication:');
        if (!code) return;

        setLoading(true);
        try {
            await axios.post(`http://localhost:8080/api/customers/2fa/disable?customerId=${user.id}`, {
                code
            });
            toast.success('2FA has been disabled');
            window.location.reload(); // Refresh to update user state
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to disable 2FA');
        } finally {
            setLoading(false);
        }
    };

    const handleSetupComplete = () => {
        setShowSetup(false);
        window.location.reload(); // Refresh to update user state
    };

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div className="min-h-screen min-w-screen bg-gray-100 py-12">
            <div className="max-w-7xl mx-auto sm:px-6 lg:px-8">
                <div className="bg-white overflow-hidden shadow-sm sm:rounded-lg">
                    <div className="p-6 bg-white border-b border-gray-200">
                        <h1 className="text-2xl font-semibold text-gray-900 mb-8">Account Settings</h1>

                        <div className="space-y-6">
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h2 className="text-lg font-medium text-gray-900 mb-4">Two-Factor Authentication</h2>
                                <p className="text-gray-600 mb-4">
                                    Two-factor authentication adds an extra layer of security to your account by requiring a code from your phone in addition to your password.
                                </p>

                                {user.tfaEnabled ? (
                                    <div className="space-y-4">
                                        <div className="flex items-center">
                                            <span className="flex h-8 w-8 items-center justify-center rounded-full bg-green-100 text-green-500">
                                                <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                                                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                                </svg>
                                            </span>
                                            <span className="ml-3 text-sm font-medium text-gray-900">Two-factor authentication is enabled</span>
                                        </div>
                                        <button
                                            onClick={handleDisable2FA}
                                            disabled={loading}
                                            className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition duration-200 disabled:opacity-50"
                                        >
                                            {loading ? 'Disabling...' : 'Disable 2FA'}
                                        </button>
                                    </div>
                                ) : showSetup ? (
                                    <TwoFactorSetup
                                        customerId={user.id}
                                        onSetupComplete={handleSetupComplete}
                                    />
                                ) : (
                                    <button
                                        onClick={() => setShowSetup(true)}
                                        className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-200"
                                    >
                                        Enable 2FA
                                    </button>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Settings; 