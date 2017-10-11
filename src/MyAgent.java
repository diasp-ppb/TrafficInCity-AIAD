package src;


public class MyAgent extends jade.core.Agent {
    public void setup () {
        System.out.println ("Hello World");
        addBehaviour(new MyBeh());
    }

    public void take () {
        System.out.println(" Bye....");
    }
}
