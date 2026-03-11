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