import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()->
        {
            gamePlay play = new gamePlay();
            JFrame frame = new JFrame("The Egg Catcher Game");
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(80,70,1360,700);
            frame.add(play);


        });

    }
}
