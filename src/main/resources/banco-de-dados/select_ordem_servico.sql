###########################
#PRIMEIRO SELECT DAS ORDENS
###########################
SELECT i.valor_servico,
       s.descricao
       FROM item_os i
                INNER JOIN servico s ON i.id_servico = s.id
       WHERE i.id_ordemservico = $P{SUB_ID_OS};

###########################
#sEGUNDO SELECT DAS ORDENS
###########################
SELECT os.id AS os_id,
       os.numero,
       os.agenda,
       os.total,
       os.desconto,
       os.status,
       v.placa,
       m.descricao AS modelo,
       c.nome AS cliente
       FROM ordem_servico os
                INNER JOIN veiculo v ON os.id_veiculo = v.id
                INNER JOIN modelo m ON v.id_modelo = m.id
                INNER JOIN cliente c ON v.id_cliente = c.id
       WHERE os.id = $P{PAR_ID_OS};