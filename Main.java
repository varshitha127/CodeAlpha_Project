import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Review {
    private String user;
    private int rating;
    private String comment;

    public Review(String user, int rating, String comment) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "User: " + user + ", Rating: " + rating + ", Comment: " + comment;
    }
}

class Hotel {
    private String name;
    private List<Review> reviews;

    public Hotel(String name) {
        this.name = name;
        this.reviews = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Review> getReviewsSortedByRating() {
        return reviews.stream()
                .sorted(Comparator.comparingInt(Review::getRating).reversed())
                .collect(Collectors.toList());
    }

    public List<Review> getReviewsByMinimumRating(int minRating) {
        return reviews.stream()
                .filter(review -> review.getRating() >= minRating)
                .collect(Collectors.toList());
    }
}

class HotelReviewSystem {
    private List<Hotel> hotels;

    public HotelReviewSystem() {
        this.hotels = new ArrayList<>();
    }

    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public Hotel findHotelByName(String name) {
        return hotels.stream()
                .filter(hotel -> hotel.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}

public class Main {
    public static void main(String[] args) {
        HotelReviewSystem system = new HotelReviewSystem();

        Hotel hotel1 = new Hotel("Hotel Sunshine");
        hotel1.addReview(new Review("Alice", 5, "Excellent stay!"));
        hotel1.addReview(new Review("Bob", 4, "Very good service."));
        hotel1.addReview(new Review("Charlie", 3, "Average experience."));

        Hotel hotel2 = new Hotel("Hotel Moonlight");
        hotel2.addReview(new Review("David", 4, "Good value for money."));
        hotel2.addReview(new Review("Eve", 5, "Outstanding hospitality!"));

        system.addHotel(hotel1);
        system.addHotel(hotel2);

        System.out.println("All reviews for Hotel Sunshine:");
        for (Review review : hotel1.getReviews()) {
            System.out.println(review);
        }

        System.out.println("\nReviews for Hotel Sunshine sorted by rating:");
        for (Review review : hotel1.getReviewsSortedByRating()) {
            System.out.println(review);
        }

        System.out.println("\nReviews for Hotel Sunshine with minimum rating of 4:");
        for (Review review : hotel1.getReviewsByMinimumRating(4)) {
            System.out.println(review);
        }

        System.out.println("\nAll reviews for Hotel Moonlight:");
        for (Review review : hotel2.getReviews()) {
            System.out.println(review);
        }

        System.out.println("\nReviews for Hotel Moonlight sorted by rating:");
        for (Review review : hotel2.getReviewsSortedByRating()) {
            System.out.println(review);
        }

        System.out.println("\nReviews for Hotel Moonlight with minimum rating of 4:");
        for (Review review : hotel2.getReviewsByMinimumRating(4)) {
            System.out.println(review);
        }
    }
}
