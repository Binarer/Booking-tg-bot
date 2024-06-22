create table "Contact"
(
	id integer not null,
	phone varchar(255),
	email varchar(255)
);

alter table "Contact" owner to postgres;

alter table "Contact"
	add primary key (id);

