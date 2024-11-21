package fr.simplex_software.workshop.executors.async.di.tests;

import fr.simplex_software.workshop.executors.async.di.*;
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
