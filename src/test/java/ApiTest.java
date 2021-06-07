import com.google.common.io.Resources;
import com.google.gson.Gson;
import dto.User;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

public class ApiTest {
    @Test
    public void createUserCheck() {
        URL url = Resources.getResource("request.json");
        String rq = null;
        try {
            rq = Resources.toString(url,StandardCharsets.UTF_8);
        } catch (IOException e){

        }
        Gson gson = new Gson();
        User userExp = gson.fromJson(rq,User.class);
        String response = given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .basePath("/users")
                .contentType(ContentType.JSON)
                .body(rq)
                .when().post()
                .then()
                .statusCode(201)
                .extract().asPrettyString();
        User userAct = gson.fromJson(response,User.class);
        Assert.assertEquals("Данные не совпадают: ",userExp,userAct);
    }
}
