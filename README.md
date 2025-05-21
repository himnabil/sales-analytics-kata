# Sales Analytics Kata

A coding kata for implementing a sales analytics system that processes order events and provides various analytics functions.

## Overview

This kata challenges you to implement a sales analytics system that can process different types of order events (new orders, cancellations, refunds) and provide various analytics functions like calculating total revenue, revenue by channel, identifying top customers, and computing average order values.

## Problem Domain

The system deals with the following concepts:

- **Orders**: Represent sales transactions with an ID, channel, customer ID, and amount
- **Channels**: Different sales platforms (WEB, MOBILE, STORE)
- **Order Events**: Different events that can occur:
  - **New Order**: A new sales transaction
  - **Cancellation**: Cancellation of an existing order
  - **Refund**: Partial or full refund of an order

## Requirements

Your task is to implement the `SalesAnalytics` interface, which provides the following functionality:

1. **Adding Events**: Add order events to the system (new orders, cancellations, refunds)
2. **Total Net Revenue**: Calculate the total revenue after accounting for refunds and cancellations
3. **Revenue by Channel**: Break down revenue by sales channel
4. **Top Customers**: Identify the top N customers by their net spend
5. **Average Order Value**: Calculate the average order value for a specific channel

## Implementation Guidelines

- The `add` method should have $O(1)$ time complexity
- Cancelled orders should be excluded from all calculations
- Refunds should be subtracted from the revenue
- The implementation should handle edge cases appropriately

## Getting Started

1. Examine the provided interfaces and domain models
2. Implement the `SalesAnalytics` interface
3. Run the tests to verify your implementation

## Testing

The project includes comprehensive tests for all required functionality. Run the tests using:

```bash
./mvnw test
```

## Technical Details

| Component | Version |
|-----------|---------|
| Java LTS  | 21      |
| JUnit 5   | 5.12.2  |
| AssertJ   | 3.27.3  |
