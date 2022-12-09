package br.app.grid.wallet.kiwify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KiwifyCommissions {
	
	private Double product_base_price;
	private Double kiwify_fee;

}
