package testeMp;
public class Configuracao {
    // Configurações gerais
    private long totalPaginas;
    private long totalQuadros;      
    private long intervaloRelogio;  

    // Dados de acesso (linhas do arquivo)
    private long numeroPagina;
    private long tempoAcesso;
    private boolean ehEscrita;

    public long getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(long totalPaginas) { this.totalPaginas = totalPaginas; }

    public long getTotalQuadros() { return totalQuadros; }
    public void setTotalQuadros(long totalQuadros) { this.totalQuadros = totalQuadros; }

    public long getIntervaloRelogio() { return intervaloRelogio; }
    public void setIntervaloRelogio(long intervaloRelogio) { this.intervaloRelogio = intervaloRelogio; }

    public long getNumeroPagina() { return numeroPagina; }
    public void setNumeroPagina(long numeroPagina) { this.numeroPagina = numeroPagina; }

    public long getTempoAcesso() { return tempoAcesso; }
    public void setTempoAcesso(long tempoAcesso) { this.tempoAcesso = tempoAcesso; }

    public boolean isEhEscrita() { return ehEscrita; }
    public void setEhEscrita(boolean ehEscrita) { this.ehEscrita = ehEscrita; }
}