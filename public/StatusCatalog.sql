create table "StatusCatalog"
(
	id integer not null,
	"StatusCode" smallint,
	"StatusName" varchar(10)
);

alter table "StatusCatalog" owner to postgres;

alter table "StatusCatalog"
	add primary key (id);

