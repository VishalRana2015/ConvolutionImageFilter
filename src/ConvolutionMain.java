import com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class ConvolutionMain extends JFrame {

    private static int STRUCT_HEIGHT = 5;
    private JButton saveImageButton;
    private JButton submitButton;
    private JButton resetButton;
    private JButton loadImageButton;
    private JComboBox<String> convolutionMatrixSelector;
    private int VERTICAL_STRUT_HEIGHT = 5;
    private int MATRIX_SiZE = 3;
    private int MATRIX_CELL_SPACING_WIDTH = 3;

    private static JPanel formatterPanel() throws ParseException {
        JPanel panel = new JPanel();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(6);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setMinimumFractionDigits(1);
        NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setValueClass(Double.class);
        DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory(numberFormatter);

        JFormattedTextField formattedTextField = new JFormattedTextField(defaultFormatterFactory);
        formattedTextField.setColumns(20);
        panel.add(formattedTextField);
        JFormattedTextField field2 = new JFormattedTextField();
        field2.setColumns(20);
        panel.add(field2);

        JFormattedTextField textField1 = new JFormattedTextField(new Float(10.01));
        textField1.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                NumberFormat format = DecimalFormat.getInstance();
                format.setMinimumFractionDigits(2);
                format.setMaximumFractionDigits(2);
                format.setRoundingMode(RoundingMode.HALF_UP);
                InternationalFormatter formatter = new InternationalFormatter(format);
                formatter.setAllowsInvalid(false);
                return formatter;
            }
        });
        textField1.setColumns(20);
        panel.add(textField1);
        return panel;
    }

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
        ConvolutionImageFilter filter = new ConvolutionImageFilter(ConvolutionImageFilter.sobelMatrix);
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
        panel.add(menuPanel, constraints);
        frame.setContentPane(panel);
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
        convolutionMatrixSelector = new JComboBox<>();
        convolutionMatrixSelector.addItem("Blur");
        convolutionMatrixSelector.addItem("Sharp");
        convolutionMatrixSelector.addItem("Edge");

        constraints.gridx = 1;
        convolutionPanel.add(convolutionMatrixSelector, constraints);
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
        JPanel matrixPanel = matrixPanel();
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
        rows = cols = MATRIX_SiZE;
        JFormattedTextField[][] formattedTextFields = new JFormattedTextField[rows][cols];
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(1);
        decimalFormat.setGroupingUsed(false); // disabling group (e.g thousands)
        NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
        DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory(numberFormatter);
        for (int i = 0; i < rows; i++) {
            constraints.gridy = 2 * i;
            convolutionMatrixPanel.add(Box.createHorizontalStrut(5));
            for (int j = 0; j < cols; j++) {
                formattedTextFields[i][j] = new JFormattedTextField(defaultFormatterFactory);
                formattedTextFields[i][j].setColumns(10);
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
        JPanel convolutionMatrixPanel = convolutionMatrixPanel(3, 3);
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
}
