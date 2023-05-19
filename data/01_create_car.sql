CREATE TABLE IF NOT EXISTS public.credential
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    role character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT credential_pkey PRIMARY KEY (id),
    CONSTRAINT uq_username UNIQUE (username)
        INCLUDE(username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.credential
    OWNER to postgres;




CREATE TABLE IF NOT EXISTS public.customer
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    surname character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    phone character varying COLLATE pg_catalog."default" NOT NULL,
    birth_date timestamp without time zone NOT NULL,
    credential_id integer NOT NULL,
    CONSTRAINT customer_pkey PRIMARY KEY (id),
    CONSTRAINT uq_phone UNIQUE (phone)
        INCLUDE(phone),
    CONSTRAINT fk_credential FOREIGN KEY (credential_id)
        REFERENCES public.credential (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.customer
    OWNER to postgres;




CREATE TABLE IF NOT EXISTS public.car
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    brand character varying COLLATE pg_catalog."default" NOT NULL,
    model character varying COLLATE pg_catalog."default" NOT NULL,
    engine character varying COLLATE pg_catalog."default" NOT NULL,
    color character varying COLLATE pg_catalog."default" NOT NULL,
    customer_id integer NOT NULL,
    CONSTRAINT car_pkey PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id)
        REFERENCES public.customer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.car
    OWNER to postgres;




CREATE TABLE IF NOT EXISTS public.maintenance
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    date timestamp without time zone NOT NULL,
    description character varying COLLATE pg_catalog."default" NOT NULL,
    car_id integer NOT NULL,
    CONSTRAINT maintenance_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car FOREIGN KEY (car_id)
        REFERENCES public.car (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.maintenance
    OWNER to postgres;