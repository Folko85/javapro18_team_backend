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
