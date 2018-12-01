package frc.robot.misc;

public class Color {

    //Outdated class replaced by CameraOuput, since Color can only return data for a single ball.
    //This does not mean the code is any less broken.

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