package nbdrawapp1;


import java.awt.*;
import static java.awt.Color.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.internal.org.jline.utils.Colors.h;
import javax.swing.Timer;


public class NBDrawApp1 extends JFrame
{
    // GUI Component dimentsions.
    private final int CANVAS_INITIAL_WIDTH = 800;
    private final int CANVAS_INITIAL_HEIGHT = 640;
    private final int CONTROL_PANEL_WIDTH = 200;
    private final int MESSAGE_AREA_HEIGHT = 100;
    
    private final int MAX_FREEHAND_PIXELS =10000;
    private Color[]freehandColour = new Color[MAX_FREEHAND_PIXELS];
    private int[][] fxy = new int[MAX_FREEHAND_PIXELS][3];
    private int freehandPixelsCount = 0;
    private int freehandSize = 5;
    
    private final int MAX_SHAPES = 50;
    private int[][] rectangle = new int[MAX_SHAPES][5];
    private int[][] oval = new int[MAX_SHAPES][5];
    private int[][] lines = new int[MAX_SHAPES][5];
    private int rectCount = 0;
    private int ovalCount = 0;
    private int lineCount = 0;
    private int startX,startY;
    private int tempX, tempY, tempWidth, tempHeight;
// Global variables for direction
    private int dxRectangle = 5;
    private int dyRectangle = 5; // Rectangle movement
    private int dxOval = 5;
    private int dyOval = 5;           // Oval movement
        
    



    
    // Drawing area class (inner class).
    class Canvas extends JPanel
    {
        // Called every time there is a change in the canvas contents.
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);        
            draw(g);
        }
    } // end inner class Canvas
    
    
    private Canvas canvas;
    
    private JPanel controlPanel;
    private JLabel coordinatesLabel;
    private JRadioButton lineRadioButton, ovalRadioButton, rectangleRadioButton, freehandRadioButton,buttonGroup;
    private JSlider freehandSizeSlider;
    private JCheckBox fineCheckBox, coarseCheckBox;
    private JButton colourButton, clearButton, animateButton;
    private Color SelectedColour = new Color(0.0F,0.0F,0.0F);
    private Color[] rectColor = new Color[MAX_SHAPES];
    private Color[] ovalColor = new Color[MAX_SHAPES];
    private Color[] lineColor = new Color[MAX_SHAPES];
    private boolean isMousePressed = false;

    
    private JTextArea messageArea;
    
    private JMenuBar menuBar;
     //Moouse event
    class CanvasMouseListener extends MouseAdapter{

        
    
    //Mouse motion event
    
        public void mousePressed(MouseEvent event){
            tempX = event.getX();
            tempY = event.getY();
            tempHeight = 0;
            startX = event.getX();
            startY = event.getY(); 
            
            isMousePressed = true;

            
            if(freehandRadioButton.isSelected()&& freehandPixelsCount< MAX_FREEHAND_PIXELS){
                int x = event.getX();
                int y = event.getY();
                
                
                freehandColour[freehandPixelsCount]= SelectedColour;
                fxy[freehandPixelsCount][0] = x;
                fxy[freehandPixelsCount][1]= y;
                fxy[freehandPixelsCount][2]= freehandSize;
                freehandPixelsCount++;
                
                int remainingInk = MAX_FREEHAND_PIXELS - freehandPixelsCount;
                messageArea.setText("You have "+remainingInk+" Frehand left ...");
                
                canvas.repaint();}
            
      else if (rectangleRadioButton.isSelected()||ovalRadioButton.isSelected()||lineRadioButton.isSelected()){
            if(rectCount<MAX_SHAPES ||ovalCount<MAX_SHAPES||lineCount<MAX_SHAPES){
            
               startX = event.getX();
               startY = event.getY();
               canvas.repaint();
            }

      }
            
 }
      public void mouseReleased(MouseEvent event){
          isMousePressed = false;
          
            if (rectangleRadioButton.isSelected()) {
                rectCount++; 
                rectColor[rectCount - 1] = SelectedColour; 
            canvas.repaint();}
            else if (ovalRadioButton.isSelected()){
                ovalCount++; 
                ovalColor[ovalCount - 1] = SelectedColour;
            canvas.repaint();}
            else if(lineRadioButton.isSelected()){
                lineCount++;
                lineColor[lineCount - 1] = SelectedColour; 
            canvas.repaint();}
                }
    
    }
    class CanvasMouseMotionListener implements MouseMotionListener{
            //private int startX,startY; ----> The Start of the shape was messed up because i re declared this but with this gone its all good.

        
        
        public void mouseMoved(MouseEvent event){
            int x = event.getX();
            int y = event.getY();
            //this is the Drawing position
            coordinatesLabel.setText("Mouse Position is: "+x+","+y);
        }
        public void mouseDragged(MouseEvent event){
            if (isMousePressed){
            if(freehandRadioButton.isSelected()&& freehandPixelsCount<MAX_FREEHAND_PIXELS){
            int x = event.getX();
            int y = event.getY();
            coordinatesLabel.setText("Mouse Position is: "+x+","+y);
          freehandColour[freehandPixelsCount] = SelectedColour;
         fxy[freehandPixelsCount][0] = x;   // x-coordinates
         fxy[freehandPixelsCount][1] = y;   // y-coordinates
         fxy[freehandPixelsCount][2] = freehandSizeSlider.getValue();    // dimention
         freehandPixelsCount++;
         
         //Thhis is for the message Area
        
         int remainingInk = MAX_FREEHAND_PIXELS - freehandPixelsCount;
         messageArea.setText("You have "+remainingInk+" Freehand left!!!");
         
                  canvas.repaint();
}
         
         else if (rectangleRadioButton.isSelected()||ovalRadioButton.isSelected()||lineRadioButton.isSelected()){
                    if(rectangleRadioButton.isSelected()){
                        int endX = event.getX();
                        int endY = event.getY();
                       
                        rectangle[rectCount][0] = tempX = Math.min(startX, endX);
                        rectangle[rectCount][1] = tempY = Math.min(startY, endY);
                        rectangle[rectCount][2] = tempWidth = Math.abs(endX - startX);
                        rectangle[rectCount][3] = tempHeight = Math.abs(endY - startY);
                        rectangle[rectCount][4] = freehandSizeSlider.getValue(); 
                        canvas.repaint();
          
                    }
                    else if (ovalRadioButton.isSelected()){
                        int endX = event.getX();
                        int endY = event.getY();
                        
                        oval[ovalCount][0] = tempX = Math.min(startX, endY);
                        oval[ovalCount][1] = tempY = Math.min(startY, endY);
                        oval[ovalCount][2] = tempWidth = Math.abs(endX - startX);
                        oval[ovalCount][3] = tempHeight = Math.abs(endY - startY);
                        oval[ovalCount][4] = freehandSizeSlider.getValue();
                        canvas.repaint();
                       
                    }
                    else if(lineRadioButton.isSelected()){
                       int endX = event.getX();
                       int endY = event.getY();
                       
                       lines[lineCount][0] = startX;
                       lines[lineCount][1] = startY;
                       lines[lineCount][2] = tempX = endX;
                       lines[lineCount][3] = tempY = endY;
                       lines[lineCount][4] = freehandSizeSlider.getValue();
                       canvas.repaint();
                       
                      
                    }canvas.repaint();
                  int shapeCount= rectCount + ovalCount + lineCount;
                  int remainingShapes= MAX_SHAPES - shapeCount;
                  messageArea.setText("You have "+remainingShapes+" Freehand left ...");
                   canvas.repaint();
            }

        }
        }
        
        public void mouseClicked(MouseEvent event){
            
        }
        public void mouseEntered(MouseEvent event){
            
        }
        public void mouseExited(MouseEvent event){
            
        }
        public void mouseReleased(MouseEvent event){}
       
    }
    class FreehandSliderListener implements ChangeListener{
        public void stateChanged(ChangeEvent event){
            freehandSize = freehandSizeSlider.getValue();
        
        }
    }
    
 

    
    
    /*****************************************************************
     * 
     * Constructor method starts here
     *    ... and goes on for quite a few lines of code 
     */
    public NBDrawApp1(){
        
        setTitle("Drawing Application (da1)");
        setLayout(new BorderLayout());  // Layout manager for the frame.
        

        // Canvas
        canvas = new Canvas();
          canvas.setBorder(new TitledBorder(new EtchedBorder(), "Canvas"));
          canvas.setPreferredSize(new Dimension(CANVAS_INITIAL_WIDTH, CANVAS_INITIAL_HEIGHT));
          // next line changes the cursor's rendering whenever the mouse drifts onto the canvas
          canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        add(canvas, BorderLayout.CENTER);
       
        
        // Menu bar
        menuBar = new JMenuBar();
          JMenu fileMenu = new JMenu("File");
            JMenuItem fileSaveMenuItem = new JMenuItem("Save");
            fileMenu.add(fileSaveMenuItem);
            
            fileSaveMenuItem.addActionListener(new ActionListener (){
                
                public void actionPerformed(ActionEvent e){
                    try{
                        //To save to the users chosen file and file name
                        
                        JFileChooser fileChooser= new JFileChooser();
                        fileChooser.setDialogTitle("Save your Drawing!!");
                        
                        int userSelection = fileChooser.showSaveDialog(null);
                        
                        if(userSelection == JFileChooser.APPROVE_OPTION){
                            File file =fileChooser.getSelectedFile();
                            
                            if(!file.getName().endsWith(".dat")){
                                file = new File(file.getAbsolutePath() + ".dat");
                            }
                        
                        
                        
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream fh = new ObjectOutputStream(fos);
                    
                    fh.writeInt(rectCount);
                    fh.writeObject(rectangle);
                    fh.writeObject(rectColor);
                    
                    fh.writeInt(ovalCount);
                    fh.writeObject(oval);
                    fh.writeObject(ovalColor);
                    
                    fh.writeInt(lineCount);
                    fh.writeObject(lines);
                    fh.writeObject(lineColor);
                    
                    fh.writeInt(freehandPixelsCount);
                    fh.writeObject(fxy);
                    fh.writeObject(freehandColour);
                    
                    fh.close();
                    fos.close();
                    
                    JOptionPane.showMessageDialog(null,"Your Drawing is saved to"+ file.getAbsolutePath());
                        }  
                }catch(IOException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Can't Save your Drawing");
                }
               }
            });
            
  
            
            JMenuItem fileLoadMenuItem = new JMenuItem("Load");
            fileMenu.add(fileLoadMenuItem);
            
            fileLoadMenuItem.addActionListener(new ActionListener(){
                
                public void actionPerformed(ActionEvent e){
                     
                    
                    try{                        
                        JFileChooser fileChooser= new JFileChooser();
                        fileChooser.setDialogTitle("Load your Drawing!!");
                        
                        System.out.println("Displaying file chooser...");
                        
                        int userSelection = fileChooser.showOpenDialog(null);
                        
                        if(userSelection == JFileChooser.APPROVE_OPTION){
                            File file =fileChooser.getSelectedFile();
                            
                            if(!file.getName().endsWith(".dat")){
                                file = new File(file.getAbsolutePath() + ".dat");
                            }
                    
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream fh = new ObjectInputStream(fis);
                    
                    rectCount = (int) fh.readInt();
                    rectangle = (int[][])fh.readObject();
                    rectColor = (Color[])fh.readObject();
                    
                    ovalCount = (int) fh.readInt();
                    oval = (int[][])fh.readObject();
                    ovalColor = (Color[])fh.readObject();
                    
                    lineCount = (int) fh.readInt();
                    lines = (int[][])fh.readObject();
                    lineColor = (Color[])fh.readObject();
                    
                   freehandPixelsCount = (int) fh.readInt();
                    fxy = (int[][])fh.readObject();
                    freehandColour = (Color[])fh.readObject();
                    
                    fh.close();
                    fis.close();
                    
                    canvas.repaint();
               
                }else {
                JOptionPane.showMessageDialog(null,"Your Drawing is being opened");
            }
                }   catch (ClassNotFoundException ex) {
                        Logger.getLogger(NBDrawApp1.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(NBDrawApp1.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(NBDrawApp1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
    });
            
            fileMenu.addSeparator();
            JMenuItem fileExitMenuItem = new JMenuItem("Exit");
            fileMenu.add(fileExitMenuItem);
          menuBar.add(fileMenu);
          
          fileExitMenuItem.addActionListener(new ActionListener(){
              
              public void actionPerformed(ActionEvent e){
                System.exit(0);  
              }
          });
          
          JMenu helpMenu = new JMenu("Help");
            JMenuItem helpAboutMenuItem = new JMenuItem("About");
            helpMenu.add(helpAboutMenuItem);
          menuBar.add(helpMenu);
          
          helpAboutMenuItem.addActionListener(new ActionListener(){
              
              public void actionPerformed(ActionEvent e){
                  Component frame = null;
                  JOptionPane.showMessageDialog(frame, "This is a Drawing application that allows you to draw a simplar version of paint!!!");
              }
          });
          
        add(menuBar, BorderLayout.PAGE_START);
        
    
        // Control Panel
        controlPanel = new JPanel();
          controlPanel.setBorder(new TitledBorder(new EtchedBorder(), "Control Panel"));
          controlPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH, CANVAS_INITIAL_HEIGHT));
          // the following two lines put the control panel in a scroll pane (nicer?).      
          JScrollPane controlPanelScrollPane = new JScrollPane(controlPanel);
          controlPanelScrollPane.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH + 30, CANVAS_INITIAL_HEIGHT));
        add(controlPanelScrollPane, BorderLayout.LINE_START);        

        
        // Control Panel contents are specified in the next section eg: 
        //    mouse coords panel; 
        //    shape tools panel; 
        //    trace-slider panel; 
        //    grid panel; 
        //    colour choice panel; 
        //    "clear" n "animate" buttons
        
        // Mouse Coordinates panel
        JPanel coordinatesPanel = new JPanel();
          coordinatesPanel.setBorder(new TitledBorder(new EtchedBorder(), "Drawing Position"));
          coordinatesPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 60));
          coordinatesLabel = new JLabel();
          coordinatesPanel.add(coordinatesLabel);
        controlPanel.add(coordinatesPanel);
        coordinatesLabel.setText("SOme TEXT");
        
        // Drawing tools panel
        JPanel drawingToolsPanel = new JPanel();
          drawingToolsPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 140));
          drawingToolsPanel.setLayout(new GridLayout(0, 1));
          drawingToolsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Drawing Tools"));
        controlPanel.add(drawingToolsPanel);
        
        

        // Radio button
     
         rectangleRadioButton = new JRadioButton("Rectangle");
         drawingToolsPanel.add(rectangleRadioButton);
         ovalRadioButton = new JRadioButton("Oval");
         drawingToolsPanel.add(ovalRadioButton);
         lineRadioButton = new JRadioButton("Line");
         drawingToolsPanel.add(lineRadioButton);
         freehandRadioButton = new JRadioButton("Free Hand");
         drawingToolsPanel.add(freehandRadioButton);
         
        
        // Freehand trace size slider
        JPanel freehandSliderPanel = new JPanel();
          freehandSliderPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 90));
          drawingToolsPanel.setLayout(new GridLayout(0, 1));
          freehandSliderPanel.setBorder(new TitledBorder(new EtchedBorder(), "Freehand Size"));
        controlPanel.add(freehandSliderPanel);
        
        freehandSizeSlider = new JSlider(1,50,5);
        //freehandSizeSlider.setMajorTickSpacing(1);
        //freehandSizeSlider.setMinorTickSpacing();
        freehandSizeSlider.setPaintTicks(true);
        freehandSizeSlider.setPaintLabels(true);
        freehandSizeSlider.addChangeListener(new FreehandSliderListener());
        
        freehandSliderPanel.add(freehandSizeSlider);
        freehandSliderPanel.add(new JLabel("Size: "+ freehandSizeSlider.getValue()));


                 //Button to group the radio button 
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rectangleRadioButton);
        buttonGroup.add(ovalRadioButton);
        buttonGroup.add(lineRadioButton);
        buttonGroup.add(freehandRadioButton);

         
               

        // Grid Panel
        JPanel gridPanel = new JPanel();
          gridPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 80));
          gridPanel.setLayout(new GridLayout(0, 1));
          gridPanel.setBorder(new TitledBorder(new EtchedBorder(), "Grid"));
        controlPanel.add(gridPanel);
                  //CheckBox 
        fineCheckBox = new JCheckBox("Fine");
        coarseCheckBox = new JCheckBox("Coarse");
        gridPanel.add(fineCheckBox);
        gridPanel.add(coarseCheckBox);
        
        
        // Colour Panel
        JPanel colourPanel = new JPanel();
          colourPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 90));
          colourPanel.setBorder(new TitledBorder(new EtchedBorder(), "Colour"));
          colourButton = new JButton();
          colourButton.setPreferredSize(new Dimension(50, 50));
          colourButton.setBackground(SelectedColour);
          colourPanel.add(colourButton);
        controlPanel.add(colourPanel);
        
        colourButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            Color newColour = JColorChooser.showDialog(null,"Chose your colour: ",SelectedColour);
            if(newColour != null){
                SelectedColour = newColour;
                colourButton.setBackground(SelectedColour);
            }
        }
    });
        // Animate button 
        

        animateButton = new JButton("Animate");
          animateButton.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 50));
        controlPanel.add(animateButton);
        
        
        Timer animationTimer;
        animationTimer = new Timer(200,new ActionListener (){
            public void actionPerformed(ActionEvent e){
                
                try{
                    if(rectangle !=null && rectangle.length>0){
                         for (int i = 0; i < rectangle.length; i++) {
                        rectangle[i][0] += dxRectangle;
                        rectangle[i][1] += dyRectangle;
                        
                        // Check for collisions with canvas edges
                        if (rectangle[i][0] <= 0 || rectangle[i][0] + rectangle[i][2] >= canvas.getWidth()) {
                            dxRectangle = -dxRectangle; // Reverse horizontal direction
                        }
                        if (rectangle[i][1] <= 0 || rectangle[i][1] + rectangle[i][3] >= canvas.getHeight()) {
                            dyRectangle = -dyRectangle; // Reverse vertical direction
                        }
                    }
                    }
                    
                    if(oval !=null && oval.length>0){
                        for(int i =0; i <oval.length; i++){
                        oval[i][0] += dxOval;
                        oval[i][1] += dyOval;
                        
                        // Check for collisions with canvas edges
                        if (oval[i][0] <= 0 || oval[i][0] + oval[i][2] >= canvas.getWidth()) {
                            dxOval = -dxOval; // Reverse horizontal direction
                        }
                        if (oval[i][1] <= 0 || oval[i][1] + oval[i][3] >= canvas.getHeight()) {
                            dyOval = -dyOval; // Reverse vertical direction
                        }
                    }
                    }
                    
                    canvas.repaint();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        animateButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
   
                if ((rectangle !=null && rectangle.length > 0 )|| (oval !=null && oval.length > 0 )){
                    if(animationTimer.isRunning()){
                        animationTimer.stop();
                        animateButton.setText("Animate");
                    }else{
                        animationTimer.start();
                        animateButton.setText("Stop Animation");
                    }
                } else{
                    JOptionPane.showMessageDialog(null,"No shapes to animate");
                }
            }
            
        });
        
        // Clear button
        clearButton = new JButton("Clear Canvas");
          clearButton.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 50));
        controlPanel.add(clearButton);
        
        clearButton.addActionListener(new ActionListener (){
       
       @Override
       public void actionPerformed(ActionEvent e){
               tempX = 0;
               tempY = 0;
               tempWidth = 0;
               tempHeight = 0;
               startX = 0;
               startY = 0;
            freehandPixelsCount= 0;
            rectCount=0;
            ovalCount=0;
            lineCount=0;
     
            canvas.repaint();
            
            messageArea.setText("Canvas Cleared! You have " + MAX_FREEHAND_PIXELS + " Freehand left & " + MAX_SHAPES + " Shapes Left!!!");
            canvas.repaint();
            animationTimer.stop();
    



        }
    });


        
        // that completes the control panel section

        
        // Message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(canvas.getBackground());
        JScrollPane textAreaScrollPane = new JScrollPane(messageArea);
        textAreaScrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Message Area"));
        textAreaScrollPane.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH + CANVAS_INITIAL_WIDTH, MESSAGE_AREA_HEIGHT));
        add(textAreaScrollPane, BorderLayout.PAGE_END);
        
         messageArea.setText("You have" +MAX_FREEHAND_PIXELS +" squares left &"+MAX_SHAPES+"Shapes left;;;;");
        

        canvas.addMouseListener(new CanvasMouseListener());
        canvas.addMouseMotionListener(new CanvasMouseMotionListener());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setVisible(true);
        
    }  // end of the NBDrawApp1 constructor method
    
    // Called by the canvas' paintComponent method
    void draw(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          //Gird CheckBox event
        
        fineCheckBox.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                canvas.repaint();
            }
        });
        coarseCheckBox.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e){
            canvas.repaint();
        }
    });
                // Freehand radio button
        freehandRadioButton.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e){
            canvas.repaint();
        }
        });
        rectangleRadioButton.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                canvas.repaint();
            }
        });
        ovalRadioButton.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                canvas.repaint();
            }
        });
        lineRadioButton.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                canvas.repaint();
            }
    });
        freehandSizeSlider.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent event){
            freehandSize = freehandSizeSlider.getValue();
        
        }
        });

        
       // g.setColor(new Color(1.0F, 0.6F, 0.5F));
        //g.drawLine(20,50, 100,210);
        if (fineCheckBox.isSelected()){
        //the 10 pixels lines
        g2.setColor(new Color(0.8F,0.8F,0.8F));
        for(int x =0; x < canvas.getWidth(); x+=10){
            g2.drawLine(x, 0, x, canvas.getHeight());
        }
        for(int y = 0; y < canvas.getHeight(); y+=10){
            g2.drawLine(0, y, canvas.getWidth(),y);
        }
        }
        
    
        if(coarseCheckBox.isSelected()){
        //the 50 pixels line
        g2.setColor(new Color(0.6F,0.6F,0.6F));
        for(int x = 0; x < canvas.getWidth();x+=50){
            g2.drawLine(x, 0, x, canvas.getHeight());
        }
        for(int y = 0; y < canvas.getHeight(); y+=50){
            g2.drawLine( 0,y, canvas.getWidth(),y);
        }
        }
                   
             // Drawing temporary (rubber-banding) rectangle
    if (rectangleRadioButton.isSelected() && isMousePressed) {
        g2.setColor(SelectedColour);
        g2.setStroke(new BasicStroke(freehandSizeSlider.getValue()));
        g2.drawRect(tempX, tempY, tempWidth, tempHeight);
        canvas.repaint();
    }

    // Drawing temporary (rubber-banding) oval
    if (ovalRadioButton.isSelected() && isMousePressed) {
        g2.setColor(SelectedColour);
        g2.setStroke(new BasicStroke(freehandSizeSlider.getValue()));
        g2.drawOval(tempX, tempY, tempWidth, tempHeight);
        canvas.repaint();
    }

    // Drawing temporary (rubber-banding) line
    if (lineRadioButton.isSelected() && isMousePressed) {
        g2.setColor(SelectedColour);
        g2.setStroke(new BasicStroke(freehandSizeSlider.getValue()));
        g2.drawLine(startX, startY, tempX, tempY);
        canvas.repaint();
    }
      
        
        // freehand radio
            for(int i = 0; i < freehandPixelsCount;i++){
            g2.setColor(freehandColour[i]);
            g2.setStroke(new BasicStroke(fxy[i][2]));
            g2.fillRect(fxy[i][0], fxy[i][1], fxy[i][2], fxy[i][2]);  // Draws squares

        }
            for(int i=0; i < rectCount; i++){
                if(rectangle[i][2]> 0 &&rectangle[i][3]>0){
                g2.setColor(rectColor[i]);
                g2.setStroke(new BasicStroke(rectangle[i][4])); 
            g2.drawRect(rectangle[i][0],rectangle[i][1],rectangle[i][2],rectangle[i][3]);
            
        }
            }
        
            for(int i=0; i < ovalCount;i++){
                if(oval[i][2]>0 &&oval[i][3]>0){
                g2.setColor(ovalColor[i]);  
                g2.setStroke(new BasicStroke(oval[i][4])); 
            g2.drawOval(oval[i][0],oval[i][1],oval[i][2],oval[i][3]);
            
 
            
        }
            }

            for(int i =0; i<lineCount; i++){
                g2.setColor (lineColor[i]);
                g2.setStroke(new BasicStroke(lines[i][4])); 
            g2.drawLine(lines[i][0],lines[i][1],lines[i][2],lines[i][3]);
            
         }
  
        
  
        
    } // end draw method   

    
    public static void main(String args[])
    {
        NBDrawApp1 NBDrawApp1Instance = new NBDrawApp1();

    } // end main method
    
} // end of NBDrawApp1 class

