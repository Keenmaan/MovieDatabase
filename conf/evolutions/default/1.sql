# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table genre (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_genre primary key (id))
;

create table movie (
  id                        integer not null,
  original_language         varchar(255),
  original_title            varchar(255),
  release_date              date,
  popularity                decimal(10,8),
  title                     varchar(255),
  vote_average              decimal(10,8),
  vote_count                integer,
  constraint pk_movie primary key (id))
;

create table movie_genre (
  id                        integer not null,
  movie_id                  integer,
  genre_id                  integer,
  constraint pk_movie_genre primary key (id))
;

create sequence genre_seq;

create sequence movie_seq;

create sequence movie_genre_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists genre;

drop table if exists movie;

drop table if exists movie_genre;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists genre_seq;

drop sequence if exists movie_seq;

drop sequence if exists movie_genre_seq;

