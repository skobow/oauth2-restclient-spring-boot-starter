# OAuth2RestClient Spring Boot Starter

This project offers a lean and easy to use OAuth2 enabled and Spring Boot powered REST client to interact with secured APIs.
Simply add it as a dependency to your Spring project and get `OAuth2RestClient` autowired.

## Releases
[![Build Status](https://dev.azure.com/SvenKobow/SvenKobow/_apis/build/status/skobow.oauth2-restclient-spring-boot-starter?branchName=develop)](https://dev.azure.com/SvenKobow/SvenKobow/_build/latest?definitionId=1?branchName=develop)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.skobow/oauth2-restclient-spring-boot-starter/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/net.skobow/oauth2-restclient-spring-boot-starter)


### Gradle
    implementation 'net.skobow:oauth2-restclient-spring-boot-starter:0.3.0'
    
### Maven
    <dependency>
      <groupId>net.skobow</groupId>
      <artifactId>oauth2-restclient-spring-boot-starter</artifactId>
      <version>0.3.0</version>
      <type>pom</type>
    </dependency>

## Usage

To use the client simply let Spring autowire an instance to your service

    import net.skobow.rest.OAuth2RestClient
    
    public class MyService {
        
        private final OAuth2RestClient restClient;
        
        public MyService(final OAuth2RestClient restClient) {
            this.restClient = restClient;
            
            ...
        }
    }
    
## OAuth2 grant types

Currently only the [Client Credentials Grant](https://oauth.net/2/grant-types/client-credentials/) is supported.