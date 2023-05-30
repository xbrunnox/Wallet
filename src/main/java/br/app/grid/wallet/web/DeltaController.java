package br.app.grid.wallet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.app.grid.wallet.delta.Delta;
import br.app.grid.wallet.delta.DeltaResumo;
import br.app.grid.wallet.delta.service.OldDeltaService;

@Controller
@RequestMapping("/delta")
public class DeltaController {

	@Autowired
	private OldDeltaService deltaService;

	@PostMapping("/registrar")
	public @ResponseBody Delta registrar(@RequestBody Delta delta) {
		return deltaService.save(delta);
	}

	@GetMapping("/listar")
	public @ResponseBody List<Delta> listar() {
		return deltaService.getDelta(15);
	}
	
	@GetMapping("/resumo")
	public @ResponseBody DeltaResumo resumo() {
		return deltaService.getResumo(15);
	}

}
