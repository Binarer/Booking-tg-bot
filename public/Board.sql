create table "Board"
(
	id uuid not null,
	title varchar(80),
	description varchar(120),
	owner uuid,
	"startTime" date default now(),
	"EndTime" date,
	status varchar(10) []
);

alter table "Board" owner to postgres;

alter table "Board"
	add primary key (id);

alter table "Board"
	add foreign key (owner) references "User";

