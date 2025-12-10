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
    //                         NRU
    // =====================================================
    public static int algoritmoNRU(List<Acesso> acessos, int m, int c) {

        class PaginaFrame {
            int pagina;
            int R;
            int M;

            PaginaFrame(int p) {
                pagina = p;
                R = 1;
                M = 0;
            }
        }

        Map<Integer, PaginaFrame> memoria = new LinkedHashMap<>();
        int faltas = 0;
        int ultimoReset = 0;

        for (Acesso a : acessos) {

            // Reset do bit R
            if (a.tempo - ultimoReset >= c) {
                for (PaginaFrame f : memoria.values()) f.R = 0;
                ultimoReset = a.tempo;
            }

            // Página já está na memória
            if (memoria.containsKey(a.pagina)) {
                PaginaFrame f = memoria.get(a.pagina);
                f.R = 1;
                if (a.tipo == 'W') f.M = 1;
                continue;
            }

            // Falta de página
            faltas++;

            // Há espaço livre?
            if (memoria.size() < m) {
                PaginaFrame f = new PaginaFrame(a.pagina);
                if (a.tipo == 'W') f.M = 1;
                memoria.put(a.pagina, f);
                continue;
            }

            // Seleção da vítima por classe NRU
            int vitima = escolherNRU(memoria);

            memoria.remove(vitima);

            PaginaFrame f = new PaginaFrame(a.pagina);
            if (a.tipo == 'W') f.M = 1;
            memoria.put(a.pagina, f);
        }

        return faltas;
    }

    private static int escolherNRU(Map<Integer, ?> memoria) {

        // classes 0,1,2,3
        List<Integer> classe0 = new ArrayList<>();
        List<Integer> classe1 = new ArrayList<>();
        List<Integer> classe2 = new ArrayList<>();
        List<Integer> classe3 = new ArrayList<>();

        for (Object obj : memoria.values()) {
            Object f = obj;
            int pagina = -1, R = -1, M = -1;

            try {
                pagina = (int) f.getClass().getField("pagina").get(f);
                R = (int) f.getClass().getField("R").get(f);
                M = (int) f.getClass().getField("M").get(f);
            } catch (Exception ignored) {}

            if (R == 0 && M == 0) classe0.add(pagina);
            else if (R == 0 && M == 1) classe1.add(pagina);
            else if (R == 1 && M == 0) classe2.add(pagina);
            else classe3.add(pagina);
        }

        if (!classe0.isEmpty()) return classe0.get(0);
        if (!classe1.isEmpty()) return classe1.get(0);
        if (!classe2.isEmpty()) return classe2.get(0);
        return classe3.get(0);
    }

    // =====================================================
    //                      RELÓGIO
    // =====================================================
    public static int algoritmoRelogio(List<Acesso> acessos, int m) {

        class Frame {
            int pagina;
            int R;
            Frame(int p) { pagina = p; R = 1; }
        }

        List<Frame> memoria = new ArrayList<>();
        int ponteiro = 0;
        int faltas = 0;

        for (Acesso a : acessos) {

            // HIT
            boolean hit = false;
            for (Frame f : memoria) {
                if (f.pagina == a.pagina) {
                    f.R = 1;
                    hit = true;
                    break;
                }
            }
            if (hit) continue;

            // Falta
            faltas++;

            // Espaço livre
            if (memoria.size() < m) {
                memoria.add(new Frame(a.pagina));
                continue;
            }

            // Substituição
            while (true) {
                Frame f = memoria.get(ponteiro);

                if (f.R == 0) {
                    // substituir
                    memoria.set(ponteiro, new Frame(a.pagina));
                    ponteiro = (ponteiro + 1) % m;
                    break;
                } else {
                    f.R = 0;
                    ponteiro = (ponteiro + 1) % m;
                }
            }
        }

        return faltas;
    }

    // =====================================================
    //                       WSClock
    // =====================================================
    public static int algoritmoWSClock(List<Acesso> acessos, int m, int c) {

        class Frame {
            int pagina;
            int R;
            int M;
            int ultimoUso;

            Frame(int p, int t) {
                pagina = p;
                R = 1;
                M = 0;
                ultimoUso = t;
            }
        }

        List<Frame> memoria = new ArrayList<>();
        int ponteiro = 0;
        int faltas = 0;

        for (Acesso a : acessos) {

            // HIT
            boolean hit = false;
            for (Frame f : memoria) {
                if (f.pagina == a.pagina) {
                    f.R = 1;
                    f.ultimoUso = a.tempo;
                    if (a.tipo == 'W') f.M = 1;
                    hit = true;
                    break;
                }
            }
            if (hit) continue;

            // FALTA
            faltas++;

            // Espaco livre
            if (memoria.size() < m) {
                Frame f = new Frame(a.pagina, a.tempo);
                if (a.tipo == 'W') f.M = 1;
                memoria.add(f);
                continue;
            }

            // Procurar vítima no WSClock
            int loops = 0;
            Frame vitima = null;

            while (vitima == null) {

                Frame f = memoria.get(ponteiro);

                if (f.R == 1) {
                    f.R = 0;
                } else {

                    int delta = a.tempo - f.ultimoUso;

                    if (delta > c) {
                        if (f.M == 0) {
                            vitima = f;
                        } else {
                            f.M = 0; // write-back simulado
                        }
                    }
                }

                ponteiro = (ponteiro + 1) % m;

                loops++;

                if (loops > m * 2) {
                    vitima = memoria.get(ponteiro);
                }
            }

            memoria.remove(vitima);

            Frame novo = new Frame(a.pagina, a.tempo);
            if (a.tipo == 'W') novo.M = 1;

            memoria.add(novo);
        }

        return faltas;
    }
}
