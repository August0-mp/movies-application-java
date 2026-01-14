import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./Movie.css";

export const Movie = () => {
  const { movieId } = useParams();
  const [movie, setMovie] = useState(null);
  const [omdbData, setOmdbData] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`http://localhost:8080/film/${movieId}`)
      .then((response) => {
        setMovie(response.data);
        fetchMovieDetails(response.data.titre);
      })
      .catch((error) => {
        console.error("Error fetching movie details:", error);
      });
  }, [movieId]);


  const fetchMovieDetails = async (movieTitle) => {
    try {
      const response = await axios.get(
        `https://www.omdbapi.com/?apikey=17eb4502&t=${encodeURIComponent(movieTitle)}`
      );

      if (response.data.Response === "True") {
        setOmdbData(response.data);
      } else {
        console.log("Movie not found");
      }
    } catch (error) {
      console.error("Error fetching movie details:", error);
    }
  };

  if (!movie || !omdbData) return <p>Loading...</p>;

  return (
    <div
      className="movie-page"
      style={{
        backgroundImage: `url(${omdbData.Poster})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        height: "100vh",
        color: "white",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        textShadow: "2px 2px 4px rgba(0, 0, 0, 0.8)",
      }}
    >
      
      
      <div className="movie-content">
        <button className="back-button" onClick={() => navigate("/accueil")}>
            ← Back
        </button>
        <h1>{movie.titre.toUpperCase()}</h1>
        <h3>Synopsis</h3>
        <p>{omdbData.Plot}</p>
        <h3>Directed by</h3>
        <p>{movie.realisateur.prenom} {movie.realisateur.nom} 🏆</p>
        <h3>Actors</h3>
        <p>{omdbData.Actors}</p>
      </div>
    </div>
  );
};
