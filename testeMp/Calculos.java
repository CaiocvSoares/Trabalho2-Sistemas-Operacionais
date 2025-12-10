package testeMp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Calculos {
    private Configuracao config;
    private ArrayList<Pagina> paginas;
    private ArrayList<Pagina> quadros; // frames
    private ArrayList<Configuracao> sequenciaAcessos;
    private String resposta;

    public Calculos() {
        paginas = new ArrayList<>();
        quadros = new ArrayList<>();
        resposta = "";
    }

    public void setConfig(Configuracao config, ArrayList<Configuracao> sequenciaAcessos) {
        this.config = config;
        this.sequenciaAcessos = sequenciaAcessos;
        inicializarPaginas();
        calcularResultados();
    }

    private void inicializarPaginas() {
        paginas.clear();
        for (long i = 0; i < config.getTotalPaginas(); i++) {
            paginas.add(new Pagina(i));
        }
    }

    private void calcularResultados() {
        long otimo = executarOtimo();
        long nru = executarNru();
        long relogio = executarRelogio();
        long wsclock = executarWsClock();

        resposta = otimo + "\n" + nru + "\n" + relogio + "\n" + wsclock;
    }

    private long executarOtimo() {
        resetarEstado();
        long faltasPagina = 0;
        for (int i = 0; i < sequenciaAcessos.size(); i++) {
            Configuracao acesso = sequenciaAcessos.get(i);
            Pagina pagina = paginas.get((int)acesso.getNumeroPagina());

            if (!pagina.isPresente()) {
                faltasPagina++;
                if (quadros.size() < config.getTotalQuadros()) {
                    quadros.add(pagina);
                } else {
                    Pagina paraRemover = encontrarVitimaOtima(i);
                    quadros.remove(paraRemover);
                    paraRemover.setPresente(false);
                    quadros.add(pagina);
                }
                pagina.setPresente(true);
            }
            atualizarStatusPagina(pagina, acesso.isEhEscrita());
        }
        return faltasPagina;
    }

    private Pagina encontrarVitimaOtima(int indiceAtual) {
        long usoMaisDistante = -1;
        Pagina vitima = null;
        for (Pagina quadro : quadros) {
            long proximoUso = encontrarProximoUso(quadro.getNumeroPagina(), indiceAtual + 1);
            if (proximoUso == -1)
                return quadro;
            if (proximoUso > usoMaisDistante) {
                usoMaisDistante = proximoUso;
                vitima = quadro;
            }
        }
        return vitima;
    }

    private void resetarEstado() {
        quadros.clear();
        for (Pagina pagina : paginas) {
            pagina.setPresente(false);
            pagina.setReferenciada(false);
            pagina.setModificada(false);
            pagina.setUltimoAcesso(0);
        }
    }

    private void atualizarStatusPagina(Pagina pagina, boolean ehEscrita) {
        pagina.setReferenciada(true);
        if (ehEscrita) {
            pagina.setModificada(true);
        }
    }

    private long encontrarProximoUso(long numeroPagina, int indiceInicio) {
        for (int i = indiceInicio; i < sequenciaAcessos.size(); i++) {
            if (sequenciaAcessos.get(i).getNumeroPagina() == numeroPagina) {
                return i;
            }
        }
        return -1;
    }

    public void escrever(BufferedWriter escritor) throws IOException {
        escritor.write(resposta);
        escritor.newLine();
        escritor.flush();
    }

    private long executarRelogio() {
        resetarEstado();
        long faltasPagina = 0;
        int ponteiroRelogio = 0;

        for (Configuracao acesso : sequenciaAcessos) {
            Pagina pagina = paginas.get((int)acesso.getNumeroPagina());

            if (!pagina.isPresente()) {
                faltasPagina++;

                if (quadros.size() < config.getTotalQuadros()) {
                    quadros.add(pagina);
                    pagina.setPresente(true);
                } else {
                    while (true) {
                        Pagina paginaQuadro = quadros.get(ponteiroRelogio);
                        if (!paginaQuadro.isReferenciada()) {
                            paginaQuadro.setPresente(false);
                            quadros.set(ponteiroRelogio, pagina);
                            pagina.setPresente(true);
                            break;
                        }
                        paginaQuadro.setReferenciada(false);
                        ponteiroRelogio = (ponteiroRelogio + 1) % (int)config.getTotalQuadros();
                    }
                }
            }
            atualizarStatusPagina(pagina, acesso.isEhEscrita());
        }
        return faltasPagina;
    }

    private long executarWsClock() {
        resetarEstado();
        long faltasPagina = 0;
        int ponteiroRelogio = 0;
        long tau = config.getIntervaloRelogio();

        for (Configuracao acesso : sequenciaAcessos) {
            Pagina pagina = paginas.get((int)acesso.getNumeroPagina());

            if (!pagina.isPresente()) {
                faltasPagina++;
                if (quadros.size() < config.getTotalQuadros()) {
                    quadros.add(pagina);
                    pagina.setPresente(true);
                } else {
                    boolean vitimaEncontrada = false;
                    int ponteiroInicial = ponteiroRelogio;

                    do {
                        Pagina atual = quadros.get(ponteiroRelogio);
                        long idade = acesso.getTempoAcesso() - atual.getUltimoAcesso();

                        if (idade > tau) {
                            if (!atual.isReferenciada() || !atual.isModificada()) {
                                atual.setPresente(false);
                                quadros.set(ponteiroRelogio, pagina);
                                pagina.setPresente(true);
                                vitimaEncontrada = true;
                                ponteiroRelogio = (ponteiroRelogio + 1) % quadros.size();
                                break;
                            }
                        }

                        atual.setReferenciada(false);
                        ponteiroRelogio = (ponteiroRelogio + 1) % quadros.size();
                    } while (ponteiroRelogio != ponteiroInicial && !vitimaEncontrada);

                    if (!vitimaEncontrada) {
                        do {
                            Pagina atual = quadros.get(ponteiroRelogio);
                            if (!atual.isReferenciada()) {
                                atual.setPresente(false);
                                quadros.set(ponteiroRelogio, pagina);
                                pagina.setPresente(true);
                                vitimaEncontrada = true;
                                ponteiroRelogio = (ponteiroRelogio + 1) % quadros.size();
                                break;
                            }
                            atual.setReferenciada(false);
                            ponteiroRelogio = (ponteiroRelogio + 1) % quadros.size();
                        } while (ponteiroRelogio != ponteiroInicial && !vitimaEncontrada);

                        if (!vitimaEncontrada) {
                            Pagina vitima = quadros.get(ponteiroRelogio);
                            vitima.setPresente(false);
                            quadros.set(ponteiroRelogio, pagina);
                            pagina.setPresente(true);
                            ponteiroRelogio = (ponteiroRelogio + 1) % quadros.size();
                        }
                    }
                }
            }

            atualizarStatusPagina(pagina, acesso.isEhEscrita());
            pagina.setUltimoAcesso(acesso.getTempoAcesso());
        }
        return faltasPagina;
    }
    
    private long executarNru() {
        resetarEstado();
        long faltasPagina = 0;
        long cicloReset = config.getIntervaloRelogio();
        long tempoAtual = 0;

        for (Configuracao acesso : sequenciaAcessos) {
            Pagina pagina = paginas.get((int)acesso.getNumeroPagina());
            tempoAtual = acesso.getTempoAcesso();

            if (!pagina.isPresente()) {
                faltasPagina++;
                if (quadros.size() < config.getTotalQuadros()) {
                    quadros.add(pagina);
                    pagina.setPresente(true);
                    atualizarStatusPagina(pagina, acesso.isEhEscrita());
                } else {
                    List<Pagina>[] classes = new ArrayList[4];
                    for (int i = 0; i < 4; i++) {
                        classes[i] = new ArrayList<>();
                    }

                    for (Pagina quadro : quadros) {
                        int numClasse = (quadro.isReferenciada() ? 2 : 0) + (quadro.isModificada() ? 1 : 0);
                        classes[numClasse].add(quadro);
                    }

                    Pagina vitima = null;
                    for (List<Pagina> cls : classes) {
                        if (!cls.isEmpty()) {
                            vitima = cls.get(0);
                            break;
                        }
                    }

                    quadros.remove(vitima);
                    vitima.setPresente(false);
                    quadros.add(pagina);
                    pagina.setPresente(true);
                    atualizarStatusPagina(pagina, acesso.isEhEscrita());
                }
            } else {
                atualizarStatusPagina(pagina, acesso.isEhEscrita());
            }

            if (cicloReset > 0 && tempoAtual % cicloReset == 0) {
                for (Pagina quadro : quadros) {
                    quadro.setReferenciada(false);
                }
            }
        }
        return faltasPagina;
    }
}
