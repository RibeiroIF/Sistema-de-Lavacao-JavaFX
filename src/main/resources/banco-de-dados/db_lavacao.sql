DROP DATABASE IF EXISTS db_lavacao;
CREATE DATABASE IF NOT EXISTS db_lavacao;
USE db_lavacao;

CREATE TABLE parametros(
    id int not null auto_increment primary key,
    pontos int not null,
    percentual_pequeno double,
    percentual_medio double,
    percentual_grande double,
    percentual_moto double,
    percentual_padrao double
) engine=InnoDB;

INSERT INTO parametros(pontos, percentual_pequeno, percentual_medio, percentual_grande, percentual_moto,
                       percentual_padrao) VALUES(20, 0, 5, 10, 7.5, 12);

CREATE TABLE cliente(
    id INT NOT NULL auto_increment,
    nome VARCHAR(50) NOT NULL,
    celular VARCHAR(20),
    email VARCHAR(100),
    dataCadastro DATE,
    CONSTRAINT pk_cliente PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE pessoa_fisica(
    id_cliente INT NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    dataNascimento DATE,
    CONSTRAINT pk_pessoa_fisica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoa_fisica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE pessoa_juridica(
    id_cliente INT NOT NULL,
    cnpj VARCHAR(20) NOT NULL,
    inscricaoEstadual VARCHAR(20),
    CONSTRAINT pk_pessoa_juridica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoa_juridica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE cor(
    id INT NOT NULL auto_increment,
    nome VARCHAR(20) NOT NULL,
    CONSTRAINT pk_cor PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE marca(
    id INT NOT NULL auto_increment,
    nome VARCHAR(20) NOT NULL,
    CONSTRAINT pk_marca PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE modelo(
    id INT NOT NULL auto_increment,
    descricao VARCHAR(50),
    id_marca INT NOT NULL,
    categoria ENUM('PEQUENO', 'MÉDIO', 'GRANDE', 'MOTO', 'PADRÃO') NOT NULL DEFAULT 'PADRÃO',
    CONSTRAINT pk_modelo PRIMARY KEY(id),
    CONSTRAINT fk_modelo_marca FOREIGN KEY(id_marca) REFERENCES marca(id)
) engine=InnoDB;

CREATE TABLE veiculo(
    id INT NOT NULL auto_increment,
    placa VARCHAR(16) NOT NULL,
    observacoes VARCHAR(100),
    id_cliente INT NOT NULL,
    id_cor INT,
    id_modelo INT,
    CONSTRAINT pk_veiculo PRIMARY KEY(id),
    CONSTRAINT uk_veiculo_placa UNIQUE(placa),
    CONSTRAINT fk_veiculo_cliente FOREIGN KEY(id_cliente) REFERENCES cliente(id) ON DELETE CASCADE,
    CONSTRAINT fk_veiculo_cor FOREIGN KEY(id_cor) REFERENCES cor(id) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY(id_modelo) REFERENCES modelo(id) ON UPDATE CASCADE ON DELETE SET NULL
) engine=InnoDB;


CREATE TABLE motor(
    id_modelo INT NOT NULL,
    potencia INT NOT NULL,
    tipoCombustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL DEFAULT 'GASOLINA',
    CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) engine=InnoDB;

CREATE TABLE servico(
    id int NOT NULL auto_increment,
    descricao varchar(40) NOT NULL,
    valor double NOT NULL,
    categoria ENUM('PEQUENO', 'MÉDIO', 'GRANDE', 'MOTO', 'PADRÃO') NOT NULL DEFAULT 'PADRÃO',
    CONSTRAINT pk_servico PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE ordem_servico(
    id INT NOT NULL auto_increment,
    numero LONG,
    total DOUBLE,
    agenda DATE,
    desconto DOUBLE,
    status ENUM('FECHADA', 'CANCELADA', 'ABERTA') NOT NULL DEFAULT 'ABERTA',
    id_veiculo INT NOT NULL,
    CONSTRAINT pk_ordem_servico PRIMARY KEY(id),
    CONSTRAINT fk_ordemservico_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id)
        ON DELETE CASCADE
) engine=InnoDB;

CREATE TABLE item_os(
    id INT NOT NULL auto_increment,
    valor_servico DOUBLE,
    observacoes VARCHAR(128),
    id_servico INT NOT NULL,
    id_ordemservico INT NOT NULL,
    CONSTRAINT pk_item_os PRIMARY KEY(id),
    CONSTRAINT fk_itemos_servico FOREIGN KEY (id_servico) REFERENCES servico(id),
    CONSTRAINT fk_itemos_ordem_servico FOREIGN KEY (id_ordemservico) REFERENCES ordem_servico(id)
    ON DELETE CASCADE
) engine=InnoDB;

CREATE TABLE pontuacao(
    id_cliente INT NOT NULL,
    quantidade INT DEFAULT 0,
    CONSTRAINT pk_pontuacao PRIMARY KEY(id_cliente),
    CONSTRAINT fk_pontuacao_cliente FOREIGN KEY(id_cliente) REFERENCES cliente(id)
) engine=InnoDB;

#####################
#POVOAÇÃO DAS TABELAS
#####################

INSERT INTO servico (descricao, valor, categoria) VALUES
    ('Lavação Completa', 150.00, 'PEQUENO'),
    ('Lavação interna', 120.00, 'GRANDE'),
    ('Polimento Comercial', 250.00, 'PEQUENO'),
    ('Higienização de Estofados', 180.00, 'MEDIO'),
    ('Lavação de Motor', 90.00, 'PEQUENO'),
    ('Vitrificação de Pintura', 600.00, 'GRANDE'),
    ('Limpeza e Hidratação de Couro', 140.00, 'MEDIO');

INSERT INTO cor (nome) VALUES
    ('Preto'),
    ('Branco'),
    ('Prata'),
    ('Vermelho'),
    ('Azul'),
    ('Chumbo'),
    ('Verde'),
    ('Amarelo'),
    ('Bege');

INSERT INTO marca (nome) VALUES
    ('Chevrolet'),
    ('Fiat'),
    ('Volkswagen'),
    ('Toyota'),
    ('Ford'),
    ('Honda'),
    ('Hyundai'),
    ('Renault');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Onix 1.0', 1,  'PEQUENO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 110, 'FLEX');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Mobi 1.0', 2,  'PADRÃO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 75, 'GASOLINA');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Gol 1.6', 3,  'MÉDIO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 170, 'DIESEL');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Hilux 2.8', 4,  'GRANDE');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 85, 'GNV');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Yamaha 2.0', 2,  'MOTO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 90, 'ETANOL');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Ranger 3.0', 5, 'GRANDE');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 200, 'DIESEL');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Civic 2.0', 6, 'MÉDIO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 155, 'FLEX');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('HB20 1.0', 7, 'PEQUENO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 80, 'FLEX');

INSERT INTO modelo (descricao, id_marca, categoria) VALUES
    ('Kwid 1.0', 8, 'PEQUENO');
INSERT INTO motor (id_modelo, potencia, tipoCombustivel) VALUES
    ((SELECT max(id) FROM modelo), 71, 'GASOLINA');

INSERT INTO cliente (nome, celular, email, dataCadastro) VALUES
    ('Carlos Silva', '(11) 98888-1111', 'carlos@gmail.com', '2026-01-10'),
    ('Ana Oliveira', '(21) 98888-2222', 'ana@gmail.com', '2026-01-15'),
    ('Marcos Pisching', '(31) 98888-3333', 'marcos@gmail.com', '2026-02-01'),
    ('Gabriel Ribeiro', '(11) 4002-8922', 'gabriel@gmail.com', '2026-02-10'),
    ('Jonas Silva', '(11) 4004-1234', 'jonas@gmail.com', '2026-02-20'),
    ('Mariana Costa', '(48) 99111-2233', 'mariana@ifsc.edu.br', '2026-01-20'),
    ('Roberto Almeida', '(48) 99222-3344', 'roberto@outlook.com', '2026-02-15'),
    ('Beatriz Santos', '(48) 99333-4455', 'beatriz@gmail.com', '2026-03-01'),
    ('LavaCar frotas Ltda', '(11) 3322-1100', 'contato@lavacar.com.br', '2026-03-10'),
    ('Oficina Sul S/A', '(48) 3224-5566', 'diretoria@oficinasul.com.br', '2026-04-05');

INSERT INTO pessoa_fisica (id_cliente, cpf, dataNascimento) VALUES
    (1, '111.222.333-44', '1985-05-12'),
    (2, '222.333.444-55', '1992-08-24'),
    (3, '333.444.555-66', '1979-03-02'),
    (6, '444.555.666-77', '1995-10-15'),
    (7, '555.666.777-88', '1988-04-20'),
    (8, '666.777.888-99', '2001-12-05');

INSERT INTO pessoa_juridica (id_cliente, cnpj, inscricaoEstadual) VALUES
    (4, '12.345.678/0001-99', '111.222.333.444'),
    (5, '98.765.432/0001-11', '555.666.777.888'),
    (9, '22.333.444/0001-22', '222.333.444.555'),
    (10, '33.444.555/0001-33', '666.777.888.999');

INSERT INTO pontuacao (quantidade, id_cliente) VALUES
    (100, 1),
    (20, 2),
    (0, 3),
    (160, 4),
    (400, 5),
    (50, 6),
    (15, 7),
    (0, 8),
    (320, 9),
    (500, 10);

INSERT INTO veiculo (placa, observacoes, id_cliente, id_cor, id_modelo) VALUES
    ('ABC-1234', 'Carro de uso diário', 1, 1, 1),
    ('XYZ-9876', 'Carro da esposa', 1, 2, 2),
    ('MNO-4567', 'Apenas para viagens', 2, 3, 3),
    ('DEF-1122', 'Sem observações', 2, 4, 4),
    ('GHI-3344', 'Necessita revisão', 3, 1, 4),
    ('JKL-5566', 'Carro de trabalho', 3, 2, 1),
    ('QWE-7788', 'Carro reserva 1', 4, 3, 5),
    ('RTY-9900', 'Carro de suporte', 4, 4, 3),
    ('UOP-1133', 'Frota Locação A', 5, 2, 5),
    ('VBN-4455', 'Frota Locação B', 5, 1, 2),
    ('IFSC-2026', 'Carro institucional', 6, 6, 7),
    ('ROB-8899', 'Polimento urgente', 7, 5, 6),
    ('BEA-5544', 'Lavação interna apenas', 8, 7, 8),
    ('FRO-0001', 'Carro de teste frota 1', 9, 3, 9),
    ('FRO-0002', 'Carro de teste frota 2', 9, 1, 9),
    ('SUL-9911', 'Veículo guincho leve', 10, 4, 6);

INSERT INTO ordem_servico (numero, total, agenda, desconto, status, id_veiculo) VALUES
    (1001, 150.00, '2026-03-01', 0.00, 'FECHADA', 1),
    (1002, 120.00, '2026-03-02', 10.00, 'FECHADA', 3),
    (1003, 270.00, '2026-03-05', 0.00, 'ABERTA', 4),
    (1004, 150.00, '2026-03-06', 15.00, 'CANCELADA', 5),
    (1005, 120.00, '2026-03-07', 0.00, 'ABERTA', 7),
    (1006, 150.00, '2026-01-15', 0.00, 'FECHADA', 1),   -- Janeiro
    (1007, 250.00, '2026-01-22', 5.00, 'FECHADA', 11),  -- Janeiro
    (1008, 180.00, '2026-02-05', 0.00, 'FECHADA', 2),   -- Fevereiro
    (1009, 600.00, '2026-02-18', 50.00, 'FECHADA', 12), -- Fevereiro
    (1010, 390.00, '2026-04-12', 10.00, 'FECHADA', 3),   -- Abril
    (1011, 140.00, '2026-04-28', 0.00, 'FECHADA', 13),  -- Abril
    (1012, 150.00, '2026-05-03', 0.00, 'FECHADA', 14),  -- Maio
    (1013, 240.00, '2026-05-20', 15.00, 'FECHADA', 6),   -- Maio
    (1014, 180.00, '2026-06-10', 0.00, 'FECHADA', 15),  -- Junho
    (1015, 690.00, '2026-06-25', 20.00, 'FECHADA', 16), -- Junho
    (1016, 150.00, '2026-07-04', 0.00, 'FECHADA', 8),   -- Julho
    (1017, 250.00, '2026-07-19', 0.00, 'FECHADA', 11),  -- Julho
    (1018, 320.00, '2026-08-11', 10.00, 'FECHADA', 4),   -- Agosto
    (1019, 140.00, '2026-08-29', 0.00, 'FECHADA', 13),  -- Agosto
    (1020, 150.00, '2026-09-05', 5.00, 'FECHADA', 9),   -- Setembro
    (1021, 600.00, '2026-09-21', 0.00, 'FECHADA', 12),  -- Setembro
    (1022, 180.00, '2026-10-02', 0.00, 'FECHADA', 2),   -- Outubro
    (1023, 240.00, '2026-10-17', 0.00, 'FECHADA', 10),  -- Outubro
    (1024, 150.00, '2026-11-08', 0.00, 'FECHADA', 14),  -- Novembro
    (1025, 250.00, '2026-11-23', 12.00, 'FECHADA', 11), -- Novembro
    (1026, 740.00, '2026-12-01', 30.00, 'FECHADA', 16), -- Dezembro
    (1027, 120.00, '2026-12-12', 0.00, 'ABERTA', 5),    -- Dezembro
    (1028, 180.00, '2026-12-15', 0.00, 'ABERTA', 15),   -- Dezembro
    (1029, 150.00, '2026-12-20', 0.00, 'FECHADA', 1),   -- Dezembro
    (1030, 270.00, '2026-12-22', 0.00, 'FECHADA', 3);   -- Dezembro

INSERT INTO item_os (valor_servico, observacoes, id_servico, id_ordemservico) VALUES
    (150.00, 'Lavação completa padrão', 1, 1),
    (120.00, 'Focar na limpeza dos bancos traseiros', 2, 2),
    (150.00, 'Lavação externa', 1, 3),
    (120.00, 'Lavação interna detalhada', 2, 3),
    (150.00, 'Cliente desistiu antes de iniciar', 1, 4),
    (120.00, 'Cuidado com os espelhos da moto', 2, 5),
    (150.00, 'Lavação básica', 1, 6),
    (250.00, 'Polimento comercial manual', 3, 7),
    (180.00, 'Remover manchas banco carona', 4, 8),
    (600.00, 'Vitrificação completa', 6, 9),
    (250.00, 'Polimento das laterais', 3, 10),
    (140.00, 'Hidratação bancos dianteiros', 7, 10),
    (140.00, 'Limpeza de couro completa', 7, 11),
    (150.00, 'Lavação Expressa', 1, 12),
    (150.00, 'Lavação externa', 1, 13),
    (90.00, 'Remoção de graxas do motor', 5, 13),
    (180.00, 'Higienização geral interna', 4, 14),
    (600.00, 'Vitrificação premium', 6, 15),
    (90.00, 'Lavagem técnica motor', 5, 15),
    (150.00, 'Lavação pós-viagem', 1, 16),
    (250.00, 'Polimento comercial capeado', 3, 17),
    (180.00, 'Limpeza teto', 4, 18),
    (140.00, 'Tratamento couro traseiro', 7, 18),
    (140.00, 'Hidratação simples', 7, 19),
    (150.00, 'Lavação normal', 1, 20),
    (600.00, 'Vitrificação protetiva', 6, 21),
    (180.00, 'Higienização de ácaros', 4, 22),
    (240.00, 'Polimento comercial em moto', 3, 23),
    (150.00, 'Lavação express', 1, 24),
    (250.00, 'Polimento de brilho', 3, 25),
    (600.00, 'Vitrificação de fim de ano', 6, 26),
    (140.00, 'Hidratação de Couro geral', 7, 26),
    (120.00, 'Apenas interna', 2, 27),
    (180.00, 'Higienização padrão', 4, 28),
    (150.00, 'Lavação simples', 1, 29),
    (150.00, 'Lavação externa simples', 1, 30),
    (120.00, 'Lavação interna comum', 2, 30);
