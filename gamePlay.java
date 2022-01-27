import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class gamePlay extends JPanel implements KeyListener, ActionListener {

    private final LinkedList<eggs> eggsList;
    int axisX = 0;
    boolean play = true;
    Timer timer;
    int delay = 8;
    int score = 0;
    Image i, i2, i6 , eggsImage , bg;
    private int henAxis = 10;
    int eggY = 117;
    Rectangle rect, eggRect, basketRect;
    private Random rand = new Random();
    int l = 0;
    private int possiblities = 10;



    public gamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        eggsList = new LinkedList<>();
        timer.start();
    }

    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Graphics2D g2D = (Graphics2D) g;

        //bg
        bg = t.getImage("D:\\bgm2.jpg");
        g2D.drawImage(bg, 0, 0, 1360, 700, this);

        //score
        g2D.setFont(new Font("Bahnschrift", Font.BOLD, 30));
        g2D.setColor(Color.BLACK);
        g2D.drawString("" + score, 1250, 38);

        //hen
        i = t.getImage("D:\\real2.png");
        g2D.drawImage(i, henAxis, 45, this);

        // borders
        g2D.setColor(Color.GREEN);
        g2D.fillRect(0, 00, 5, 700);
        g2D.fillRect(0, 0, 1350, 5);
        g2D.fillRect(1340, 0, 6, 700);

        //basket
        i2 = t.getImage("D:\\basket.png");
        g2D.drawImage(i2, axisX, 550, 160, 100, this);

        //hearts
        for (l = 0; l < possiblities; l++) {
            i6 = t.getImage("D:\\heart22.png");
            g2D.drawImage(i6, l * 50, 10, 40, 40, this);
        }


        //falling eggs
        if (!play) {

        } else {
            for (eggs ball : eggsList) {
                ball.drawEggs(g2D);
            }
        }

        // when you lose the game
        if (possiblities == 0) {
            play = false;
            g.setColor(Color.BLACK);
            g.setFont(new Font("branschrift", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " + score, getBounds().width - 830, 400);

            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("branschrift", Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart or (Esc) to Exit", getBounds().width - 855, 450);

        }


        g2D.dispose();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //to exit
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        //to restart
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            if (!play){
                axisX=0;
                henAxis=10;
                score=0;
                possiblities= 10;

            }

        }

        //Basket movement
        if (possiblities > 0) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) { //motion towards right
                if (axisX >= 1190) {
                    axisX = 1190;
                } else {
                    moveRight();
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) { //motion towards left
                if (axisX <= 10) {
                    axisX = 10;
                } else {
                    moveLeft();

                }
            }

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {

            //hen movement
            if (henAxis == 1250) {
                henAxis = 10;
            } else {
                moveRightHen();
            }

            Iterator<gamePlay.eggs> it = eggsList.iterator();
            while (it.hasNext()) {
                gamePlay.eggs egg = it.next();
                egg.x = henAxis;
                eggY = egg.y;

                //egg movement
                if (henAxis >= 1200 || henAxis <= 15) {
                    it.remove();
                } else {
                    egg.y += 10;

                }

                //if egg and basket collides
                if (basketRect.intersects(eggRect)) {
                    score += 5;
                    it.remove();
                    try {
                        sounds(new URL("file:d:/mixkit-retro-arcade-casino-notification-211 (1).wav")); //audio for scoring
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ioException) {
                        ioException.printStackTrace();
                    }
                }

                //if egg doesn't collide to basket
                if (!(basketRect.intersects(eggRect)) && eggY == 670) {
                    try {
                        sounds(new URL("file:d:/mixkit-player-jumping-in-a-video-game-2043.wav"));     //audio for losing
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ioException) {
                        ioException.printStackTrace();
                    }
                    //decrementing life
                    possiblities--;
                }

            }
        }

        //to add eggs
        try {
            addEggs();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //collision detector
        rect = new Rectangle(axisX - 20, 550, 160, 1);
        eggRect = new Rectangle(henAxis, eggY, 10, 5);
        basketRect = rect;


        repaint();
    }

    //basket motion functions
    private void moveRight() {
        play = true;
        axisX += 20;
    }

    private void moveLeft() {
        play = true;
        axisX -= 20;
    }

    //hen motion function (towards right only since hen moves in one direction only)
    private void moveRightHen() {
        timer.setDelay(16);//separate delay for the speed of hen
        henAxis += 4;
    }



    //adds egg randomly
    private void addEggs() throws IOException {
        if (rand.nextInt(900) > 7) return; //reduce the frequency of adding eggs
        eggsList.add(new gamePlay.eggs());
    }


    private class eggs {
        private int x = henAxis;
        private int y = 140;

        public eggs() throws IOException {
            eggsImage = getegg();
        }


        public void drawEggs(Graphics g) {
            g.drawImage(eggsImage, x, y, 85, 85, null);
        }

        private BufferedImage getegg() throws IOException {
            BufferedImage img = ImageIO.read(new File("D:\\egg.png")); //image of egg
            return img;
        }


    }

    //sound function to add audio in program
    private  void  sounds(URL url) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        URL file = url;
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        clip.start();
        }
}
