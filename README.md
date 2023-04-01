```mermaid
sequenceDiagram
    AuthenticationWebFilter->>ServerAuthenticationConverter: convert
    activate ServerAuthenticationConverter
    AuthenticationWebFilter->>ReactiveAuthenticationManager: authenticate
    activate ReactiveAuthenticationManager
    ReactiveAuthenticationManager->>ReactiveUserDetailsService: findByUsername
    activate ReactiveUserDetailsService
    ReactiveUserDetailsService->>ReactiveAuthenticationManager: userDetail
    activate ReactiveAuthenticationManager
    ReactiveAuthenticationManager->>AuthenticationWebFilter: authentication
```
