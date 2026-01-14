import React from 'react';
import { Route, Navigate } from 'react-router-dom';

const PrivateRoute = ({ element, ...rest }) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn'); // Example: Check login status from localStorage

  return (
    <Route
      {...rest}
      element={isLoggedIn ? element : <Navigate to="/" />}
    />
  );
};

export default PrivateRoute;
