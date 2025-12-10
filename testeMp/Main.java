package testeMp;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String diretorio = ".";

        for (int i = 1; i <= 10; i++) {
            String numero = String.format("%02d", i);
            String nomeEntrada = "TESTE-" + numero + ".txt";
            String nomeSaida = "TESTE-" + numero + "-RESULTADO.txt";

            Path caminhoEntrada = Paths.get(diretorio, nomeEntrada);
            Path caminhoSaida = Paths.get(diretorio, nomeSaida);

            if (!Files.exists(caminhoEntrada)) {
                System.out.println("Arquivo não encontrado: " + nomeEntrada);
                continue;
            }

            try {
                List<String> linhas = Files.readAllLines(caminhoEntrada, StandardCharsets.UTF_8);

                LeituraEscrita leitor = new LeituraEscrita();
                leitor.processarLinhas(linhas);

                try (BufferedWriter escritor = Files.newBufferedWriter(caminhoSaida, StandardCharsets.UTF_8)) {
                    leitor.escreverResultado(escritor);
                }

                System.out.println("Gerado: " + nomeSaida);

            } catch (IOException e) {
                System.err.println("Erro ao processar " + nomeEntrada + ": " + e.getMessage());
            }
        }
        System.out.println("Processamento concluído.");
    }
}