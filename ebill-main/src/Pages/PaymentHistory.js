import React, { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { FaSun, FaMoon } from "react-icons/fa";
import axios from "axios";

const PaymentHistory = () => {
  const [darkMode, setDarkMode] = useState(false);
  const [paymentHistory, setPaymentHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    const fetchPaymentHistory = async () => {
      console.log("Fetching payment history for ID:", id);
      try {
        const response = await axios.get(`http://localhost:8081/bills/id/${id}`);
        console.log("Fetched data:", response.data);
        setPaymentHistory(response.data);
      } catch (err) {
        console.error("Error fetching payment history:", err.message);
        if (err.response) {
          console.error("Response error:", err.response.data);
        }
        setError("Failed to fetch payment history.");
      } finally {
        setLoading(false);
      }
    };

    fetchPaymentHistory();
  }, [id]);
  return (
    <div
      className={`min-h-screen ${
        darkMode ? "bg-black text-white" : "bg-gray-100 text-gray-900"
      }`}
    >
      <nav
        className={`p-4 ${
          darkMode ? "bg-gray-700" : "bg-orange-600"
        } text-white flex justify-between items-center`}
      >
        <h1 className="text-xl font-bold">Ebill history</h1>
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

      {/* Content */}
      <div className="container mx-auto p-6">
        <h1 className="text-3xl font-bold mb-6 text-center">Payment History</h1>

        {loading ? (
          <div className="text-center">Loading...</div>
        ) : error ? (
          <div className="text-center text-red-500">{error}</div>
        ) : paymentHistory.length === 0 ? (
          <div className="text-center">No payment history available.</div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {paymentHistory.map((payment, index) => (
              <div
                key={index}
                className={`${
                  darkMode
                    ? "bg-gray-700 text-gray-200"
                    : "bg-white text-gray-900"
                } rounded-lg shadow-lg p-6 space-y-4 transition-all duration-300 hover:scale-105 hover:shadow-xl`}
              >
                <div className="flex justify-between">
                  <div className="font-semibold text-lg">
                    Bill No: {payment.billNo}
                  </div>
                  <div
                    className={`px-3 py-1 text-sm rounded-full ${
                      payment.status === "Paid" || payment.status === "PAID"
                        ? "bg-green-600 text-white"
                        : payment.status === "Pending"
                        ? "bg-yellow-500 text-white"
                        : "bg-red-600 text-white"
                    }`}
                  >
                    {payment.status || "N/A"}
                  </div>
                </div>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="font-medium">Due Date:</span>
                    <span>{new Date(payment.dueDate).toLocaleDateString()}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-medium">Total Amount:</span>
                    <span>${payment.totalAmount}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-medium">Overdue Amount:</span>
                    <span>${payment.overdueAmount}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-medium">Units Consumed:</span>
                    <span>{payment.unitsConsumed}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Back to Dashboard Button */}
        <div className="flex justify-center mt-8">
          <Link
            to="/dashboard"
            className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-500 transition duration-300"
          >
            Back to Dashboard
          </Link>
        </div>
      </div>
    </div>
  );
};

export default PaymentHistory;
