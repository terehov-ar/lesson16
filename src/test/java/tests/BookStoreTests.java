package tests;

import api.BookApi;
import api.LoginApi;
import helpers.CookieHelper;
import models.BookDataModel;
import models.LoginRequestBodyModel;
import models.LoginResponseBodyModel;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;

public class BookStoreTests extends TestBase {

    @Test
    void deleteBookFromUserList() {

        LoginApi bookStoreApi = new LoginApi();
        BookApi bookApi = new BookApi();
        ProfilePage profilePage = new ProfilePage();

        LoginRequestBodyModel authData = new LoginRequestBodyModel();
        authData.setUserName(LOGIN);
        authData.setPassword(PASSWORD);

        LoginResponseBodyModel authResponse = step("Авторизация на сайте через API", () ->
                bookStoreApi.responseBodyModel(authData));

        step("Очистка корзины с книгами", () ->
                bookApi.deleteAllBooks(authResponse.getToken(), authResponse.getUserId()));

        step("Добавление cookies", () ->
                CookieHelper.addAuthCookies(authResponse.getUserId(), authResponse.getToken(), authResponse.getExpires()));

        BookDataModel bookData = BookDataModel.createWithSingleIsbn(authResponse.getUserId(), ISBN);

        step("Добавление книги", () ->
                bookApi.addBook(authResponse.getToken(), bookData));

        step("Проверка авторизации и наличии книги через UI", () -> {
            profilePage.openPage()
                    .assertUserLogin()
                    .assertUserBook();
        });

        step("Удаление книги c проверкой через UI", () -> {
            profilePage.deleteBook()
                    .assertBookList();
        });
    }
}