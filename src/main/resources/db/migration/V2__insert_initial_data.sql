-- =====================================================================
-- EcoDenuncia API - Dados iniciais (Oracle)
-- - Categorias de residuo da construcao civil
-- - Usuario administrador padrao (senha BCrypt: admin123)
-- =====================================================================

-- ---------------------------------------------------------------------
-- Categorias de residuo
-- ---------------------------------------------------------------------
INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('CONCRETO', 'Restos de concreto, argamassa e cimento provenientes de demolicoes e obras');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('MADEIRA', 'Sobras de madeira de formas, escoramentos, tabuas e moveis de obra');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('METAL', 'Sucatas metalicas, vergalhoes, ferragens e estruturas de aco');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('GESSO', 'Placas, blocos e residuos de gesso e drywall');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('CERAMICA', 'Pisos, azulejos, telhas, tijolos e blocos ceramicos');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('PLASTICO', 'Tubulacoes, conexoes, embalagens e demais plasticos de construcao');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('TINTAS_E_SOLVENTES', 'Latas de tinta, vernizes, solventes e outros produtos quimicos perigosos');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('MISTO', 'Entulho misto contendo diferentes tipos de residuos da construcao civil');

INSERT INTO TB_CATEGORIA_RESIDUO (NM_CATEGORIA, DS_CATEGORIA)
VALUES ('OUTROS', 'Demais residuos da construcao civil nao enquadrados nas categorias acima');

-- ---------------------------------------------------------------------
-- Usuario administrador padrao
-- Email...: admin@ecodenuncia.com
-- Senha...: admin123  (hash BCrypt abaixo)
-- ---------------------------------------------------------------------
INSERT INTO TB_USUARIO (NM_USUARIO, DS_EMAIL, DS_SENHA, DS_ROLE)
VALUES (
    'Administrador',
    'admin@ecodenuncia.com',
    '$2a$10$XnPhEHOTwMGpddihT3tWxed35hZOXJAFLflKbf94SNe5oCtoTXkkK',
    'ADMIN'
);
