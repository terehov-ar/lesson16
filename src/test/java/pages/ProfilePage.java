package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.TestData.LOGIN;

public class ProfilePage {

    private final SelenideElement deleteButton = $$(".btn.btn-primary").find(text("Delete All Books"));

    private final SelenideElement userNameInfo = $("#userName-value"),
            submitDeleteButton = $("#closeSmallModal-ok"),
            bookList = $(".ReactTable");

    public ProfilePage openPage() {
        open("/profile");

        return this;
    }

    public ProfilePage assertUserLogin() {
        userNameInfo.shouldHave(text(LOGIN));

        return this;
    }

    public ProfilePage assertUserBook() {
        bookList.shouldHave(text("You Don't Know JS"));

        return this;
    }

    public ProfilePage deleteBook() {
        deleteButton.click();
        submitDeleteButton.click();

        return this;
    }

    public void assertBookList() {
        bookList.shouldNotHave(text("You Don't Know JS"));
    }
}
