# 🎓 Климова Юлия Васильевна Выполнение задания.

## 🚀 Запуск проекта

### Frontend

Перейдите в папку `frontend`, установите зависимости и запустите проект:

```bash
cd frontend
npm install
npm run dev
```

### Backend

Скомпилируйте проект, соберите JAR-файл и запустите приложение:

```bash
mvn clean
mvn package -Dmaven.test.skip=true
java -jar target/my-blog-back-app.jar
```

### Запуск автоматических тестов

```bash
mvn test
```

## Проверка работы API

### Создание поста

```bash
curl -X POST "http://localhost:8080/api/posts" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Мой первый пост",
    "text": "Это тестовый пост",
    "tags": ["тест", "первый"]
  }'
```

### Обновление поста

```bash
curl -X PUT "http://localhost:8080/api/posts/1" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "title": "Обновлённый заголовок",
    "text": "Обновлённый текст поста",
    "tags": ["java", "spring", "backend"]
  }'
```

### Удаление поста

```bash
curl -X DELETE "http://localhost:8080/api/posts/1"
```

### Получение списка постов

```bash
curl "http://localhost:8080/api/posts?search=&pageNumber=1&pageSize=10"
```

### Получение поста по ID

```bash
curl "http://localhost:8080/api/posts/1"
```

### Загрузка изображения к посту

```bash
curl -X PUT "http://localhost:8080/api/posts/1/image" \
  -F "image=@/path/to/image.jpg"
```

### Добавление лайка

```bash
curl -X POST "http://localhost:8080/api/posts/1/likes"
```

### Удаление лайка

```bash
curl -X DELETE "http://localhost:8080/api/posts/1/likes"
```

### Создание комментария

```bash
curl -X POST "http://localhost:8080/api/posts/1/comments" \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Отличный пост!",
    "postId": 1
  }'
```

### Удаление комментария

```bash
curl -X DELETE "http://localhost:8080/api/posts/1/comments/2"
```

### Получение комментарий поста

```bash
curl "http://localhost:8080/api/posts/1/comments"
```

### Обновление комментария

```bash
curl -X PUT "http://localhost:8080/api/posts/1/comments/2" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 2,
    "text": "Обновлённый текст комментария",
    "postId": 1
  }'
```