package xl.application.hr.whoami;

import io.restassured.authentication.FormAuthConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xl.application.hr.whoami.model.Resume;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WhoamiApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	public void canLoginAsAdmin() {
		given().
			auth().
			form("admin", "admin", new FormAuthConfig(url("/auth/login"), "username", "password")).
		then().
			statusCode(200);
	}

	@Test
	public void loginFailureShouldReturn401() {
		given().
			auth().
			form("admin", "", new FormAuthConfig(url("/auth/login"), "username", "password")).
		then().
			statusCode(401);
	}

	@Test
	public void allFieldsOfResumeAreMandatory() {
		Resume resume = new Resume(null, "Tom", "Tester", "Some Company", null);
		given().
			contentType("application/json").
			body(resume).
		when().
			post(url("/api/v1/resume")).
		then().
			statusCode(400);
	}

	@Test
	public void returnIdUponResumeSubmission() {
		Resume resume = new Resume(null, "Tom", "Tester", "Some Company", "...");
		given().
			contentType("application/json").
			body(resume).
		when().
			post(url("/api/v1/resume")).
		then().
			body("id", not(nullValue()));
	}

	@Test
	public void searchByIdWithoutLogin() {
		Resume resume = new Resume(null, "Tom", "Tester", "Some Company", "...");
		String id = given()
				.contentType("application/json")
				.body(resume)
				.post(url("/api/v1/resume"))
				.path("id");

		given().
			queryParam("q", id).
		when().
			get(url("/api/v1/resume")).
		then()
			.body("found", not(nullValue()));
	}

	private String url(String path) {
		return "http://localhost:" + port + path;
	}
}
