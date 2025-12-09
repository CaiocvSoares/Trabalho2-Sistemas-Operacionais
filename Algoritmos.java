import java.util.*;

public class Algoritmos {

    // ----------------- Classe Acesso -----------------
    public static class Acesso {
        public int pagina;
        public int tempo;
        public char tipo; // R ou W

        public Acesso(int p, int t, char tp) {
            pagina = p;
            tempo = t;
            tipo  = tp;
        }
    }

    // =====================================================
    //                    ALGORITMO ÓTIMO
    // =====================================================
    public static int algoritmoOtimo(List<Acesso> acessos, int m) {

        Set<Integer> memoria = new HashSet<>();
        int faltas = 0;

        for (int i = 0; i < acessos.size(); i++) {

            int pagina = acessos.get(i).pagina;

            if (!memoria.contains(pagina)) {

                faltas++;

                if (memoria.size() < m) {
                    memoria.add(pagina);
                } else {

                    int vitima = escolherVitimaOtimo(memoria, acessos, i + 1);
                    memoria.remove(vitima);
                    memoria.add(pagina);
                }
            }
        }

        return faltas;
    }

    private static int escolherVitimaOtimo(Set<Integer> memoria, List<Acesso> acessos, int pos) {

        int vitima = -1;
        int maisDistante = -1;

        for (int pagina : memoria) {

            int proximoUso = Integer.MAX_VALUE;

            for (int i = pos; i < acessos.size(); i++) {
                if (acessos.get(i).pagina == pagina) {
                    proximoUso = i;
                    break;
                }
            }

            if (proximoUso > maisDistante) {
                maisDistante = proximoUso;
                vitima = pagina;
            }
        }

        return vitima;
    }

    // =====================================================
    //                       NRU
    // =====================================================
    public static int algoritmoNRU(List<Acesso> acessos, int m, int c) {
        // daqui você implementa
        return 0;
    }

    // =====================================================
    //                      RELÓGIO
    // =====================================================
    public static int algoritmoRelogio(List<Acesso> acessos, int m) {
        // daqui você implementa
        return 0;
    }

    // =====================================================
    //                       WSClock
    // =====================================================
    public static int algoritmoWSClock(List<Acesso> acessos, int m, int c) {
        // daqui você implementa
        return 0;
    }
}
