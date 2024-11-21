package fr.simplex_software.workshop.executors.async.fork_join;

import fr.simplex_software.workshop.executors.base.*;
import jakarta.ws.rs.*;

import java.util.concurrent.*;

@Path("time-steal")
public class TimeResource extends BaseTimeResource
{
  @Override
  protected ExecutorService getExecutor()
  {
    return Executors.newWorkStealingPool(2);
  }
}
