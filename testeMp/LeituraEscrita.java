package testeMp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeituraEscrita {
    private ArrayList<Configuracao> sequenciaAcessos;
    private Configuracao configGlobal;
    private Calculos calculos; 

    public void processarLinhas(List<String> linhas) {
        configGlobal = new Configuracao();
        sequenciaAcessos = new ArrayList<>();
        
        int numLinha = 0;
        
        for (String linha : linhas) {
            if (!linha.isEmpty()) {
                numLinha++;
                processarLinhaIndividual(linha.trim(), numLinha);
            }
        }
        
        calculos = new Calculos();
        calculos.setConfig(configGlobal, sequenciaAcessos);
    }

    private void processarLinhaIndividual(String linha, int numLinha) {
        String[] partes = linha.trim().split("\\s+");
        
        if (numLinha == 1) {
            configGlobal.setTotalPaginas(Long.parseLong(partes[0]));
        } else if (numLinha == 2) {
            configGlobal.setTotalQuadros(Long.parseLong(partes[0]));
        } else if (numLinha == 3) {
            configGlobal.setIntervaloRelogio(Long.parseLong(partes[0]));
        } else {
            Configuracao acesso = new Configuracao();
            acesso.setNumeroPagina(Long.parseLong(partes[0]));
            acesso.setTempoAcesso(Long.parseLong(partes[1]));
            acesso.setEhEscrita(partes[2].equals("W"));
            sequenciaAcessos.add(acesso);
        }
    }

    public void escreverResultado(BufferedWriter escritor) throws IOException {
        calculos.escrever(escritor);
    }
}
