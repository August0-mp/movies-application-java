import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Navbar.css";

export const Navbar = () => {
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("userId");
    localStorage.removeItem("is_admin");
    navigate("/");
  };

  return (
    <div>
      <div id="main-navbar" className="navbar">
        <a href="/" className="logo"></a>
        <nav>
          <ul>
            <li
              className="user-tab"
              onMouseEnter={() => setShowDropdown(true)}
              onMouseLeave={() => setShowDropdown(false)}
            >
              <a href="#">User</a>
              {showDropdown && (
                <div className="dropdown">
                  <button onClick={handleLogout}>Disconnect</button>
                </div>
              )}
            </li>
          </ul>
        </nav>
      </div>
    </div>
  );
};
