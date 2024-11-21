package fr.simplex_software.workshop.executors.async.thread_per_task.virtual;

import io.smallrye.common.annotation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;

@Path("time-vt")
@Produces(MediaType.TEXT_PLAIN)
@RunOnVirtualThread
public class TimeResourceVT
{
  private static final String FMT = "d MMM uuuu, HH:mm:ss XXX z";
  private static final Logger LOG = LoggerFactory.getLogger(TimeResourceVT.class);

  @GET
  public String getCurrentDateAndTimeAtDefaultZone(@QueryParam("delay") @DefaultValue("0") int delay) throws InterruptedException
  {
    Thread.sleep(delay * 1000L);
    LOG.debug("*** TimeResource.getCurrentDateAndTimeAtDefaultZone() ***");
    return ZonedDateTime.now().format(DateTimeFormatter.ofPattern(FMT)
      .withZone(ZoneId.systemDefault()));
  }

  @GET
  @Path("{zoneId}")
  @Produces(MediaType.TEXT_PLAIN)
  public String getCurrentDateAndTimeAtZone(@PathParam("zoneId") String zoneId, @QueryParam("delay") @DefaultValue("0") int delay) throws InterruptedException
  {
    Thread.sleep(delay * 1000L);
    String decodedZoneId = URLDecoder.decode(zoneId, StandardCharsets.UTF_8);
    return ZonedDateTime.now(ZoneId.of(decodedZoneId)).format(DateTimeFormatter
      .ofPattern(FMT).withZone(ZoneId.of(decodedZoneId)));
  }
}
