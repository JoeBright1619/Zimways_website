import { useState } from 'react';
import { QRCodeCanvas } from 'qrcode.react';
import axios from 'axios';
import { toast } from 'react-toastify';

const TwoFactorSetup = ({ customerId, onSetupComplete }) => {
    const [step, setStep] = useState('initial');
    const [qrCodeData, setQrCodeData] = useState(null);
    const [verificationCode, setVerificationCode] = useState('');
    const [secret, setSecret] = useState('');
    const [loading, setLoading] = useState(false);

    const startSetup = async () => {
        setLoading(true);
        try {
            const response = await axios.post(`http://localhost:8080/api/customers/2fa/setup?customerId=${customerId}`);
            setQrCodeData(response.data.qrCodeImage);
            setSecret(response.data.secret);
            setStep('qr');
        } catch (error) {
            toast.error('Failed to start 2FA setup');
            console.error('2FA setup error:', error);
        } finally {
            setLoading(false);
        }
    };

    const verifyCode = async () => {
        if (!verificationCode) {
            toast.error('Please enter the verification code');
            return;
        }

        setLoading(true);
        try {
            await axios.post(`http://localhost:8080/api/customers/2fa/verify?customerId=${customerId}`, {
                code: verificationCode,
                secret: secret
            });
            toast.success('2FA enabled successfully');
            onSetupComplete();
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to verify code');
            console.error('2FA verification error:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center p-4">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    return (
        <div className="p-4 max-w-md mx-auto text-black">
            {step === 'initial' && (
                <div className="space-y-4">
                    <h2 className="text-xl font-semibold">Enable Two-Factor Authentication</h2>
                    <p className="text-gray-600">
                        Two-factor authentication adds an extra layer of security to your account.
                        You'll need to enter a verification code from your authenticator app when signing in.
                    </p>
                    <button
                        onClick={startSetup}
                        className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition duration-200"
                    >
                        Start Setup
                    </button>
                </div>
            )}

            {step === 'qr' && (
                <div className="space-y-6">
                    <h2 className="text-xl font-semibold">Scan QR Code</h2>
                    <p className="text-gray-600">
                        Scan this QR code with your authenticator app (like Google Authenticator or Authy).
                    </p>
                    <div className="flex justify-center p-4 bg-white rounded-lg shadow-sm">
                        <img 
                            src={qrCodeData} 
                            alt="2FA QR Code"
                            className="w-48 h-48"
                        />
                    </div>
                    <div className="space-y-4">
                        <label className="block">
                            <span className="text-gray-700">Enter verification code:</span>
                            <input
                                type="text"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value)}
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
                                placeholder="Enter 6-digit code"
                                maxLength={6}
                            />
                        </label>
                        <button
                            onClick={verifyCode}
                            className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition duration-200"
                        >
                            Verify and Enable
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TwoFactorSetup; 