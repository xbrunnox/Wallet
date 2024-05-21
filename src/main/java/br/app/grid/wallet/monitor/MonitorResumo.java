package br.app.grid.wallet.monitor;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import br.app.grid.wallet.client.ExpertUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitorResumo {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime horario;

	private int usuariosOnline;

	private int expertsOnline;

	private List<MonitorResumoCorretora> corretoras;

	private List<MonitorResumoServidor> servidores;
	
	private List<ExpertUser> experts;

}
