import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaSun, FaMoon } from "react-icons/fa";

const AccountDetails = () => {
  const { id } = useParams();
  const [customerData, setCustomerData] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [updatedData, setUpdatedData] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);
  const [darkMode, setDarkMode] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCustomerData = async () => {
      try {
        const response = await fetch(`http://localhost:8081/customers/${id}`);
        if (!response.ok) throw new Error("Failed to fetch customer data");
        const data = await response.json();
        setCustomerData(data);
        setUpdatedData(data);
      } catch (error) {
        setErrorMessage(error.message);
      }
    };
    fetchCustomerData();
  }, [id]);

  const handleEdit = () => setEditMode(true);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdatedData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch(`http://localhost:8081/customers/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedData),
      });
      if (!response.ok) throw new Error("Unable to update customer details");
      const updatedCustomer = await response.json();
      setCustomerData(updatedCustomer);
      setEditMode(false);
      setSuccessMessage("Updated Successfully!");
      setTimeout(() => setSuccessMessage(""), 3000); 
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  if (errorMessage) {
    return (
      <div
        className={`min-h-screen flex items-center justify-center ${
          darkMode ? "bg-gray-800 text-gray-200" : "bg-gray-100 text-gray-900"
        }`}
      >
        <p>Error: {errorMessage}</p>
      </div>
    );
  }

  if (!customerData) {
    return (
      <div
        className={`min-h-screen flex items-center justify-center ${
          darkMode ? "bg-gray-800 text-gray-200" : "bg-gray-100 text-gray-900"
        }`}
      >
        <p>Loading...</p>
      </div>
    );
  }

  return (
    <div
      className={`min-h-screen ${
        darkMode ? "bg-black text-gray-200" : "bg-gray-100 text-gray-900"
      }`}
    >
      <nav
        className={`p-4 ${
          darkMode ? "bg-gray-700" : "bg-orange-600"
        } text-white flex justify-between items-center`}
      >
        <h1 className="text-xl font-bold">Customer Portal</h1>
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
        <h1 className="text-3xl font-bold mb-6 text-center">Account Details</h1>
        {successMessage && (
          <p className="text-center text-green-600 font-semibold">
            {successMessage}
          </p>
        )}
        {errorMessage && (
          <p className="text-center text-red-600 font-semibold">
            {errorMessage}
          </p>
        )}
        <div
          className={`${
            darkMode ? "bg-gray-700 text-gray-200" : "bg-white"
          } shadow-lg rounded-lg p-6`}
        >
          {editMode ? (
            <>
              <form>
  {Object.keys(customerData).map((key) => {
    console.log("Field key:", key); // Debug the keys being iterated
    return (
      <div key={key} className="mb-4">
        <label
          className={`block text-lg font-semibold mb-2 ${
            darkMode ? "text-gray-300" : "text-gray-700"
          }`}
        >
          {key.charAt(0).toUpperCase() + key.slice(1)}:
        </label>
        <input
          type="text"
          name={key}
          value={updatedData[key]}
          onChange={handleInputChange}
          className={`w-full px-4 py-2 border rounded-md focus:outline-none ${
            darkMode
              ? "bg-gray-800 text-gray-200 border-gray-600"
              : "bg-gray-100 border-gray-300"
          }`}
          disabled={key === "email" || key === "password" || key === "customerId"}
        />
      </div>
    );
  })}
</form>

              <div className="flex justify-center gap-4 mt-4">
                <button
                  onClick={handleSubmit}
                  className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-500"
                >
                  Submit
                </button>
                <button
                  onClick={() => setEditMode(false)}
                  className="px-6 py-2 bg-red-600 text-white rounded-md hover:bg-red-500"
                >
                  Cancel
                </button>
              </div>
            </>
          ) : (
            <table
              className={`w-full text-left ${
                darkMode ? "text-gray-200" : "text-gray-900"
              }`}
            >
              <tbody>
                {Object.keys(customerData).map((key) => (
                  <tr key={key}>
                    <th className="px-4 py-2 text-lg font-semibold">
                      {key.charAt(0).toUpperCase() + key.slice(1)}
                    </th>
                    <td className="px-4 py-2">{customerData[key]}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
          {!editMode && (
            <div className="flex justify-center gap-4 mt-6">
              <button
                className="px-6 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-500"
                onClick={() => navigate("/dashboard")}
              >
                Back to Dashboard
              </button>
              <button
                className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-500"
                onClick={handleEdit}
              >
                Edit
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AccountDetails;
