-- Inserindo Alunos (Exemplos de Kendo/Kenjutsu)
INSERT INTO alunos (nome, nome_zekken, data_cadastro, graduacao) VALUES ('Mateus Costa', 'M.COSTA', '2022-01-10', '1Dan');
INSERT INTO alunos (nome, nome_zekken, data_cadastro, graduacao) VALUES ('Luís Silva', 'L.Siva', '2021-02-15', '2DAN');
INSERT INTO alunos (nome, nome_zekken, data_cadastro, graduacao) VALUES ('Roberto Miranda', 'R.KISHIKAWA', '1998-03-01', '6Dan');
INSERT INTO alunos (nome, nome_zekken, data_cadastro, graduacao) VALUES ('Miguel Barbosa', 'M.BARBOSA', '2025-05-01', '2Kyu');

-- Inserindo Treinos
INSERT INTO treinos (data, descricao_treino) VALUES ('2026-03-20', 'Treino de Kihon e Men-Uchi');
INSERT INTO treinos (data, descricao_treino) VALUES ('2026-03-22', 'Treino de Waza e Ji-Geiko');
INSERT INTO treinos (data, descricao_treino) VALUES ('2026-03-24', 'Katas de Kendo');

-- Associando Alunos aos Treinos (Tabela de Presença)
-- Mateus e Tainara no primeiro treino
INSERT INTO presencas_treino (treino_id, aluno_id) VALUES (1, 1);
INSERT INTO presencas_treino (treino_id, aluno_id) VALUES (1, 2);

-- Todos no segundo treino
INSERT INTO presencas_treino (treino_id, aluno_id) VALUES (2, 1);
INSERT INTO presencas_treino (treino_id, aluno_id) VALUES (2, 2);
INSERT INTO presencas_treino (treino_id, aluno_id) VALUES (2, 3);

-- Apenas Mateus no terceiro treino
INSERT INTO presencas_treino (treino_id, aluno_id) VALUES (3, 1);
