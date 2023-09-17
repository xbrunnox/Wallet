package br.app.grid.wallet.estrategia;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstrategiaFechamentoItem {
	
	private String codigo;
	
	private String ativo;
	
	private int acertos;
	
	private int erros;
	
	private BigDecimal gatilho;
	
	private BigDecimal resultado;
	
	private BigDecimal resultadoPercentual;
	
	private BigDecimal volumeNegociado;

}
