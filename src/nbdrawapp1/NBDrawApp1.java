package nbdrawapp1;


import java.awt.*;
import static java.awt.Color.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;
import static jdk.internal.org.jline.utils.Colors.h;


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
    
    private final int MAX_SHAPES = 10;
    private int[][] rectangle = new int[MAX_SHAPES][4];
    private int[][] oval = new int[MAX_SHAPES][4];
    private int[][] lines = new int[MAX_SHAPES][4];
    private int rectCount = 0;
    private int ovalCount = 0;
    private int lineCount = 0;
    private int startX,startY;
    



    
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
    
    private JTextArea messageArea;
    
    private JMenuBar menuBar;
     //Moouse event
    class CanvasMouseListener extends MouseAdapter{

        
    
    //Mouse motion event
    
        public void mousePressed(MouseEvent event){
            
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
            }

      }
 }
                public void mouseReleased(MouseEvent event){
            if (rectangleRadioButton.isSelected()) {
                rectCount++; 
            canvas.repaint();}
            else if (ovalRadioButton.isSelected()){
                ovalCount++; 
            canvas.repaint();}
            else if(lineRadioButton.isSelected()){
                lineCount++;
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
                       
                        rectangle[rectCount][0] = Math.min(startX, endX);
                        rectangle[rectCount][1] = Math.min(startY, endY);
                        rectangle[rectCount][2] = Math.abs(endX - startX);
                        rectangle[rectCount][3] = Math.abs(endY - startY);
                        rectColor[rectCount]= SelectedColour;
                    }
                    else if (ovalRadioButton.isSelected()){
                        int endX = event.getX();
                        int endY = event.getY();
                        
                        oval[ovalCount][0] = Math.min(startX, endY);
                        oval[ovalCount][1] = Math.min(startY, endY);
                        oval[ovalCount][2] = Math.abs(endX - startX);
                        oval[ovalCount][3] = Math.abs(endY - startY);
                        ovalColor[ovalCount]=SelectedColour;
                    }
                    else if(lineRadioButton.isSelected()){
                       int endX = event.getX();
                       int endY = event.getY();
                       
                       lines[lineCount][0] = startX;
                       lines[lineCount][1] = startY;
                       lines[lineCount][2] = endX;
                       lines[lineCount][3] = endY;
                       lineColor[lineCount]=SelectedColour;
                    }
                  int shapeCount= rectCount + ovalCount + lineCount;
                  int remainingShapes= MAX_SHAPES - shapeCount;
                  messageArea.setText("You have "+remainingShapes+" Freehand left ...");
                    canvas.repaint();
            }

        }
        
        public void mouseClicked(MouseEvent event){
            
        }
        public void mouseEntered(MouseEvent event){
            
        }
        public void mouseExited(MouseEvent event){
            
        }
       
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
            JMenuItem fileLoadMenuItem = new JMenuItem("Load");
            fileMenu.add(fileLoadMenuItem);
            fileMenu.addSeparator();
            JMenuItem fileExitMenuItem = new JMenuItem("Exit");
            fileMenu.add(fileExitMenuItem);
          menuBar.add(fileMenu);
          JMenu helpMenu = new JMenu("Help");
            JMenuItem helpAboutMenuItem = new JMenuItem("About");
            helpMenu.add(helpAboutMenuItem);
          menuBar.add(helpMenu);
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
               //RadioButton
          freehandRadioButton = new JRadioButton("Free Hand");
         freehandSliderPanel.add( freehandRadioButton);

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

        // Clear button
        clearButton = new JButton("Clear Canvas");
          clearButton.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 50));
        controlPanel.add(clearButton);
        
        clearButton.addActionListener(new ActionListener (){
       
       @Override
       public void actionPerformed(ActionEvent e){
            freehandPixelsCount= 0;
            rectCount=0;
            ovalCount=0;
            lineCount=0;
            canvas.repaint();
            
            messageArea.setText("Canvas Cleared you have "+MAX_FREEHAND_PIXELS+" left & "+ MAX_SHAPES+" Shapes Left!!!");
    



        }
    });

        // Animate button 
        animateButton = new JButton("Animate");
          animateButton.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH - 20, 50));
        controlPanel.add(animateButton);
        
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
        
       // g.setColor(new Color(1.0F, 0.6F, 0.5F));
        //g.drawLine(20,50, 100,210);
        if (fineCheckBox.isSelected()){
        //the 10 pixels lines
        g.setColor(new Color(0.8F,0.8F,0.8F));
        for(int x =0; x < canvas.getWidth(); x+=10){
            g.drawLine(x, 0, x, canvas.getHeight());
        }
        for(int y = 0; y < canvas.getHeight(); y+=10){
            g.drawLine(0, y, canvas.getWidth(),y);
        }
        }
        
    
        if(coarseCheckBox.isSelected()){
        //the 50 pixels line
        g.setColor(new Color(0.6F,0.6F,0.6F));
        for(int x = 0; x < canvas.getWidth();x+=50){
            g.drawLine(x, 0, x, canvas.getHeight());
        }
        for(int y = 0; y< canvas.getHeight(); y+=50){
            g.drawLine( 0,y, canvas.getWidth(),y);
        }
        }
        
        // freehand radio
            for(int i = 0; i < freehandPixelsCount;i++){
            g.setColor(freehandColour[i]);
            int size = fxy[i][2];
            g.fillRect(fxy[i][0], fxy[i][1], size,size);  // Draws squares

        }
            for(int i=0; i<rectCount; i++){
                if(rectangle[i][2]> 0 &&rectangle[i][3]>0){
                g.setColor(rectColor[i]);
            g.drawRect(rectangle[i][0],rectangle[i][1],rectangle[i][2],rectangle[i][3]);
        }}
        
            for(int i=0; i< ovalCount;i++){
                if(oval[i][2]>0 &&oval[i][3]>0){
                g.setColor(ovalColor[i]);  
            g.drawOval(oval[i][0],oval[i][1],oval[i][2],oval[i][3]);
 
            
        }}

            for(int i =0; i<lineCount; i++){
                g.setColor(lineColor[i]);
            g.drawLine(lines[i][0],lines[i][1],lines[i][2],lines[i][3]);
         }
       
        
 
        
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

        

        

        
        
        
    } // end draw method   

    
    public static void main(String args[])
    {
        NBDrawApp1 NBDrawApp1Instance = new NBDrawApp1();

    } // end main method
    
} // end of NBDrawApp1 class
