# javapro18_team_backend
Дипломный проект группы 18 Skillbox 

Основные участники со строны бэка:
<a href="https://github.com/Folko85">Folko85</a>
<a href="https://github.com/Shkiller">Shkiller</a>
<a href="https://github.com/KronCosta">KronCosta</a>
<a href="https://github.com/Brosha">Brosha</a>
<a href="https://github.com/Artemk70">Artemk70</a>

Как это посмотреть:
**mvn clean package** - чтобы сбилдить jar-файлы(модуль microservice может долго билдится, 
так как под капотом идёт загрузка библиотек с помощью npm) 
**docker-compose up -d** - чтобы проект поднялся 
(P.S. Обычно поднимается долго, так как контейнеры зависят друг от друга. 
Нужно дождаться загрузки проекта cloud-config)
**Россыпь технологий:**
Spring Boot, Spring Security, Spring Cloud Config, Hibernate, Liquibase, 
RabbitMQ, Redis, SocketIO, Vaadin, Junit, Grafana, Prometheus, Loki, Promtail, 
Lombok, Docker Compose, Maven

ToDo:
2. Сделать инструкцию по работе с графаной.
3. Поменять редис на другой контейнер для кэша(Ignite?) или вообще убрать
4. Убрать вторую базу mySQL (пусть данные хранятся в mongoDB)
5. Добавить в проект кафку для обратного ответа от микросервиса
