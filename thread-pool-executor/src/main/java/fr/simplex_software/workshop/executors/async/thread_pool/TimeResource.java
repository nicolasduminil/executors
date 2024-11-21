package fr.simplex_software.workshop.executors.async.thread_pool;

import fr.simplex_software.workshop.executors.base.*;
import jakarta.ws.rs.*;

import java.util.concurrent.*;

@Path("time-async-direct")
public class TimeResource extends BaseTimeResource
{
  @Override
  protected ExecutorService getExecutor()
  {
    return Executors.newFixedThreadPool(2);
  }
}
