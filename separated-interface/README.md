---
title: Separated Interface
category: Structural
language: en
tag:
    - API design
    - Decoupling
    - Interface
---

## Also known as

* API Segregation
* Client-Server Interface

## Intent

To define a client interface in a separate package from its implementation to allow for easier swapping of implementations and better separation of concerns.

## Explanation

Real world example

> Consider a restaurant where the menu (interface) is separate from the kitchen operations (implementation).
>
> In this analogy, the menu lists the dishes customers can order, without detailing how they are prepared. Different restaurants (or even different chefs within the same restaurant) can use their own recipes and methods to prepare the dishes listed on the menu. This separation allows the restaurant to update its menu or change its chefs without disrupting the overall dining experience. Similarly, in software, the Separated Interface pattern decouples the interface from its implementation, allowing changes and variations in the implementation without affecting the client code that relies on the interface.

In plain words

> Defines a client interface separate from its implementation to allow for flexible and interchangeable components.

A client code may abstract some specific functionality to an interface, and define the definition of 
the interface as an SPI ([Service Programming Interface](https://en.wikipedia.org/wiki/Service_provider_interface) 
is an API intended and open to be implemented or extended by a third party). Another package may 
implement this interface definition with a concrete logic, which will be injected into the client 
code at runtime (with a third class, injecting the implementation in the client) or at compile time 
(using Plugin pattern with some configurable file).

**Programmatic Example**

The Separated Interface design pattern is a software design pattern that encourages the separation of the definition of an interface from its implementation. This allows the client to be completely unaware of the implementation, promoting loose coupling and enhancing flexibility.

In the given code, the `InvoiceGenerator` class is the client that uses the `TaxCalculator` interface to calculate tax. The `TaxCalculator` interface is implemented by two classes: `ForeignTaxCalculator` and `DomesticTaxCalculator`. These implementations are injected into the `InvoiceGenerator` class at runtime, demonstrating the Separated Interface pattern.

Let's break down the code:

First, we have the `TaxCalculator` interface. This interface defines a single method `calculate` that takes an amount and returns the calculated tax.

```java
public interface TaxCalculator {
  double calculate(double amount);
}
```

Next, we have two classes `ForeignTaxCalculator` and `DomesticTaxCalculator` that implement the `TaxCalculator` interface. These classes provide the concrete logic for tax calculation.

```java
public class ForeignTaxCalculator implements TaxCalculator {
  public static final double TAX_PERCENTAGE = 60;

  @Override
  public double calculate(double amount) {
    return amount * TAX_PERCENTAGE / 100.0;
  }
}

public class DomesticTaxCalculator implements TaxCalculator {
  public static final double TAX_PERCENTAGE = 20;

  @Override
  public double calculate(double amount) {
    return amount * TAX_PERCENTAGE / 100.0;
  }
}
```

The `InvoiceGenerator` class is the client that uses the `TaxCalculator` interface. It doesn't know about the concrete implementations of the `TaxCalculator` interface. It just knows that it has a `TaxCalculator` that can calculate tax.

```java
public class InvoiceGenerator {
  private final TaxCalculator taxCalculator;
  private final double amount;

  public InvoiceGenerator(double amount, TaxCalculator taxCalculator) {
    this.amount = amount;
    this.taxCalculator = taxCalculator;
  }

  public double getAmountWithTax() {
    return amount + taxCalculator.calculate(amount);
  }
}
```

Finally, in the `App` class, we create instances of `InvoiceGenerator` with different `TaxCalculator` implementations. This demonstrates how the Separated Interface pattern allows us to inject different implementations at runtime.

```java
public class App {
  public static final double PRODUCT_COST = 50.0;

  public static void main(String[] args) {
    var internationalProductInvoice = new InvoiceGenerator(PRODUCT_COST, new ForeignTaxCalculator());
    LOGGER.info("Foreign Tax applied: {}", "" + internationalProductInvoice.getAmountWithTax());

    var domesticProductInvoice = new InvoiceGenerator(PRODUCT_COST, new DomesticTaxCalculator());
    LOGGER.info("Domestic Tax applied: {}", "" + domesticProductInvoice.getAmountWithTax());
  }
}
```

In this way, the Separated Interface pattern allows us to decouple the interface of a component from its implementation, enhancing flexibility and maintainability.

## Class diagram

![Separated Interface](./etc/class_diagram.png "Separated Interface")

## Applicability

* Use when you want to decouple the interface of a component from its implementation.
* Useful in large systems where different teams work on different parts of the system.
* Ideal when the implementation might change over time or vary between deployments.

## Tutorial

* [Separated Interface Tutorial - YouTube](https://www.youtube.com/watch?v=d3k-hOA7k2Y)

## Known Uses

* Java's JDBC (Java Database Connectivity) API separates the client interface from the database driver implementations.
* Remote Method Invocation (RMI) in Java, where the client and server interfaces are defined separately from the implementations.

## Consequences

Benefits:

* Enhances flexibility by allowing multiple implementations to coexist.
* Facilitates testing by allowing mock implementations.
* Improves maintainability by isolating changes to specific parts of the code.

Trade-offs:

* Initial setup might be more complex.
* May lead to increased number of classes and interfaces in the codebase.

## Related Patterns

* Adapter: Adapts one interface to another, which can be used alongside Separated Interface to integrate different implementations.
* Dependency Injection: Often used to inject the implementation of a separated interface, promoting loose coupling.
* Bridge: Separates an object’s interface from its implementation, similar to Separated Interface but usually applied to larger-scale architectural issues.

## Credits

* [Design Patterns: Elements of Reusable Object-Oriented Software](https://amzn.to/3w0pvKI)
* [Effective Java](https://amzn.to/4cGk2Jz)
* [Pattern-Oriented Software Architecture Volume 1: A System of Patterns](https://amzn.to/3xZ1ELU)
* [Patterns of Enterprise Application Architecture](https://amzn.to/3WfKBPR)
* [Separated Interface - Martin Fowler](https://www.martinfowler.com/eaaCatalog/separatedInterface.html)
