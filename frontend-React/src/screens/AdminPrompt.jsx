import { useEffect, useState } from "react";

export default function AdminPrompt({ show, onClose, onSuccess }) {
  const [name, setName] = useState("");
  const [adminCode, setAdminCode] = useState("");
  const [error, setError] = useState(false);

  useEffect(() => {
    if (!show) {
      setName("");
      setAdminCode("");
      setError(false);
    }
  }, [show]);

  const handleSubmit = () => {
    if (adminCode === "2002") {
      sessionStorage.setItem("admin", JSON.stringify({ username: name }));
      onSuccess();
    } else {
      setError(true);
      setTimeout(() => setError(false), 2500);
    }
  };

  if (!show) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center">
      <div
        className={`w-full max-w-md bg-white rounded-2xl shadow-xl p-6 relative transition-all duration-300 transform ${
          error ? "animate-shake" : "scale-100"
        }`}
      >
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-3 right-4 text-[var(--color-secondary)] text-xl font-bold hover:scale-125 transition-transform"
        >
          ✕
        </button>

        <h2 className="text-center text-2xl font-bold font-[var(--font-primary)] text-[var(--color-primary)] mb-2">
          Admin Access
        </h2>
        <p className="text-center text-sm font-[var(--font-secondary)] text-[var(--color-text)] mb-6">
          Authorized personnel only. Enter your name and access code.
        </p>

        {/* Name input */}
        <div className="mb-4">
          <label htmlFor="name" className="block text-sm mb-1 font-medium text-[var(--color-text)]">
            Name
          </label>
          <input
            id="name"
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] bg-[var(--color-background-light)] text-[var(--color-text)]"
            placeholder="Username"
          />
        </div>

        {/* Code input */}
        <div className="mb-4">
          <label htmlFor="adminCode" className="block text-sm mb-1 font-medium text-[var(--color-text)]">
            Access Code
          </label>
          <input
            id="adminCode"
            type="password"
            value={adminCode}
            onChange={(e) => setAdminCode(e.target.value)}
            className={`w-full px-3 py-2 border rounded-lg focus:outline-none ${
              error
                ? "border-[var(--color-secondary)]"
                : "border-gray-300 focus:ring-2 focus:ring-[var(--color-secondary)]"
            } bg-[var(--color-background-light)] text-[var(--color-text)]`}
            placeholder="Enter Code"
          />
        </div>

        {error && (
          <p className="text-sm text-center text-[var(--color-secondary)] mb-4 animate-pulse">
            🚫 Incorrect access code. Try again.
          </p>
        )}

        <button
          onClick={handleSubmit}
          className="w-full py-2 bg-[var(--color-tertiary)] text-white rounded-lg hover:bg-green-700 transition font-semibold"
        >
          Enter Admin Mode
        </button>
      </div>
    </div>
  );
}
