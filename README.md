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
            "availability": "IN_STOCK",
            "count": 10,
            "deleted": false
          },
          {
            "id": 2,
            "name": "Product 2",
            "description": "Description of Product 2",
            "price": 200.00,
            "availability": "OUT_OF_STOCK",
            "count": 0,
            "deleted": false
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
        "availability": "IN_STOCK",
        "count": 10,
        "deleted": false
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
    "price": 150.00
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
        "availability": "OUT_OF_STOCK",
        "count": 0,
        "deleted": false
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
    "price": 250.00
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
        "availability": "IN_STOCK",
        "count": 10,
        "deleted": false
      }
      ```

### 6. Получение списка всех поставок

- **URL:** `/delivery`
- **Метод:** `GET`
- **Описание:** Возвращает список всех поставок с возможностью фильтрации, сортировки и пагинации.
- **Параметры:**
    - `name` (Query Param, optional): Фильтр по названию поставки.
    - `productId` (Query Param, optional): Идентификатор продукта.
    - `sortBy` (Query Param, optional): Поле для сортировки (`createdAt` или `updatedAt`).
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
            "name": "Delivery 1",
            "count": 10,
            "product": {
              "id": 1
            },
            "createdAt": "2023-10-01T12:00:00",
            "updatedAt": "2023-10-01T12:00:00"
          },
          {
            "id": 2,
            "name": "Delivery 2",
            "count": 20,
            "product": {
              "id": 2
            },
            "createdAt": "2023-10-02T12:00:00",
            "updatedAt": "2023-10-02T12:00:00"
          }
        ],
        "totalElements": 2,
        "totalPages": 1,
        "number": 0,
        "size": 10
      }
      ```

### 7. Получение поставки по ID

- **URL:** `/delivery/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает поставку с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор поставки.
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "Delivery 1",
        "count": 10,
        "product": {
          "id": 1
        },
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
      }
      ```

### 8. Удаление поставки по ID

- **URL:** `/delivery/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет поставку с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор поставки.
- **Ответ:**
    - **Статус:** `204 No Content`

### 9. Создание новой поставки

- **URL:** `/delivery`
- **Метод:** `POST`
- **Описание:** Создает новую поставку.
- **Тело запроса:**
  ```json
  {
    "name": "New Delivery",
    "count": 10,
    "product": {
      "id": 1
    }
  }
  ```
- **Ответ:**
    - **Статус:** `201 Created`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "New Delivery",
        "count": 10,
        "product": {
          "id": 1
        },
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
      }
      ```

### 10. Обновление поставки по ID

- **URL:** `/delivery/{id}`
- **Метод:** `PATCH`
- **Описание:** Обновляет поставку с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор поставки.
- **Тело запроса:**
  ```json
  {
    "name": "Updated Delivery"
  }
  ```
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "Updated Delivery",
        "count": 10,
        "product": {
          "id": 1
        },
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
      }
      ```

### 11. Получение списка всех продаж

- **URL:** `/sale`
- **Метод:** `GET`
- **Описание:** Возвращает список всех продаж с возможностью фильтрации, сортировки и пагинации.
- **Параметры:**
    - `name` (Query Param, optional): Фильтр по названию продажи.
    - `productId` (Query Param, optional): Идентификатор продукта.
    - `minCost` (Query Param, optional): Минимальная стоимость продажи.
    - `maxCost` (Query Param, optional): Максимальная стоимость продажи.
    - `sortBy` (Query Param, optional): Поле для сортировки (`createdAt` или `updatedAt`).
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
            "name": "Sale 1",
            "count": 10,
            "cost": 100.00,
            "product": {
              "id": 1
            },
            "createdAt": "2023-10-01T12:00:00",
            "updatedAt": "2023-10-01T12:00:00"
          },
          {
            "id": 2,
            "name": "Sale 2",
            "count": 20,
            "cost": 200.00,
            "product": {
              "id": 2
            },
            "createdAt": "2023-10-02T12:00:00",
            "updatedAt": "2023-10-02T12:00:00"
          }
        ],
        "totalElements": 2,
        "totalPages": 1,
        "number": 0,
        "size": 10
      }
      ```

### 12. Получение продажи по ID

- **URL:** `/sale/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает продажу с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор продажи.
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "Sale 1",
        "count": 10,
        "cost": 100.00,
        "product": {
          "id": 1
        },
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
      }
      ```

### 13. Удаление продажи по ID

- **URL:** `/sale/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет продажу с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор продажи.
- **Ответ:**
    - **Статус:** `204 No Content`

### 14. Создание новой продажи

- **URL:** `/sale`
- **Метод:** `POST`
- **Описание:** Создает новую продажу.
- **Тело запроса:**
  ```json
  {
    "name": "New Sale",
    "count": 10,
    "product": {
      "id": 1
    }
  }
  ```
- **Ответ:**
    - **Статус:** `201 Created`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "New Sale",
        "count": 10,
        "cost": 100.00,
        "product": {
          "id": 1
        },
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
      }
      ```

### 15. Обновление продажи по ID

- **URL:** `/sale/{id}`
- **Метод:** `PATCH`
- **Описание:** Обновляет продажу с указанным ID.
- **Параметры:**
    - `id` (Path Variable): Идентификатор продажи.
- **Тело запроса:**
  ```json
  {
    "name": "Updated Sale"
  }
  ```
- **Ответ:**
    - **Статус:** `200 OK`
    - **Тело ответа:**
      ```json
      {
        "id": 1,
        "name": "Updated Sale",
        "count": 10,
        "cost": 100.00,
        "product": {
          "id": 1
        },
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
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
    "message": "Продукт с id 3 не найден или помечен как удален",
    "details": {}
  }
  ```

### 4. Продажа не найдена

- **Статус:** `404 Not Found`
- **Тело ответа:**
  ```json
  {
    "uri": "/sale/3",
    "message": "Продажа товара с id 3 не найден",
    "details": {}
  }
  ```

### 5. Доставка не найдена

- **Статус:** `404 Not Found`
- **Тело ответа:**
  ```json
  {
    "uri": "/delivery/3",
    "message": "Поставка товара с id 3 не найден",
    "details": {}
  }
  ```

### 6. Ошибка парсинга JSON

- **Статус:** `400 Bad Request`
- **Тело ответа:**
  ```json
  {
    "uri": "/product",
    "message": "Ошибка парсинга JSON. Пожалуйста, проверьте формат данных.",
    "details": {}
  }
  ```

### 7. Неподдерживаемый тип медиа

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
  "price": 150.00
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
  "availability": "OUT_OF_STOCK",
  "count": 0,
  "deleted": false
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
  "availability": "IN_STOCK",
  "count": 10,
  "deleted": false
}
```
