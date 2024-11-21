package fr.simplex_software.workshop.executors.async.di;

import fr.simplex_software.workshop.executors.base.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;

import java.util.concurrent.*;

@Path("time-async-di")
public class TimeResource extends BaseTimeResource
{
  @Inject
  private ExecutorService executor;

  @Override
  protected ExecutorService getExecutor()
  {
    return executor;
  }
}
