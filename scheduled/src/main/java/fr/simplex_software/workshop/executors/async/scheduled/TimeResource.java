package fr.simplex_software.workshop.executors.async.scheduled;

import fr.simplex_software.workshop.executors.base.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

@Path("time-scheduled")
public class TimeResource extends BaseTimeResource
{
  @Override
  protected ExecutorService getExecutor()
  {
    return Executors.newScheduledThreadPool(2);
  }

  @GET
  @Path("/delayed")
  public void getDelayedTime(@QueryParam("delay") @DefaultValue("5") int delay,
                             @Suspended AsyncResponse asyncResponse)
  {
    ScheduledExecutorService scheduler = (ScheduledExecutorService) executor;
    scheduler.schedule(() ->
    {
      LOG.debug(MSG, scheduler.getClass().getName());
      try
      {
        asyncResponse.resume(getCurrentDateAndTimeAtDefaultZone());
      }
      catch (Exception e)
      {
        asyncResponse.resume(e);
      }
    }, delay, TimeUnit.SECONDS);
  }

  @GET
  @Path("/periodic")
  public void getPeriodicTime(@QueryParam("initialDelay") @DefaultValue("0") int initialDelay,
                              @QueryParam("period") @DefaultValue("5") int period,
                              @Suspended AsyncResponse asyncResponse)
  {
    ScheduledExecutorService scheduler = (ScheduledExecutorService) executor;
    final AtomicInteger counter = new AtomicInteger(0);
    final AtomicReference<ScheduledFuture<?>> future = new AtomicReference<>();
    future.set(scheduler.scheduleAtFixedRate(() ->
    {
      String time = getCurrentDateAndTimeAtDefaultZone();
      LOG.debug("Periodic update {}: {}", counter.incrementAndGet(), time);
      if (counter.get() >= 5)
      {
        asyncResponse.resume(time);
        future.get().cancel(false);
      }
    }, initialDelay, period, TimeUnit.SECONDS));
  }

  @GET
  @Path("/fixedDelay")
  public void getFixedDelayTime(@QueryParam("initialDelay") @DefaultValue("0") int initialDelay,
                                @QueryParam("delay") @DefaultValue("5") int delay,
                                @Suspended AsyncResponse asyncResponse)
  {
    ScheduledExecutorService scheduler = (ScheduledExecutorService) executor;
    final AtomicInteger counter = new AtomicInteger(0);
    final AtomicReference<ScheduledFuture<?>> future = new AtomicReference<>();
    future.set(scheduler.scheduleWithFixedDelay(() ->
    {
      String time = getCurrentDateAndTimeAtDefaultZone();
      LOG.debug("Fixed delay update {}: {}", counter.incrementAndGet(), time);
      try
      {
        Thread.sleep(delay * 1000L);
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
      }
      if (counter.get() >= 5)
      {
        asyncResponse.resume(time);
        future.get().cancel(false);
      }
    }, initialDelay, delay, TimeUnit.SECONDS));
  }
}
