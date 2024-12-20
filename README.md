# Socks Inventory
В данном репозитории располагается исходный код клиент-серверного веб-приложения для учета носков на складе магазина.



## Содержание
- [Технологии](#технологии)
- [Использование](#использование)
- [Тестирование](#тестирование)

## Технологии
- [Java](https://www.java.com/ru/)
- [Spring Framework](https://spring.io/)
- [Gradle](https://gradle.org/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Postgresql](https://www.postgresql.org/)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Swagger](https://swagger.io/)

## Использование
Освободите порт 8080.

Чтобы запустить приложение, введите:
```sh
$ ./gradlew clean build
$ ./gradlew bootRun
```

## Тестирование
Проект покрыт юнит-тестами JUnit. Для их запуска выполните команду:
```sh
$ ./gradlew test
```
