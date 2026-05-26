package grafos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Lê CSVs e popula o grafo
public class LeitorCSV {

    /**
     * Carrega arestas de um CSV com colunas: Source, Target, Weight, Distance_km
     * Adiciona somente arestas cujos dois vértices já existam no grafo.
     */
    public static void carregarArestas(Grafo grafo, String caminhoArquivo) throws IOException {
        try (BufferedReader br = abrirArquivo(caminhoArquivo)) {
            String linha = br.readLine(); // cabeçalho
            if (linha == null) throw new IOException("Arquivo de arestas vazio: " + caminhoArquivo);

            int[] idx = mapearColunas(linha, "Source", "Target", "Weight", "Distance_km");
            int total = 0, ignoradas = 0;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] cols = linha.split(",", -1);
                try {
                    int    origem  = Integer.parseInt(cols[idx[0]].trim());
                    int    destino = Integer.parseInt(cols[idx[1]].trim());
                    double peso    = Double.parseDouble(cols[idx[2]].trim());
                    double dist    = Double.parseDouble(cols[idx[3]].trim());

                    // Só adiciona se os dois vértices existirem
                    if (grafo.getVertices().containsKey(origem) &&
                        grafo.getVertices().containsKey(destino)) {
                        grafo.adicionarAresta(new Aresta(origem, destino, peso, dist));
                    } else {
                        ignoradas++;
                    }
                    total++;
                } catch (NumberFormatException e) {
                    System.err.println("  [aviso] linha mal formatada ignorada: " + linha);
                }
            }
            System.out.printf("[OK] Arestas: %d lidas, %d ignoradas (vértice ausente)%n",
                              total - ignoradas, ignoradas);
        }
    }

    /**
     * Carrega vértices de um CSV com colunas:
     * id, nome, sangue, comida, roupas, brinquedos, dinheiro  (+ outras ignoradas)
     */
    public static void carregarVertices(Grafo grafo, String caminhoArquivo) throws IOException {
        try (BufferedReader br = abrirArquivo(caminhoArquivo)) {
            String linha = br.readLine();
            if (linha == null) throw new IOException("Arquivo de vértices vazio: " + caminhoArquivo);

            int[] idx = mapearColunas(linha, "id", "nome", "sangue", "dinheiro",
                                              "roupas", "comida", "brinquedos");
            int total = 0;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] cols = linha.split(",", -1);
                try {
                    int    id    = Integer.parseInt(cols[idx[0]].trim());
                    String nome  = cols[idx[1]].trim().replaceAll("^\"|\"$", "");
                    boolean sang = parseBool(cols[idx[2]]);
                    boolean din  = parseBool(cols[idx[3]]);
                    boolean rou  = parseBool(cols[idx[4]]);
                    boolean com  = parseBool(cols[idx[5]]);
                    boolean bri  = parseBool(cols[idx[6]]);

                    grafo.adicionarVertice(new Vertice(id, nome, sang, din, rou, com, bri));
                    total++;
                } catch (Exception e) {
                    System.err.println("  [aviso] linha mal formatada ignorada: " + linha);
                }
            }
            System.out.printf("[OK] Vértices: %d carregados%n", total);
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────

    private static BufferedReader abrirArquivo(String caminho) throws IOException {
        File f = new File(caminho);
        if (!f.exists()) throw new FileNotFoundException("Arquivo não encontrado: " + caminho);
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(f), StandardCharsets.UTF_8));
    }

    // Retorna os índices das colunas solicitadas pelo cabeçalho
    private static int[] mapearColunas(String cabecalho, String... nomes) {
        String[] colunas = cabecalho.split(",", -1);
        Map<String, Integer> mapa = new HashMap<>();
        for (int i = 0; i < colunas.length; i++) {
            mapa.put(colunas[i].trim().replaceAll("^\"|\"$", "").toLowerCase(), i);
        }

        int[] idx = new int[nomes.length];
        for (int i = 0; i < nomes.length; i++) {
            String chave = nomes[i].toLowerCase();
            if (!mapa.containsKey(chave))
                throw new IllegalArgumentException("Coluna não encontrada no CSV: " + nomes[i]);
            idx[i] = mapa.get(chave);
        }
        return idx;
    }

    private static boolean parseBool(String s) {
        s = s.trim().toLowerCase().replaceAll("^\"|\"$", "");
        return s.equals("true") || s.equals("1") || s.equals("sim") || s.equals("yes");
    }
}
