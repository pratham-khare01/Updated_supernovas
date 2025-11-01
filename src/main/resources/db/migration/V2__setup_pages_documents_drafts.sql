create table file
(
    file_id         uuid      not null primary key default gen_random_uuid(),
    file_name       text      not null,
    creator_user_id uuid,
    foreign key (creator_user_id) references account (user_id),

    uploaded_at     timestamp not null default (now() at time zone 'utc')
);

create type image_mime_type as enum (
    'image/jpeg',
    'image/png',
    'image/gif',
    'image/webp',
    'image/bmp',
    'image/svg+xml',
    'image/tiff'
    );

create table image
(
    file_id   uuid            not null primary key,
    foreign key (file_id) references file (file_id) on delete cascade,

    mime_type image_mime_type not null,
    data      bytea           not null
);

create table resource
(
    file_id     uuid not null primary key,
    foreign key (file_id) references file (file_id) on delete cascade,

    data_object oid  not null
);

create table document
(
    document_id   uuid  not null primary key default gen_random_uuid(),
    document_data jsonb not null
);

create table published_document
(
    document_draft_id uuid not null primary key
);

create table document_draft
(
    document_id       uuid      not null primary key,
    foreign key (document_id) references document (document_id),

    draft_name        text      not null,

    created_date      timestamp not null default (now() at time zone 'utc'),
    updated_date      timestamp not null default (now() at time zone 'utc'),
    check (updated_date >= created_date),

    author_user_id    uuid,
    foreign key (author_user_id) references account (user_id),

    based_on_draft_id uuid,
    foreign key (based_on_draft_id) references published_document (document_draft_id)
);

alter table published_document
    add constraint document_draft_published foreign key (document_draft_id) references document_draft (document_id) on delete cascade;

create table document_image
(
    document_id uuid not null,
    foreign key (document_id) references document (document_id),

    image_id    uuid not null,
    foreign key (image_id) references image (file_id),
    primary key (document_id, image_id)
);

create table page_draft
(
    document_id uuid not null primary key,
    foreign key (document_id) references document_draft (document_id) on delete cascade,
    page_route  text not null
);

create table simple_document_draft
(
    document_id          uuid not null primary key,
    foreign key (document_id) references document_draft (document_id) on delete cascade,
    document_entity_name varchar(64)
);

create index idx_page_draft_page_route on page_draft (page_route);
create index idx_document_last_modified on document_draft (updated_date desc);
create index idx_document_author on document_draft (author_user_id);
create index idx_account_email_address on account (email_address);

create view page as
select document_draft.*,
       page_draft.page_route
from page_draft
         join document_draft on page_draft.document_id = document_draft.document_id
         join published_document on published_document.document_draft_id = document_draft.document_id
         join (select page_draft.page_route, max(document_draft.updated_date) as max_updated_date
               from page_draft
                        join document_draft on page_draft.document_id = document_draft.document_id
                        join published_document on document_draft.document_id = published_document.document_draft_id
               group by page_draft.page_route) latest_page_draft on
    latest_page_draft.max_updated_date = document_draft.updated_date and
    latest_page_draft.page_route = page_draft.page_route
;

create view true_page_draft as
select document_draft.*,
       page_draft.page_route
from page_draft
         join document_draft on page_draft.document_id = document_draft.document_id
         left join published_document on page_draft.document_id = published_document.document_draft_id
where published_document.document_draft_id is null;

create view simple_document as
select document_draft.*,
       simple_document_draft.document_entity_name
from simple_document_draft
         join document_draft on simple_document_draft.document_id = document_draft.document_id
         join published_document on published_document.document_draft_id = document_draft.document_id
         join (select simple_document_draft.document_entity_name, max(document_draft.updated_date) as max_updated_date
               from simple_document_draft
                        join document_draft on simple_document_draft.document_id = document_draft.document_id
                        join published_document on document_draft.document_id = published_document.document_draft_id
               group by simple_document_draft.document_entity_name) latest_simple_document_draft on
    latest_simple_document_draft.max_updated_date = document_draft.updated_date and
    latest_simple_document_draft.document_entity_name = simple_document_draft.document_entity_name
;

create view true_simple_document_draft as
select document_draft.*,
       simple_document_draft.document_entity_name
from simple_document_draft
         join document_draft on simple_document_draft.document_id = document_draft.document_id
         left join published_document on document_draft.document_id = published_document.document_draft_id
where published_document.document_draft_id is null;

alter table account alter column registered_date set default (now() at time zone 'utc');
alter table session alter column created_date set default (now() at time zone 'utc');