package Examples;

public class AddRules {
    private final int x, y;
    private boolean result;


    public AddRules(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean rule(int g) {
        if(g >= x && g <= y) result = true;
        else result = false;

        return result;
    }
}