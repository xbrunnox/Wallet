package br.app.grid.wallet;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import br.app.grid.wallet.socket.DispatcherServer;

@SpringBootApplication(scanBasePackages = "br.app.grid")
@ComponentScan({ "br.app.grid.*" })
public class WalletApplication {
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(WalletApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
//		DispatcherServer.getInstance().aguardarConexao();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
