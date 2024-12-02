package org.example.Structures.Implementations;

import org.example.Structures.Interfaces.NetworkADT;

import java.text.DecimalFormat;
import java.util.Iterator;

public class Network<T> extends Graph<T> implements NetworkADT<T> {
    protected double[][] networkAdjMatrix;    // adjacency matrix

    /**
     * Creates an empty network
     */
    public Network() {
        numVertices = 0;
        this.networkAdjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
    }

    /**
     * Returns a string representation of the adjacency matrix
     *
     * @return {@link String}
     */
    public String toString() {
        if (numVertices == 0)
            return "Empty network!";

        String result = "";

        result += "Adjacency matrix\n";
        result += "----------------";
        result += "\t";

        for (int i = 0; i < numVertices; i++) {
            if (i < 10)
                result += " ";
        }
        result += "\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + (i+1) + "\t";

            for (int j = 0; j < numVertices; j++) {
                if (networkAdjMatrix[i][j] < Double.POSITIVE_INFINITY)
                    result += "1 ";
                else
                    result += "0 ";
            }
            result += "\n";
        }

        /** Print the vertex values */
        result += "\n\nVertices values";
        result += "\n-------------\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + (i+1) + "\t";
            result += vertices[i].toString() + "\n";
        }

        result += "\n\nWeights";
        result += "\n----------------\n";
        result += "index\tweight\n\n";

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < numVertices; i++) {
            for (int j = numVertices - 1; j > i; j--) {
                if (networkAdjMatrix[i][j] < Double.POSITIVE_INFINITY) {
                    result += (i+1) + " to " + (j+1) + "\t";
                    result += df.format(networkAdjMatrix[i][j]) + "km\n";
                }
            }
        }

        result += "\n";
        return result;
    }

    public String vertexToString() {
        if (numVertices == 0)
            return "Empty network!";

        String result = "";


        /** Print the vertex values */
        for (int i = 0; i < numVertices; i++) {
            result += "" + (i+1) + "\t";
            result += vertices[i].toString() + "\n";
        }

        result += "\n";
        return result;
    }

    public String edgeToString() {
        if (numVertices == 0)
            return "Empty network";
        String result = "";
        result += "\n\nWeights";
        result += "\n----------------\n";
        result += "index\tweight\n\n";

        for (int i = 0; i < numVertices; i++) {
            for (int j = numVertices - 1; j > i; j--) {
                if (networkAdjMatrix[i][j] < Double.POSITIVE_INFINITY) {
                    result += i + " para " + j + "\t";
                    result += networkAdjMatrix[i][j] + "\n";
                }
            }
        }

        result += "\n";
        return result;
    }

    /**
     * Inserts an edge between two vertices of the network
     *
     * @param index1 first index
     * @param index2 second index
     * @param weight edge weight
     */
    public void addEdge(int index1, int index2, double weight) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            networkAdjMatrix[index1][index2] = weight;
            networkAdjMatrix[index2][index1] = weight;
        }
    }

    /**
     * Removes an edge between two vertices of the network
     *
     * @param index1 first index
     * @param index2 second index
     */
    public void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            networkAdjMatrix[index1][index2] = Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Inserts an edge with a given weight between two vertices of the network
     *
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     * @param weight  edge weight
     */
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    /**
     * Inserts an edge between two vertices of the network. Assumes a weight of zero
     *
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     */
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2), 0);
    }

    /**
     * Removes an edge between two vertices of the network
     *
     * @param vertex1
     * @param vertex2
     */
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }


    /**
     * Adds a vertex to the network, expanding the capacity of the network if necessary
     */
    public void addVertex() {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = null;
        for (int i = 0; i <= numVertices; i++) {
            networkAdjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            networkAdjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }
        numVertices++;
    }

    /**
     * Adds a vertex to the network, expanding the capacity of the network if necessary. It also associates a given
     * element to the vertex
     *
     * @param vertex vertex element
     */
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            networkAdjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            networkAdjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }
        numVertices++;
    }

    public T getVertex(int index) {
        if (indexIsValid(index)) {
            return vertices[index];
        }
        return null;
    }


    /**
     * Removes a vertex at the given index from the network
     *
     * @param index index of the vertex to remove
     */
    public void removeVertex(int index) {
        if (indexIsValid(index)) {
            numVertices--;

            for (int i = index; i < numVertices; i++)
                vertices[i] = vertices[i + 1];

            for (int i = index; i < numVertices; i++)
                for (int j = 0; j <= numVertices; j++)
                    networkAdjMatrix[i][j] = networkAdjMatrix[i + 1][j];

            for (int i = index; i < numVertices; i++)
                for (int j = 0; j < numVertices; j++)
                    networkAdjMatrix[j][i] = networkAdjMatrix[j][i + 1];
        }
    }

    /**
     * Removes a single vertex with the given value from the graph
     *
     * @param vertex vertex value
     */
    public void removeVertex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertex.equals(vertices[i])) {
                removeVertex(i);
                return;
            }
        }
    }


    /**
     * Returns an iterator that performs a depth first search traversal, starting at the given index
     *
     * @param startIndex starting index
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorDFS(int startIndex) {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(startIndex))
            return resultList.iterator();

        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            /** Find a vertex adjacent to x that has not been visited
             and push it on the stack */
            for (int i = 0; (i < numVertices) && !found; i++) {
                if ((networkAdjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY)
                        && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }


    /**
     * Returns an iterator that performs a depth first search traversal, starting at the given vertex
     *
     * @param startVertex starting vertex
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorDFS(T startVertex) {
        return iteratorDFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator that performs a breadth first search traversal starting at the given index
     *
     * @param startIndex starting index
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();

        if (!indexIsValid(startIndex))
            return resultList.iterator();

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;


        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);

            /** Find all vertices adjacent to x that have not been
             visited and queue them up */
            for (int i = 0; i < numVertices; i++) {
                if ((networkAdjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY)
                        && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }


    /**
     * Returns an iterator that performs a breadth first search traversal starting at the given vertex
     *
     * @param startVertex starting vertex
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator that contains the indices of the vertices that are in the shortest path between two given
     * vertices
     *
     * @param startIndex  starting index
     * @param targetIndex target index
     * @return {@link Iterator}
     */
    protected Iterator<Integer> iteratorShortestPathIndices
    (int startIndex, int targetIndex) {
        int index;
        double weight;
        int[] predecessor = new int[numVertices];
        Heap<Double> traversalMinHeap = new Heap<>();
        ArrayUnorderedList<Integer> resultList =
                new ArrayUnorderedList<>();
        LinkedStack<Integer> stack = new LinkedStack<>();

        int[] pathIndex = new int[numVertices];
        double[] pathWeight = new double[numVertices];
        for (int i = 0; i < numVertices; i++)
            pathWeight[i] = Double.POSITIVE_INFINITY;

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) ||
                (startIndex == targetIndex) || isEmpty())
            return resultList.iterator();

        pathWeight[startIndex] = 0;
        predecessor[startIndex] = -1;
        visited[startIndex] = true;
        weight = 0;

        /** Update the pathWeight for each vertex except the
         startVertex. Notice that all vertices not adjacent
         to the startVertex  will have a pathWeight of
         infinity for now. */
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                pathWeight[i] = pathWeight[startIndex] +
                        networkAdjMatrix[startIndex][i];
                predecessor[i] = startIndex;
                traversalMinHeap.addElement(pathWeight[i]);
            }
        }

        do {
            weight = (traversalMinHeap.removeMin()).doubleValue();
            traversalMinHeap.removeAllElements();
            if (weight == Double.POSITIVE_INFINITY)  // no possible path
                return resultList.iterator();
            else {
                index = getIndexOfAdjVertexWithWeightOf(visited, pathWeight,
                        weight);
                visited[index] = true;
            }

            /** Update the pathWeight for each vertex that has not been
             visited and is adjacent to the last vertex that was visited.
             Also, add each unvisited vertex to the heap. */
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i]) {
                    if ((networkAdjMatrix[index][i] < Double.POSITIVE_INFINITY) &&
                            (pathWeight[index] + networkAdjMatrix[index][i]) < pathWeight[i]) {
                        pathWeight[i] = pathWeight[index] + networkAdjMatrix[index][i];
                        predecessor[i] = index;
                    }
                    traversalMinHeap.addElement(pathWeight[i]);
                }
            }
        } while (!traversalMinHeap.isEmpty() && !visited[targetIndex]);

        index = targetIndex;
        stack.push(index);
        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != startIndex);

        while (!stack.isEmpty())
            resultList.addToRear((stack.pop()));

        return resultList.iterator();
    }

    /**
     * Returns the index of the vertex that is adjacent to the vertex with the given index and also has a pathweight
     * equal to the weight
     *
     * @param visited    array of visited elements
     * @param pathWeight pathWeight
     * @param weight     weight
     * @return index
     */
    protected int getIndexOfAdjVertexWithWeightOf(boolean[] visited,
                                                  double[] pathWeight,
                                                  double weight) {
        for (int i = 0; i < numVertices; i++)
            if ((pathWeight[i] == weight) && !visited[i])
                for (int j = 0; j < numVertices; j++)
                    if ((networkAdjMatrix[i][j] < Double.POSITIVE_INFINITY) &&
                            visited[j])
                        return i;

        return -1;  // should never get to here
    }

    /**
     * Returns an iterator that contains the shortest path between two vertices
     *
     * @param startIndex  starting index
     * @param targetIndex target index
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList templist = new ArrayUnorderedList();
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return templist.iterator();

        Iterator<Integer> it = iteratorShortestPathIndices(startIndex,
                targetIndex);
        while (it.hasNext())
            templist.addToRear(vertices[(it.next()).intValue()]);
        return templist.iterator();
    }


    /**
     * Returns an iterator that contains the shortest path between two vertives
     *
     * @param startVertex  starting vertex
     * @param targetVertex target vertex
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return iteratorShortestPath(getIndex(startVertex),
                getIndex(targetVertex));
    }

    /**
     * Returns the weight of the least weight path in the network
     *
     * @param startIndex  starting index
     * @param targetIndex target index
     * @return path weight; {@link Double}.POSITIVE_INFINITY if not found
     */
    public double shortestPathWeight(int startIndex, int targetIndex) {
        double result = 0;
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return Double.POSITIVE_INFINITY;

        int index1, index2;
        Iterator<Integer> it = iteratorShortestPathIndices(startIndex,
                targetIndex);

        if (it.hasNext())
            index1 = it.next().intValue();
        else
            return Double.POSITIVE_INFINITY;

        while (it.hasNext()) {
            index2 = (it.next()).intValue();
            result += networkAdjMatrix[index1][index2];
            index1 = index2;
        }

        return result;
    }

    /**
     * Returns the weight of the least weight path in the network
     *
     * @param startVertex  starting vertex
     * @param targetVertex target vertex
     * @return path weight; {@link Double}.POSITIVE_INFINITY if not found
     */
    public double shortestPathWeight(T startVertex, T targetVertex) {
        return shortestPathWeight(getIndex(startVertex),
                getIndex(targetVertex));
    }

    /**
     * Returns a minimum spanning tree of the network
     *
     * @return minimum spanning tree {@link Network}
     */
    public Network minimumSpanningTreeNetwork() {
        int x, y;
        int index;
        double weight;
        int[] edge = new int[2];
        Heap<Double> minHeap = new Heap<Double>();
        Network<T> resultGraph = new Network<T>();

        if (isEmpty() || !isConnected())
            return resultGraph;

        resultGraph.networkAdjMatrix = new double[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                resultGraph.networkAdjMatrix[i][j] = Double.POSITIVE_INFINITY;
        resultGraph.vertices = (T[]) (new Object[numVertices]);

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        /** Add all edges, which are adjacent to the starting vertex,
         to the heap */
        for (int i = 0; i < numVertices; i++)
            minHeap.addElement(networkAdjMatrix[0][i]);

        while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {
            /** Get the edge with the smallest weight that has exactly
             one vertex already in the resultGraph */
            do {
                weight = (minHeap.removeMin()).doubleValue();
                edge = getEdgeWithWeightOf(weight, visited);
            } while (!indexIsValid(edge[0]) || !indexIsValid(edge[1]));

            x = edge[0];
            y = edge[1];
            if (!visited[x])
                index = x;
            else
                index = y;

            /** Add the new edge and vertex to the resultGraph */
            resultGraph.vertices[index] = this.vertices[index];
            visited[index] = true;
            resultGraph.numVertices++;

            resultGraph.networkAdjMatrix[x][y] = this.networkAdjMatrix[x][y];
            resultGraph.networkAdjMatrix[y][x] = this.networkAdjMatrix[y][x];

            /** Add all edges, that are adjacent to the newly added vertex,
             to the heap */
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && (this.networkAdjMatrix[i][index] <
                        Double.POSITIVE_INFINITY)) {
                    edge[0] = index;
                    edge[1] = i;
                    minHeap.addElement(networkAdjMatrix[index][i]);
                }
            }
        }
        return resultGraph;
    }


    /**
     * Returns the edge with the given weight. Exactly one vertex of the edge must have been visited
     *
     * @param weight  weight
     * @param visited visited edges array
     * @return edges with given weight
     */
    protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = new int[2];
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                if ((networkAdjMatrix[i][j] == weight) && (visited[i] ^ visited[j])) {
                    edge[0] = i;
                    edge[1] = j;
                    return edge;
                }

        /** Will only get to here if a valid edge is not found */
        edge[0] = -1;
        edge[1] = -1;
        return edge;
    }

    /**
     * Creates new arrays to store the contents of the network with ten more slots
     */
    protected void expandCapacity() {
        T[] largerVertices = (T[]) (new Object[vertices.length + 10]);
        double[][] largerAdjMatrix =
                new double[vertices.length * 2][vertices.length + 10];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                largerAdjMatrix[i][j] = networkAdjMatrix[i][j];
            }
            largerVertices[i] = vertices[i];
        }

        vertices = largerVertices;
        networkAdjMatrix = largerAdjMatrix;
    }

}