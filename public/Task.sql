create table "Task"
(
	"Id" uuid not null,
	"Title" varchar(80),
	"Description" varchar(255),
	"CreatedBy" uuid,
	"CreatedAt" date default now(),
	"AssignedTo" uuid,
	"Status_id" integer,
	"StartTime" date,
	"EndTime" date,
	"BoardID" uuid
);

alter table "Task" owner to postgres;

alter table "Task"
	add primary key ("Id");

alter table "Task"
	add foreign key ("Status_id") references "StatusCatalog";

alter table "Task"
	add foreign key ("BoardID") references "Board";

alter table "Task"
	add foreign key ("CreatedBy") references "User";

alter table "Task"
	add constraint task_user_id_fk
		foreign key ("AssignedTo") references "User";

alter table "Task"
	add constraint task_user_id
		foreign key ("CreatedBy") references "User";

