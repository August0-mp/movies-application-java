import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './index.css';
import { Home } from './pages/Home/Home';
import { Accueil } from './pages/Accueil/Accueil';
import { Movie } from './pages/Movie/Movie';
import { Navbar } from './components/Navbar/Navbar';
import Footer from './components/Footer/Footer';
import { NotFound } from './pages/NotFound/NotFound';

const isLoggedIn = () => {
  return localStorage.getItem('userId'); 
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/accueil"
          element={isLoggedIn ? <Accueil /> : <Navigate to="/" />}
        />
        <Route
          path="/movie/:movieId"
          element={isLoggedIn ? <Movie /> : <Navigate to="/" />}
        />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </Router>
    
  </React.StrictMode>
);
