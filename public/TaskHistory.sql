create table "TaskHistory"
(
	"Id" uuid not null,
	"TaskId" uuid,
	"Time" timestamp with time zone default now(),
	"TaskData" jsonb
);

alter table "TaskHistory" owner to postgres;

alter table "TaskHistory"
	add primary key ("Id");

alter table "TaskHistory"
	add foreign key ("TaskId") references "Task";

