# ED_EN_8200306_8190701


# Objetivo: Busca do Melhor Caminho em um Grafo Ponderado

## Descrição Geral
Este projeto implementa um algoritmo para identificar o **melhor caminho** entre duas divisões (“EntryPoint” e “TargetDivision”) em um grafo ponderado. A solução combina elementos dos algoritmos **Breadth-First Search (BFS)** e **Dijkstra** para atender às necessidades do problema, considerando tanto a estrutura do grafo quanto um custo dinâmico que varia conforme os eventos encontrados no percurso.

### Contexto do Problema
O objetivo é encontrar o caminho mais eficiente entre dois pontos em um grafo. Apesar de as arestas do grafo não terem pesos fixos, o custo de atravessar cada nó é influenciado por:

- **Perdas causadas por inimigos**: Impactam negativamente os recursos/pontuação.
- **Benefícios ganhos por itens**: Aumentam a pontuação, permitindo a continuação da jornada.

A combinação do BFS e da lógica do Dijkstra permite avaliar a viabilidade de cada caminho e determinar o melhor com base na maximização dos pontos restantes.

---

## Algoritmo Implementado

### Estratégia Utilizada
1. **Baseado no BFS**:
    - O algoritmo explora o grafo em uma busca em largura, garantindo que todas as opções em um nível sejam avaliadas antes de passar para o próximo.
    - Isso assegura que o menor número de passos seja considerado inicialmente.

2. **Baseado no Dijkstra**:
    - A lógica de custos acumulados é incorporada ao BFS, permitindo avaliar dinamicamente os impactos positivos e negativos ao longo do caminho.
    - Cada vizinho é avaliado quanto ao impacto de pontuação:
        - **Impactos negativos**: Causados por colisões com inimigos.
        - **Impactos positivos**: Benefícios obtidos por itens encontrados.
    - Somente caminhos com pontuação positiva são explorados.

3. **Combinação dos Algoritmos**:
    - O BFS é usado para explorar os caminhos.
    - A lógica de custo dinâmico do Dijkstra determina quais caminhos são viáveis e prioriza aqueles com maior pontuação restante.

### Principais Características
- **Busca baseada em níveis**: Garante que todas as opções de um nível sejam exploradas antes de passar para o próximo.
- **Controle de viabilidade**: Apenas caminhos que mantêm pontuação positiva são considerados.
- **Caminho mais eficiente**: Retorna o caminho que maximiza os pontos restantes ao chegar no destino.

---

## Estruturas de Dados

1. **Fila (Queue)**:
    - Guarda as divisões a serem visitadas durante o processo de busca.

2. **Listas**:
    - **Visited**: Armazena as divisões que já foram exploradas.
    - **PointsRemaining**: Armazena a pontuação restante para cada divisão visitada.
    - **Predecessors**: Armazena o rastreamento do caminho.

---

## Como o Algoritmo Funciona

1. Inicialização:
    - A divisão inicial (“EntryPoint”) é adicionada à Queue, com pontuação inicial definida.
    - Predecessores e divisões visitadas são configurados.

2. Processamento:
    - Enquanto a Queue não está vazia:
        - Remove a próxima divisão da fila.
        - Avalia seus vizinhos.
        - Calcula o impacto de cada vizinho usando a função `calculateImpact`.
        - Adiciona à Queue os vizinhos viáveis (pontuação positiva) e atualiza as listas de pontos e predecessores.

3Resultado:
    - Retorna uma QueueADTcom o caminho encontrado (sequência de divisões).

---

## Conclusão
Este projeto demonstra como combinar as técnicas do BFS e Dijkstra para resolver problemas em grafos ponderados dinâmicos. A solução é altamente flexível e pode ser adaptada para diferentes cenários envolvendo custos dinâmicos em caminhos de grafos.

---

## Referências

Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022). *Introduction to algorithms* (4th ed.). MIT Press.

### Capítulo Relevante: Shortest Paths

A lógica para a busca do melhor caminho entre as duas divisões guiada pelo Capítulo (*Shortest Paths*), que aborda algoritmos para a identificação de caminhos mínimos em grafos. Este capítulo detalha algoritmos como o Dijkstra.

Acesso para o livro:  
[Introduction to Algorithms (4ª edição)](https://dl.ebooksworld.ir/books/Introduction.to.Algorithms.4th.Leiserson.Stein.Rivest.Cormen.MIT.Press.9780262046305.EBooksWorld.ir.pdf).

