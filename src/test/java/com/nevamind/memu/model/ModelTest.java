package com.nevamind.memu.model;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  void testMemoryItemCoverage() throws Exception {
    MemoryItem item1 =
        MemoryItem.builder()
            .id("test-id")
            .createdAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .resourceId("res-1")
            .memoryType(MemoryType.EVENT)
            .summary("Summary")
            .embedding(Collections.singletonList(0.1f))
            .build();

    MemoryItem item2 =
        MemoryItem.builder()
            .id("test-id")
            .createdAt(item1.getCreatedAt())
            .updatedAt(item1.getUpdatedAt())
            .resourceId("res-1")
            .memoryType(MemoryType.EVENT)
            .summary("Summary")
            .embedding(Collections.singletonList(0.1f))
            .build();

    assertEquals(item1, item2);
    assertEquals(item1.hashCode(), item2.hashCode());
    assertNotNull(item1.toString());

    String json = mapper.writeValueAsString(item1);
    MemoryItem deserialized = mapper.readValue(json, MemoryItem.class);

    assertEquals(item1.getId(), deserialized.getId());
    assertEquals(item1.getResourceId(), deserialized.getResourceId());
    assertEquals(item1.getMemoryType(), deserialized.getMemoryType());
    assertEquals(item1.getSummary(), deserialized.getSummary());
  }

  @Test
  void testResourceCoverage() throws Exception {
    Resource resource1 =
        Resource.builder()
            .id("res-id")
            .url("http://example.com")
            .modality("text")
            .localPath("/tmp/file")
            .caption("Caption")
            .embedding(Collections.singletonList(0.5f))
            .build();

    Resource resource2 =
        Resource.builder()
            .id("res-id")
            .url("http://example.com")
            .modality("text")
            .localPath("/tmp/file")
            .caption("Caption")
            .embedding(Collections.singletonList(0.5f))
            .build();

    assertEquals(resource1, resource2);
    assertEquals(resource1.hashCode(), resource2.hashCode());
    assertNotNull(resource1.toString());

    String json = mapper.writeValueAsString(resource1);
    Resource deserialized = mapper.readValue(json, Resource.class);
    assertEquals(resource1.getId(), deserialized.getId());
    assertEquals(resource1.getUrl(), deserialized.getUrl());
  }

  @Test
  void testMemoryCategoryCoverage() throws Exception {
    MemoryCategory cat1 =
        MemoryCategory.builder()
            .id("cat-id")
            .name("Category")
            .description("Desc")
            .summary("Sum")
            .embedding(Collections.singletonList(0.9f))
            .build();

    MemoryCategory cat2 =
        MemoryCategory.builder()
            .id("cat-id")
            .name("Category")
            .description("Desc")
            .summary("Sum")
            .embedding(Collections.singletonList(0.9f))
            .build();

    assertEquals(cat1, cat2);
    assertEquals(cat1.hashCode(), cat2.hashCode());
    assertNotNull(cat1.toString());

    String json = mapper.writeValueAsString(cat1);
    MemoryCategory deserialized = mapper.readValue(json, MemoryCategory.class);
    assertEquals(cat1.getName(), deserialized.getName());
  }

  @Test
  void testCategoryItemCoverage() throws Exception {
    CategoryItem item1 =
        CategoryItem.builder().id("join-id").itemId("item-1").categoryId("cat-1").build();

    CategoryItem item2 =
        CategoryItem.builder().id("join-id").itemId("item-1").categoryId("cat-1").build();

    assertEquals(item1, item2);
    assertEquals(item1.hashCode(), item2.hashCode());
    assertNotNull(item1.toString());

    String json = mapper.writeValueAsString(item1);
    CategoryItem deserialized = mapper.readValue(json, CategoryItem.class);
    assertEquals(item1.getItemId(), deserialized.getItemId());
  }

  @Test
  void testMemoryTypeCoverage() throws Exception {
    // Test Enum serialization
    String json = mapper.writeValueAsString(MemoryType.EVENT);
    assertEquals("\"event\"", json);

    MemoryType deserialized = mapper.readValue("\"event\"", MemoryType.class);
    assertEquals(MemoryType.EVENT, deserialized);

    // Verify all values present
    assertNotNull(MemoryType.valueOf("EVENT"));
    assertNotNull(MemoryType.valueOf("PROFILE"));
    assertNotNull(MemoryType.valueOf("KNOWLEDGE"));
    assertNotNull(MemoryType.valueOf("BEHAVIOR"));
    assertNotNull(MemoryType.valueOf("SKILL"));
    assertNotNull(MemoryType.valueOf("EPISODIC"));
  }

  @Test
  void testSearchRequestCoverage() throws Exception {
    SearchRequest req1 =
        SearchRequest.builder()
            .queries(Collections.singletonList("query"))
            .limit(10)
            .minScore(0.5)
            .build();

    SearchRequest req2 = new SearchRequest();
    req2.setQueries(Collections.singletonList("query"));
    req2.setLimit(10);
    req2.setMinScore(0.5);

    // Test AllArgsConstructor via Builder vs NoArgsConstructor + Setters
    // (indirectly)
    assertEquals(req1, req2);
    assertEquals(req1.hashCode(), req2.hashCode());
    assertNotNull(req1.toString());

    String json = mapper.writeValueAsString(req1);
    SearchRequest deserialized = mapper.readValue(json, SearchRequest.class);
    assertEquals(req1.getLimit(), deserialized.getLimit());
  }

  @Test
  void testSearchResponseCoverage() throws Exception {
    SearchResultItem item = new SearchResultItem("id", "content", 1.0, null);
    SearchResponse res1 = new SearchResponse(Collections.singletonList(item));
    SearchResponse res2 = new SearchResponse();
    res2.setResults(Collections.singletonList(item));

    assertEquals(res1, res2);
    assertEquals(res1.hashCode(), res2.hashCode());
    assertNotNull(res1.toString());

    String json = mapper.writeValueAsString(res1);
    SearchResponse deserialized = mapper.readValue(json, SearchResponse.class);
    assertEquals(1, deserialized.getResults().size());
  }

  @Test
  void testSearchResultItemCoverage() throws Exception {
    SearchResultItem item1 = new SearchResultItem("id", "content", 0.9, Map.of("key", "val"));
    SearchResultItem item2 = new SearchResultItem();
    item2.setId("id");
    item2.setContent("content");
    item2.setScore(0.9);
    item2.setMetadata(Map.of("key", "val"));

    assertEquals(item1, item2);
    assertEquals(item1.hashCode(), item2.hashCode());
    assertNotNull(item1.toString());

    String json = mapper.writeValueAsString(item1);
    SearchResultItem deserialized = mapper.readValue(json, SearchResultItem.class);
    assertEquals(item1.getContent(), deserialized.getContent());
  }
}
