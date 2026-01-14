package com.nevamind.memu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nevamind.memu.exception.MemUException;
import com.nevamind.memu.model.MemoryItem;
import com.nevamind.memu.model.SearchRequest;
import com.nevamind.memu.model.SearchResponse;
import java.io.IOException;
import okhttp3.*;

/**
 * Client for interacting with the MemU API.
 *
 * <p>Provides methods to create memories and search/retrieve information.
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>{@code
 * MemUClient client = new MemUClient("https://api.memu.ai", "your-api-key");
 *
 * // Create a memory
 * MemoryItem item = MemoryItem.builder()
 *     .summary("User passed the Java certification")
 *     .memoryType(MemoryType.SKILL)
 *     .build();
 * client.createMemory(item);
 *
 * // Search for memories
 * SearchRequest request = SearchRequest.builder()
 *     .queries(List.of("java certification"))
 *     .limit(5)
 *     .build();
 * SearchResponse response = client.search(request);
 * }</pre>
 */
public class MemUClient {

  private static final String ENDPOINT_MEMORY = "/v1/memory";
  private static final String ENDPOINT_RETRIEVE = "/v1/retrieve";
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private final String baseUrl;
  private final String apiKey;
  private final OkHttpClient client;
  private final ObjectMapper jsonMapper;

  /**
   * Constructs a new MemUClient.
   *
   * @param baseUrl The base URL of the MemU API (e.g., "https://api.memu.ai").
   * @param apiKey The API key for authentication.
   */
  public MemUClient(String baseUrl, String apiKey) {
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
    this.client = new OkHttpClient();
    this.jsonMapper = new ObjectMapper();
    this.jsonMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Creates a new memory item in the MemU system.
   *
   * @param item The {@link MemoryItem} to create.
   * @return {@code true} if the operation was successful.
   * @throws MemUException If a network error occurs or the API returns a non-success status code.
   */
  public boolean createMemory(MemoryItem item) {
    try {
      String json = jsonMapper.writeValueAsString(item);
      RequestBody body = RequestBody.create(json, JSON);
      Request request =
          new Request.Builder()
              .url(baseUrl + ENDPOINT_MEMORY)
              .addHeader("Authorization", "Bearer " + apiKey)
              .post(body)
              .build();

      try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
          throw new MemUException(
              "Create memory failed: " + response.code() + " " + response.message());
        }
        return true;
      }
    } catch (IOException e) {
      throw new MemUException("Network error creating memory", e);
    }
  }

  /**
   * Searches for memories based on the provided criteria.
   *
   * @param searchRequest The {@link SearchRequest} containing queries and filters.
   * @return A {@link SearchResponse} containing the search results.
   * @throws MemUException If a network error occurs, the API returns a non-success status, or the
   *     response body is empty.
   */
  public SearchResponse search(SearchRequest searchRequest) {
    try {
      String json = jsonMapper.writeValueAsString(searchRequest);
      RequestBody body = RequestBody.create(json, JSON);
      Request request =
          new Request.Builder()
              .url(baseUrl + ENDPOINT_RETRIEVE)
              .addHeader("Authorization", "Bearer " + apiKey)
              .post(body)
              .build();

      try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
          throw new MemUException("Search failed: " + response.code() + " " + response.message());
        }
        if (response.body() == null) {
          throw new MemUException("Search returned empty body");
        }
        return jsonMapper.readValue(response.body().string(), SearchResponse.class);
      }
    } catch (IOException e) {
      throw new MemUException("Network error searching", e);
    }
  }
}
