# MemU Java SDK

![Build Status](https://github.com/nevamind-ai/memu-sdk-java/actions/workflows/ci.yml/badge.svg)
![Version](https://img.shields.io/badge/version-0.1.0--SNAPSHOT-blue)
![License](https://img.shields.io/badge/license-MIT-green)

The official Java SDK for MemU, an advanced memory storage and retrieval API for AI applications.

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.nevamind.ai</groupId>
    <artifactId>memu-sdk-java</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Quick Start

Here is a complete example of how to initialize the client, create a memory, and search for it.

```java
import com.nevamind.memu.MemUClient;
import com.nevamind.memu.model.*;
import java.util.List;

public class MemUExample {
    public static void main(String[] args) {
        // 1. Initialize the client
        String baseUrl = "https://api.memu.ai"; // Replace with actual API URL
        String apiKey = System.getenv("MEMU_API_KEY");
        
        MemUClient client = new MemUClient(baseUrl, apiKey);

        // 2. Create a Memory
        MemoryItem memory = MemoryItem.builder()
            .summary("The user prefers dark mode in their IDE.")
            .memoryType(MemoryType.preference)
            .build();

        try {
            boolean success = client.createMemory(memory);
            System.out.println("Memory created: " + success);
        } catch (Exception e) {
            System.err.println("Failed to create memory: " + e.getMessage());
        }

        // 3. Search for Memories
        SearchRequest request = SearchRequest.builder()
            .queries(List.of("What IDE theme does the user like?"))
            .limit(3)
            .build();

        try {
            SearchResponse response = client.search(request);
            response.getResults().forEach(result -> {
                System.out.println("Found: " + result.getContent() + " (Score: " + result.getScore() + ")");
            });
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
        }
    }
}
```

## Configuration

The `MemUClient` constructor requires two parameters:

| Parameter | Description | Example |
|-----------|-------------|---------|
| `baseUrl` | The URL of your MemU API instance. | `https://api.memu.ai` |
| `apiKey`  | Your authentication token. | `mem_12345abcde` |

We recommend storing your API key in an environment variable (e.g., `MEMU_API_KEY`) rather than hardcoding it.

## Building from Source

To build the SDK and run tests locally, ensure you have **Java 21** and **Maven** installed.

```bash
# Clone the repository
git clone https://github.com/nevamind-ai/memu-sdk-java.git
cd memu-sdk-java

# Build and run integrity checks (Tests + Formatting + Coverage)
mvn clean verify
```

## Requirements

* Java 21 or higher
* Maven 3.6+
