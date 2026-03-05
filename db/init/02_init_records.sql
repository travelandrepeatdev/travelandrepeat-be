-- RECORDS ------------------------------------------
insert into tyrdb.public.users(user_id, email, hashed_password, is_active, last_login, display_name, avatar_url, created_at, updated_at)
values ('96ff51f7-b7c7-46e1-a767-0d7f8ae559b0', 'travelandrepeatdev@gmail.com', '$2a$10$l9IcGkL85DDWerCBZdEFJuLVWMoG.A92e9Y2DSNO8F9IMCMS2l03e', true, null, 'ChecoAlfa', null, now(), now());

insert into tyrdb.public.roles (role_id, name, description, created_at)
values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'ADMIN', 'Rol acceso administrador con todos los permisos y roles', now());

insert into tyrdb.public.user_roles (user_id, role_id, assigned_at)
values ('96ff51f7-b7c7-46e1-a767-0d7f8ae559b0', '5775047b-fd11-4f8e-8bb9-78df36e0960b', now());

insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('24319b6f-aba4-4aee-ac06-3ba6785b3ae5', 'DASHBOARD_STATS', 'Permiso para visualizar las estadisticas del menu principal', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('26d55de9-add9-4db6-b115-68cbcf8700c3', 'BLOG_DELETE', 'Permiso para borrar blogs', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('34c66215-9bd8-4d93-9bce-5a5f853e3d85', 'BLOG_UPDATE', 'Permiso para actualizar blogs', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('37d5d159-f66d-4c40-9b2f-87924f147452', 'BLOG_CREATE', 'Permiso para crear blogs', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('45c18936-cc2c-4f95-854c-6be2bc6c116c', 'CLIENT_DELETE', 'Permiso para borrar clientes', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('5665fdaa-085f-4152-b66c-203ca9d424cc', 'CLIENT_UPDATE', 'Permiso para actualizar clientes', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('5e6306f1-b79d-4ddb-9aa4-bff24ba7f28e', 'CLIENT_CREATE', 'Permiso para crear clientes', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('5fc00748-8ba5-40df-bf4a-4d0de1f39494', 'PROMOTION_DELETE', 'Permiso para borrar promociones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('6804c1fd-aa6a-4325-a5a5-6e3b4affba0e', 'PROMOTION_UPDATE', 'Permiso para actualizar promociones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('78ec4509-4886-420e-8242-1b48a3750963', 'PROMOTION_CREATE', 'Permiso para crear promociones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('96465dbc-bea1-4c44-a9e2-7049ed34cbbe', 'PROMOTION_ENABLE_DISABLE', 'Permiso para habilitar/deshabilitar promociones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('9a1f223c-0980-4e12-a440-372334a8455f', 'PROVIDER_DELETE', 'Permiso para borrar proveedores', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('ace4b2e9-950e-4a9d-afa3-7751b5f4b478', 'PROVIDER_UPDATE', 'Permiso para actualizar proveedores', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('b12f7c16-b768-48b3-879d-03cc9a7e6840', 'PROVIDER_CREATE', 'Permiso para crear proveedores', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('b691f60a-3b00-4642-8b7f-61287e080ada', 'MODULE_DASHBOARD', 'Permiso para visualizar el modulo principal', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('b69b7ef3-c8ba-45c5-83cf-834e1dfe6e48', 'MODULE_CLIENTES', 'Permiso para visualizar el modulo de clientes', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('b92c1710-9f94-4ef6-b9bf-4f1428833eff', 'MODULE_PROVEEDORES', 'Permiso para visualizar el modulo de proveedores', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('c2e758e6-c28c-4b4f-8c7f-bb1956bd0476', 'MODULE_COMISIONES', 'Permiso para visualizar el modulo de comisiones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('d2c895e0-47e3-4879-a8d3-9e83829c19cd', 'MODULE_GASTOS', 'Permiso para visualizar el modulo de gastos', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('d44bfc27-607c-486f-b514-18c5c1a2dcd7', 'MODULE_PROMOCIONES', 'Permiso para visualizar el modulo de promociones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('d7022740-54ed-4084-ac43-7b7786bf4a82', 'MODULE_BLOGS', 'Permiso para visualizar el modulo de blogs', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('d764f986-8cf9-4e42-b013-72a3fea9f4ac', 'MODULE_USUARIOS', 'Permiso para visualizar el modulo de usuarios', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('eef2c43d-67d0-407e-aba6-0ffa21dca5af', 'MODULE_ROLES', 'Permiso para visualizar el modulo de roles', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('fc9e938f-2f0a-4d4b-928a-fa4f5773945d', 'MODULE_PERMISOS', 'Permiso para visualizar el modulo de permisos', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('755e8a04-56a1-4986-be44-e2744ed41441', 'MODULE_AUDITORIA', 'Permiso para visualizar el modulo de auditoria', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('2e597f7f-932d-4ffd-baa2-5f47e9e7879b', 'CLIENT_READ', 'Permiso para visualizar clientes', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('9b89118a-df27-447b-a0c4-a5595c5a2a68', 'BLOG_READ', 'Permiso para visualizar blogs', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('2f01a70b-27db-4705-9129-f7deca32d43e', 'PROMOTION_READ', 'Permiso para visualizar promociones', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('e1c7523c-585c-4d76-93bc-73efd1ae1501', 'PROVIDER_READ', 'Permiso para visualizar proveedores', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('9fa38e62-1a08-4964-bb50-b087835f965a', 'DASHBOARD_FINANCIAL', 'Permiso para visualizar datos finacieros del menu principal', now());
insert into tyrdb.public.permissions (permission_id, name, description, created_at) values ('656851f3-104e-45d4-ae26-bb3c2928483e', 'DASHBOARD_ACTIVITY', 'Permiso para visualizar las actividades del menu principal', now());

insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '24319b6f-aba4-4aee-ac06-3ba6785b3ae5');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '26d55de9-add9-4db6-b115-68cbcf8700c3');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '34c66215-9bd8-4d93-9bce-5a5f853e3d85');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '37d5d159-f66d-4c40-9b2f-87924f147452');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '45c18936-cc2c-4f95-854c-6be2bc6c116c');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '5665fdaa-085f-4152-b66c-203ca9d424cc');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '5e6306f1-b79d-4ddb-9aa4-bff24ba7f28e');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '5fc00748-8ba5-40df-bf4a-4d0de1f39494');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '6804c1fd-aa6a-4325-a5a5-6e3b4affba0e');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '78ec4509-4886-420e-8242-1b48a3750963');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '96465dbc-bea1-4c44-a9e2-7049ed34cbbe');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '9a1f223c-0980-4e12-a440-372334a8455f');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'ace4b2e9-950e-4a9d-afa3-7751b5f4b478');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'b12f7c16-b768-48b3-879d-03cc9a7e6840');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'b691f60a-3b00-4642-8b7f-61287e080ada');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'b69b7ef3-c8ba-45c5-83cf-834e1dfe6e48');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'b92c1710-9f94-4ef6-b9bf-4f1428833eff');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'c2e758e6-c28c-4b4f-8c7f-bb1956bd0476');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'd2c895e0-47e3-4879-a8d3-9e83829c19cd');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'd44bfc27-607c-486f-b514-18c5c1a2dcd7');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'd7022740-54ed-4084-ac43-7b7786bf4a82');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'd764f986-8cf9-4e42-b013-72a3fea9f4ac');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'eef2c43d-67d0-407e-aba6-0ffa21dca5af');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'fc9e938f-2f0a-4d4b-928a-fa4f5773945d');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '755e8a04-56a1-4986-be44-e2744ed41441');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '2e597f7f-932d-4ffd-baa2-5f47e9e7879b');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '9b89118a-df27-447b-a0c4-a5595c5a2a68');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '2f01a70b-27db-4705-9129-f7deca32d43e');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', 'e1c7523c-585c-4d76-93bc-73efd1ae1501');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '9fa38e62-1a08-4964-bb50-b087835f965a');
insert into tyrdb.public.role_permissions (role_id, permission_id) values ('5775047b-fd11-4f8e-8bb9-78df36e0960b', '656851f3-104e-45d4-ae26-bb3c2928483e');