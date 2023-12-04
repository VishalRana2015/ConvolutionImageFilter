import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;

public class ConvolutionMain extends JFrame {

    private static int STRUCT_HEIGHT = 5;
    private JButton saveImageButton;
    private JButton submitButton;
    private JButton resetButton;
    private JButton loadImageButton;
    private JComboBox<String> convolutionComboBox;
    private int identityMatrixIndex = -1;
    private int VERTICAL_STRUT_HEIGHT = 5;
    private int MATRIX_SIZE = 3;
    private int MATRIX_CELL_SPACING_WIDTH = 3;
    private JPanel convolutionMatrixPanel ;
    private JFormattedTextField[][] formattedTextFields;
    private JPanel matrixPanel;

    public static void main(String[] args) {
        ConvolutionMain frame= new ConvolutionMain();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.7;
        constraints.gridx = 0;
        constraints.gridy =0;
        ImageIcon defaultIcon = new ImageIcon("images/jiraya.png");
        ConvolutionImageFilter filter = new ConvolutionImageFilter(ConvolutionImageFilter.getConvolutionMatricesMap().get(ConvolutionImageFilter.IDENTITY_MATRIX_NAME));
        ImageRendererComponent comp = new ImageRendererComponent(defaultIcon.getImage(),filter);
        comp.setSize(1000, 600);
        comp.setMinimumSize(new Dimension(defaultIcon.getIconWidth(), defaultIcon.getIconHeight()));
        comp.setPreferredSize(new Dimension(defaultIcon.getIconWidth(), defaultIcon.getIconHeight()));
        comp.setMaximumSize(new Dimension(defaultIcon.getIconWidth(), defaultIcon.getIconHeight()));
        comp.setLocation(0,0);
        comp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(comp, constraints);
        constraints.weightx = 0.3;
        constraints.gridx = 1;
        JPanel menuPanel = frame.createMenuPanel();
        frame.setValuesForFormattedTextFields();
        panel.add(menuPanel, constraints);
        frame.setContentPane(panel);
        frame.addActions();
        frame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Convolution Matrix Selector -------------------------------------------------------
        JPanel convolutionPanel = new JPanel();
        convolutionPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel convolutionMatrixDropDownLabel = new JLabel("<html>Convolution Matrix</html>");
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        convolutionPanel.add(convolutionMatrixDropDownLabel, constraints);
        convolutionComboBox = new JComboBox<>();
        Iterator<String> iterator = ConvolutionImageFilter.getConvolutionMatricesMap().keySet().iterator();
        int index = 0;
        while ( iterator.hasNext()){
            String key = iterator.next();
            convolutionComboBox.addItem(key);
            if ( key.equals("Identity")){
                identityMatrixIndex = index;
            }
            index++;
        }
        System.out.println(identityMatrixIndex);
        if ( identityMatrixIndex != -1){
            convolutionComboBox.setSelectedIndex(identityMatrixIndex);
        }


        constraints.gridx = 1;
        convolutionPanel.add(convolutionComboBox, constraints);
        convolutionPanel.setPreferredSize(new Dimension(300, 50));
        convolutionPanel.setMinimumSize(new Dimension(300, 50));
        convolutionPanel.setMaximumSize(new Dimension(300, 50));
        convolutionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        menuPanel.add(convolutionPanel);
        convolutionPanel.setBackground(Color.PINK);
        menuPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        menuPanel.add(separator2);
        menuPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));

        // -----------------------------------------------------------
        matrixPanel = matrixPanel();
        menuPanel.add(matrixPanel);
        matrixPanel.add(Box.createVerticalStrut(STRUCT_HEIGHT));
        menuPanel.add(getSeparator());
        menuPanel.add(Box.createVerticalStrut(STRUCT_HEIGHT));
        //------------------------------------------------------------------------
        JPanel buttonPanel = getButtonPanel();

        Dimension dimension = buttonPanel.getMinimumSize();

        System.out.println(dimension);
        menuPanel.add(buttonPanel);
        menuPanel.add(Box.createVerticalStrut(STRUCT_HEIGHT));
        menuPanel.add(getSeparator());
        menuPanel.add(Box.createVerticalGlue());
        return menuPanel;
    }

    private static JSeparator getSeparator() {
        return new JSeparator(JSeparator.HORIZONTAL);
    }

    private JPanel convolutionMatrixPanel(int rows, int cols) {
        JPanel convolutionMatrixPanel = new JPanel();
        convolutionMatrixPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        formattedTextFields = new JFormattedTextField[rows][cols];
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(6);
        decimalFormat.setMinimumFractionDigits(1);
        decimalFormat.setGroupingUsed(false); // disabling group (e.g thousands)
        NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
        DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory(numberFormatter);
        for (int i = 0; i < rows; i++) {
            constraints.gridy = 2 * i;
            convolutionMatrixPanel.add(Box.createHorizontalStrut(5));
            for (int j = 0; j < cols; j++) {
                formattedTextFields[i][j] = new JFormattedTextField(defaultFormatterFactory);
                formattedTextFields[i][j].setColumns(4);
                constraints.gridx = 2 * j;
                formattedTextFields[i][j].setMinimumSize(formattedTextFields[i][j].getPreferredSize());
                formattedTextFields[i][j].setMaximumSize(formattedTextFields[i][j].getPreferredSize());
                convolutionMatrixPanel.add(formattedTextFields[i][j], constraints);
                constraints.gridx = 2 * j + 1;
                convolutionMatrixPanel.add(Box.createHorizontalStrut(MATRIX_CELL_SPACING_WIDTH), constraints);
            }
            constraints.gridy = 2 * i + 1;
            convolutionMatrixPanel.add(Box.createVerticalStrut(MATRIX_CELL_SPACING_WIDTH), constraints);
        }
        convolutionMatrixPanel.setBackground(Color.PINK);
        return convolutionMatrixPanel;
    }

    private JPanel matrixPanel() {
        JPanel matrixPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(matrixPanel, BoxLayout.Y_AXIS);
        matrixPanel.setLayout(boxLayout);
        JLabel matrixLabel = new JLabel("Matrix");
        matrixLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        matrixPanel.add(matrixLabel);
        matrixPanel.add(Box.createVerticalStrut(STRUCT_HEIGHT));
        convolutionMatrixPanel = convolutionMatrixPanel(MATRIX_SIZE, MATRIX_SIZE);
        matrixPanel.add(convolutionMatrixPanel);
        matrixPanel.add(Box.createVerticalStrut(STRUCT_HEIGHT));
        matrixPanel.setPreferredSize(new Dimension(300, 300));
        matrixPanel.setMinimumSize(new Dimension(300, 300));
        matrixPanel.setMaximumSize(new Dimension(300, 300));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 2;
        constraints.gridx = 0;
        constraints.gridy = 0; // Hello world from
        submitButton = new JButton("<html>Submit</html>");
        buttonPanel.add(submitButton, constraints);
        resetButton = new JButton("<html>Reset</html>");
        constraints.gridx = 1;
        buttonPanel.add(resetButton, constraints);
        matrixPanel.add(buttonPanel);
        matrixPanel.add(Box.createVerticalStrut(STRUCT_HEIGHT));
        return matrixPanel;
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        loadImageButton = new JButton("<html>Load<br/>Image</html>");
        saveImageButton = new JButton("<html>Save<br/>Image</html>");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(loadImageButton, constraints);
        constraints.gridx = 1;
        buttonPanel.add(saveImageButton, constraints);
        return buttonPanel;
    }

    private void addActions(){
        convolutionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, Double[][]> convolutionMatrices = ConvolutionImageFilter.getConvolutionMatricesMap();
                String value= (String)((JComboBox)e.getSource()).getSelectedItem();
                Double[][] matrix = convolutionMatrices.get(value);
                if ( formattedTextFields == null || formattedTextFields.length != matrix.length || formattedTextFields[0].length != matrix[0].length){
                    formattedTextFields= new JFormattedTextField[matrix.length][matrix[0].length];
                    JPanel convolutionMatrixPanel = convolutionMatrixPanel(matrix.length, matrix[0].length);
                    matrixPanel.remove(2);
                    matrixPanel.add(convolutionMatrixPanel, 2);
                    matrixPanel.revalidate();
                    matrixPanel.repaint();
                }
                setValuesForFormattedTextFields();
            }
        });
    }

    private void setValuesForFormattedTextFields(){
        String selectedMatrixName = (String)convolutionComboBox.getSelectedItem();
        Double[][] matrix = ConvolutionImageFilter.getConvolutionMatricesMap().get(selectedMatrixName);
        for ( int i = 0; i < matrix.length; i++){
            for ( int j=0; j < matrix[0].length; j++){
                formattedTextFields[i][j].setValue(matrix[i][j]);
            }
        }
   }
}
