package grafos;

import java.util.LinkedHashMap;
import java.util.Map;

// Representa um local de doação com suas características
public class Vertice {

    private final int id;
    private final String nome;
    private final Map<String, Boolean> caracteristicas;

    public static final String[] TIPOS = {"sangue", "dinheiro", "roupas", "comida", "brinquedos"};

    public Vertice(int id, String nome,
                   boolean sangue, boolean dinheiro, boolean roupas,
                   boolean comida, boolean brinquedos) {
        this.id = id;
        this.nome = nome;

        caracteristicas = new LinkedHashMap<>();
        caracteristicas.put("sangue",      sangue);
        caracteristicas.put("dinheiro",    dinheiro);
        caracteristicas.put("roupas",      roupas);
        caracteristicas.put("comida",      comida);
        caracteristicas.put("brinquedos",  brinquedos);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    public boolean temCaracteristica(String tipo) {
        return caracteristicas.getOrDefault(tipo, false);
    }

    // Retorna string resumida das características ativas
    public String resumoCaracteristicas() {
        StringBuilder sb = new StringBuilder("[");
        for (Map.Entry<String, Boolean> e : caracteristicas.entrySet()) {
            if (e.getValue()) sb.append(e.getKey()).append(", ");
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("V%d: %s %s", id, nome, resumoCaracteristicas());
    }
}
