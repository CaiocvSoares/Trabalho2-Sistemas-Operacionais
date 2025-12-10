import java.util.ArrayList;
import java.util.List;

public class Execucao {

    public String processarArquivo(List<String> linhas) {

    List<String> filtrado = new ArrayList<>();

    for (String linha : linhas) {

        if (linha == null) continue;

        // Remove BOM
        linha = linha.replace("\uFEFF", "");

        linha = linha.trim();

        if (!linha.isEmpty()) {
            filtrado.add(linha);
        }
    }

    // >>> PERMITE ARQUIVOS VAZIOS <<<
    if (filtrado.size() < 3) {
        return "0\n0\n0\n0";
    }

    int p = Integer.parseInt(filtrado.get(0));
    int m = Integer.parseInt(filtrado.get(1));
    int c = Integer.parseInt(filtrado.get(2));

    List<Algoritmos.Acesso> acessos = new ArrayList<>();

    for (int i = 3; i < filtrado.size(); i++) {
        String[] partes = filtrado.get(i).split("\\s+");

        if (partes.length < 3) continue;

        int pagina = Integer.parseInt(partes[0]);
        int tempo  = Integer.parseInt(partes[1]);
        char tipo  = partes[2].charAt(0);

        acessos.add(new Algoritmos.Acesso(pagina, tempo, tipo));
    }

    int faltasOtimo   = Algoritmos.algoritmoOtimo(acessos, m);
    int faltasNRU     = Algoritmos.algoritmoNRU(acessos, m, c);
    int faltasRelogio = Algoritmos.algoritmoRelogio(acessos, m);
    int faltasWSClock = Algoritmos.algoritmoWSClock(acessos, m, c);

    return faltasOtimo + "\n" +
        faltasNRU + "\n" +
        faltasRelogio + "\n" +
        faltasWSClock;
}


}
