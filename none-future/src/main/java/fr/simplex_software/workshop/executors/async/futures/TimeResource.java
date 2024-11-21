package fr.simplex_software.workshop.executors.async.futures;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;
import java.util.concurrent.*;

@Path("time-f")
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
    CompletableFuture.supplyAsync(() ->
      {
        try
        {
          Thread.sleep(delay * 1000L);
          return ZonedDateTime.now().format(DateTimeFormatter.ofPattern(FMT)
            .withZone(ZoneId.systemDefault()));
        }
        catch (Exception e)
        {
          ar.resume(e);
          return null;
        }
      })
      .thenAccept(result ->
      {
        ar.resume(result);
        LOG.debug(MSG);
      })
      .exceptionally(throwable ->
      {
        ar.resume(throwable);
        return null;
      });
  }

  @GET
  @Path("{zoneId}")
  public void getCurrentDateAndTimeAtZone(@PathParam("zoneId") String zoneId,
                                          @QueryParam("delay") @DefaultValue("0") int delay,
                                          @Suspended AsyncResponse ar)
  {
    LOG.debug(MSG);
    CompletableFuture.supplyAsync(() ->
      {
        try
        {
          Thread.sleep(delay * 1000L);
          String decodedZoneId = URLDecoder.decode(zoneId, StandardCharsets.UTF_8);
          return ZonedDateTime.now().format(DateTimeFormatter.ofPattern(FMT)
            .withZone(ZoneId.of(decodedZoneId)));
        }
        catch (Exception e)
        {
          ar.resume(e);
          return null;
        }
      })
      .thenAccept(result ->
      {
        ar.resume(result);
        LOG.debug(MSG);
      })
      .exceptionally(throwable ->
      {
        ar.resume(throwable);
        return null;
      });
  }
}
