-- Table: public.admin

-- DROP TABLE public.admin;

CREATE TABLE public.admin
(
    admin_id       bigint                            NOT NULL,
    admin_email    text COLLATE pg_catalog."default" NOT NULL,
    admin_password text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT admin_pk PRIMARY KEY (admin_id),
    CONSTRAINT admin_email_unique UNIQUE (admin_email)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.admin
    OWNER to bookmetutor;

-- Table: public.booking

-- DROP TABLE public.booking;

CREATE TABLE public.booking
(
    booking_id          bigint                            NOT NULL,
    board               text COLLATE pg_catalog."default" NOT NULL,
    cancellation_reason text COLLATE pg_catalog."default",
    class_number        smallint                          NOT NULL,
    rescheduled         boolean                           NOT NULL,
    rescheduling_reason text COLLATE pg_catalog."default",
    comment             text COLLATE pg_catalog."default",
    deadline            timestamp without time zone       NOT NULL,
    scheduled_time      timestamp without time zone       NOT NULL,
    score               smallint                          NOT NULL,
    secret              text COLLATE pg_catalog."default" NOT NULL,
    start_time          timestamp without time zone,
    end_time            timestamp without time zone,
    status              text COLLATE pg_catalog."default" NOT NULL,
    subject             text COLLATE pg_catalog."default" NOT NULL,
    tutor_id            bigint,
    student_id          bigint                            NOT NULL,
    CONSTRAINT booking_pk PRIMARY KEY (booking_id),
    CONSTRAINT student_fk FOREIGN KEY (student_id)
        REFERENCES public.student (student_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT tutor_fk FOREIGN KEY (tutor_id)
        REFERENCES public.tutor (tutor_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.booking
    OWNER to bookmetutor;

-- Table: public.booking_address

-- DROP TABLE public.booking_address;

CREATE TABLE public.booking_address
(
    address_id bigint                            NOT NULL,
    line_1     text COLLATE pg_catalog."default" NOT NULL,
    line_2     text COLLATE pg_catalog."default",
    landmark   text COLLATE pg_catalog."default",
    city       text COLLATE pg_catalog."default" NOT NULL,
    pin_code   text COLLATE pg_catalog."default" NOT NULL,
    booking_id bigint                            NOT NULL,
    CONSTRAINT booking_address_pk PRIMARY KEY (address_id),
    CONSTRAINT booking_address_id_unique UNIQUE (booking_id),
    CONSTRAINT booking_fk FOREIGN KEY (booking_id)
        REFERENCES public.booking (booking_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.booking_address
    OWNER to bookmetutor;

-- Table: public.booking_invoice

-- DROP TABLE public.booking_invoice;

CREATE TABLE public.booking_invoice
(
    invoice_id bigint                            NOT NULL,
    amount     real                              NOT NULL,
    method     text COLLATE pg_catalog."default" NOT NULL,
    summary    text COLLATE pg_catalog."default",
    booking_id bigint                            NOT NULL,
    CONSTRAINT booking_invoice_pk PRIMARY KEY (invoice_id),
    CONSTRAINT booking_invoice_id_unique UNIQUE (booking_id),
    CONSTRAINT booking_fk FOREIGN KEY (booking_id)
        REFERENCES public.booking (booking_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.booking_invoice
    OWNER to bookmetutor;

-- Table: public.booking_reject

-- DROP TABLE public.booking_reject;

CREATE TABLE public.booking_reject
(
    booking_id bigint                            NOT NULL,
    reject     text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT booking_reject_pk PRIMARY KEY (booking_id, reject),
    CONSTRAINT booking_fk FOREIGN KEY (booking_id)
        REFERENCES public.booking (booking_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.booking_reject
    OWNER to bookmetutor;

-- Table: public.booking_topic

-- DROP TABLE public.booking_topic;

CREATE TABLE public.booking_topic
(
    booking_id bigint                            NOT NULL,
    topic      text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT booking_topic_pk PRIMARY KEY (booking_id, topic),
    CONSTRAINT booking_fk FOREIGN KEY (booking_id)
        REFERENCES public.booking (booking_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.booking_topic
    OWNER to bookmetutor;

-- Table: public.student

-- DROP TABLE public.student;

CREATE TABLE public.student
(
    student_id bigint                            NOT NULL,
    email      text COLLATE pg_catalog."default" NOT NULL,
    password   text COLLATE pg_catalog."default" NOT NULL,
    first_name text COLLATE pg_catalog."default" NOT NULL,
    last_name  text COLLATE pg_catalog."default",
    gender     text COLLATE pg_catalog."default" NOT NULL,
    registered timestamp without time zone       NOT NULL,
    verified   boolean                           NOT NULL,
    CONSTRAINT student_pk PRIMARY KEY (student_id),
    CONSTRAINT student_email_unique UNIQUE (email)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.student
    OWNER to bookmetutor;

-- Table: public.student_address

-- DROP TABLE public.student_address;

CREATE TABLE public.student_address
(
    address_id bigint                            NOT NULL,
    line_1     text COLLATE pg_catalog."default" NOT NULL,
    line_2     text COLLATE pg_catalog."default",
    landmark   text COLLATE pg_catalog."default",
    city       text COLLATE pg_catalog."default" NOT NULL,
    pin_code   text COLLATE pg_catalog."default" NOT NULL,
    student_id bigint                            NOT NULL,
    CONSTRAINT student_address_pk PRIMARY KEY (address_id),
    CONSTRAINT student_fk FOREIGN KEY (student_id)
        REFERENCES public.student (student_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.student_address
    OWNER to bookmetutor;

-- Table: public.student_phone

-- DROP TABLE public.student_phone;

CREATE TABLE public.student_phone
(
    student_id bigint                            NOT NULL,
    phone      text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT student_phone_pk PRIMARY KEY (student_id, phone),
    CONSTRAINT student_fk FOREIGN KEY (student_id)
        REFERENCES public.student (student_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.student_phone
    OWNER to bookmetutor;

-- Table: public.subject

-- DROP TABLE public.subject;

CREATE TABLE public.subject
(
    subject_id   bigint                            NOT NULL,
    subject_name text COLLATE pg_catalog."default" NOT NULL,
    class_number smallint                          NOT NULL,
    CONSTRAINT subject_pk PRIMARY KEY (subject_id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.subject
    OWNER to bookmetutor;

-- Table: public.subject_topic

-- DROP TABLE public.subject_topic;

CREATE TABLE public.subject_topic
(
    subject_id bigint                            NOT NULL,
    topic      text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT subject_topic_pk PRIMARY KEY (subject_id, topic),
    CONSTRAINT subject_fk FOREIGN KEY (subject_id)
        REFERENCES public.subject (subject_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.subject_topic
    OWNER to bookmetutor;

-- Table: public.tutor

-- DROP TABLE public.tutor;

CREATE TABLE public.tutor
(
    tutor_id       bigint                            NOT NULL,
    tutor_email    text COLLATE pg_catalog."default" NOT NULL,
    tutor_password text COLLATE pg_catalog."default" NOT NULL,
    gender         text COLLATE pg_catalog."default" NOT NULL,
    last_picked    timestamp without time zone       NOT NULL,
    first_name     text COLLATE pg_catalog."default" NOT NULL,
    last_name      text COLLATE pg_catalog."default",
    registered     date                              NOT NULL,
    screening      text COLLATE pg_catalog."default" NOT NULL,
    verified       boolean                           NOT NULL,
    CONSTRAINT tutor_pk PRIMARY KEY (tutor_id),
    CONSTRAINT tutor_email_unique UNIQUE (tutor_email)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.tutor
    OWNER to bookmetutor;

-- Table: public.tutor_address

-- DROP TABLE public.tutor_address;

CREATE TABLE public.tutor_address
(
    address_id bigint                            NOT NULL,
    line_1     text COLLATE pg_catalog."default" NOT NULL,
    line_2     text COLLATE pg_catalog."default",
    landmark   text COLLATE pg_catalog."default",
    city       text COLLATE pg_catalog."default" NOT NULL,
    pin_code   text COLLATE pg_catalog."default" NOT NULL,
    tutor_id   bigint                            NOT NULL,
    CONSTRAINT tutor_address_pk PRIMARY KEY (address_id),
    CONSTRAINT tutor_address_tutor_id_unique UNIQUE (tutor_id),
    CONSTRAINT tutor_fk FOREIGN KEY (tutor_id)
        REFERENCES public.tutor (tutor_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.tutor_address
    OWNER to bookmetutor;

-- Table: public.tutor_phone

-- DROP TABLE public.tutor_phone;

CREATE TABLE public.tutor_phone
(
    tutor_id bigint                            NOT NULL,
    phone    text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tutor_phone_pk PRIMARY KEY (tutor_id, phone),
    CONSTRAINT tutor_fk FOREIGN KEY (tutor_id)
        REFERENCES public.tutor (tutor_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.tutor_phone
    OWNER to bookmetutor;

-- Table: public.tutor_qualification

-- DROP TABLE public.tutor_qualification;

CREATE TABLE public.tutor_qualification
(
    qualification_id bigint                            NOT NULL,
    degree           text COLLATE pg_catalog."default" NOT NULL,
    university       text COLLATE pg_catalog."default" NOT NULL,
    percentile       real                              NOT NULL,
    tutor_id         bigint                            NOT NULL,
    CONSTRAINT tutor_qualification_pk PRIMARY KEY (qualification_id),
    CONSTRAINT tutor_qualification_tutor_id_unique UNIQUE (tutor_id),
    CONSTRAINT tutor_fk FOREIGN KEY (tutor_id)
        REFERENCES public.tutor (tutor_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.tutor_qualification
    OWNER to bookmetutor;