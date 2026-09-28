# Employee Management System

A simple **Spring Core** application demonstrating IoC, Dependency Injection, Bean Configuration, Profiles, Bean Scopes, Lifecycle Callbacks, Collection Injection, Validation, and Externalized Configuration.

## Technologies

* Java 17
* Spring Core
* Maven
* Annotation-based Configuration
* Java-based Configuration

## Project Structure

```text
com.training.empmanager
├── audit
│   └── AuditLogger
├── config
│   └── AppConfig
├── model
│   └── Employee
├── notify
│   ├── Notifier
│   ├── NotificationManager
│   └── impl
│       ├── EmailNotifier
│       ├── SmsNotifier
│       └── PushNotifier
├── repository
│   ├── EmployeeRepository
│   └── impl
│       ├── InMemoryEmployeeRepository
│       └── FileBackedEmployeeRepository
├── service
│   ├── EmployeeService
│   └── impl
│       └── EmployeeServiceImpl
├── validator
│   ├── EmployeeValidator
│   └── InvalidEmployeeException
└── Main
```

## Features

### 1. IoC and Dependency Injection

Spring manages the application beans using `ApplicationContext`.

Constructor injection is used in `EmployeeServiceImpl` for:

* `EmployeeRepository`
* `NotificationManager`
* `ObjectProvider<AuditLogger>`

Field injection is used for `EmployeeValidator` and `@Value` configuration properties.

### 2. Repository Profiles

The application provides two repository implementations:

* `dev` → `InMemoryEmployeeRepository`
* `prod` → `FileBackedEmployeeRepository`

The active profile is selected in `Main`.

```java
context.getEnvironment().setActiveProfiles("dev");
```

The `dev` profile stores employees in memory, while the `prod` profile stores employees in a CSV file.

### 3. Notification System

`NotificationManager` receives all `Notifier` implementations as a `List<Notifier>`.

There are three notifiers:

1. Email
2. SMS
3. Push

`@Order` is used to control the notification order.

`EmployeeServiceImpl` sends notifications when:

* A new employee is added.
* An employee receives a raise.

### 4. Validation

`EmployeeValidator` validates employees before saving or giving a raise.

It rejects:

* Blank employee names.
* Negative salaries.

Invalid data throws `InvalidEmployeeException`.

### 5. Prototype Scope and Lifecycle

`AuditLogger` is configured with:

```java
@Scope("prototype")
```

A new `AuditLogger` instance is created every time it is requested.

`ObjectProvider<AuditLogger>` is used in `EmployeeServiceImpl` so that a fresh prototype instance can be obtained for each service operation.

The logger also demonstrates bean lifecycle callbacks:

```java
@PostConstruct
@PreDestroy
```

The Spring context is closed at the end of the application to trigger destruction callbacks.

### 6. Externalized Configuration

Configuration values are stored in:

```text
application.properties
```

The application uses `@PropertySource` and `@Value` to load values such as:

* Company name
* Currency
* Notification retry count
* Maximum raise percentage

For example, a raise above the configured maximum percentage is rejected.

## Example Configuration

```properties
company.name=Tech Company
company.currency=EGP
notification.retry-count=3
raise.max-percentage=20
```

## How to Run

Run the `Main` class.

The application will:

1. Start the Spring ApplicationContext.
2. Activate the `dev` profile.
3. Add a valid employee.
4. Attempt to add an invalid employee.
5. Give an allowed raise.
6. Reject a raise above the maximum percentage.
7. Demonstrate prototype `AuditLogger` instances.
8. Display all employees.
9. Print externalized configuration values.
10. Close the Spring context.

## Spring Concepts Demonstrated

* IoC Container
* Dependency Injection
* Constructor Injection
* Field Injection
* `@Configuration`
* `@Bean`
* `@ComponentScan`
* `@Component`
* `@Service`
* `@Repository`
* `@Profile`
* `@Primary`
* `@Order`
* Collection Injection
* Singleton Scope
* Prototype Scope
* `ObjectProvider`
* `@PostConstruct`
* `@PreDestroy`
* `@Value`
* `@PropertySource`

## Author

**Hend Okasha**

Java Backend Developer | Spring Core
