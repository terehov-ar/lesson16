package tests;

import io.restassured.response.Response;
import models.BookDataModel;
import models.LoginBodyModel;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;

public class BookStoreTests extends TestBase {

    @Test
    void deleteBookFromUserList() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setUserName(login);
        authData.setPassword(password);

        Response authResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .log().status()
                .statusCode(204);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        BookDataModel bookData = new BookDataModel();
        bookData.setUserId(authResponse.path("userId"));
        bookData.setIsbn(isbn);

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/bookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(201);

        open("/profile");
        $("#userName-value").shouldHave(text(login));
        $(".ReactTable").shouldHave(text("You Don't Know JS"));
        $$(".btn.btn-primary").findBy(text("Delete All Books")).click();
        $("#closeSmallModal-ok").click();
        $(".ReactTable").shouldNotHave(text("You Don't Know JS"));
    }
}
