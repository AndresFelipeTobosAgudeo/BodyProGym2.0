/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  usuario
 * Created: 28/05/2020
 */

CREATE DATABASE bdbodyprogym
    WITH 
    OWNER = unicentral
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

------------------------------------

CREATE TABLE public.instructor
(
    cedula integer NOT NULL,
    nombre character varying(45) COLLATE pg_catalog."default",
    especialidad character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT instructor_pkey PRIMARY KEY (cedula)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.instructor
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.tipo_plan
(
    identificador integer NOT NULL,
    nombre character varying(45) COLLATE pg_catalog."default",
    CONSTRAINT tipo_plan_pkey PRIMARY KEY (identificador)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tipo_plan
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.usuario
(
    cedulamod character varying(8) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT usuario_pkey PRIMARY KEY (cedulamod)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.usuario
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.clase
(
    identificador integer NOT NULL,
    nombre character varying(20) COLLATE pg_catalog."default",
    fecha date,
    descripcion text COLLATE pg_catalog."default",
    capacidad integer,
    costo integer,
    lugar character varying(20) COLLATE pg_catalog."default",
    ins_cedula integer,
    CONSTRAINT clase_pkey PRIMARY KEY (identificador),
    CONSTRAINT fk_instructor_clase FOREIGN KEY (ins_cedula)
        REFERENCES public.instructor (cedula) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.clase
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.plan_deportista
(
    identificador integer NOT NULL,
    nombre character varying(20) COLLATE pg_catalog."default",
    fecha_inicial date,
    valor integer,
    tipo_plan integer,
    CONSTRAINT plan_deportista_pkey PRIMARY KEY (identificador),
    CONSTRAINT fk_plan_tipoplan FOREIGN KEY (tipo_plan)
        REFERENCES public.tipo_plan (identificador) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.plan_deportista
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.deportista
(
    cedula integer NOT NULL,
    nombre character varying(45) COLLATE pg_catalog."default",
    ciudad character varying(20) COLLATE pg_catalog."default",
    fecha_nacimiento date,
    telefono numeric(10,0),
    usuario character varying(8) COLLATE pg_catalog."default",
    plan integer,
    CONSTRAINT deportista_pkey PRIMARY KEY (cedula),
    CONSTRAINT fk_plan_deportista FOREIGN KEY (plan)
        REFERENCES public.plan_deportista (identificador) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_usuario_deportista FOREIGN KEY (usuario)
        REFERENCES public.usuario (cedulamod) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.deportista
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.registro_compra
(
    identificador integer NOT NULL,
    fecha date,
    producto text COLLATE pg_catalog."default",
    descripcion text COLLATE pg_catalog."default",
    valor integer,
    cedula_deportista integer,
    CONSTRAINT registro_compra_pkey PRIMARY KEY (identificador),
    CONSTRAINT fk_compra_deportista FOREIGN KEY (cedula_deportista)
        REFERENCES public.deportista (cedula) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.registro_compra
    OWNER to unicentral;

------------------------------------

CREATE TABLE public.deportista_clase
(
    deportista integer NOT NULL,
    clase integer NOT NULL,
    CONSTRAINT deportista_clase_pkey PRIMARY KEY (deportista, clase),
    CONSTRAINT fk_clase FOREIGN KEY (clase)
        REFERENCES public.clase (identificador) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_deportista FOREIGN KEY (deportista)
        REFERENCES public.deportista (cedula) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.deportista_clase
    OWNER to unicentral;


------------------------------------------

CREATE DATABASE bdexterna
    WITH 
    OWNER = unicentral
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-------------------------------------

CREATE TABLE public.asistente
(
    nombre character varying(40) COLLATE pg_catalog."default",
    cedula character varying(20) COLLATE pg_catalog."default",
    cedulamod character varying(8) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT asistente_pkey PRIMARY KEY (cedulamod)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.asistente
    OWNER to unicentral;