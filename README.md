# Simple Flutter mobile app communicating with reactive backend

## Description

This project consists of two modules:
- [Mobile app](mobile/)
- [Reactive backend](backend/)

Mobile application is written in `Dart` with `Flutter` framework.

Backend application is written in `Java` using `Spring Boot` and `WebFlux` module. It provides non-blocking request-response model with `Netty` server under the hood. The main storage for the backend is `MongoDB`.

Backend application uses `REST API` for clients communication and `JWT` tokens for users authorization and authentication. It also provides `OpenAPI` documentation using `springdoc-openapi` library.
