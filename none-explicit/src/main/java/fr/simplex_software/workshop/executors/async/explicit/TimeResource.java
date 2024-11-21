package fr.simplex_software.workshop.executors.async.explicit;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;

@Path("time-ex")
@Produces(MediaType.TEXT_PLAIN)
public class TimeResource
{
  private static final String FMT = "d MMM uuuu, HH:mm:ss XXX z";
  private static final Logger LOG = LoggerFactory.getLogger(TimeResource.class);
  private static final String MSG = "*** TimeResource.getCurrentDateAndTimeAtDefaultZone() ***";

  @GET
  public void getCurrentDateAndTimeAtDefaultZone(@QueryParam("delay") @DefaultValue("0") int delay,
                                                 @Suspended AsyncResponse ar)
  {
    LOG.debug(MSG);
    new Thread(() ->
    {
      try
      {
        Thread.sleep(delay * 1000L);
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
      ar.resume(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(FMT)
        .withZone(ZoneId.systemDefault())));
      LOG.debug(MSG);
    }).start();
  }

  @GET
  @Path("{zoneId}")
  public void getCurrentDateAndTimeAtZone(@PathParam("zoneId") String zoneId,
                                          @QueryParam("delay") @DefaultValue("0") int delay,
                                          @Suspended AsyncResponse ar)
  {
    LOG.debug(MSG);
    new Thread(() ->
    {
      try
      {
        Thread.sleep(delay * 1000L);
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
      String decodedZoneId = URLDecoder.decode(zoneId, StandardCharsets.UTF_8);
      ar.resume(ZonedDateTime.now(ZoneId.of(decodedZoneId)).format(DateTimeFormatter.ofPattern(FMT)
        .withZone(ZoneId.systemDefault())));
      LOG.debug(MSG);
    }).start();
  }
}
