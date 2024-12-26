import React, { useState } from "react";
import { motion } from "framer-motion";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const SignInSignUp = () => {
  const [isSignin, setisSignin] = useState(true);
  const [formdata, setformdata] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    phoneNumber: "",
    address: "",
    city: "",
    state: "",
    accountType: "regular",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const toggleSignInSignUp = () => {
    setisSignin((prevState) => !prevState);
    setError(""); 
    setSuccess(""); 
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setformdata({ ...formdata, [name]: value });
  };


  const handleClick = () => {
    navigate('/admin');}

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isSignin && formdata.password !== formdata.confirmPassword) {
      setError("Password mismatch");
      return;
    }

    const endpoint = isSignin
      ? "http://localhost:8081/customers/login"
      : "http://localhost:8081/customers/signup";

    const payload = isSignin
      ? { email: formdata.email, password: formdata.password }
      : {
          name: formdata.name,
          email: formdata.email,
          password: formdata.password,
          phoneNumber: formdata.phoneNumber,
          address: formdata.address,
          city: formdata.city,
          state: formdata.state,
          accountType: formdata.accountType,
        };

    try {
      const response = await axios.post(endpoint, payload);
      if (isSignin) {
        const { token, customerId, name, email } = response.data;
      localStorage.setItem("token", token);
      localStorage.setItem(
        "customerDetails",
        JSON.stringify({ customerId, name, email })
      );
       
        setSuccess("Sign In Successful!");
        navigate("/dashboard");
      } else {
        
        setformdata({
          name: "",
          email: "",
          password: "",
          confirmPassword: "",
          phoneNumber: "",
          address: "",
          city: "",
          state: "",
          accountType: "regular",
        });
        setSuccess("Sign Up Successful! Please sign in.");
        setTimeout(() => {
          navigate("/");
        }, 2000); 
      }
      setError(""); 
    } catch (error) {
      setError(error.response?.data?.message || "Try again.");
      setSuccess(""); 
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-gray-200">
      <div className="w-full max-w-4xl bg-white rounded-lg shadow-lg flex overflow-hidden flex-col md:flex-row">
        <div
          className={`w-full md:w-1/2 p-8 flex flex-col justify-center ${
            isSignin ? "bg-orange-600" : "bg-orange-600"
          } text-white transition-all duration-500 ease-in-out`}
        >
          <h2 className="text-3xl font-bold mb-4">
            {isSignin ? "Hello, Friend!" : "Welcome Back!"}
          </h2>
          <p className="mb-8">
            {isSignin
              ? "Fill up personal information and start your journey with us."
              : "To keep connected with us, please login with your personal info."}
          </p>
          <button
            className="py-2 px-6 font-bold bg-white text-orange-600 rounded-full hover:bg-gray-100 transition-all duration-300 ease-in-out"
            onClick={toggleSignInSignUp}
          >
            {isSignin ? "Sign Up" : "Sign In"}
          </button>
          <p onClick={handleClick} className="mt-4 cursor-pointer text-sm hover:underline" style={{ color: 'white' }}>
      If you are admin click here to login.
    </p>
        </div>

        <motion.div
          className="w-full md:w-1/2 p-8 flex flex-col justify-center bg-white"
          initial={{ opacity: 0, x: isSignin ? -50 : 50 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5, ease: "easeInOut" }}
        >
          <h2 className="text-2xl font-bold text-center mb-4">
            {isSignin ? "Sign In to Account" : "Create an Account"}
          </h2>
          
          {error && (
            <div className="text-red-500 text-center mb-4">
              <strong>{error}</strong>
            </div>
          )}
          {success && (
            <div className="text-green-500 text-center mb-4">
              <strong>{success}</strong>
            </div>
          )}
          <form className="space-y-4" onSubmit={handleSubmit}>
            {!isSignin && (
              <>
                <input
                  type="text"
                  name="name"
                  placeholder="Full Name"
                  value={formdata.name}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                />
                <input
                  type="text"
                  name="address"
                  placeholder="Address"
                  value={formdata.address}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                />
                <input
                  type="text"
                  name="city"
                  placeholder="City"
                  value={formdata.city}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                />
                <input
                  type="text"
                  name="state"
                  placeholder="State"
                  value={formdata.state}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                />
                <input
                  type="tel"
                  name="phoneNumber"
                  placeholder="Phone Number"
                  value={formdata.phoneNumber}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                />
              </>
            )}
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={formdata.email}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
            />
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formdata.password}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
            />
            {!isSignin && (
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm Password"
                value={formdata.confirmPassword}
                onChange={handleChange}
                required
                className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
              />
            )}
            <button
              type="submit"
              className="w-full py-2 bg-orange-600 text-white rounded-md hover:bg-orange-500 transition-all duration-300 ease-in-out"
            >
              {isSignin ? "Sign In" : "Sign Up"}
            </button>
          </form>
        </motion.div>
      </div>
    </div>
  );
};

export default SignInSignUp;
