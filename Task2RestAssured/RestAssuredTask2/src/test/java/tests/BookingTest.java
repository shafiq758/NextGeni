package tests;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pages.BookingPage;
import utils.TestBase;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  
public class BookingTest extends TestBase {

    BookingPage bookingPage = new BookingPage();  // Using BookingPage for API actions
    static int bookingId=0;
    @Test
    @Order(1)
    public void createBooking() {
        // Step 1: Create a booking payload
        JSONObject bookingPayload = bookingPage.createBookingPayload("testFirstName", "lastName", 10.11, true, "2022-01-01", "2024-01-01", "testAdd");

        // Step 2: Create booking via API
        Response createResponse = bookingPage.createBooking(bookingPayload);
        createResponse.prettyPrint();

        // Step 3: Validate booking creation response
        Assertions.assertEquals(200, createResponse.getStatusCode());
        bookingId = createResponse.jsonPath().getInt("bookingid");
        
        // Step 4: Fetch and validate booking by ID
        Response getResponse = bookingPage.getBookingById(bookingId);
        getResponse.prettyPrint();

        Assertions.assertEquals(200, getResponse.getStatusCode());

    }
    
    @Test
    @Order(2)
    public void testListBookingById() {
        // Step 1: Use a valid booking ID (dynamically fetched)
     

        // Step 2: Fetch the booking by ID and validate the response
    	
        Response getResponse = bookingPage.getBookingById(bookingId);
        getResponse.prettyPrint();

        Assertions.assertEquals(200, getResponse.getStatusCode());
        Assertions.assertEquals("testFirstName", getResponse.jsonPath().getString("firstname"));
        Assertions.assertEquals("lastName", getResponse.jsonPath().getString("lastname"));
        Assertions.assertEquals(10, getResponse.jsonPath().getFloat("totalprice"));
        Assertions.assertEquals("testAdd", getResponse.jsonPath().getString("additionalneeds"));
       // System.out.println(getResponse.jsonPath().getString("bookingdates")); // to check if data is coming
        Assertions.assertEquals("2022-01-01", getResponse.jsonPath().getString("bookingdates.checkin"));
        Assertions.assertEquals("2024-01-01", getResponse.jsonPath().getString("bookingdates.checkout"));
    }

    @Test
    @Order(3)
    public void negativeTestInvalidBooking() {
        // Step 1: Try fetching a booking with an invalid ID
        Response getResponse = bookingPage.getBookingById(999999);

        // Step 2: Validate response code 404 for non-existent booking
        Assertions.assertEquals(404, getResponse.getStatusCode());
    }
    
    @Test
    @Order(4)
    public void negativeScenario2() {
        // Step 1: Try fetching a booking with a recently created ID
        Response getResponse = bookingPage.getBookingById(bookingId);

        // Step 2: Validate Request and Response value of test case 1 Do not matches each other
        Assertions.assertNotEquals(10.11, getResponse.jsonPath().getFloat("totalprice"));
    }


   
}
