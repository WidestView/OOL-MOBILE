
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

## Some Screenshots

**Login Screen**
![image](https://user-images.githubusercontent.com/55855728/145611507-eed5cd9c-646f-4b4f-aee1-5b55302953bf.png)

**Home Screen**
![image](https://user-images.githubusercontent.com/55855728/145611740-46de6208-8c50-43ad-8f1a-2a7cfb1c7953.png)

**Navigation**
![image](https://user-images.githubusercontent.com/55855728/145612116-61324853-da10-44aa-af78-8c9ab7db55e7.png)

**Calendar**
![image](https://user-images.githubusercontent.com/55855728/145611760-55ee44a8-51a2-46a3-b570-d9f36925d0e7.png)

**Equipment**
![image](https://user-images.githubusercontent.com/55855728/145611842-966bf5b3-4634-4113-9fa6-2bac5ea27985.png)

**Equipment Details**
![image](https://user-images.githubusercontent.com/55855728/145611868-8584f25d-0c2c-4262-91bc-0ca3f2e595b9.png)

**Add Equipment Details**
![image](https://user-images.githubusercontent.com/55855728/145612037-4e313712-2e23-48db-803a-b1cc2b43071e.png)

**Perform Equipment Withdraw**
![image](https://user-images.githubusercontent.com/55855728/145612080-c57a8789-1060-430f-8b46-262a63b0bb63.png)

**Export Logs**
![image](https://user-images.githubusercontent.com/55855728/145612154-cbc569df-44a5-4cb1-8c56-11e9411cee69.png)


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
