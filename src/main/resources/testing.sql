-- Disable FK
ALTER TABLE public.admin
    DISABLE TRIGGER ALL;
ALTER TABLE public.booking
    DISABLE TRIGGER ALL;
ALTER TABLE public.booking_address
    DISABLE TRIGGER ALL;
ALTER TABLE public.booking_invoice
    DISABLE TRIGGER ALL;
ALTER TABLE public.booking_reject
    DISABLE TRIGGER ALL;
ALTER TABLE public.booking_topic
    DISABLE TRIGGER ALL;
ALTER TABLE public.student
    DISABLE TRIGGER ALL;
ALTER TABLE public.student_address
    DISABLE TRIGGER ALL;
ALTER TABLE public.student_phone
    DISABLE TRIGGER ALL;
ALTER TABLE public.subject
    DISABLE TRIGGER ALL;
ALTER TABLE public.subject_topic
    DISABLE TRIGGER ALL;
ALTER TABLE public.tutor
    DISABLE TRIGGER ALL;
ALTER TABLE public.tutor_address
    DISABLE TRIGGER ALL;
ALTER TABLE public.tutor_phone
    DISABLE TRIGGER ALL;
ALTER TABLE public.tutor_qualification
    DISABLE TRIGGER ALL;