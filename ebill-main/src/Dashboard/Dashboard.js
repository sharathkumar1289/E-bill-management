import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaSun,
  FaMoon,
  FaUser,
  FaWallet,
  FaHistory,
  FaKey,
  FaSignOutAlt,
} from "react-icons/fa";
import Navbar from "../Components/Navbar";

const Dashboard = () => {
  const [darkMode, setDarkMode] = useState(false);
  const [customerDetails, setCustomerDetails] = useState(null);
  const navigate = useNavigate();
  
  const toggleDarkMode = () => setDarkMode(!darkMode);
  
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("customerDetails");
    navigate("/");
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    const storedDetails = localStorage.getItem("customerDetails");

    if (!token || !storedDetails) {
      navigate("/"); 
    } else {
      try {
        const parsedDetails = JSON.parse(storedDetails);
        setCustomerDetails(parsedDetails);
      } catch (error) {
        console.error("Error parsing customer details:", error);
        localStorage.removeItem("token");
        localStorage.removeItem("customerDetails");
        navigate("/"); 
      }
    }
  }, [navigate]);
  if (!customerDetails) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100 text-gray-900">
        <p>Loading...</p>
      </div>
    );
  }

  return (
    <div className={`${darkMode ? "bg-black text-white" : "bg-gray-100 text-gray-900"} min-h-screen`}>
      {/* Navbar */}
      <Navbar
        darkMode={darkMode}
        toggleDarkMode={toggleDarkMode}
        handleLogout={handleLogout}
        customerDetails={customerDetails}
      />
      <div className="container mx-auto p-6">
        <h1 className="text-4xl font-bold mb-8 text-center">
          Welcome, {customerDetails.name}
        </h1>
        <h1 className="text-2xl font-bold mb-8 text-center">
          Your Id is:  {customerDetails.customerId}, Remember ID for future reference
        </h1>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <DashboardCard
            title="My Account"
            description="View and update your account information"
            icon={<FaUser />}
            darkMode={darkMode}
            onClick={() => navigate(`/account/${customerDetails.customerId}`)}
          />
          <DashboardCard
            title="Pay Bill"
            description="Make payments and manage billing"
            icon={<FaWallet />}
            darkMode={darkMode}
            onClick={() => navigate(`/pay-bill/${customerDetails.customerId}`)}
          />
          <DashboardCard
            title="Payment History"
            description="View your past payments"
            icon={<FaHistory />}
            darkMode={darkMode}
            onClick={() => navigate(`/payment-history/${customerDetails.customerId}`)}
          />
          <DashboardCard
            title="Change Password"
            description="Update your password for security"
            icon={<FaKey />}
            darkMode={darkMode}
            onClick={() => navigate(`/change-password/${customerDetails.customerId}`)}
          />
          <DashboardCard
            title="Feedback"
            description="Provide your feedback and issues here"
            icon={<FaWallet />}
            darkMode={darkMode}
            onClick={() => navigate(`/feedback/${customerDetails.customerId}`)}
          />
          <DashboardCard
            title="Logout"
            description="Sign out of your account"
            icon={<FaSignOutAlt />}
            darkMode={darkMode}
            onClick={handleLogout}
          />
        </div>
      </div>
    </div>
  );
};

const DashboardCard = ({ title, description, icon, darkMode, onClick }) => (
  <div
    className={`cursor-pointer rounded-lg overflow-hidden shadow-lg transition-transform hover:scale-105 transform ${
      darkMode ? "bg-gray-800 text-white" : "bg-white text-gray-900"
    }`}
    onClick={onClick}
  >
    <div className="p-6">
      <div className="flex items-center space-x-4 mb-4">
        <div className="text-3xl text-orange-600">{icon}</div>
        <h2 className="text-2xl font-semibold">{title}</h2>
      </div>
      <p>{description}</p>
    </div>
  </div>
);

export default Dashboard;

