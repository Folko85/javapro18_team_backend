create table messages
(
    id                  integer    not null auto_increment,
    date_of_application datetime(6) not null,
    message             mediumtext not null,
    client_id           integer,
    primary key (id)
) engine=InnoDB;
GO

alter table messages
    add constraint FK9sfs7a1s2a1hnpspj1pm8pjx0 foreign key (client_id) references clients (id);
GO