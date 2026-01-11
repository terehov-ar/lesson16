package api;

import models.BookDataModel;

import static io.restassured.RestAssured.given;
import static specs.LoginSpec.*;

public class BookApi {

    public void deleteAllBooks(String token, String userId) {
        given(deleteBooksRequestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParams("UserId", userId)
                .when()
                .delete()
                .then()
                .spec(deleteBooksResponseSpec);
    }

    public void addBook(String token, BookDataModel bookData) {
        given(addBookRequestSpec)
                .header("Authorization", "Bearer " + token)
                .body(bookData)
                .when()
                .post()
                .then()
                .spec(addBookResponseSpec);
    }
}
