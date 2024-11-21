package fr.simplex_software.workshop.executors.async.fork_join;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import org.slf4j.*;

import java.util.concurrent.*;
import java.util.stream.*;

@Path("execs-steal")
public class ExecutorsResource
{
  private static final Logger LOG = LoggerFactory.getLogger(ExecutorsResource.class);
  private ExecutorService threadPool = Executors.newFixedThreadPool(2);
  private ExecutorService workStealingPool = Executors.newWorkStealingPool(2);

  @GET
  public void getDateAndTime(@Suspended AsyncResponse asyncResponse)
  {
    LOG.debug(">>> Testing Thread Pool:");
    testPool(threadPool);
    LOG.debug("\n>>> Testing Work Stealing Pool:");
    testPool(workStealingPool);
    threadPool.shutdown();
    workStealingPool.shutdown();
    asyncResponse.resume(Response.ok("OK").build());
  }

  private static void testPool(ExecutorService pool)
  {
    long startTime = System.currentTimeMillis();
    CompletableFuture<?>[] futures = IntStream.range(0, 4)
      .mapToObj(i -> CompletableFuture.runAsync(() ->
      {
        try
        {
          if (i % 2 == 0)
          {
            Thread.sleep(3000);
            LOG.debug("Long task {} completed by {}", i, Thread.currentThread().getName());
          } else
          {
            Thread.sleep(500);
            LOG.debug("Quick task {} completed by {}", i, Thread.currentThread());
          }
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
      }, pool))
      .toArray(CompletableFuture[]::new);
    CompletableFuture.allOf(futures).join();
    long duration = System.currentTimeMillis() - startTime;
    LOG.debug("Total execution time: {}ms", duration);
  }
}
