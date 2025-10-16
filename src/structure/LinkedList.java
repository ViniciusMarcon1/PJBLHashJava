package structure;
import model.Registro;

public class LinkedList {
    private Node head;
    private int length;

    public LinkedList() {
        this.head = null;
        this.length = 0;
    }

    // Getters
    public int getSize() {
        return length;
    }

    public Node getHead() {
        return head;
    }

    // Setters
    public void setHead(Node head) {
        this.head = head;
    }

    //Insere um Registro em ordem crescente de código e retorna o número de colisões (nós visitados antes da inserção).
    public int insertAndCount(Registro registro) {
        int collisions = 0;

        if (head == null) {
            head = new Node(registro);
            length++;
            return collisions; // sem colisão
        }

        int codigo = registro.getCodigo();
        Node current = head;
        Node previous = null;

        // percorre até achar o local correto de inserção
        while (current != null && current.getValue().getCodigo() < codigo) {
            collisions++;
            previous = current;
            current = current.getNext();
        }

        Node newNode = new Node(registro);

        // insere no início
        if (previous == null) {
            newNode.setNext(head);
            head = newNode;
        } else { // insere entre previous e current
            previous.setNext(newNode);
            newNode.setNext(current);
        }

        length++;
        return collisions;
    }

    // Verifica se um código existe na lista (busca ordenada).
    public boolean contains(int codigo) {
        Node current = head;

        while (current != null && current.getValue().getCodigo() <= codigo) {
            if (current.getValue().getCodigo() == codigo)
                return true;
            current = current.getNext();
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.getValue().getCodigoString());
            if (current.getNext() != null) sb.append(" -> ");
            current = current.getNext();
        }
        return sb.toString();
    }
}