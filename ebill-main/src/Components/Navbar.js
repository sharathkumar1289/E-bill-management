import React from "react";
import { FaSun, FaMoon, FaUser, FaSignOutAlt } from "react-icons/fa";

const Navbar = ({ darkMode, toggleDarkMode, handleLogout, customerDetails }) => (
  <nav
    className={`flex flex-wrap items-center justify-between px-4 py-3 shadow-md rounded-lg transition-all ${
      darkMode ? "bg-gray-900 text-white" : "bg-orange-600 text-white"
    }`}
  >
    {/* Left side (Logo/Title) */}
    <div className="flex items-center space-x-4">
      <span className="text-2xl font-semibold tracking-wide sm:text-3xl pl-20">
        Ebills
      </span>
    </div>

    {/* Right side (User Info, Dark Mode Toggle, Logout) */}
    <div className="flex items-center space-x-6">
      {/* User Info */}
      <div className="flex items-center space-x-2">
        <FaUser className="text-lg sm:text-xl" />
        <p className="text-sm sm:text-lg font-medium truncate">
          ID: {customerDetails?.customerId || "Guest"}
        </p>
      </div>

      {/* Dark Mode Toggle */}
      <button
        className="p-2 rounded-full hover:bg-opacity-80 transition duration-200"
        onClick={toggleDarkMode}
        aria-label="Toggle Dark Mode"
      >
        {darkMode ? (
          <FaSun className="text-yellow-500 text-xl sm:text-2xl" />
        ) : (
          <FaMoon className="text-gray-200 text-xl sm:text-2xl" />
        )}
      </button>

      {/* Logout Button */}
      <button
        className="p-2 rounded-full hover:bg-opacity-80 transition duration-200"
        onClick={handleLogout}
        aria-label="Logout"
      >
        <FaSignOutAlt className="text-xl sm:text-2xl" />
      </button>
    </div>
  </nav>
);

export default Navbar;
