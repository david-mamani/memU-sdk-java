package com.nevamind.memu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MemoryType {
  @JsonProperty("profile")
  PROFILE,

  @JsonProperty("event")
  EVENT,

  @JsonProperty("knowledge")
  KNOWLEDGE,

  @JsonProperty("behavior")
  BEHAVIOR,

  @JsonProperty("skill")
  SKILL,

  @JsonProperty("episodic")
  EPISODIC;
}
