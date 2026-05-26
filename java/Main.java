package grafos;

import java.util.*;

/**
 * Ponto de entrada.
 * Uso: java grafos.Main <arestas.csv> <vertices.csv>
 *
 * Fluxo:
 *   1. Carrega vértices (locais de doação)
 *   2. Carrega arestas (proximidade geográfica)
 *   3. Exibe o grafo
 *   4. Coloração de vértices por característica
 *   5. Coloração de arestas
 */
public class Main {

    public static void main(String[] args) {
        // Caminhos padrão (sobrescritos se passados via args)
        String csvArestas  = args.length > 0 ? args[0] : "arestas_proximidade_geografica.csv";
        String csvVertices = args.length > 1 ? args[1] : "locais_doacao_sao_paulo.csv";

        Grafo grafo = new Grafo();

        // ── 1. Carregamento ──────────────────────────────────────
        System.out.println("\n[Carregando dados...]");
        try {
            LeitorCSV.carregarVertices(grafo, csvVertices);
            LeitorCSV.carregarArestas(grafo,  csvArestas);
        } catch (Exception e) {
            System.err.println("Erro ao carregar CSV: " + e.getMessage());
            System.exit(1);
        }

        // ── 2. Exibição do grafo ─────────────────────────────────
        System.out.println();
        grafo.mostrarGrafo();

        // ── 3. Coloração de vértices por característica ──────────
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  COLORAÇÃO DE VÉRTICES (Sequencial)");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // Exemplos de filtros; ajuste conforme necessidade
        String[][] filtros = {
            {"sangue"},
            {"dinheiro"},
            {"roupas"},
            {"comida"},
            {"brinquedos"},
            {"dinheiro", "roupas"},
        };

        for (String[] f : filtros) {
            Map<Integer, Integer> cores = grafo.colorirVertices(f);
            grafo.mostrarColoracaoVertices(cores, f);
        }

        // ── 4. Coloração de arestas ──────────────────────────────
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  COLORAÇÃO DE ARESTAS (Sequencial)");
        System.out.println("═══════════════════════════════════════════════════════\n");

        Map<String, Integer> coresArestas = grafo.colorirArestas();
        grafo.mostrarColoracaoArestas(coresArestas);

        // Exemplo: mostrar arestas de uma cor específica
        int corAlvo = 0;
        System.out.printf("   Primeiras 10 arestas com cor %d:%n", corAlvo);
        int count = 0;
        for (Aresta a : grafo.getArestas()) {
            if (coresArestas.getOrDefault(a.chave(), -1) == corAlvo) {
                System.out.println("     " + a);
                if (++count >= 10) break;
            }
        }
        System.out.println();
    }
}
