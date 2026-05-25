# Algoritmos aplicados ao projeto

import networkx as nx

def grau_vertices(grafo):
    return dict(grafo.degree())

def verificar_conexo(grafo):
    return nx.is_connected(grafo)
