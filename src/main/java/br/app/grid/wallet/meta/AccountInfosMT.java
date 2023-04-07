package br.app.grid.wallet.meta;

import java.util.List;

import br.app.grid.wallet.commons.AccountInfoMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfosMT {
	
	private List<AccountInfoMeta> infos;
	
}
