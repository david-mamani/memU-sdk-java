# Contributing to MemU Java SDK

Thank you for your interest in contributing to the MemU Java SDK! We welcome contributions from the community.

## Development Setup

1.  **Java Version**: Ensure you have Java 21 installed.
2.  **Maven**: This project uses Maven for dependency management and building.

## Coding Style

We use **Google Java Format** to maintain code consistency. This is enforced via the [Spotless Maven Plugin](https://github.com/diffplug/spotless).

Before committing your changes, please run the following command to automatically format your code:

```bash
mvn spotless:apply
```

If you do not run this, the CI build will fail during the formatting check.

## Running Tests

To run the unit and integration tests, use the standard Maven test command:

```bash
mvn test
```

To run the full verification suite (tests + code coverage + formatting check), run:

```bash
mvn clean verify
```

## Pull Requests

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/amazing-feature`).
3.  Commit your changes.
4.  Run `mvn spotless:apply` and `mvn verify` to ensure everything is correct.
5.  Push to the branch (`git push origin feature/amazing-feature`).
6.  Open a Pull Request.

## Code of Conduct

Please note that this project is released with a Contributor Code of Conduct. By participating in this project you agree to abide by its terms.
