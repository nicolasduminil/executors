package fr.simplex_software.workshop.executors.base.tests;

import io.restassured.response.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTimeResourceTest
{
  protected static final String FMT = "d MMM uuuu, HH:mm:ss XXX z";
  protected URI timeSrvUri;

  @BeforeAll
  public void beforeAll() throws URISyntaxException
  {
    timeSrvUri = getTimeSrvURL().toURI();
    assertThat(timeSrvUri).isNotNull();
  }

  @AfterAll
  public void afterAll()
  {
    timeSrvUri = null;
  }

  @Test
  public void testCurrentTime()
  {
    Response response = given().when().get(timeSrvUri);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(LocalDateTime.parse(response.prettyPrint(), DateTimeFormatter.ofPattern(FMT)))
      .isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.HOURS));
  }

  @Test
  public void testCurrentTimeWithZoneId()
  {
    Response response = given().baseUri(timeSrvUri.toString())
      .pathParam("zoneId", URLEncoder.encode("Europe/Kaliningrad", StandardCharsets.UTF_8))
      .when().get("{zoneId}");
    assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(LocalDateTime.parse(response.prettyPrint(), DateTimeFormatter.ofPattern(FMT)))
      .isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.HOURS));
  }

  protected abstract URL getTimeSrvURL();
}
