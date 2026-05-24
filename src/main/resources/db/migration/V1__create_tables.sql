-- =====================================================================
-- EcoDenuncia API - Estrutura inicial do banco (Oracle)
-- Tabelas: TB_USUARIO, TB_CATEGORIA_RESIDUO, TB_DENUNCIA
-- =====================================================================

-- ---------------------------------------------------------------------
-- Tabela de usuarios
-- ---------------------------------------------------------------------
CREATE TABLE TB_USUARIO (
    ID_USUARIO   NUMBER(19)        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NM_USUARIO   VARCHAR2(120)     NOT NULL,
    DS_EMAIL     VARCHAR2(150)     NOT NULL,
    DS_SENHA     VARCHAR2(255)     NOT NULL,
    DS_ROLE      VARCHAR2(20)      DEFAULT 'USER' NOT NULL,
    CONSTRAINT UK_USUARIO_EMAIL UNIQUE (DS_EMAIL),
    CONSTRAINT CK_USUARIO_ROLE  CHECK (DS_ROLE IN ('USER', 'ADMIN'))
);

CREATE INDEX IX_USUARIO_EMAIL ON TB_USUARIO (DS_EMAIL);

-- ---------------------------------------------------------------------
-- Tabela de categorias de residuo
-- ---------------------------------------------------------------------
CREATE TABLE TB_CATEGORIA_RESIDUO (
    ID_CATEGORIA   NUMBER(19)      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NM_CATEGORIA   VARCHAR2(80)    NOT NULL,
    DS_CATEGORIA   VARCHAR2(255)   NOT NULL,
    CONSTRAINT UK_CATEGORIA_NOME UNIQUE (NM_CATEGORIA)
);

-- ---------------------------------------------------------------------
-- Tabela de denuncias
-- ---------------------------------------------------------------------
CREATE TABLE TB_DENUNCIA (
    ID_DENUNCIA     NUMBER(19)        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NM_TITULO       VARCHAR2(150)     NOT NULL,
    DS_DESCRICAO    VARCHAR2(2000)    NOT NULL,
    DS_ENDERECO     VARCHAR2(200)     NOT NULL,
    NM_BAIRRO       VARCHAR2(100)     NOT NULL,
    NM_CIDADE       VARCHAR2(100)     NOT NULL,
    SG_ESTADO       VARCHAR2(2)       NOT NULL,
    VL_LATITUDE     NUMBER(10,7)      NOT NULL,
    VL_LONGITUDE    NUMBER(10,7)      NOT NULL,
    DS_STATUS       VARCHAR2(20)      DEFAULT 'ABERTA' NOT NULL,
    DT_CRIACAO      TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ID_USUARIO      NUMBER(19)        NOT NULL,
    ID_CATEGORIA    NUMBER(19)        NOT NULL,
    CONSTRAINT FK_DENUNCIA_USUARIO   FOREIGN KEY (ID_USUARIO)   REFERENCES TB_USUARIO (ID_USUARIO),
    CONSTRAINT FK_DENUNCIA_CATEGORIA FOREIGN KEY (ID_CATEGORIA) REFERENCES TB_CATEGORIA_RESIDUO (ID_CATEGORIA),
    CONSTRAINT CK_DENUNCIA_STATUS    CHECK (DS_STATUS IN ('ABERTA', 'EM_ANALISE', 'RESOLVIDA', 'CANCELADA'))
);

CREATE INDEX IX_DENUNCIA_USUARIO   ON TB_DENUNCIA (ID_USUARIO);
CREATE INDEX IX_DENUNCIA_CATEGORIA ON TB_DENUNCIA (ID_CATEGORIA);
CREATE INDEX IX_DENUNCIA_STATUS    ON TB_DENUNCIA (DS_STATUS);
CREATE INDEX IX_DENUNCIA_CIDADE    ON TB_DENUNCIA (NM_CIDADE);
