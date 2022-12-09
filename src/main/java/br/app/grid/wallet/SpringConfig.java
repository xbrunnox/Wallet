package br.app.grid.wallet;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.app.grid.wallet.client.EndpointService;
import br.app.grid.wallet.client.EndpointStatusResponse;
import br.app.grid.wallet.log.LogOnline;
import br.app.grid.wallet.log.LogOnlineService;

@Configuration
@EnableScheduling
public class SpringConfig {

	@Autowired
	private LogOnlineService logOnlineService;

	@Autowired
	private EndpointService endpointService;

	@Scheduled(cron = "0 0 * * * ?")
	@Scheduled(cron = "0 5 * * * ?")
	@Scheduled(cron = "0 10 * * * ?")
	@Scheduled(cron = "0 15 * * * ?")
	@Scheduled(cron = "0 20 * * * ?")
	@Scheduled(cron = "0 25 * * * ?")
	@Scheduled(cron = "0 30 * * * ?")
	@Scheduled(cron = "0 35 * * * ?")
	@Scheduled(cron = "0 40 * * * ?")
	@Scheduled(cron = "0 45 * * * ?")
	@Scheduled(cron = "0 50 * * * ?")
	@Scheduled(cron = "0 55 * * * ?")
	public void scheduleFixedRateTask() {
		LocalDateTime data = LocalDateTime.now();
		try {
			EndpointStatusResponse status = endpointService.getStatus();
			if (status != null)
				logOnlineService.gravar(LogOnline.builder().horario(data).experts(status.getOnlineExperts().size())
						.usuarios(status.getOnlineUsers().size()).build());
		} catch (Exception e) {

		}
	}

}
