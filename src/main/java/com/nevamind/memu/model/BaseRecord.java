package com.nevamind.memu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseRecord {

  @lombok.Builder.Default private String id = UUID.randomUUID().toString();

  private ZonedDateTime createdAt;

  private ZonedDateTime updatedAt;
}
