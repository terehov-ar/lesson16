package tests;

import helpers.CookieHelper;
import io.restassured.response.Response;
import models.BookDataModel;
import models.LoginRequestBodyModel;
import models.LoginResponseBodyModel;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.*;
import static io.restassured.RestAssured.given;
import static specs.LoginSpec.*;

public class BookStoreTests extends TestBase {

    @Test
    void deleteBookFromUserList() {

        LoginRequestBodyModel authData = new LoginRequestBodyModel();
        authData.setUserName(login);
        authData.setPassword(password);

        LoginResponseBodyModel authResponse = given(loginRequestSpec)
                .body(authData)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .extract().response().as(LoginResponseBodyModel.class);

        given(deleteBooksRequestSpec)
                .header("Authorization", "Bearer " + authResponse.getToken())
                .queryParams("UserId", authResponse.getUserId())
                .when()
                .delete()
                .then()
                .spec(deleteBooksResponseSpec);

        CookieHelper.addAuthCookies(authResponse.getUserId(), authResponse.getToken(), authResponse.getExpires());

        BookDataModel bookData = BookDataModel.createWithSingleIsbn(
                authResponse.getUserId(), isbn);

        given(addBookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.getToken())
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
