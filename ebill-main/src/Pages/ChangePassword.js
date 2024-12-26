import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaSun, FaMoon } from "react-icons/fa";

const ChangePassword = () => {
  const { id } = useParams(); 
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [darkMode, setDarkMode] = useState(false);
  const navigate = useNavigate();

  const handleChangePassword = async (e) => {
    e.preventDefault();

    if (newPassword.length < 6) {
      setErrorMessage("Password must be at least 6 characters long.");
      setSuccessMessage("");
      return;
    }

    if (newPassword !== confirmPassword) {
      setErrorMessage("New password and confirm password do not match.");
      setSuccessMessage("");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8081/customers/password/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          oldPassword,
          newPassword,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      const result = await response.text();
      setSuccessMessage(result);
      setErrorMessage("");
      setOldPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } catch (error) {
      setErrorMessage(error.message || "Something went wrong. Please try again.");
      setSuccessMessage("");
    }
  };

  return (
    <div
      className={`min-h-screen flex flex-col ${
        darkMode ? "bg-gray-900 text-white" : "bg-gray-100 text-gray-900"
      }`}
    >
      <nav className={`p-4 ${darkMode ? "bg-gray-800" : "bg-orange-600"} text-white flex justify-between items-center`}>
        <h1 className="text-xl font-bold">Ebills</h1>
        <div>
          <button
            className="px-4 py-2 rounded-md mr-4 transition"
            onClick={() => setDarkMode(!darkMode)}
          >
            {darkMode ? <FaSun className="text-yellow-500" /> : <FaMoon />}
          </button>
          <button
            className="px-4 py-2 bg-white text-gray-800 rounded-md"
            onClick={() => navigate("/dashboard")}
          >
            Dashboard
          </button>
        </div>
      </nav>

      <div className="flex items-center justify-center flex-grow">
        <div
          className={`w-full max-w-md p-8 rounded-lg shadow-md ${
            darkMode ? "bg-gray-700 text-gray-200" : "bg-white"
          }`}
        >
          <h1 className="text-2xl font-bold mb-6 text-center">Change Password</h1>
          {successMessage && (
            <p className="text-center text-green-600 font-semibold mb-4">
              {successMessage}
            </p>
          )}
          {errorMessage && (
            <p className="text-center text-red-600 font-semibold mb-4">
              {errorMessage}
            </p>
          )}
          <form onSubmit={handleChangePassword}>
            <div className="mb-4">
              <label
                className={`block text-lg font-semibold mb-2 ${
                  darkMode ? "text-gray-300" : "text-gray-700"
                }`}
              >
                Old Password
              </label>
              <input
                type="password"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
                className={`w-full px-4 py-2 border rounded-md focus:outline-none ${
                  darkMode
                    ? "bg-gray-800 text-gray-200 border-gray-600"
                    : "bg-gray-100 border-gray-300"
                }`}
                required
              />
            </div>

            <div className="mb-4">
              <label
                className={`block text-lg font-semibold mb-2 ${
                  darkMode ? "text-gray-300" : "text-gray-700"
                }`}
              >
                New Password
              </label>
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className={`w-full px-4 py-2 border rounded-md focus:outline-none ${
                  darkMode
                    ? "bg-gray-800 text-gray-200 border-gray-600"
                    : "bg-gray-100 border-gray-300"
                }`}
                required
              />
            </div>

            <div className="mb-4">
              <label
                className={`block text-lg font-semibold mb-2 ${
                  darkMode ? "text-gray-300" : "text-gray-700"
                }`}
              >
                Confirm Password
              </label>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className={`w-full px-4 py-2 border rounded-md focus:outline-none ${
                  darkMode
                    ? "bg-gray-800 text-gray-200 border-gray-600"
                    : "bg-gray-100 border-gray-300"
                }`}
                required
              />
            </div>

            <button
              type="submit"
              className="w-full bg-orange-600 text-white py-2 px-4 rounded-md hover:bg-orange-400"
            >
              Change Password
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
