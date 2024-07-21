create table reminders
(
    id                  bigserial primary key,
    chat_id             bigint     not null,
    name                text       not null,
    birthday            varchar(5) not null,
    days_until_birthday integer[] not null
);
