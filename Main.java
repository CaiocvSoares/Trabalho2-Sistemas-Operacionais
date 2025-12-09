import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String diretorio = "E:\\";

        for (int i = 1; i <= 10; i++) {

            String numero = String.format("%02d", i);
            String nomeEntrada = "TESTE-" + numero + ".txt";
            String nomeSaida   = "TESTE-" + numero + "-RESULTADO.txt";

            Path caminhoEntrada = Paths.get(diretorio, nomeEntrada);
            Path caminhoSaida   = Paths.get(diretorio, nomeSaida);

            try {
                // Lê todas as linhas do arquivo
                List<String> linhas = Files.readAllLines(caminhoEntrada, StandardCharsets.UTF_8);

                // Processa com classe Execucao
                Execucao exec = new Execucao();
                String resultado = exec.processarArquivo(linhas);

                // Grava o arquivo de saída
                try (BufferedWriter escritor = Files.newBufferedWriter(caminhoSaida, StandardCharsets.UTF_8)) {
                    escritor.write(resultado);
                }

                System.out.println("Gerado: " + nomeSaida);

            } catch (IOException e) {
                System.err.println("Erro ao processar " + nomeEntrada + ": " + e.getMessage());
            }
        }

        System.out.println("Todos os arquivos foram processados.");
    }
}
