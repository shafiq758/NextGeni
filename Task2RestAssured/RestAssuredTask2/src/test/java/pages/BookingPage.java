package pages;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class BookingPage {

    public BookingPage() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    // Method to create a booking
    public Response createBooking(JSONObject bookingPayload) {
        return given()
                .header("Content-Type", "application/json")
                .body(bookingPayload.toString())
                .when()
                .post("/booking")
                .then()
                .extract().response();
    }

    // Method to get a booking by ID
    public Response getBookingById(int bookingId) {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get("/booking/" + bookingId)
                .then()
                .extract().response();
    }

    // Method to create a sample booking payload for the tests
    public JSONObject createBookingPayload(String firstName, String lastName, double totalPrice, boolean depositPaid, String checkInDate, String checkOutDate, String additionalNeeds) {
        JSONObject bookingPayload = new JSONObject();
        bookingPayload.put("firstname", firstName);
        bookingPayload.put("lastname", lastName);
        bookingPayload.put("totalprice", totalPrice);
        bookingPayload.put("depositpaid", depositPaid);

        // Add booking dates
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", checkInDate);
        bookingDates.put("checkout", checkOutDate);
        bookingPayload.put("bookingdates", bookingDates);
        bookingPayload.put("additionalneeds", additionalNeeds);

        return bookingPayload;
    }
}
