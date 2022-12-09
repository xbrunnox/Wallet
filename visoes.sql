DROP VIEW assinatura_expert_view ;
CREATE VIEW assinatura_expert_view 
(id, id_assinatura, conta, expert, ativo, volume, data_vencimento, ativado)
AS
SELECT ase.id, ass.id, ass.conta, ase.expert, exper.ativo, ase.volume, ass.data_vencimento, ase.ativado 
FROM assinatura ass, assinatura_expert ase, robo exper
WHERE ase.id_assinatura = ass.id AND ase.expert = exper.id;

CREATE VIEW conta_resultado_view (id, nome, corretora, resultado)
AS 
SELECT ct.id, ct.nome, ct.corretora, tr.resultado
FROM conta ct LEFT JOIN trade_resultado_view tr ON ct.id = tr.conta;

CREATE VIEW assinatura_pendencia_view 
(id, conta, nome, corretora, data_vencimento, observacao) AS
SELECT ass.id, ass.conta, cont.nome, cont.corretora, ass.data_vencimento, ass.observacao
FROM assinatura ass, conta cont
where ass.conta = cont.id AND ass.id not in (select t.id_assinatura from assinatura_pagamento t);

CREATE VIEW assinatura_ativa_view 
(id, conta, nome, corretora, data_vencimento, observacao) AS
SELECT ass.id, ass.conta, cont.nome, cont.corretora, ass.data_vencimento, ass.observacao
FROM assinatura ass, conta cont
where ass.conta = cont.id AND ass.id in (select t.id_assinatura from assinatura_pagamento t);