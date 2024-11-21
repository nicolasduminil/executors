package fr.simplex_software.workshop.executors.thread_per_task.tests;

import fr.simplex_software.workshop.executors.async.thread_per_task.*;
import io.quarkus.test.common.http.*;
import io.quarkus.test.junit.*;
import io.restassured.response.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;

import java.net.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class TestExecutorResource
{
  @TestHTTPEndpoint(ExecutorsResource.class)
  @TestHTTPResource
  URL url;

  @Test
  public void testCurrentTime()
  {
    Response response = given().when().get(url);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
  }
}
