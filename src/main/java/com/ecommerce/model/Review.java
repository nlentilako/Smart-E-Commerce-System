package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Represents a product review in the e-commerce system.
 * This class encapsulates user feedback and ratings for products.
 */
public class Review {
    private final int reviewId;
    private final int productId;
    private final int userId;
    private int rating; // 1-5 stars
    private String title;
    private String comment;
    private final LocalDateTime createdAt;
    private boolean isVerifiedPurchase;

    /**
     * Constructor for creating a new Review object.
     *
     * @param reviewId           The unique identifier for the review
     * @param productId          The ID of the reviewed product
     * @param userId             The ID of the user who wrote the review
     * @param rating             The rating given (1-5 stars)
     * @param title              The title of the review
     * @param comment            The detailed comment
     * @param createdAt          The timestamp when the review was created
     * @param isVerifiedPurchase Whether this is a verified purchase review
     */
    public Review(int reviewId, int productId, int userId, int rating, String title,
                  String comment, LocalDateTime createdAt, boolean isVerifiedPurchase) {
        validateRating(rating);
        
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.isVerifiedPurchase = isVerifiedPurchase;
    }

    // Getters
    public int getReviewId() { return reviewId; }
    public int getProductId() { return productId; }
    public int getUserId() { return userId; }
    public int getRating() { return rating; }
    public String getTitle() { return title; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isVerifiedPurchase() { return isVerifiedPurchase; }

    // Setters (with validation)
    public void setRating(int rating) {
        validateRating(rating);
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
        this.isVerifiedPurchase = verifiedPurchase;
    }

    /**
     * Validates that the rating is between 1 and 5.
     *
     * @param rating The rating to validate
     * @throws IllegalArgumentException if the rating is not between 1 and 5
     */
    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    /**
     * Gets the rating as a string representation (e.g., "★★★★☆").
     *
     * @return A string representation of the rating
     */
    public String getStarRating() {
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    /**
     * Checks if the review is considered positive (rating >= 4).
     *
     * @return true if the review is positive, false otherwise
     */
    public boolean isPositive() {
        return rating >= 4;
    }

    /**
     * Checks if the review is considered negative (rating <= 2).
     *
     * @return true if the review is negative, false otherwise
     */
    public boolean isNegative() {
        return rating <= 2;
    }

    /**
     * Checks if the review has a neutral rating (rating == 3).
     *
     * @return true if the review is neutral, false otherwise
     */
    public boolean isNeutral() {
        return rating == 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Review review = (Review) obj;
        return reviewId == review.reviewId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(reviewId);
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", title='" + title + '\'' +
                ", isVerifiedPurchase=" + isVerifiedPurchase +
                ", createdAt=" + createdAt +
                '}';
    }
}