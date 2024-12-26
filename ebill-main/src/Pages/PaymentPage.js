import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { FaSun, FaMoon } from "react-icons/fa";
import axios from "axios";

const PaymentPage = () => {
  const { customerId, billNo } = useParams();
  const navigate = useNavigate();
  const [darkMode, setDarkMode] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState("CREDIT_CARD"); // Default to CREDIT_CARD
  const [transactionId, setTransactionId] = useState("");
  const [billDetails, setBillDetails] = useState(null);
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);
  const [paymentDetails, setPaymentDetails] = useState([]);
  const [paymentDetailInput, setPaymentDetailInput] = useState("");

  // Map display values to API values
  const paymentMethodsMap = {
    "Credit Card": "CREDIT_CARD",
    "Debit Card": "DEBIT_CARD",
    "Net Banking": "NET_BANKING",
    "UPI": "UPI"
  };

  // Generate a unique transaction ID when the component mounts
  useEffect(() => {
    const generateTransactionId = () => {
      return `TXN${Date.now()}${Math.floor(Math.random() * 1000)}`;
    };
    setTransactionId(generateTransactionId());
  }, []);

  // Fetch the bill details when the component mounts
  useEffect(() => {
    const fetchBillDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/bills/${billNo}`);
        const payingbill = response.data;
        setBillDetails(payingbill);
      } catch (err) {
        console.error("Error fetching bill details:", err.message);
        setStatus("Failed to fetch bill details.");
      } finally {
        setLoading(false);
      }
    };

    if (billNo) {
      fetchBillDetails();
    }
  }, [billNo]);

  // Fetch the payment details when the component mounts
  useEffect(() => {
    const fetchPaymentDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/api/paymentDetails`);
        setPaymentDetails(response.data);
      } catch (err) {
        console.error("Error fetching payment details:", err.message);
      }
    };

    fetchPaymentDetails();
  }, []);

  // Handle the payment process
  const handlePayment = async () => {
    try {
      // Validate payment details by calling the backend API
      const response = await axios.post("http://localhost:8081/api/paymentDetails/validatePayment", {
        paymentType: paymentMethod,  // Send the mapped value
        paymentDetail: paymentDetailInput,  
      });
  
      if (response.data.includes("Success")) {
        // Proceed with further payment logic
        const paymentResponse = await axios.post(`http://localhost:8081/payments/${billNo}`, {
          paymentMethod,
          paymentDetail: paymentDetailInput,
          transactionId,
          amountPaid: parseFloat(billDetails.totalAmount),
        });
  
        setStatus("Payment Successful!");
        setTimeout(() => navigate("/dashboard"), 2000);
      } else {
        setStatus(response.data);  // Show error if validation fails
      }
    } catch (error) {
      console.error("Payment failed:", error);
      setStatus("Payment Failed.");
    }
  };
  

  if (loading) {
    return <div className="text-center text-xl">Loading...</div>;
  }

  return (
    <div className={`h-screen ${darkMode ? "bg-gray-900 text-white" : "bg-gray-100 text-black"}`}>
      <nav
        className={`p-4 ${darkMode ? "bg-gray-800" : "bg-orange-600"} text-white flex justify-between items-center shadow-md`}
      >
        <h1 className="text-xl font-bold">Customer Portal</h1>
        <div>
          <button
            className="px-4 py-2 rounded-md mr-4 bg-white text-gray-800"
            onClick={() => setDarkMode(!darkMode)}
          >
            {darkMode ? <FaSun className="text-yellow-500" /> : <FaMoon />}
          </button>
          <button
            className="px-4 py-2 bg-gray-700 hover:bg-gray-600 text-white rounded-md"
            onClick={() => navigate("/dashboard")}
          >
            Dashboard
          </button>
        </div>
      </nav>

      <div className={`shadow-lg rounded-lg p-6 max-w-md w-full mx-auto mt-12 ${darkMode ? "bg-gray-800 text-white" : "bg-white text-black"}`}>
        <h1 className="text-2xl font-bold mb-6 text-center">
          Payment for Bill No: <span className="text-orange-600">{billNo}</span>
        </h1>

        {billDetails && (
          <div className="space-y-4">
            <div>
              <label className="block font-medium mb-2">Transaction ID</label>
              <input
                type="text"
                className="w-full p-2 border rounded bg-gray-100 text-gray-600 cursor-not-allowed"
                value={transactionId}
                readOnly
              />
            </div>

            <div className={`${darkMode ? "bg-gray-800 text-white" : "bg-white text-black"}`}>
              <label className="block font-medium mb-2">Payment Method</label>
              <select
                className="w-full p-2 border rounded bg-gray-100 text-gray-600"
                value={paymentMethod} // State stores the API value
                onChange={(e) => setPaymentMethod(paymentMethodsMap[e.target.value])} // Map UI text to API value
              >
                <option value="Credit Card">Credit Card</option>
                <option value="Debit Card">Debit Card</option>
                <option value="Net Banking">Net Banking</option>
                <option value="UPI">UPI</option>
              </select>
            </div>

            <div>
              <label className="block font-medium mb-2">Enter {paymentMethod} details</label>
              <input
                type="text"
                className="w-full p-2 border rounded bg-gray-100 text-gray-600"
                value={paymentDetailInput}
                onChange={(e) => setPaymentDetailInput(e.target.value)}
              />
            </div>

            <div>
              <label className="block font-medium mb-2">Amount to be Paid: ${billDetails.totalAmount}</label>
              <input
                type="number"
                className="w-full p-2 border rounded bg-gray-100 text-gray-600 cursor-not-allowed"
                value={billDetails.totalAmount}
                readOnly
              />
            </div>

            <button
              className="mt-4 w-full bg-orange-600 hover:bg-orange-500 text-white py-2 font-bold rounded transition duration-300"
              onClick={handlePayment}
            >
              Pay Now
            </button>
            <button
                className="mt-4 w-full bg-orange-600 hover:bg-orange-500 text-white py-2 font-bold rounded transition duration-300"
                onClick={() => navigate(`/pay-bill/${customerId}`)}
              >
                Back to Bills
              </button>

            {status && (
              <p
                className={`text-center mt-4 font-bold ${
                  status === "Payment Successful!"
                    ? "text-green-600"
                    : "text-red-600"
                }`}
              >
                {status}
              </p>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentPage;
