package src;


public class MyBeh extends jade.core.behaviours.Behaviour{
    int c = 0;
    @Override
    public void action() {
        System.out.println(++c);
    }

    @Override
    public boolean done() {
        return c==3;
    }
}
