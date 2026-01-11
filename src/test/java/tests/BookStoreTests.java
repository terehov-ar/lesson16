package tests;

import helpers.CookieHelper;
import io.restassured.response.Response;
import models.BookDataModel;
import models.LoginBodyModel;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static specs.LoginSpec.*;

public class BookStoreTests extends TestBase {

    @Test
    void deleteBookFromUserList() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setUserName(login);
        authData.setPassword(password);

        Response authResponse = given(loginRequestSpec)
                .body(authData)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .extract().response();

        given(deleteBooksRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete()
                .then()
                .spec(deleteBooksResponseSpec);

        CookieHelper.addAuthCookies(authResponse.path("userId"), authResponse.path("token"), authResponse.path("expires"));

        BookDataModel bookData = BookDataModel.createWithSingleIsbn(
                authResponse.path("userId"), isbn);

        given(addBookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post()
                .then()
                .spec(addBookResponseSpec);

        open("/profile");
        $("#userName-value").shouldHave(text(login));
        $(".ReactTable").shouldHave(text("You Don't Know JS"));
        $$(".btn.btn-primary").findBy(text("Delete All Books")).click();
        $("#closeSmallModal-ok").click();
        $(".ReactTable").shouldNotHave(text("You Don't Know JS"));

    }
}
