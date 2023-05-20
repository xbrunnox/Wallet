CREATE VIEW backtest_operacao_resultado_mes_view 
(id, ano, mes, resultado, operacoes)
AS SELECT bo.id_backtest, YEAR(bo.data_saida), MONTH(bo.data_saida), SUM(lucro), count(*)
FROM backtest_operacao bo
GROUP BY YEAR(bo.data_saida), MONTH(bo.data_saida), id_backtest 
ORDER BY YEAR(bo.data_saida), MONTH(bo.data_saida);


CREATE VIEW backtest_operacao_resultado_dia_view 
(id, data, resultado, operacoes)
AS
SELECT bo.id_backtest, DATE(bo.data_saida), SUM(lucro), count(*)
FROM backtest_operacao bo
GROUP BY DATE(bo.data_saida), id_backtest 
ORDER BY DATE(bo.data_saida);

DROP VIEW backtest_operacao_view;
CREATE VIEW backtest_operacao_view (id, id_backtest, volume, direcao, data_entrada, data_hora_entrada, data_saida, data_hora_saida, duracao, preco_entrada, preco_saida, lucro)
AS
SELECT id, id_backtest, volume, direcao, DATE(data_entrada), data_entrada, DATE(data_saida), data_saida, duracao, preco_entrada, preco_saida, lucro
FROM backtest_operacao bo ORDER BY data_entrada;

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

DROP VIEW trade_view;
CREATE VIEW trade_view (id, conta, nome, corretora, servidor, expert, ativo, direcao, volume, compra, venda, pontos, data_entrada, data_saida, duracao, resultado)
AS SELECT tr.id, tr.conta, ct.nome, ct.corretora, serv.nome, tr.expert, tr.ativo, tr.direcao, tr.volume, tr.compra, tr.venda, tr.pontos, tr.data_entrada, tr.data_saida, tr.duracao, tr.resultado
FROM trade tr, conta ct, assinatura ass LEFT JOIN servidor serv ON ass.servidor = serv.id
WHERE tr.conta = ct.id AND tr.conta = ass.conta

DROP VIEW assinatura_view;
CREATE VIEW assinatura_view
(id, conta, nome, corretora, servidor, maquina, data_vencimento)
AS
SELECT ass.id, ass.conta, cont.nome, cont.corretora, serv.nome, maq.nome, ass.data_vencimento
FROM assinatura ass LEFT JOIN maquina maq ON ass.maquina = maq.id LEFT JOIN servidor serv ON ass.servidor = serv.id , 
conta cont
WHERE ass.conta = cont.id;

DROP VIEW trade_resultado_dia_view;
CREATE VIEW trade_resultado_dia_view (fake_id, data, expert, resultado) 
AS
SELECT ROW_NUMBER() OVER (ORDER BY DATE(tr.data_saida)), DATE(tr.data_saida), tr.expert, SUM(tr.resultado)
FROM trade tr
GROUP BY DATE(tr.data_saida), tr.expert