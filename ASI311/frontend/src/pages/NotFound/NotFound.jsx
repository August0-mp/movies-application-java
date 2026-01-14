import React from 'react';
import { Link } from 'react-router-dom'; 
import "./NotFound.css";

export const NotFound = () => {
  return (
    <div className='container'>
      <h1 className='heading'>404 - Page Not Found</h1>
      <p className='message'>Oops! The page you're looking for doesn't exist.</p>
      <Link to="/" className='link'>
        Go back to Home
      </Link>
    </div>
  );
};



export default NotFound;
