# Документация по API

### 1. Получение списка всех продуктов

- **URL:** `/product`
- **Метод:** `GET`
- **Описание:** Возвращает список всех продуктов с возможностью фильтрации, сортировки и пагинации.
- **Параметры:**
    - `name` (Query Param, optional): Фильтр по названию продукта.
    - `minPrice` (Query Param, optional): Минимальная цена продукта.
    - `maxPrice` (Query Param, optional): Максимальная цена продукта.
    - `availability` (Query Param, optional): Фильтр по наличию продукта (`IN_STOCK` или `OUT_OF_STOCK`).
    - `sortBy` (Query Param, optional): Поле для сортировки (`name` или `price`).
    - `page` (Query Param, default: `0`): Номер страницы.
    - `size` (Query Param, default: `10`): Размер страницы (от 1 до 100).
    - `reverse` (Query Param, default: `false`): Обратный порядок сортировки (`true` или `false`).
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "content": [
          {
            "id": 1,
            "name": "Product 1",
            "description": "Description of Product 1",
            "price": 100.00,
            "availability": "IN_STOCK"
          },
          {
            "id": 2,
            "name": "Product 2",
            "description": "Description of Product 2",
            "price": 200.00,
            "availability": "OUT_OF_STOCK"
          }
        ],
        "totalElements": 2,
        "totalPages": 1,
        "number": 0,
        "size": 10
      }
      ```

### 2. Получение продукта по ID

- **URL:** `/product/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает продукт с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор продукта.
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "Product 1",
        "description": "Description of Product 1",
        "price": 100.00,
        "availability": "IN_STOCK"
      }
      ```

### 3. Удаление продукта по ID

- **URL:** `/product/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет продукт с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор продукта.
- **Ответ:**
    - **Статус:** `204 No Content`

### 4. Создание нового продукта

- **URL:** `/product`
- **Метод:** `POST`
- **Описание:** Создает новый продукт.
- **Тело запроса:**
  ```json
  {
    "name": "New Product",
    "description": "Description of New Product",
    "price": 150.00,
    "availability": "IN_STOCK"
  }
  ```
- **Ответ:**
    - **Статус:** `201 Created`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "New Product",
        "description": "Description of New Product",
        "price": 150.00,
        "availability": "IN_STOCK"
      }
      ```

### 5. Обновление продукта по ID

- **URL:** `/product/{id}`
- **Метод:** `PATCH`
- **Описание:** Обновляет продукт с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор продукта.
- **Тело запроса:**
  ```json
  {
    "name": "Updated Product",
    "description": "Updated description",
    "price": 250.00,
    "availability": "OUT_OF_STOCK"
  }
  ```
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "Updated Product",
        "description": "Updated description",
        "price": 250.00,
        "availability": "OUT_OF_STOCK"
      }
      ```

## Обработка ошибок

### 1. Ошибка валидации данных

- **Статус:** `400 Bad Request`
- **Тело ответа:**
  ```json
  {
    "uri": "/product",
    "message": "Ошибка валидации данных",
    "details": {
      "name": "Название не может быть пустым",
      "price": "Цена должна быть больше или равна 0.00"
    }
  }
  ```

### 2. Ошибка валидации параметров запроса

- **Статус:** `400 Bad Request`
- **Тело ответа:**
  ```json
  {
    "uri": "/product",
    "message": "Ошибка валидации параметров запроса",
    "details": {
      "minPrice": "Значение должно быть больше или равно 0.00",
      "sortBy": "Значение должно быть 'name' или 'price'"
    }
  }
  ```

### 3. Продукт не найден

- **Статус:** `404 Not Found`
- **Тело ответа:**
  ```json
  {
    "uri": "/product/3",
    "message": "Продукт с id 3 не найден",
    "details": {}
  }
  ```

### 4. Ошибка парсинга JSON

- **Статус:** `400 Bad Request`
- **Тело ответа:**
  ```json
  {
    "uri": "/product",
    "message": "Ошибка парсинга JSON. Пожалуйста, проверьте формат данных.",
    "details": {}
  }
  ```

### 5. Неподдерживаемый тип медиа

- **Статус:** `415 Unsupported Media Type`
- **Тело ответа:**
  ```json
  {
    "uri": "/product",
    "message": "Неподдерживаемый тип медиа: application/xml",
    "details": {}
  }
  ```

## Примеры использования

### Пример создания продукта

**Запрос:**

```http
POST /product
Content-Type: application/json

{
  "name": "New Product",
  "description": "Description of New Product",
  "price": 150.00,
  "availability": "IN_STOCK"
}
```

**Ответ:**

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "name": "New Product",
  "description": "Description of New Product",
  "price": 150.00,
  "availability": "IN_STOCK"
}
```

### Пример обновления продукта

**Запрос:**

```http
PATCH /product/1
Content-Type: application/json

{
  "name": "Updated Product",
  "price": 250.00
}
```

**Ответ:**

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "name": "Updated Product",
  "description": "Description of Product 1",
  "price": 250.00,
  "availability": "IN_STOCK"
}
```
