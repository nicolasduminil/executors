package fr.simplex_software.workshop.executors.async.thread_per_task.virtual;

import fr.simplex_software.workshop.executors.base.*;
import jakarta.ws.rs.*;

import java.util.concurrent.*;

@Path("time-async-tptv")
public class TimeResource extends BaseTimeResource
{
  @Override
  protected ExecutorService getExecutor()
  {
    return Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
  }
}
