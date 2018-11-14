package frc.robot.misc;

public class Color {

    public static Color red = new Color("red", 1),
                        blue = new Color("blue", 2),
                        NULL = new Color("null", 0);

    public String name;
    public int ID;

    private Color(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

}