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
public class MemoryItem extends BaseRecord {

  private String resourceId;

  private MemoryType memoryType;

  private String summary;

  private List<Float> embedding;
}
