package io.securecodebox.persistence.defectdojo;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * This is just a PoC to test if WireMock is sufficient to test our REST calls
 * <p>
 * See https://wiremock.org/docs/quickstart/java-junit/
 * and https://wiremock.org/docs/junit-jupiter/
 */
@WireMockTest(httpPort = HttpClientTest.PORT)
final class HttpClientTest {

  public static final int PORT = 8888;

  private URI createUri(String path) {
    return URI.create("http://localhost:%d/%s".formatted(PORT, path));
  }

  @Test
  @Disabled
  void test_something_with_wiremock(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, InterruptedException {
    stubFor(get("/my/resource")
      .withHeader("Content-Type", containing("xml"))
      .willReturn(ok()
        .withHeader("Content-Type", "text/xml")
        .withBody("<response>SUCCESS</response>")));

    // Setup HTTP POST request (with HTTP Client embedded in Java 11+)
    final HttpClient client = HttpClient.newBuilder().build();
    final HttpRequest request = HttpRequest.newBuilder()
      .uri(createUri("my/resource"))
      .header("Content-Type", "text/xml")
      .GET()
      .build();

    // Send the request and receive the response
    final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // Verify the response (with AssertJ)
    assertThat(response.statusCode(), is(200));
    assertThat(response.body(), containsString("<response>SUCCESS</response>"));
  }
}
