import { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

const TwoFactorVerify = ({ customerId, onVerificationComplete }) => {
    const [verificationCode, setVerificationCode] = useState('');
    const [loading, setLoading] = useState(false);

    const handleVerify = async (e) => {
        e.preventDefault();
        if (!verificationCode) {
            toast.error('Please enter the verification code');
            return;
        }

        setLoading(true);
        try {
            const response = await axios.post(`http://localhost:8080/api/customers/2fa/validate?customerId=${customerId}`, {
                code: verificationCode
            });

            if (response.data.valid) {
                onVerificationComplete(true);
            } else {
                toast.error('Invalid verification code');
                onVerificationComplete(false);
            }
        } catch (error) {
            toast.error('Failed to verify code');
            console.error('2FA verification error:', error);
            onVerificationComplete(false);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto">
            <form onSubmit={handleVerify} className="space-y-6">
                <h2 className="text-xl font-semibold">Two-Factor Authentication</h2>
                <p className="text-gray-600">
                    Please enter the verification code from your authenticator app.
                </p>
                <div className="space-y-4">
                    <label className="block">
                        <span className="text-gray-700">Verification Code:</span>
                        <input
                            type="text"
                            value={verificationCode}
                            onChange={(e) => setVerificationCode(e.target.value)}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
                            placeholder="Enter 6-digit code"
                            maxLength={6}
                            required
                        />
                    </label>
                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition duration-200 disabled:opacity-50"
                    >
                        {loading ? 'Verifying...' : 'Verify'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default TwoFactorVerify; 