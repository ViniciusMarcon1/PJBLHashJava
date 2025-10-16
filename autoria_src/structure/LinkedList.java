package structure;
import model.Registro;

public class LinkedList {
    private Node head;
    private int length;

    public LinkedList() {
        this.head = null;
        this.length = 0;
    }

    
    public int getSize() {
        return length;
    }

    public Node getHead() {
        return head;
    }

    
    public void setHead(Node head) {
        this.head = head;
    }

    
    public int insertAndCount(Registro registro) {
        int collisions = 0;

        if (head == null) {
            head = new Node(registro);
            length++;
            return collisions; 
        }

        int codigo = registro.getCodigo();
        Node current = head;
        Node previous = null;

        
        while (current != null && current.getValue().getCodigo() < codigo) {
            collisions++;
            previous = current;
            current = current.getNext();
        }

        Node newNode = new Node(registro);

        
        if (previous == null) {
            newNode.setNext(head);
            head = newNode;
        } else { 
            previous.setNext(newNode);
            newNode.setNext(current);
        }

        length++;
        return collisions;
    }

    
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