## Конвертер валют

### Используемые технологии:

+ Java 11, Stream API
+ Maven
+ Spring Boot 2.6.3
+ Spring Security
+ Spring Data JPA
+ PostgreSQL 13
+ Thymeleaf
+ HTML5/CSS(Bootstrap)

### Техническое задание

При запуске приложения необходимо получить список актуальных валют и их курсов с сайта ЦБРФ:  
http://www.cbr.ru/scripts/XML_daily.asp  
дополнительная информация:  
https://cbr.ru/development/sxml/  
и записать их в базу данных (идентификаторы, коды, названия), а также курсы (*привязанные к валюте*) на дату запроса.
В конвертере должна быть авторизация по логину и паролю. Пользователь, пройдя авторизацию, попадает на главный экран, где может выбрать из какой валюты и в какую будет конвертация, указывает количество переводимых средств и нажать кнопку "Конвертировать". После чего происходит запрос в БД на получение актуального курса на текущую дату, если данные на текущую дату отсутствуют, необходимо произвести получение курсов с сайта ЦБ и добавить новые записи в базу данных.
Также, в конвертере должна вестись история произведенных конвертаций с записью в базу данных с указанием курса, по которому была произведена конвертация.
Для пар валют, с которыми проводилась конвертация, необходимо подсчитать статистику - средний курс конвертации и суммарный объём конвертаций по каждой паре за неделю.
Статистику и историю конвертаций можно посмотреть на той же странице конвертера или отдельной вкладке (возможна реализация базовых фильтров).

### Рекомендации по сборке и запуску проекта:

+ создать на сервере БД пользователя `postgres` с паролем `postgres`
+ создать на сервере БД пустую базу данных `currency_converter_db`
+ запуск из командной строки из корневой папки `mvnw spring-boot:run`
+ создание исполняемого jar `mvnw clean package`
+ запуск jar файла из командной строки:
    + перейти в папку с jar файлом
    + выполнить команду `java -jar CurrencyConverter-0.0.1-SNAPSHOT.jar`

#### Данные пользователя по умолчанию:  
логин `user`  
пароль `password`

Репозиторий проекта: https://gitlab.com/jimaltair/currencyconverter