## 1. Основні сутності

### **Order — основна сутність**
Сутність описує одне замовлення та містить такі атрибути:

- `orderId` — унікальний ідентифікатор замовлення
- `customerName` — ім’я клієнта
- `date` — дата замовлення
- `totalPrice` — вартість
- `products` — категорія товару або список категорій через кому  
  (наприклад: `"mouse, keyboard"`)

### **Підтримувані атрибути для статистики**
- `customerName`
- `date`
- `products` *(має кілька значень через кому)*
- `orderId`
- `totalPrice`

---
## 2. Приклад вхідного JSON-файлу

Кожен файл містить **одне** замовлення:

```json
{
  "orderId": "41",
  "customerName": "Nina",
  "date": "2024-03-01",
  "totalPrice": 312.0,
  "products": "monitor, speaker"
}
```
3. Приклад вихідного XML-файлу
   Файл генерується як:
   statistics_by_{attribute}.xml

При статистиці за products:
```xml
<statistics>
<item>
<value>monitor</value>
<count>1</count>
</item>
<item>
<value>Laptop</value>
<count>1</count>
</item>
<item>
<value>keyboard</value>
<count>1</count>
</item>
</statistics>
```
sample-orders/
- order1.json
- order2.json
- order3.json
4. Результати експериментів з кількістю потоків 
Тестування проводилося на великій кількості JSON файлів.
Порівнювалась швидкість обробки при 1, 2, 4 і 8 потоках.
   На 100 000 JSON файлах:
Поток: 1 Час: 93472 mc
                           
Потоки: 2 Час: 8 698 mc
                           
Потоки: 4 Час: 5214 mc
                          
Потоки: 8 Час:  4641 mc

Висновок:
Розпаралелювання дає значне прискорення.
При збільшенні кількості потоків час обробки зменшується в десятки разів.
5. Структура проекту
```pgsql
   src/
   └── main/
   ├── java/com/orders
   │   ├── cli/Main.java
   │   ├── entities/Order.java
   │   ├── parser/
   │   │      ├── FileReaderTask.java
   │   │      └── JsonOrderParser.java
   │   ├── statistics/StatisticsCalculator.java
   │   └── xml/XmlWriter.java
   └── resources/sample-orders/
   ├── order1.json
   ├── order2.json
   └── order3.json

src/
└── test/java
├── FileReaderTaskTest.java
├── JsonOrderParserTest.java
├── StatisticsCalculatorTest.java
└── XmlWriterTest.java
```
6.Як запускати програму
```bash
mvn clean package
java -jar target/orders-json-parser-1.0-SNAPSHOT.jar <folderPath> <attributeName> <threads>
```
