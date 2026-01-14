-- Subservices fields
alter table subservices
    add column title text,
    add column description text,
    add column work_hours text,
    add column average_price integer;

-- Example fields and image storage as text
alter table service_type_examples
    add column title text,
    add column description text,
    add column price integer;

alter table service_type_examples
    alter column image type text;
