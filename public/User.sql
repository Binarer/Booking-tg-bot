create table "User"
(
	"Id" uuid not null,
	"Фио" varchar(255),
	contact_id integer
);

alter table "User" owner to postgres;

alter table "User"
	add primary key ("Id");

alter table "User"
	add constraint user_contact_id_fk
		foreign key (contact_id) references "Contact";

