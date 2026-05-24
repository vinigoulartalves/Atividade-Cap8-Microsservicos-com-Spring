CREATE SEQUENCE usuarios_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE usuarios (
    id NUMBER(19) NOT NULL,
    nome VARCHAR2(120) NOT NULL,
    email VARCHAR2(160) NOT NULL,
    senha VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) DEFAULT 'USER' NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT ck_usuarios_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE SEQUENCE denuncias_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE denuncias (
    id NUMBER(19) NOT NULL,
    titulo VARCHAR2(120) NOT NULL,
    descricao VARCHAR2(2000) NOT NULL,
    endereco VARCHAR2(200) NOT NULL,
    bairro VARCHAR2(100) NOT NULL,
    cidade VARCHAR2(100) NOT NULL,
    estado VARCHAR2(2) NOT NULL,
    latitude NUMBER(10, 7) NOT NULL,
    longitude NUMBER(10, 7) NOT NULL,
    categoria VARCHAR2(30) NOT NULL,
    status VARCHAR2(30) DEFAULT 'ABERTA' NOT NULL,
    usuario_id NUMBER(19) NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_denuncias PRIMARY KEY (id),
    CONSTRAINT fk_denuncias_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT ck_denuncias_categoria CHECK (
        categoria IN ('ENTULHO', 'MADEIRA', 'METAL', 'PLASTICO', 'GESSO', 'CONCRETO', 'SOLO', 'OUTROS')
    ),
    CONSTRAINT ck_denuncias_status CHECK (status IN ('ABERTA', 'EM_ANALISE', 'RESOLVIDA', 'CANCELADA')),
    CONSTRAINT ck_denuncias_estado CHECK (estado IN (
        'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG',
        'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
    )),
    CONSTRAINT ck_denuncias_latitude CHECK (latitude BETWEEN -90 AND 90),
    CONSTRAINT ck_denuncias_longitude CHECK (longitude BETWEEN -180 AND 180)
);

CREATE INDEX idx_denuncias_usuario ON denuncias (usuario_id);
CREATE INDEX idx_denuncias_status ON denuncias (status);
