import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import "./Login.css";

export const Login = ({ onClose }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [prenom, setPrenom] = useState("");
  const [nom, setNom] = useState("");
  const [isSignup, setIsSignup] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const navigate = useNavigate(); 

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setErrorMessage("");

    const url = isSignup
      ? "http://localhost:8080/utilisateur/signup"
      : "http://localhost:8080/utilisateur/login";

    const payload = isSignup
      ? `email=${encodeURIComponent(email.trim())}&password=${encodeURIComponent(password)}&prenom=${encodeURIComponent(prenom)}&nom=${encodeURIComponent(nom)}&isAdmin=false`
      : `email=${encodeURIComponent(email.trim())}&password=${encodeURIComponent(password)}`;

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: payload,
      });

      const contentType = response.headers.get("Content-Type");
      let data = {};

      if (contentType && contentType.includes("application/json")) {
        data = await response.json();
      } else {
        const text = await response.text();
        setErrorMessage(text || "An error occurred");
        return;
      }

      if (response.ok) {
        localStorage.setItem("userId", data.id);
        localStorage.setItem("is_admin", data.admin);
        navigate("/accueil");
      } else {
        setErrorMessage(data.message || "An error occurred");
      }
    } catch (error) {
      console.error("Error:", error);
      setErrorMessage("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h2>{isSignup ? "Sign Up" : "Login"}</h2>

      {errorMessage && <p className="error-message">{errorMessage}</p>}

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          disabled={loading}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          disabled={loading}
        />

        {isSignup && (
          <>
            <input
              type="text"
              placeholder="First Name"
              value={prenom}
              onChange={(e) => setPrenom(e.target.value)}
              required
              disabled={loading}
            />
            <input
              type="text"
              placeholder="Last Name"
              value={nom}
              onChange={(e) => setNom(e.target.value)}
              required
              disabled={loading}
            />
          </>
        )}

        <button type="submit" disabled={loading}>
          {loading ? "Processing..." : isSignup ? "Sign Up" : "Login"}
        </button>
      </form>

      <p onClick={() => setIsSignup(!isSignup)} className="toggle-link">
        {isSignup ? "Already have an account? Login here" : "Don't have an account? Sign up here"}
      </p>
    </div>
  );
};
