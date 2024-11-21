package fr.simplex_software.workshop.executors.sync.none.tests;

import fr.simplex_software.workshop.executors.base.tests.*;
import fr.simplex_software.workshop.executors.sync.none.*;
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
