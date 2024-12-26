import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { FaSun, FaMoon } from "react-icons/fa";
import axios from "axios";

const BillPayment = () => {
  const [darkMode, setDarkMode] = useState(false);
  const [billDetails, setBillDetails] = useState([]);
  const [billsThisMonth, setBillsThisMonth] = useState([]);
  const [previousBills, setPreviousBills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    const fetchBillDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/bills/pending/id/${id}`);
        const bills = response.data;

        const currentMonth = new Date().getMonth();
        const currentYear = new Date().getFullYear();

        const billsOfThisMonth = bills.filter(bill => {
          const dueDate = new Date(bill.dueDate);
          return dueDate.getMonth() === currentMonth && dueDate.getFullYear() === currentYear;
        });

        const previousBills = bills.filter(bill => {
          const dueDate = new Date(bill.dueDate);
          return dueDate.getMonth() !== currentMonth || dueDate.getFullYear() !== currentYear;
        });

        setBillDetails(bills);
        setBillsThisMonth(billsOfThisMonth);
        setPreviousBills(previousBills);
      } catch (err) {
        console.error("Error fetching bill details:", err.message);
        setError("Failed to fetch bill details.");
      } finally {
        setLoading(false);
      }
    };

    fetchBillDetails();
  }, [id]);

  if (loading) {
    return <div className="text-center text-xl">Loading...</div>;
  }

  if (error) {
    return <div className="text-center text-red-500">{error}</div>;
  }

  const getStatusClass = (status) => {
    switch (status.toLowerCase()) {
      case "paid":
        return "bg-green-500 text-white";
      case "pending":
        return "bg-yellow-500 text-white";
      case "overdue":
        return "bg-red-500 text-white";
      default:
        return "bg-gray-400 text-white";
    }
  };

  const renderBills = (bills, heading) => (
    <div className="mb-12">
      <h2 className="text-2xl font-semibold mb-6">{heading}</h2>
      {bills.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {bills.map((bill) => (
            <div
            key={bill.billNo}
            className={`p-6 rounded-lg shadow-lg transform hover:scale-105 transition duration-300 ${
              darkMode ? "bg-gray-800 text-white" : "bg-white text-gray-800"
            }`}
          >
            <h3 className="text-xl font-bold mb-4">Bill No: {bill.billNo}</h3>
            <div className="space-y-2">
              <div className="flex">
                <span className="font-semibold w-36">Total Amount:</span>
                <span>${bill.totalAmount}</span>
              </div>
              <div className="flex">
                <span className="font-semibold w-36">Due Date:</span>
                <span>{new Date(bill.dueDate).toLocaleDateString()}</span>
              </div>
              <div className="flex">
                <span className="font-semibold w-36">Units Consumed:</span>
                <span>{bill.unitsConsumed} kWh</span>
              </div>
              <div className="flex">
                <span className="font-semibold w-36">Status:</span>
                <span
                  className={`px-3 py-1 text-sm font-medium ${getStatusClass(
                    bill.status || "pending"
                  )}`}
                >
                  {bill.status || "Pending"}
                </span>
              </div>
            </div>
            <button
  className="mt-6 px-6 py-2 w-full bg-orange-600 hover:bg-orange-500 text-white font-bold rounded-md"
  onClick={() => navigate(`/customer/${id}/${bill.billNo}`)}
>
  Pay Now
</button>

          </div>
          
          ))}
        </div>
      ) : (
        <p className="text-center">No bills available.</p>
      )}
    </div>
  );

  return (
    <div className={`min-h-screen ${darkMode ? "bg-gray-900 text-white" : "bg-gray-100 text-gray-900"}`}>
      <nav
        className={`p-4 ${
          darkMode ? "bg-gray-800" : "bg-orange-600"
        } text-white flex justify-between items-center shadow-md`}
      >
        <h1 className="text-xl font-bold">Unpaid Bills</h1>
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

      <div className="p-6">
        <h1 className="text-3xl font-bold text-center mb-12">Bill Payment</h1>
        {renderBills(billsThisMonth, "Bills of This Month")}
        {renderBills(previousBills, "Previous Bills")}
      </div>
    </div>
  );
};

export default BillPayment;
