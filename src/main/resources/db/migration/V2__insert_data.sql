-- 1. INSERINDO 15 USUÁRIOS NO TOTAL (5 de cada Role)
-- Hash para senha '123' (ou a que você validou no cadastro)
-- Admin
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('admin1@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'ADMIN');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('admin2@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'ADMIN');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('admin3@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'ADMIN');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('admin4@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'ADMIN');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('admin5@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'ADMIN');

-- Colaboradores
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('medico1@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'COLABORADOR');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('medico2@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'COLABORADOR');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('recepcao1@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'COLABORADOR');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('medico3@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'COLABORADOR');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('recepcao2@medix.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'COLABORADOR');

-- Pacientes
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('paciente1@gmail.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'PACIENTE');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('paciente2@gmail.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'PACIENTE');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('paciente3@gmail.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'PACIENTE');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('paciente4@gmail.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'PACIENTE');
INSERT INTO TB_USUARIO (email, senha, role) VALUES ('paciente5@gmail.com', '$2a$10$1nryMwcgH4ev6oV9Pp9yHeNdNt5NUkgu0tRgXHb5Zzq.xHPfyj4bm', 'PACIENTE');


-- 2. TABELA FILHA: TB_ADMIN
INSERT INTO TB_ADMIN (usuario_id) VALUES (1);
INSERT INTO TB_ADMIN (usuario_id) VALUES (2);
INSERT INTO TB_ADMIN (usuario_id) VALUES (3);
INSERT INTO TB_ADMIN (usuario_id) VALUES (4);
INSERT INTO TB_ADMIN (usuario_id) VALUES (5);


-- 3. TABELA FILHA: TB_COLABORADOR
INSERT INTO TB_COLABORADOR (usuario_id, nome, cpf, tipo_colaborador) VALUES (6, 'Dr. Mateus Lima', '111.222.333-44', 'OPERACIONAL_MEDICO');
INSERT INTO TB_COLABORADOR (usuario_id, nome, cpf, tipo_colaborador) VALUES (7, 'Dra. Ana Costa', '222.333.444-55', 'OPERACIONAL_MEDICO');
INSERT INTO TB_COLABORADOR (usuario_id, nome, cpf, tipo_colaborador) VALUES (8, 'Carlos Souza', '333.444.555-66', 'ADMINISTRATIVO');
INSERT INTO TB_COLABORADOR (usuario_id, nome, cpf, tipo_colaborador) VALUES (9, 'Dr. Ricardo Oliveira', '444.555.666-77', 'OPERACIONAL_MEDICO');
INSERT INTO TB_COLABORADOR (usuario_id, nome, cpf, tipo_colaborador) VALUES (10, 'Mariana Santos', '555.666.777-88', 'ADMINISTRATIVO');


-- 4. TABELA FILHA: TB_PACIENTE
INSERT INTO TB_PACIENTE (usuario_id, nome, cpf, tipo_sanguineo, altura) VALUES (11, 'Joao Silva', '123.123.123-11', 'O+', 1.75);
INSERT INTO TB_PACIENTE (usuario_id, nome, cpf, tipo_sanguineo, altura) VALUES (12, 'Maria Oliveira', '234.234.234-22', 'A-', 1.62);
INSERT INTO TB_PACIENTE (usuario_id, nome, cpf, tipo_sanguineo, altura) VALUES (13, 'Pedro Rocha', '345.345.345-33', 'B+', 1.80);
INSERT INTO TB_PACIENTE (usuario_id, nome, cpf, tipo_sanguineo, altura) VALUES (14, 'Juliana Lima', '456.456.456-44', 'AB+', 1.68);
INSERT INTO TB_PACIENTE (usuario_id, nome, cpf, tipo_sanguineo, altura) VALUES (15, 'Lucas Mendes', '567.567.567-55', 'O-', 1.72);

-- 5. OPCIONAL: ALERGIAS (Para deixar o banco rico para o vídeo)
INSERT INTO TB_PACIENTE_ALERGIAS (paciente_id, alergia) VALUES (11, 'Dipirona');
INSERT INTO TB_PACIENTE_ALERGIAS (paciente_id, alergia) VALUES (11, 'Poeira');
INSERT INTO TB_PACIENTE_ALERGIAS (paciente_id, alergia) VALUES (12, 'Lactose');