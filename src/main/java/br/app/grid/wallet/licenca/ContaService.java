package br.app.grid.wallet.licenca;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.app.grid.wallet.afiliado.Afiliado;
import br.app.grid.wallet.assinatura.Assinatura;
import br.app.grid.wallet.assinatura.repository.AssinaturaRepository;
import br.app.grid.wallet.assinatura.service.AssinaturaService;
import br.app.grid.wallet.servidor.Servidor;
import br.app.grid.wallet.usuario.UsuarioUtil;
import br.app.grid.wallet.util.CorretoraUtil;

@Service
public class ContaService {

  @Autowired
  private ContaRepository repository;

  @Autowired
  private AssinaturaRepository assinaturaRepository;

  @Autowired
  private AssinaturaService assinaturaService;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public ContaResponse autenticar(String nome, String corretora, Integer conta) {
    Conta licenca = repository.get(corretora, conta);
    if (licenca == null) {
      licenca = repository.get(CorretoraUtil.format(corretora), conta);
    }
    if (licenca == null) {
      licenca = Conta.builder().conta(conta).corretora(CorretoraUtil.format(corretora)).nome(nome)
          .id(nome.substring(0, 2).toUpperCase() + gerarId(4)).dataDeCadastro(LocalDateTime.now())
          .build();
      repository.save(licenca);
    }
    return autenticar(licenca.getId());
  }

  public ContaResponse autenticar(String licenceKey) {
    List<Assinatura> assinaturas = assinaturaRepository.getListAtivas(licenceKey, LocalDate.now());
    if (assinaturas.size() == 0)
      return ContaResponse.builder().ativo(false).id(licenceKey).build();

    LocalDate maiorVencimento = assinaturas.get(0).getDataVencimento();
    Assinatura assinaturaMaiorVencimento = assinaturas.get(0);
    for (Assinatura assinatura : assinaturas) {
      if (assinatura.getDataVencimento().compareTo(maiorVencimento) > 0) {
        maiorVencimento = assinatura.getDataVencimento();
        assinaturaMaiorVencimento = assinatura;
      }
    }

    Servidor servidor = assinaturaMaiorVencimento.getServidor();
    if (servidor == null) {
      servidor = assinaturaService.getServidorParaAlocacao();
      assinaturaMaiorVencimento.setServidor(servidor);
      assinaturaService.gravar(assinaturaMaiorVencimento);
    }

    return ContaResponse.builder().ativo(true).id(licenceKey)
        .expiracao(formatter.format(maiorVencimento)).servidor(servidor.getHostname())
        .porta(servidor.getPorta()).build();
  }

  public Boolean isAtivo(String nome, String corretora, Integer conta) {
    LocalDate data = LocalDate.now();
    Conta licensa = repository.get(corretora, conta);
    if (licensa != null) {
      if (licensa.getDataDeVencimento() == null)
        return null;
      return (data.compareTo(licensa.getDataDeVencimento()) <= 0);
    } else {
      licensa = Conta.builder().conta(conta).corretora(corretora).nome(nome)
          .id(nome.substring(0, 2).toUpperCase() + gerarId(4)).build();
      repository.save(licensa);
    }
    return false;
  }

  private String gerarId(int digitos) {
    Random random = new Random();
    String id = "";
    String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    for (int i = 0; i < digitos; i++) {
      String letra = letras.charAt(random.nextInt(letras.length())) + "";
      id += letra;
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("ID Gerado: " + id);
    return id;
  }

  public Conta get(String licenseKey) {
    return repository.findById(licenseKey).get();
  }

  public ContaInfoResponse info(String license) {
    Conta conta = repository.get(license);
    if (conta == null) {
      return ContaInfoResponse.builder().ativo(false).conta(license).pausado(true).build();
    }
    List<Assinatura> assinaturas = assinaturaRepository.getListAtivas(license, LocalDate.now());
    if (assinaturas.size() == 0)
      return ContaInfoResponse.builder().ativo(false).conta(conta.getId()).nome(conta.getNome())
          .pausado(true).corretora(conta.getCorretora()).build();

    LocalDate maiorVencimento = assinaturas.get(0).getDataVencimento();
    Assinatura ass = assinaturas.get(0);
    for (Assinatura assinatura : assinaturas) {
      if (assinatura.getDataVencimento().compareTo(maiorVencimento) > 0) {
        maiorVencimento = assinatura.getDataVencimento();
        ass = assinatura;
      }
    }

    return ContaInfoResponse.builder().ativo(true).conta(conta.getId()).nome(conta.getNome())
        .corretora(conta.getCorretora()).pausado(ass.isPausado())
        .expiracao(maiorVencimento.format(formatter)).idAfiliado(ass.getAfiliado().getId())
        .maquina((Objects.isNull(ass.getMaquina()) ? null : ass.getMaquina().getNome())).build();
  }

  public ContaInfoResponse info(String license, HttpServletRequest request) {
    Conta conta = repository.get(license);
    if (Objects.isNull(conta)) {
      return ContaInfoResponse.builder().ativo(false).conta(license).pausado(true).build();
    }

    List<Assinatura> assinaturas = assinaturaRepository.getList(conta.getId());
    Assinatura assinatura = null;
    for (Assinatura ass : assinaturas) {
      if (assinatura == null)
        assinatura = ass;
      else if (ass.getDataVencimento().compareTo(assinatura.getDataVencimento()) > 0)
        assinatura = ass;
    }
    
//    Assinatura assinatura =
//        assinaturaRepository.getByContaAfiliado(license, UsuarioUtil.getAfiliado(request));

    if (Objects.isNull(assinatura)) {
      return ContaInfoResponse.builder().nome(conta.getNome()).corretora(conta.getCorretora())
          .ativo(false).conta(license).pausado(true).build();
    }

    return ContaInfoResponse.builder().ativo(true).conta(conta.getId()).nome(conta.getNome())
        .corretora(conta.getCorretora()).pausado(assinatura.isPausado())
        .expiracao(assinatura.getDataVencimento().format(formatter))
        .idAfiliado(assinatura.getAfiliado().getId()).idAssinatura(assinatura.getId())
        .maquina(
            (Objects.isNull(assinatura.getMaquina()) ? null : assinatura.getMaquina().getNome()))
        .build();
  }

  public List<ContaResultadoView> getListContaResultado() {
    return repository.getListContaResultado();
  }

  public List<Conta> getList() {
    return repository.getList();
  }

  public void excluir(String idConta) {
    Conta conta = get(idConta);
    repository.delete(conta);
  }

  public List<ContaResultadoView> getListContaResultado(Afiliado afiliado) {
    if (Objects.isNull(afiliado))
      return new ArrayList<>();
    return repository.getListContaResultado(afiliado.getId());
  }



}
