package snakegame;
import javax.swing.*;
import java.awt.*;//color class
import java.awt.event.*;//implements ActionListener:to catch the actions like uo,down,left,right using mouse or keyboard
/**
 *
 * @author Arindam
 */
//using the pannel to use the frame.unit of jframe=jpannel 
//jpanel present in swing library
public class Board extends JPanel implements ActionListener{//action listener ka functionaddkeylistener()
    private Image apple;
    private Image dot;
    private Image head;
    
    private final int ALL_DOTS=900;//max size for dots
    private final int DOT_SIZE=10;
    private final int RANDOM_POSITION=29;
    
    private int apple_x;
    private int apple_y;
    
    private final int x[]=new int[ALL_DOTS];
    private final int y[]=new int[ALL_DOTS];
    
    private boolean leftDirection=false;
    private boolean rightDirection=true;//default right move 
    private boolean upDirection=false;
    private boolean downDirection=false;
    
    private boolean inGame=true;
    
    private int dots=3;//globaly declearing,,,initially 3 dots ka snake hoga.
    private Timer timer;
    Board(){
        addKeyListener(new TAdapter());//from action listener
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300,300));//(width,height)
        setFocusable(true);//agar isko use nehi karte toh board start hone k baad hi game start ho jayega ,,to avoid this we use focusable()
        //now start the game
        loadImages();//actually the snake build on images,so we made this fucntion
        initGame();//game function
    }
    public void loadImages(){
        //iamgeicon is a class in jframe
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple=i1.getImage();
        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot=i2.getImage();
        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
        head=i3.getImage(); 
    }
    public void initGame(){
        for(int i=0;i<dots;i++){
            y[i]=50;
            x[i]=50-i*DOT_SIZE;
        }
        locateApple();//create a function to initially place the apple
        timer=new Timer(140,this);
        timer.start();
    }
    
    
    public void locateApple(){
        int r=(int)( Math.random()*RANDOM_POSITION);//a random position for the apple_x;
        apple_x=r*DOT_SIZE;
        r=(int)( Math.random()*RANDOM_POSITION);//a random position apple_y
        apple_y=r*DOT_SIZE;
    }
    public void paintComponent(Graphics g){//paintComponent is a method of ghraphics class
        super.paintComponent(g);//to call the parent
        draw(g);
    }
    
    public void draw(Graphics g){
        if(inGame){
        g.drawImage(apple,apple_x,apple_y,this);
        for(int i=0;i<dots;i++){
            if(i==0){
                g.drawImage(head,x[i],y[i],this);
            }else{
                g.drawImage(dot,x[i],y[i],this);
            }
        }
        Toolkit.getDefaultToolkit().sync();//to initialise default
        }else{
            gameOver(g);
        }
        
    }
    public void gameOver(Graphics g){
        String msg="!!Game Over!!";
        Font font=new Font("SAN_SARIF",Font.BOLD,14);//in Font class Font(family,type,size)
        FontMetrics metrices=getFontMetrics(font);//
        g.setColor(Color.RED);
        g.drawString(msg,(300-metrices.stringWidth(msg))/2,300/2);
    }
    public void move(){
        for(int i=dots;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        if(leftDirection){
            x[0]=x[0]-DOT_SIZE;
        }
        if(rightDirection){
            x[0]=x[0]+DOT_SIZE;
        }
        if(upDirection){
            y[0]=y[0]-DOT_SIZE;
        }
        if(downDirection){
            y[0]=y[0]+DOT_SIZE;
        }
        //x[0]+=DOT_SIZE;//only for head
       // y[0]+=DOT_SIZE;//only for head//IF WE icrease y also snake will go diagonaly
    }
    public void eat(){
        if((x[0]==apple_x)&&(y[0]==apple_y)){
            dots++;
            locateApple();
        }
    }
    public void collision(){
        for(int i=dots;i>0;i--){
            if((i>4)&&(x[0]==x[i])&&(y[0]==y[i])){
                inGame=false;
            }
        }
        if(y[0]>=300){
            inGame=false;
        }
        if(x[0]>=300){
            inGame=false;
        }
        if(y[0]<0){
            inGame=false;
        }
        if(x[0]<0){
            inGame=false;
        }
        if(!inGame){
            timer.stop();
        }
    }
    public void actionPerformed(ActionEvent ae){//overriding 
        if(inGame){
        collision();
        eat();
        move();//to move the snake;
        }
        repaint();//refresh the screen similar to pack()     
    }
    public class TAdapter extends KeyAdapter{//
        @Override
        public void keyPressed(KeyEvent e){
            int key=e.getKeyCode();
            if(key==KeyEvent.VK_LEFT && (!rightDirection)){//if we want to move it to the left drecton we cant directly turn it,,,at first it  have to go up or down then can move to other direction.
                leftDirection=true;                
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection=true;                
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_UP && (!downDirection)){
                upDirection=true;                
                leftDirection=false;
                rightDirection=false;
            }
            if(key==KeyEvent.VK_DOWN && (!upDirection)){
                downDirection=true;                
                leftDirection=false;
                rightDirection=false;
            }
        }
    }
    
}
