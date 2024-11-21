package fr.simplex_software.workshop.executors.async.thread_per_task.virtual.tests;

import fr.simplex_software.workshop.executors.async.thread_per_task.virtual.*;
import fr.simplex_software.workshop.executors.base.tests.*;
import io.quarkus.test.common.http.*;
import io.quarkus.test.junit.*;

import java.net.*;

@QuarkusTest
public class TestTimeResourceVT extends BaseTimeResourceTest
{
  @TestHTTPEndpoint(TimeResource.class)
  @TestHTTPResource
  URL timeSrvUrl;

  @Override
  protected URL getTimeSrvURL()
  {
    return timeSrvUrl;
  }
}
