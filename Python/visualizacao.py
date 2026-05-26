# Visualização gráfica

import matplotlib.pyplot as plt
import networkx as nx

def mostrar_grafo(grafo):
    nx.draw(grafo, with_labels=True)
    plt.show()
