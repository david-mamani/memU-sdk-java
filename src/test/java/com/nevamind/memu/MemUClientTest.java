package com.nevamind.memu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nevamind.memu.exception.MemUException;
import com.nevamind.memu.model.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemUClientTest {

  private MockWebServer mockWebServer;
  private MemUClient client;
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    // Inject mock URL into client
    String baseUrl = mockWebServer.url("/").toString();
    if (baseUrl.endsWith("/")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }
    client = new MemUClient(baseUrl, "test-api-key");

    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void testCreateMemory_Success() throws Exception {
    // Arrange
    MemoryItem item =
        MemoryItem.builder()
            .summary("Test memory")
            .memoryType(MemoryType.EVENT)
            .resourceId("res-123")
            .build();

    // Enqueue 200 OK (Assuming API returns empty JSON or similar on success)
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

    // Act
    boolean success = client.createMemory(item);

    // Assert
    assertThat(success).isTrue();

    RecordedRequest request = mockWebServer.takeRequest();
    assertThat(request.getMethod()).isEqualTo("POST");
    assertThat(request.getPath()).isEqualTo("/v1/memory");
    assertThat(request.getHeader("Authorization")).isEqualTo("Bearer test-api-key");
    assertThat(request.getHeader("Content-Type")).contains("application/json");

    // Verify Body
    MemoryItem sentItem = mapper.readValue(request.getBody().readUtf8(), MemoryItem.class);
    assertThat(sentItem.getSummary()).isEqualTo("Test memory");
    assertThat(sentItem.getMemoryType()).isEqualTo(MemoryType.EVENT);
    assertThat(sentItem.getResourceId()).isEqualTo("res-123");
  }

  @Test
  void testSearch_Success() throws Exception {
    // Arrange
    SearchRequest searchRequest =
        SearchRequest.builder().queries(Collections.singletonList("test query")).limit(5).build();

    SearchResultItem resultItem =
        new SearchResultItem("mem-1", "Found content", 0.95, Map.of("source", "chat"));
    SearchResponse responseBody = new SearchResponse(List.of(resultItem));

    mockWebServer.enqueue(
        new MockResponse().setResponseCode(200).setBody(mapper.writeValueAsString(responseBody)));

    // Act
    SearchResponse response = client.search(searchRequest);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.getResults()).hasSize(1);
    assertThat(response.getResults().get(0).getId()).isEqualTo("mem-1");
    assertThat(response.getResults().get(0).getContent()).isEqualTo("Found content");

    RecordedRequest request = mockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/v1/retrieve");

    // Verify Request Body
    SearchRequest sentRequest = mapper.readValue(request.getBody().readUtf8(), SearchRequest.class);
    assertThat(sentRequest.getQueries()).containsExactly("test query");
    assertThat(sentRequest.getLimit()).isEqualTo(5);
  }

  @Test
  void testUnauthorized_ThrowsException() {
    // Arrange
    MemoryItem item = MemoryItem.builder().summary("Secret").build();

    mockWebServer.enqueue(
        new MockResponse().setResponseCode(401).setBody("{\"error\":\"Unauthorized\"}"));

    // Act & Assert
    MemUException exception = assertThrows(MemUException.class, () -> client.createMemory(item));
    assertThat(exception.getMessage()).contains("401");
  }

  @Test
  void testServerError_ThrowsException() {
    // Arrange
    SearchRequest request = SearchRequest.builder().queries(List.of("query")).build();

    mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

    // Act & Assert
    MemUException exception = assertThrows(MemUException.class, () -> client.search(request));
    assertThat(exception.getMessage()).contains("500");
  }
}
