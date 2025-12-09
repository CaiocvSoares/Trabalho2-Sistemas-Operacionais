import java.util.ArrayList;
import java.util.List;

public class Execucao {

    public String processarArquivo(List<String> linhas) {

        int p = Integer.parseInt(linhas.get(0));  // páginas do espaço virtual
        int m = Integer.parseInt(linhas.get(1));  // molduras
        int c = Integer.parseInt(linhas.get(2));  // ciclo bit R reset

        List<Algoritmos.Acesso> acessos = new ArrayList<>();

        for (int i = 3; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(" ");

            int pagina = Integer.parseInt(partes[0]);
            int tempo  = Integer.parseInt(partes[1]);
            char tipo  = partes[2].charAt(0);

            acessos.add(new Algoritmos.Acesso(pagina, tempo, tipo));
        }

        // ----- CHAMADA DOS ALGORITMOS -----
        int faltasOtimo   = Algoritmos.algoritmoOtimo(acessos, m);
        int faltasNRU     = Algoritmos.algoritmoNRU(acessos, m, c);
        int faltasRelogio = Algoritmos.algoritmoRelogio(acessos, m);
        int faltasWSClock = Algoritmos.algoritmoWSClock(acessos, m, c);

        // ----- FORMATANDO SAÍDA -----
        return faltasOtimo + "\n" +
            faltasNRU + "\n" +
            faltasRelogio + "\n" +
            faltasWSClock;
    }
}
