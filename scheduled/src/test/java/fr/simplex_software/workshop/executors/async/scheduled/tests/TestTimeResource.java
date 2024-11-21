package fr.simplex_software.workshop.executors.async.scheduled.tests;

import fr.simplex_software.workshop.executors.async.scheduled.*;
import fr.simplex_software.workshop.executors.base.tests.*;
import io.quarkus.test.common.http.*;
import io.quarkus.test.junit.*;
import io.restassured.response.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;

import java.net.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class TestTimeResource extends BaseTimeResourceTest
{
  @TestHTTPEndpoint(TimeResource.class)
  @TestHTTPResource
  URL timeSrvUrl;

  @Override
  protected URL getTimeSrvURL()
  {
    return timeSrvUrl;
  }

  @Test
  public void testGetDelayedTime()
  {
    Response response = given().baseUri(timeSrvUri.toString())
      .queryParam("delay", 3).when().get("/delayed");
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(LocalDateTime.parse(response.prettyPrint(), DateTimeFormatter.ofPattern(FMT)))
      .isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.HOURS));
  }

  @Test
  public void testGetPeriodicTime()
  {
    Response response = given().baseUri(timeSrvUri.toString())
      .queryParam("initialDelay", 1)
      .queryParam("delay", 3).when().get("/periodic");
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(LocalDateTime.parse(response.prettyPrint(), DateTimeFormatter.ofPattern(FMT)))
      .isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.HOURS));
  }

  @Test
  public void testGetFixedDelayTime()
  {
    Response response = given().baseUri(timeSrvUri.toString())
      .queryParam("initialDelay", 1)
      .queryParam("delay", 3).when().get("/fixedDelay");
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(LocalDateTime.parse(response.prettyPrint(), DateTimeFormatter.ofPattern(FMT)))
      .isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.HOURS));
  }
}
