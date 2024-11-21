package fr.simplex_software.workshop.executors.async.thread_per_task;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.util.concurrent.*;
import java.util.stream.*;


@Path("execs")
public class ExecutorsResource
{
  private static final Logger LOG = LoggerFactory.getLogger(ExecutorsResource.class);
  private final ExecutorService fixedPool = Executors.newFixedThreadPool(2);
  private final ExecutorService perTaskExecutor =
    Executors.newThreadPerTaskExecutor(Thread.ofPlatform().factory());

  @GET
  public void getDateAndTime(@QueryParam("delay") @DefaultValue("0") int delay,
                             @Suspended AsyncResponse asyncResponse)
  {
    Runnable task = () ->
    {
      String threadName = Thread.currentThread().getName();
      LOG.debug("\t>>> Starting task on: {}", threadName);
      try
      {
        Thread.sleep(delay * 1000L);
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
      }
      LOG.debug("\t>>> Ending task on: {}", threadName);
    };
    LOG.debug("*** Fixed pool execution:");
    IntStream.range(0, 4).forEach(i -> fixedPool.submit(task));
    LOG.debug("*** Per-task execution:");
    IntStream.range(0, 4).forEach(i -> perTaskExecutor.submit(task));
    asyncResponse.resume(Response.ok("OK").build());
  }
}
