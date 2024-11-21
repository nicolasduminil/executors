package fr.simplex_software.workshop.executors.async.none;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;

@Path("time-async")
@Produces(MediaType.TEXT_PLAIN)
public class TimeResource
{
  private static final String FMT = "d MMM uuuu, HH:mm:ss XXX z";
  private static final Logger LOG = LoggerFactory.getLogger(TimeResource.class);

  @GET
  public void getCurrentDateAndTimeAtDefaultZone(@QueryParam("delay") @DefaultValue("0") int delay, @Suspended AsyncResponse ar) throws InterruptedException
  {
    Thread.sleep(delay * 1000L);
    ar.resume(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(FMT)
      .withZone(ZoneId.systemDefault())));
    LOG.debug("*** TimeResource.getCurrentDateAndTimeAtDefaultZone() ***");
  }

  @GET
  @Path("{zoneId}")
  public void getCurrentDateAndTimeAtZone(@PathParam("zoneId") String zoneId, @QueryParam("delay") @DefaultValue("0") int delay, @Suspended AsyncResponse ar) throws InterruptedException
  {
    Thread.sleep(delay * 1000L);
    String decodedZoneId = URLDecoder.decode(zoneId, StandardCharsets.UTF_8);
    ar.resume(ZonedDateTime.now(ZoneId.of(decodedZoneId)).format(DateTimeFormatter
      .ofPattern(FMT).withZone(ZoneId.of(decodedZoneId))));
  }
}
