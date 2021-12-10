
# OOL-MOBILE

The mobile utility application for OutOfLens.

[![Android CI](https://github.com/WidestView/OOL-MOBILE/actions/workflows/android-ci.yml/badge.svg?branch=develop)](https://github.com/WidestView/OOL-MOBILE/actions/workflows/android-ci.yml)

---

## Members
- Felipe Beserra
- Isabela Borejo
- Julia Souza
- Pedro Tamaz
- Renan Ribeiro
- Sophia Carvalho
- Yasmin Francisquetti

## Screenshots

The screenshots can be found at the [drive directory](https://drive.google.com/drive/folders/1gRfqPYc7ckXxfdvrLqRjFnF1q4ING1l3?usp=sharing)


## Modern Resources
This application uses some resources that may make it a bit different from
classic android java applications.

### RxJava
RxJava is the library of choice for handling asyncrounous operations, it may be a little difficult to read
at first, but method documentation is in progress to help with that.

### ViewModels
ViewModels are classes that help decoupling application logic from view logic. By using ViewModels,
Activities do activiy things, such as displaying dialogs, spawning intents and formatting views while
ViewModels handle application logic, such as performing requests, uploading images and validating input.

### DataBinding
DataBinding allows developers to decouple application logic from view displaying even more,
by allowing data to bound to xml files, JavaScript framework style, while retaining customization
capabilities by defining custom data adapters.

### Kotlin
This application does in fact utilize kotlin for some operations with a lot of boilerplate
specially the view models, but it remains mostly in java. For more details, check the documentation
comments of HomeViewModel.
