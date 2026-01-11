package api;

import models.LoginRequestBodyModel;
import models.LoginResponseBodyModel;

import static io.restassured.RestAssured.given;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.loginResponseSpec;

public class LoginApi {

    public LoginResponseBodyModel responseBodyModel(LoginRequestBodyModel authData) {
        return given(loginRequestSpec)
                .body(authData)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .extract().response().as(LoginResponseBodyModel.class);
    }
}
