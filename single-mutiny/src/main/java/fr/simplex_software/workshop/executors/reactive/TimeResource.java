package fr.simplex_software.workshop.executors.reactive;

import io.smallrye.mutiny.*;
import jakarta.annotation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;
import java.util.concurrent.*;

@Path("time-react")
@Produces(MediaType.TEXT_PLAIN)
public class TimeResource
{
  private static final String TIME_FORMAT = "d MMM uuuu, HH:mm:ss XXX z";
  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  @GET
  public Uni<String> getCurrentTime(@QueryParam("delay") @DefaultValue("0") int delay)
  {
    return Uni.createFrom()
      .item(ZonedDateTime::now)
      .emitOn(executor)
      .onItem()
      .transformToUni(time -> delay > 0 ?
        Uni.createFrom().item(time).onItem().delayIt().by(Duration.ofSeconds(delay)) :
        Uni.createFrom().item(time))
      .map(time -> time.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
  }

  @GET
  @Path("/{zoneId}")
  public Uni<String> getTimeForZone(@PathParam("zoneId") String zoneId, @QueryParam("delay") @DefaultValue("0") int delay)
  {
    return Uni.createFrom()
      .item(() -> ZonedDateTime.now(ZoneId.of(URLDecoder.decode(zoneId, StandardCharsets.UTF_8))))
      .emitOn(executor)
      .onItem()
      .transformToUni(time -> delay > 0 ?
        Uni.createFrom().item(time).onItem().delayIt().by(Duration.ofSeconds(delay)) :
        Uni.createFrom().item(time))
      .map(time -> time.format(DateTimeFormatter.ofPattern(TIME_FORMAT)))
      .onFailure()
      .recoverWithItem(throwable ->
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)) +
          " (Error: " + throwable.getMessage() + ")");
  }

  @PreDestroy
  void cleanup()
  {
    executor.shutdown();
  }
}
