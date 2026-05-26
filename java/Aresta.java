package grafos;

// Uma aresta entre dois vértices, com peso e distância em km
public class Aresta {

    private final int origem;
    private final int destino;
    private final double peso;
    private final double distanciaKm;

    public Aresta(int origem, int destino, double peso, double distanciaKm) {
        this.origem      = origem;
        this.destino     = destino;
        this.peso        = peso;
        this.distanciaKm = distanciaKm;
    }

    public int getOrigem()       { return origem; }
    public int getDestino()      { return destino; }
    public double getPeso()      { return peso; }
    public double getDistanciaKm() { return distanciaKm; }

    // Chave única para identificar a aresta (ordem não importa)
    public String chave() {
        int a = Math.min(origem, destino);
        int b = Math.max(origem, destino);
        return a + "-" + b;
    }

    @Override
    public String toString() {
        return String.format("(%d <-> %d | peso=%.4f | %.3f km)", origem, destino, peso, distanciaKm);
    }
}
