import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dto.User;
import io.restassured.http.ContentType;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

@Slf4j
public class ApiTest {
    @Test
    public void createUserCheck() {
        URL url = Resources.getResource("request.json");
        String rq = null;
        try {
            rq = Resources.toString(url,StandardCharsets.UTF_8);
        } catch (IOException e){
            log.error("Ошибка при вычитывании json в строку: {}",e.getMessage());
        }

        Gson gson = new Gson();
        User userExp = new User();
        try {
            userExp = gson.fromJson(rq, User.class);
        }catch (JsonSyntaxException e){
            log.error("Ошибка синтаксиса json: {}",e.getMessage());
        }catch (JsonParseException e){
            log.error("Ошибка парсинга json: {}",e.getMessage());
        }

        User userAct = given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .basePath("/users")
                .contentType(ContentType.JSON)
                .body(rq)
                .when().post()
                .then()
                .statusCode(201)
                .extract().as(User.class);
        Assert.assertEquals("Данные не совпадают: ",userExp,userAct);
    }
}
