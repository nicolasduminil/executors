package fr.simplex_software.workshop.executors.async.thread_per_task;

import fr.simplex_software.workshop.executors.base.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.*;

import java.util.concurrent.*;

@Path("time-async-tpt")
public class TimeResource extends BaseTimeResource
{
  @Override
  protected ExecutorService getExecutor()
  {
    return Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory());
  }
}
