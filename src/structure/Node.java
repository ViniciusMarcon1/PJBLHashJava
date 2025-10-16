package structure;
import model.Registro;

public class Node {
    Registro value;
    Node next;
    public Node(Registro value) {
        this.value = value;
        this.next = null;
    }

    //Getters
    Registro getValue(){
        return this.value;
    }
    Node getNext(){
        return this.next;
    }

    //Seters
    public void setValue(Registro value){
        this.value = value;
    }
    public void setNext(Node next){
        this.next = next;
    }

    //To String
    public String toString(){
        return "Node(" + value + ")";
    }
}
