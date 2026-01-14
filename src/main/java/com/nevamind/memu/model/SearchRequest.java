package com.nevamind.memu.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

  private List<String> queries;

  @Builder.Default private Integer limit = 10;

  private Double minScore;
}
