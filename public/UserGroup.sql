create table "UserGroup"
(
	"UserID" uuid not null,
	"BoardID" uuid not null
);

alter table "UserGroup" owner to postgres;

alter table "UserGroup"
	add primary key ("UserID", "BoardID");

alter table "UserGroup"
	add foreign key ("UserID") references "User";

alter table "UserGroup"
	add foreign key ("BoardID") references "Board";

