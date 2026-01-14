package com.nevamind.memu.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@lombok.EqualsAndHashCode(callSuper = true)
public class MemoryCategory extends BaseRecord {

  private String name;

  private String description;

  private String summary;

  private List<Float> embedding;
}
