package fr.simplex_software.workshop.executors.base;

import jakarta.annotation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;
import java.util.concurrent.*;

@Produces(MediaType.TEXT_PLAIN)
public abstract class BaseTimeResource
{
  protected static final String FMT = "d MMM uuuu, HH:mm:ss XXX z";
  protected ExecutorService executor;
  protected static final Logger LOG = LoggerFactory.getLogger(BaseTimeResource.class);
  protected static final String MSG = "BaseTimeResource.getCurrentDateAndTimeAsync() *** Executor: {}";

  @PostConstruct
  public void postConstruct()
  {
    executor = getExecutor();
  }

  @PreDestroy
  void cleanup()
  {
    executor.shutdown();
  }

  @GET
  public void getCurrentDateAndTimeAsync(@QueryParam("delay") @DefaultValue("0") int delay, @Suspended AsyncResponse asyncResponse)
  {
    LOG.debug(MSG);
    executor.submit(() ->
    {
      LOG.debug(MSG, executor.getClass().getName());
      try
      {
        Thread.sleep(delay * 1000L);
        asyncResponse.resume(getCurrentDateAndTimeAtDefaultZone());
      }
      catch (Exception e)
      {
        executor.shutdown();
        asyncResponse.resume(e);
      }
    });
  }

  @GET
  @Path("{zoneId}")
  public void getCurrentDateAndTimeAtZoneAsync(@PathParam("zoneId") String zoneId, @QueryParam("delay") @DefaultValue("0") int delay, @Suspended AsyncResponse asyncResponse)
  {
    executor.submit(() ->
    {
      try
      {
        Thread.sleep(delay * 1000L);
        asyncResponse.resume(getCurrentDateAndTimeAtZone(zoneId));
      }
      catch (Exception e)
      {
        asyncResponse.resume(e);
      }
    });
  }

  protected String getCurrentDateAndTimeAtDefaultZone()
  {
    return ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter
      .ofPattern(FMT).withZone(ZoneId.systemDefault()));
  }

  protected String getCurrentDateAndTimeAtZone(String zoneId)
  {
    String decodedZoneId = URLDecoder.decode(zoneId, StandardCharsets.UTF_8);
    return ZonedDateTime.now(ZoneId.of(decodedZoneId)).format(DateTimeFormatter
      .ofPattern(FMT).withZone(ZoneId.of(decodedZoneId)));
  }

  protected abstract ExecutorService getExecutor();
}
