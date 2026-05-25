# Estrutura principal do grafo

import networkx as nx

grafo = nx.Graph()

def adicionar_vertice(nome):
    grafo.add_node(nome)

def adicionar_aresta(v1, v2):
    grafo.add_edge(v1, v2)
