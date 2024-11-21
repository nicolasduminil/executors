package fr.simplex_software.workshop.executors.thread_per_task.tests;

import fr.simplex_software.workshop.executors.async.thread_per_task.*;
import fr.simplex_software.workshop.executors.base.tests.*;
import io.quarkus.test.common.http.*;
import io.quarkus.test.junit.*;

import java.net.*;

@QuarkusTest
public class TestTimeResource extends BaseTimeResourceTest
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
