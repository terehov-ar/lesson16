package tests;

import api.BookApi;
import api.LoginApi;
import helpers.CookieHelper;
import models.BookDataModel;
import models.LoginRequestBodyModel;
import models.LoginResponseBodyModel;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.*;
import static io.qameta.allure.Allure.step;

public class BookStoreTests extends TestBase {

    @Test
    void deleteBookFromUserList() {

        LoginApi bookStoreApi = new LoginApi();
        BookApi bookApi = new BookApi();

        LoginRequestBodyModel authData = new LoginRequestBodyModel();
        authData.setUserName(login);
        authData.setPassword(password);

        LoginResponseBodyModel authResponse = step("Авторизация на сайте через API", () ->
                bookStoreApi.responseBodyModel(authData));

        step("Очистка корзины с книгами", () ->
                bookApi.deleteAllBooks(authResponse.getToken(), authResponse.getUserId()));

        step("Добавление cookies", () ->
                CookieHelper.addAuthCookies(authResponse.getUserId(), authResponse.getToken(), authResponse.getExpires()));

        BookDataModel bookData = BookDataModel.createWithSingleIsbn(authResponse.getUserId(), isbn);

        step("Добавление книги", () ->
                bookApi.addBook(authResponse.getToken(), bookData));

        step("Проверка авторизации и наличии книги через UI", () -> {
            open("/profile");
            $("#userName-value").shouldHave(text(login));
            $(".ReactTable").shouldHave(text("You Don't Know JS"));
        });

        step("Удаление книги c проверкой через UI", () -> {
            $$(".btn.btn-primary").findBy(text("Delete All Books")).click();
            $("#closeSmallModal-ok").click();
            $(".ReactTable").shouldNotHave(text("You Don't Know JS"));
        });
    }
}