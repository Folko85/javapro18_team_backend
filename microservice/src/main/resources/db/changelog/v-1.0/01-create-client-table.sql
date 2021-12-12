create table clients
(
    id         integer      not null auto_increment,
    e_mail     varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    primary key (id)
) engine=InnoDB;
GO

alter table clients
    add constraint UK_mkf1rhbej8k4k9tes7vhkurq5 unique (e_mail)
GO