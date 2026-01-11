package helpers;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import org.openqa.selenium.Cookie;

public class CookieHelper {

    public static void addAuthCookies(String userId, String token, String expires) {
        open("/favicon.ico"); // открываем любую страницу для установки кук
        getWebDriver().manage().addCookie(new Cookie("userID", userId));
        getWebDriver().manage().addCookie(new Cookie("expires", expires));
        getWebDriver().manage().addCookie(new Cookie("token", token));
    }
}
