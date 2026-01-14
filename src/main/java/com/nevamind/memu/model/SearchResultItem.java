package com.nevamind.memu.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultItem {

  private String id;

  private String content;

  private Double score;

  private Map<String, Object> metadata;
}
