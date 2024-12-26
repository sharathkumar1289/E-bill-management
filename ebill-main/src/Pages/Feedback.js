import React, { useState, useEffect } from 'react';
import { FaSun, FaMoon } from 'react-icons/fa'; 
import { useNavigate } from 'react-router-dom';

const FeedbackForm = () => {
  const [darkMode, setDarkMode] = useState(false);
  const [customerDetails, setCustomerDetails] = useState(null);
  const [feedback, setFeedback] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitStatus, setSubmitStatus] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  
  useEffect(() => {
    const storedDarkMode = localStorage.getItem('darkMode') === 'true';
    setDarkMode(storedDarkMode);
  }, []);

  
  const toggleDarkMode = () => {
    setDarkMode((prevMode) => {
      const newMode = !prevMode;
      localStorage.setItem('darkMode', newMode);
      return newMode;
    });
  };

  useEffect(() => {
    const storedDetails = localStorage.getItem("customerDetails");
    if (storedDetails) {
      try {
        const parsedDetails = JSON.parse(storedDetails);
        setCustomerDetails(parsedDetails);
      } catch (error) {
        console.error("Error parsing customer details:", error);
        setErrorMessage("Error loading customer details");
      }
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitStatus(null);
    setErrorMessage('');

    if (!feedback.trim()) {
      setErrorMessage("Please enter your feedback");
      setIsSubmitting(false);
      return;
    }

    const feedbackData = {
      customer: {
        customerId: customerDetails?.customerId
      },
      feedback: feedback,
      feedbackDate: new Date().toISOString(),
      status: "NEW"
    };

    try {
      const response = await fetch('http://localhost:8081/feedbacks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify(feedbackData)
      });

      if (response.ok) {
        setSubmitStatus('success');
        setFeedback('');
      } else {
        throw new Error('Failed to submit feedback');
      }
    } catch (error) {
      console.error('Error submitting feedback:', error);
      setSubmitStatus('error');
      setErrorMessage('Failed to submit feedback. Please try again later.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className={`${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-50 text-gray-800'} min-h-screen`}>

      <nav
        className={`p-4 ${
          darkMode ? "bg-gray-700" : "bg-orange-600"
        } text-white flex justify-between items-center`}
      >
        <h1 className="text-xl font-bold">Feedback</h1>
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


      <div className="max-w-3xl mx-auto bg-white shadow-xl rounded-lg p-8 mt-8">
  <h2 className="text-3xl font-bold text-gray-900">Customer Feedback</h2>
  <p className="mt-4 text-lg text-gray-700">
    {customerDetails && (
      <div className="mt-4 text-m text-black-600 font-semibold">
        Customer ID: <span className="">{customerDetails.customerId}</span>
      </div>
    )}
  </p>

  <form onSubmit={handleSubmit} className="space-y-6 mt-6">
    {/* Feedback Textarea */}
    <div>
      <label
        htmlFor="feedback"
        className="block text-sm font-medium text-gray-700"
      >
        Your Feedback
      </label>
      <textarea
        id="feedback"
        value={feedback}
        onChange={(e) => setFeedback(e.target.value)}
        placeholder="Please share your experience with us..."
        className="mt-1 w-full p-4 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200 ease-in-out"
      />
    </div>

    {/* Success Message */}
    {submitStatus === 'success' && (
      <div className="bg-green-50 border-l-4 border-green-400 p-4 rounded-md">
        <div className="flex items-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-6 w-6 text-green-500 mr-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M5 13l4 4L19 7"
            />
          </svg>
          <p className="font-semibold text-green-600">Success!</p>
        </div>
        <p className="mt-2 text-sm text-green-600">
          Thank you for your feedback. We appreciate your input!
        </p>
        <p
            className="underline text-blue-600 rounded-md cursor-pointer" 
            onClick={() => navigate("/dashboard")}
          >
            Dashboard
          </p>
      </div>
    )}
    
    {submitStatus === 'error' && (
      <div className="bg-red-50 border-l-4 border-red-400 p-4 rounded-md">
        <div className="flex items-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-6 w-6 text-red-500 mr-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
          <p className="font-semibold text-red-600">Error</p>
        </div>
        <p className="mt-2 text-sm text-red-600">{errorMessage}</p>
      </div>
    )}

    {/* Submit Button */}
    <button
      type="submit"
      disabled={isSubmitting}
      className="w-full bg-blue-600 text-white p-3 rounded-lg text-lg font-semibold hover:bg-blue-700 focus:ring-2 focus:ring-blue-400 transition duration-200 ease-in-out"
    >
      {isSubmitting ? 'Submitting...' : 'Submit Feedback'}
    </button>
  </form>
</div>

    </div>

  );
};

export default FeedbackForm;
