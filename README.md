# Spring 5 Functional Web Framework Currency Conversion Sample

This repository contains a sample application that uses the functional web framework introduced in Spring 5.
It consists of the following types:

| Class/Package                   | Description                                   |
| ----------------------- | --------------------------------------------- |
| `Main`                | Contains a `main` method to start the client                  |
| `vm.ex.currency.configuration`                | Configuration package                    |
| `MainConfiguration`                | Main configuration, contains `RouterFunction`                   |
| `vm.ex.currency.handler`                | Handlers package                    |
| `CurrencyConverterHandler`                | Conversion handler, contains validation logic                    |
| `vm.ex.currency.model`                | Package for POJO and entity models                    |
| `vm.ex.currency.model.provider`      | Package for `Provider` entities             |
| `ConversionRequest` | Conversion request POJO    |
| `ConversionResponse`         | Conversion response POJO  |
| `vm.ex.currency.service`                | Package for services  |
| `ExternalProviderService`                | Service to get external providers. Note if `Provider` will require authorization than `Authorization` header will be added. Header should be provided in `ProviderInfo` entity class |
| `ExtyernalProviderServiceTest`                | Contains `ExternalProviderService` tests  |
| `APIEndpointTests`                | Contains API `/currency/convert` endpoint tests  |

### Running the Reactor Netty server
 - Build using maven
 - Run the `vm.ex.currency.Main` class

### Sample curl commands

Instead of running the client, here are some sample `curl` commands that access services exposed
by this sample:

```sh
#correct request body
curl -X POST -d '{"from":"USD","to":"EUR", "amount": 10}' -H "Content-Type: application/json" http://localhost:8080/currency/convert

#incorrect request body
curl -X POST -d '{"from":"USD","to":"NotExistedCurrency(more thsan 3 characters)", "amount": 10}' -H "Content-Type: application/json" http://localhost:8080/currency/convert

```

### Possible extension

Caching could be added to be more efficient. But to be precise and consistent we need to consider how long currency rate data is valid. In today's reality currency rates are volatile. So to reach more efficient we will need some data or external service that provide some time of data validity. Then we could use spring caching functionality.
For example:
```java
Caffeine.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(Duration.ofSeconds(validityTime))
        .build();
```
