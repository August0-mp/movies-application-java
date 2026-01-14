import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";
import { Login } from "../../components/Login/Login";

export const Home = () => {
  const navigate = useNavigate();
  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (userId) {
      navigate("/accueil"); 
    }
  }, [navigate]); 

  const [showLogin, setShowLogin] = useState(false);

  const handleCloseLogin = (e) => {
    if (e.target.classList.contains("overlay")) {
      setShowLogin(false);
    }
  };

  return (
    <div className="header">
      <div className="container">
        <h2>ALL NEW EVERYTHING</h2>
        {!showLogin ? (
          <a href="#" onClick={(e) => { e.preventDefault(); setShowLogin(true); }}>EXPLORE</a>
        ) : (
          <div className="overlay" onClick={handleCloseLogin}>         
            <div className="login-wrapper" onClick={(e) => e.stopPropagation()}>
              <button className="close-btn" onClick={() => setShowLogin(false)}>✖</button>
              <Login onClose={() => setShowLogin(false)} />
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
