Changelog
=========

## Version 1.0.0

_2025-01-13_

* Add support for further customization of the call factory.
* Switch the callback execution into another thread to avoid dispatcher exhaustion and ensure proper call cancellation management.

## Version 0.0.6

_2024-06-28_

* Add support for decoding properties of type `Map`.
* Add a new proguard rule to keep classes extending from `HalResource`.

## Version 0.0.5

_2024-02-27_

* Tag options per Retrofit service interface method.

## Version 0.0.4

_2023-03-31_

* Consume KotlinX Serialization Transient annotation in serialization and deserialization.

## Version 0.0.3

_2023-03-17_

* Implement a workaround for Retrofit converter factory caching.
* Update dependencies.

## Version 0.0.2

_2023-01-12_

* Update dependencies.
* Fix serialization deprecation.
* Implement sanitization of templated links without value for placeholders.
* Return imperative arguments when there are no annotated arguments present.
* Implement common template arguments.

## Version 0.0.1

_2022-08-16_

* Initial public release.
