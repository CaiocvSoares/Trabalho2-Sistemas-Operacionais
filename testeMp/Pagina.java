package testeMp;
public class Pagina {
    private long numeroPagina;
    private boolean referenciada; 
    private boolean modificada;   
    private long ultimoAcesso;    
    private boolean presente;     

    public Pagina(long numeroPagina) {
        this.numeroPagina = numeroPagina;
        this.referenciada = false;
        this.modificada = false;
        this.ultimoAcesso = 0;
        this.presente = false;
    }

    public long getNumeroPagina() { return numeroPagina; }

    public boolean isReferenciada() { return referenciada; }
    public void setReferenciada(boolean referenciada) { this.referenciada = referenciada; }

    public boolean isModificada() { return modificada; }
    public void setModificada(boolean modificada) { this.modificada = modificada; }

    public long getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(long tempo) { this.ultimoAcesso = tempo; }

    public boolean isPresente() { return presente; }
    public void setPresente(boolean presente) { this.presente = presente; }
}