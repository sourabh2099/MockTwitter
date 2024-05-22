CREATE EXTENSION IF NOT EXISTS "uuid-ossp";



INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('8b0b7849-bbe2-471e-be94-4983a71d1076', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('f0e529f4-ef9a-4855-9e42-e7e02f8ac0d0', 'app_admin', 'Admin', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('852d00bc-e839-47e7-953a-5e1c73e4a82a', 'app_super_user', 'Super', 'User');

insert into documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 1);
insert into documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', 2);
insert into documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', 3);

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'8b0b7849-bbe2-471e-be94-4983a71d1076', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'f0e529f4-ef9a-4855-9e42-e7e02f8ac0d0', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'f0e529f4-ef9a-4855-9e42-e7e02f8ac0d0', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), 'df0e529f4-ef9a-4855-9e42-e7e02f8ac0d0', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '3852d00bc-e839-47e7-953a-5e1c73e4a82a', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');


