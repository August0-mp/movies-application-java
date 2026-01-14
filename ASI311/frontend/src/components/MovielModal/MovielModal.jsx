import React, { useState, useEffect } from "react";
import "./MovieModal.css";

export const MovieModal = ({
  selectedMovie,
  closeModal,
  isAdmin,
  favoriteMovies,
  setFavoriteMovies,
}) => {
  const userId = localStorage.getItem("userId");
  const [isFavorite, setIsFavorite] = useState(false);
  const [averageRating, setAverageRating] = useState(null);
  const [personalRating, setPersonalRating] = useState(null);
  const [newRating, setNewRating] = useState("");

  // ✅ Resetar o estado sempre que selectedMovie mudar
  useEffect(() => {
    if (selectedMovie?.id && userId) {
      setIsFavorite(false); // Reset favorito
      setAverageRating(null); // Reset média de notas
      setPersonalRating(null); // Reset nota pessoal
      setNewRating(""); // Reset input de nota

      fetchFavoriteStatus();
      fetchAverageRating();
      fetchPersonalRating();
    }
  }, [selectedMovie?.id, userId]);

  const fetchFavoriteStatus = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/film/favorite/${selectedMovie.id}/${userId}`
      );
      if (!response.ok) {
        console.error("Failed to fetch favorite status:", response.statusText);
        return;
      }
      const isFav = await response.json();
      setIsFavorite(isFav);
    } catch (error) {
      console.error("Error fetching favorite status:", error);
    }
  };

  const fetchAverageRating = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/film/note/average/${selectedMovie.id}`
      );
      if (!response.ok) {
        console.error("Failed to fetch average rating:", response.statusText);
        return;
      }
      const data = await response.json();
      setAverageRating(data);
    } catch (error) {
      console.error("Error fetching average rating:", error);
    }
  };

  const fetchPersonalRating = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/film/note/personal/${selectedMovie.id}/${userId}`
      );
      if (!response.ok) {
        console.error("Failed to fetch personal rating:", response.statusText);
        return;
      }
      const data = await response.json();
      setPersonalRating(data);
    } catch (error) {
      console.error("Error fetching personal rating:", error);
    }
  };

  const handleFavoriteToggle = async () => {
    const isCurrentlyFavorite = favoriteMovies.some(
      (movie) => movie.id === selectedMovie.id
    );
    const newFavoriteList = isCurrentlyFavorite
      ? favoriteMovies.filter((movie) => movie.id !== selectedMovie.id)
      : [...favoriteMovies, selectedMovie];

    setFavoriteMovies(newFavoriteList);

    try {
      const method = isCurrentlyFavorite ? "DELETE" : "POST";
      const response = await fetch(
        `http://localhost:8080/film/favorite/${selectedMovie.id}/${userId}`,
        { method }
      );

      if (!response.ok) {
        throw new Error("Failed to update favorite status");
      }
    } catch (error) {
      console.error("Error updating favorite status:", error);
      setFavoriteMovies(favoriteMovies);
    }
  };

  const handleRatingSubmit = async () => {
    const ratingValue = parseInt(newRating, 10);
    if (ratingValue >= 0 && ratingValue <= 20) {
      try {
        const response = await fetch(
          `http://localhost:8080/film/note/eval/${selectedMovie.id}/${userId}/${ratingValue}`,
          {
            method: "POST",
          }
        );
        if (response.ok) {
          setPersonalRating(ratingValue);
          fetchAverageRating();
        } else {
          console.error("Failed to submit rating:", response.statusText);
        }
      } catch (error) {
        console.error("Error submitting rating:", error);
      }
    } else {
      alert("Please enter a rating between 0 and 20.");
    }
  };

  if (!selectedMovie || !userId) return null;
  console.log(selectedMovie)

  return (
    <div className="modal-overlay" onClick={closeModal}>
      <div
        className="modal-content"
        style={{ backgroundImage: `url(${selectedMovie.Poster})` }}
        onClick={(e) => e.stopPropagation()}
      >
        <span className="close-btn" onClick={closeModal}>
          &times;
        </span>

        <div className="modal-info">
          <h2>
            {selectedMovie.Title} ({selectedMovie.Year})
          </h2>
          <p>{selectedMovie.realisateur.prenom} {selectedMovie.realisateur.nom} {selectedMovie.realisateur.celebre ? "🏆" : ""}</p>
        </div>

        <div className="modal-body">
          <img
            src={selectedMovie.Poster}
            alt={selectedMovie.Title}
            className="modal-poster"
          />
          <div className="synopsis">
            <p>{selectedMovie.Plot === "N/A" ? "" : selectedMovie.Plot}</p>

            <div className="genres">
              {selectedMovie.Genre &&
                selectedMovie.Genre.split(",").map((genre, index) => (
                  <span key={index} className="genre">
                    {genre.trim()}
                  </span>
                ))}
            </div>

            <div className="ratings">
              <p>
                ⭐ Average Rating:{" "}
                {averageRating !== null ? averageRating : "No ratings yet"}
              </p>
              <p>
                ⭐ Your Rating:{" "}
                {personalRating !== null
                  ? personalRating
                  : "You haven't rated this movie yet"}
              </p>

              <div className="rating-input">
                <label>Enter your rating (0-20):</label>
                <input
                  className="rating-input-number"
                  type="number"
                  value={newRating}
                  onChange={(e) => {
                    if (
                      e.target.value === "" ||
                      (parseInt(e.target.value) >= 0 &&
                        parseInt(e.target.value) <= 20)
                    )
                      setNewRating(e.target.value);
                  }}
                  placeholder={personalRating !== null ? personalRating : ""}
                />
                <button
                  className="submit-rating-button"
                  onClick={handleRatingSubmit}
                >
                  🍿Submit Rating!🍿
                </button>
              </div>
            </div>

            <div className="icon-buttons">
              <button
                className="icon-btn bookmark-btn"
                onClick={handleFavoriteToggle}
              >
                {isFavorite ? "💖 Remove Favorite" : "🔖 Add to Favorites"}
              </button>

              {isAdmin && isFavorite && (
                <button
                  className="icon-btn delete-btn"
                  onClick={handleFavoriteToggle}
                >
                  ❌ Remove (Admin)
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
