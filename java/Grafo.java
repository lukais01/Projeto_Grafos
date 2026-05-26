package grafos;

import java.util.*;

// Grafo não-dirigido com lista de adjacência
public class Grafo {

    private final Map<Integer, Vertice>       vertices  = new LinkedHashMap<>();
    private final Map<Integer, List<Aresta>>  adj       = new LinkedHashMap<>();
    private final List<Aresta>                arestas   = new ArrayList<>();

    // ─── Construção ──────────────────────────────────────────────

    public void adicionarVertice(Vertice v) {
        vertices.put(v.getId(), v);
        adj.putIfAbsent(v.getId(), new ArrayList<>());
    }

    public void adicionarAresta(Aresta a) {
        arestas.add(a);
        adj.computeIfAbsent(a.getOrigem(),  k -> new ArrayList<>()).add(a);
        adj.computeIfAbsent(a.getDestino(), k -> new ArrayList<>()).add(a);
    }

    public Map<Integer, Vertice>      getVertices() { return vertices; }
    public List<Aresta>               getArestas()  { return arestas; }
    public Map<Integer, List<Aresta>> getAdj()      { return adj; }

    // ─── Exibição ────────────────────────────────────────────────

    public void mostrarGrafo() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.printf("  GRAFO  |  %d vértices  |  %d arestas%n",
                          vertices.size(), arestas.size());
        System.out.println("═══════════════════════════════════════════════════════");

        System.out.println("\n── Vértices ──");
        for (Vertice v : vertices.values()) {
            System.out.println("  " + v);
        }

        System.out.println("\n── Arestas (lista de adjacência resumida) ──");
        for (Map.Entry<Integer, List<Aresta>> e : adj.entrySet()) {
            Vertice v = vertices.get(e.getKey());
            String nome = v != null ? v.getNome() : "?";
            System.out.printf("  V%-3d %-40s →", e.getKey(), "(" + nome + ")");
            for (Aresta a : e.getValue()) {
                int vizinho = (a.getOrigem() == e.getKey()) ? a.getDestino() : a.getOrigem();
                System.out.printf(" V%d(%.2fkm)", vizinho, a.getDistanciaKm());
            }
            System.out.println();
        }
        System.out.println();
    }

    // ─── Coloração Sequencial de Vértices ────────────────────────

    /**
     * Aplica coloração sequencial apenas nos vértices que possuem TODOS
     * os tipos especificados em {@code filtros}.
     * Vértices que não atendem ao filtro ficam com cor -1 (sem cor).
     */
    public Map<Integer, Integer> colorirVertices(String... filtros) {
        // Seleciona apenas os vértices que passam no filtro
        List<Integer> alvo = new ArrayList<>();
        for (Vertice v : vertices.values()) {
            boolean passa = true;
            for (String f : filtros) {
                if (!v.temCaracteristica(f)) { passa = false; break; }
            }
            if (passa) alvo.add(v.getId());
        }

        Map<Integer, Integer> cores = new HashMap<>();
        for (int id : alvo) {
            Set<Integer> vizinhosCores = new HashSet<>();
            for (Aresta a : adj.getOrDefault(id, Collections.emptyList())) {
                int viz = (a.getOrigem() == id) ? a.getDestino() : a.getOrigem();
                if (cores.containsKey(viz)) vizinhosCores.add(cores.get(viz));
            }
            // Menor cor disponível
            int cor = 0;
            while (vizinhosCores.contains(cor)) cor++;
            cores.put(id, cor);
        }

        return cores;
    }

    public void mostrarColoracaoVertices(Map<Integer, Integer> cores, String... filtros) {
        System.out.println("── Coloração de Vértices ──");
        System.out.print("   Filtro: ");
        System.out.println(filtros.length == 0 ? "todos" : String.join(" + ", filtros));

        // Conta cores usadas
        int maxCor = cores.values().stream().mapToInt(Integer::intValue).max().orElse(-1);
        System.out.println("   Cores necessárias: " + (maxCor + 1));
        System.out.println();

        // Agrupa por cor para exibição
        Map<Integer, List<Integer>> porCor = new TreeMap<>();
        for (Map.Entry<Integer, Integer> e : cores.entrySet()) {
            porCor.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
        }
        for (Map.Entry<Integer, List<Integer>> e : porCor.entrySet()) {
            System.out.printf("   Cor %-2d → ", e.getValue());
            for (int id : e.getValue()) {
                Vertice v = vertices.get(id);
                System.out.printf("V%d(%s)  ", id, v != null ? abreviar(v.getNome()) : "?");
            }
            System.out.println();
        }
        System.out.println();
    }

    // ─── Coloração Sequencial de Arestas ─────────────────────────

    /**
     * Coloração sequencial de arestas: duas arestas adjacentes
     * (que compartilham um vértice) não podem ter a mesma cor.
     */
    public Map<String, Integer> colorirArestas() {
        Map<String, Integer> cores = new LinkedHashMap<>();

        for (Aresta a : arestas) {
            Set<Integer> usadas = new HashSet<>();

            // Cores das arestas que tocam a.origem
            for (Aresta viz : adj.getOrDefault(a.getOrigem(), Collections.emptyList())) {
                if (!viz.chave().equals(a.chave()) && cores.containsKey(viz.chave())) {
                    usadas.add(cores.get(viz.chave()));
                }
            }
            // Cores das arestas que tocam a.destino
            for (Aresta viz : adj.getOrDefault(a.getDestino(), Collections.emptyList())) {
                if (!viz.chave().equals(a.chave()) && cores.containsKey(viz.chave())) {
                    usadas.add(cores.get(viz.chave()));
                }
            }

            int cor = 0;
            while (usadas.contains(cor)) cor++;
            cores.put(a.chave(), cor);
        }

        return cores;
    }

    public void mostrarColoracaoArestas(Map<String, Integer> cores) {
        System.out.println("── Coloração de Arestas ──");
        int maxCor = cores.values().stream().mapToInt(Integer::intValue).max().orElse(-1);
        System.out.println("   Cores necessárias: " + (maxCor + 1));
        System.out.println();

        // Agrupa por cor
        Map<Integer, List<String>> porCor = new TreeMap<>();
        for (Map.Entry<String, Integer> e : cores.entrySet()) {
            porCor.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
        }
        for (Map.Entry<Integer, List<String>> e : porCor.entrySet()) {
            System.out.printf("   Cor %-2d (%3d arestas) → %s ...%n",
                              e.getValue(), e.getValue().size(),
                              e.getValue().subList(0, Math.min(5, e.getValue().size())));
        }
        System.out.println();
    }

    // ─── Utilitário ──────────────────────────────────────────────

    private String abreviar(String nome) {
        return nome.length() <= 22 ? nome : nome.substring(0, 20) + "…";
    }
}
