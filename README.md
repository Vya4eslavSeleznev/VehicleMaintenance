# Задание

1. Выбрать предметную область. На основе выбранной области, придумать композицию,
состоящей из не менее 2 объектов, отношения между которыми "один ко многим". Пример
композиций: город - достопримечательность, университет - факультет, студент - оценка
по предмету. Необходимо в произвольной форме сделать описание предметной области,
выбранной композиции и используемые в ней объекты. Для каждого объекта описать не
менее 5 свойств.

2. Создать Git репозиторий, в котором будут хранится все файлы, необходимые для
сборки и запуска будущего приложения.

3. Установить реляционную базу данных (БД) на выбор: MySQL или PostgreSQL. Создать
схему БД для хранения и управления данными для смоделированных объектов. Добавить
файлы инициализации схемы данных в Git репозиторий. Дополнительным, совсем
необязательным, плюсом будет использование Docker контейнера для БД.

4. Создать REST API приложение в архитектурном стиле MVC на основе фрэймворка
Spring Boot. Требования:
- Java, версия от 8+
- приложение содержит пул подключений к БД
- имплементированы классы-модели в Java для отображения смоделированных
объектов
- решена задача объектно-реляционного отображения
- создан интерфейс API (Application Programming Interface) на основе архитектурного
стиля REST для проведения CRUD (Create, Read, Update, Delete) операций над
объектами. Взаимодействие с API реализовано в формате JSON
- в корне проекта содержится README.md файл. README.md содержит описание всех
зависимостей, подключенных в проект, а также пошаговые инструкции для сборки и
запуска приложения
- подготовлены по одному примеру запроса для каждого эндпоинта в одном из
следующих форматов: сURL, HTTP, wget. Примечание: можно сгенерировать результат с
использованием инструмента тестирования Postman или инструмента автоматической
генерации документации Swagger. При использовании Postman добавить файлы
коллекции Postman в структуру проекта

### Ожидаемый результат:

1. Описана предметная область и композиция, формулировки четкие и не допускают
двусмысленности.
2. Код приложения можно скачать с Git-хранилища.
3. Приложение запускается по инструкции. Все зависимости описаны.
4. Подготовленные запросы проходят успешно, соответствующие данные в БД
обновляются.

# Стек

Использовались следующие технологии:

- Java 11
- Spring Boot
- Spring Mvc
- Spring Security
- Spring Data
- Swagger 3
- Junit 5
- Mockito
- Docker
- Lombok
- PostgreSQL

# Описание предметной области и пояснения к заданию

- В качестве предметной области была выбрана автоматизация станции технического обслуживания автомобилей. Существует
таблица клиентов. У каждого клиента может быть несколько автомобилей (связь один ко многим). У каждого автомобиля могут
быть несколько (связь один ко многим) записей на техническое обслуживание

- В системе предусмотрено две роли: `ADMIN` и `USER`. `USER` способен добавлять записи на техническое обслуживание.
Админ способен делать все что угодно, за исключением добавления записей на обслуживание

- Были реализованы юнит-тесты бизнес-логики приложения

- Все контейнеризировано при помощи Docker. Более того, написан docker-compose и образ приложения выложен на DockerHub

# Описание базы данных

![Db](docs/db.png)

- База данных содержит в себе четыре таблицы:
    - `credential` - хранит секреты пользователя: роль, пароль и логин
    - `customer` - хранит информацию о пользователе
    - `car` - хранит данные об автомобилях пользователя
    - `maintenance` - содержит информацию о записи на техническое обслуживаине автомобиля

# Данные в БД

- При запуске приложения уже будут доступны клиенты и админ со следующими персональными данными:

|   Роль   |   Имя    |  Пароль  |
|----------|----------|----------|
|   USER   |    q     |   pwd    |
|   USER   |    w     |   pwd    |
|   USER   |    e     |   pwd    |
|  ADMIN   |  admin   |   pwd    |

- Администратор в системе может делать все что угодно, кроме добавления новой записи в таблицу `maintenance`

- У пользователя есть права на добавления записи в таблицу `maintenance`

# Описание эндпоинтов

### `http://localhost:8081/auth/signin`

- Для использование приложения необходимо авторизироваться

- Body:

```json
{
    "username" : "admin",
    "password" : "pwd"
}
```

- Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTY4NDUzNjA2NywiZXhwIjoxNjg0NTQ2ODY3fQ.Mi6PjU6mfrlTA7EQffMK6AXFEhCbrqoMcSNQO0E9x10"
}
```

---

### `http://localhost:8081/customer/{id}`

- Получение пользователя по id

- Response:

```json
{
  "id": 1,
  "name": "Ivanov",
  "surname": "Ivan",
  "lastName": "Ivanovich",
  "phone": "89990008877",
  "birthDate": "1999-01-01T00:00:00.000+00:00",
  "username": "q"
}
```

### `http://localhost:8081/customer`

- Добавление пользователя

- Body:

```json
{
  "name": "Vyacheslav",
  "surname": "Seleznev",
  "lastName": "Seleznev",
  "phone": "78881111100",
  "birthDate": "2023-05-19T22:42:28.396Z",
  "username": "vya4esalva",
  "password": "pwd"
}
```

### `http://localhost:8081/customer/refreshment`

- Обновление пользователя

- Body:

```json
{
  "id": 1,
  "name": "updated",
  "surname": "updated",
  "lastName": "updated",
  "phone": "70000000000",
  "birthDate": "2023-05-19T22:43:58.921Z"
}
```

### `http://localhost:8081/customer/all`

- Просмотр всех пользователей

- Response:

```json
[
  {
    "id": 2,
    "name": "Petrov",
    "surname": "Petr",
    "lastName": "Petrovich",
    "phone": "89990008878",
    "birthDate": "1999-01-02T00:00:00.000+00:00",
    "username": "w"
  },
  {
    "id": 3,
    "name": "Sergeev",
    "surname": "Ivan",
    "lastName": "Petrovich",
    "phone": "89990008879",
    "birthDate": "1999-01-03T00:00:00.000+00:00",
    "username": "e"
  },
  {
    "id": 4,
    "name": "Vyacheslav",
    "surname": "Seleznev",
    "lastName": "Seleznev",
    "phone": "78881111100",
    "birthDate": "2023-05-19T22:42:28.396+00:00",
    "username": "vya4esalva"
  },
  {
    "id": 1,
    "name": "updated",
    "surname": "updated",
    "lastName": "updated",
    "phone": "70000000000",
    "birthDate": "2023-05-19T22:43:58.921+00:00",
    "username": "q"
  }
]
```

### `http://localhost:8081/customer/{id}`

- Удаление пользователя по id. Удаляются все его записи в таблицах `credential`, `car`, `maintenance`

---

### `http://localhost:8081/car/all`

- Просмотр всех автомобилей пользователей

- Response:

```json
[
  {
    "id": 1,
    "brand": "Toyota",
    "model": "Supra",
    "engine": "2JZ",
    "color": "pink",
    "name": "updated",
    "surname": "updated",
    "lastName": "updated",
    "phone": "70000000000",
    "birthDate": "2023-05-19T22:43:58.921+00:00"
  },
  {
    "id": 2,
    "brand": "Lada",
    "model": "2102",
    "engine": "1.5",
    "color": "red",
    "name": "Petrov",
    "surname": "Petr",
    "lastName": "Petrovich",
    "phone": "89990008878",
    "birthDate": "1999-01-02T00:00:00.000+00:00"
  },
  {
    "id": 3,
    "brand": "Ford",
    "model": "Mustang",
    "engine": "V8",
    "color": "black",
    "name": "Sergeev",
    "surname": "Ivan",
    "lastName": "Petrovich",
    "phone": "89990008879",
    "birthDate": "1999-01-03T00:00:00.000+00:00"
  }
]
```

### `http://localhost:8081/car/{id}`

- Просмотр автомобиля пользователя по id

- Response:

```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Supra",
  "engine": "2JZ",
  "color": "pink",
  "name": "updated",
  "surname": "updated",
  "lastName": "updated",
  "phone": "70000000000",
  "birthDate": "2023-05-19T22:43:58.921+00:00"
}
```

### `http://localhost:8081/car`

- Добавление нового автомобиля

- Body:

```json
{
  "brand": "Tesla",
  "model": "Model3",
  "engine": "Electric",
  "color": "black",
  "customerId": 1
}
```

### `http://localhost:8081/car/{id}`

- Удаление автомобиля по id. Удаляются все записи в таблице `maintenance`

### `http://localhost:8081/car/refreshment`

- Обновление данных об автомобиле пользователя

- Response:

```json
{
  "id": 1,
  "brand": "updated",
  "model": "updated",
  "engine": "updated",
  "color": "updated"
}
```

---

### `http://localhost:8081/maintenance/all`

- Просмотр всех записей на техобслуживание

- Response:

```json
[
  {
    "id": 1,
    "date": "2023-12-05T00:00:00.000+00:00",
    "description": "Swap engine",
    "brand": "updated",
    "model": "updated",
    "engine": "updated",
    "color": "updated",
    "name": "updated",
    "lastName": "updated",
    "phone": "70000000000"
  },
  {
    "id": 2,
    "date": "2023-12-05T00:00:00.000+00:00",
    "description": "Engine oil",
    "brand": "Lada",
    "model": "2102",
    "engine": "1.5",
    "color": "red",
    "name": "Petrov",
    "lastName": "Petrovich",
    "phone": "89990008878"
  },
  {
    "id": 3,
    "date": "2023-12-05T00:00:00.000+00:00",
    "description": "Engine oil",
    "brand": "Ford",
    "model": "Mustang",
    "engine": "V8",
    "color": "black",
    "name": "Sergeev",
    "lastName": "Petrovich",
    "phone": "89990008879"
  },
  {
    "id": 4,
    "date": "2023-05-19T22:31:12.501+00:00",
    "description": "string",
    "brand": "updated",
    "model": "updated",
    "engine": "updated",
    "color": "updated",
    "name": "updated",
    "lastName": "updated",
    "phone": "70000000000"
  }
]
```

### `http://localhost:8081/maintenance/{id}`

- Просмотр записи по id

- Response:

```json
{
  "id": 1,
  "date": "2023-12-05T00:00:00.000+00:00",
  "description": "Swap engine",
  "brand": "updated",
  "model": "updated",
  "engine": "updated",
  "color": "updated",
  "name": "updated",
  "lastName": "updated",
  "phone": "70000000000"
}
```

### `http://localhost:8081/maintenance/conservation`

- Добавление записи на техобслуживаине. Добавлять может только пользователь

- Body:

```json
{
  "carId": 1,
  "date": "2023-05-19T22:31:12.501Z",
  "description": "rims"
}
```

### `http://localhost:8081/maintenance/{id}`

- Удаление записи по id

### `http://localhost:8081/maintenance/refreshment`

- Обновление записи на техобслуживание

```json
{
  "id": 1,
  "date": "2023-05-19T23:00:16.469Z",
  "description": "updated"
}
```

---

### Swagger

- для запуска Swagger необходимо воспользоваться url:

```
http://localhost:8081/swagger-ui/index.html#/
```

# Как запустить?

- Предварительно мной был создан образ приложения и добавлен на [DockerHub](https://hub.docker.com/u/vya4eslava):

- Для запуска Вам необходимо клонировать на свой компьютер репозиторий. Либо скачать .zip архив с проектом. После этого
выполнить команду:

```shell script
docker compose up -d
```
