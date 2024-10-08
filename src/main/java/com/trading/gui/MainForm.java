package com.trading.gui;

import com.trading.EWrapperImpl;
import com.trading.scan.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class MainForm {
    private final EWrapperImpl wrapper;
    private final Map <String,List<String>> storageIndexTicker;
    public MainForm(EWrapperImpl wrapper, Map<String,List<String>> tickers){
        this.storageIndexTicker = tickers;
        this.wrapper = wrapper;
    }

    private JPanel componentWithLabel(String labelText, JComponent textField){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jLabel = new JLabel(labelText);
        panel.add(jLabel);
        panel.add(textField);
        return panel;
    }
    public void display() {
        JFrame frame = new JFrame("Trading scanner");



        Object[][] data = {
                {"AAPL", false, "100", "10000", "1000"},
                {"NVDA", false, "50", "5000", "500"},
                {"TSLA", false, "200", "20000", "2000"}
        };

        String[] columnNames = {"Tickers", "ON/OFF Switch", "Quantity", "Position Size", "PNL"};

        // Create a table model and make the first column editable
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 1;
               // return column == 1; // Only allow editing the ON/OFF Switch column
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {


                if (columnIndex == 1) {
                    return Boolean.class; // Set the ON/OFF Switch column as Boolean
                }
                return super.getColumnClass(columnIndex);
            }
        };

        JTable table = new JTable(model);

        JList stockIndexesListUI = new JList(storageIndexTicker.keySet().toArray());
        stockIndexesListUI.setSelectedIndex(1);
        stockIndexesListUI.setVisibleRowCount(1);
        JPanel panelIndexChooser = componentWithLabel("Select index",stockIndexesListUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());
        frame.add(panelIndexChooser);
      //  frame.add(stockIndexesListUI);

        // Create a button
        JButton showVolumeButton = new JButton("Run Volume scanner");
        JPanel volumeConfigurationPanel = new JPanel(new BorderLayout());
        JTextField volumeSettings = new JTextField();
        volumeSettings.setText("1.8");
        volumeSettings.setColumns(5);
        volumeConfigurationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        volumeConfigurationPanel.add(volumeSettings);
        volumeConfigurationPanel.add(componentWithLabel("Volume settings",showVolumeButton));
        JButton searchCross = new JButton("CrossEnterPoint search execute trade");


        // Create a panel to add the button
        JPanel panel = new JPanel();

        panel.add(panelIndexChooser);
        JPanel crossPanel = new JPanel();

        //JButton stockAboveSmaSearchButton = new JButton("Stock above 200 sma");
       // frame.add(stockAboveSmaSearchButton, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
       // panel.add(volumeConfigurationPanel);
       // panel.add(volumeSettings);
        //panel.add(showVolumeButton);
       // panel.add(stockAboveSmaSearchButton);
        crossPanel.setLayout(new BoxLayout(crossPanel, BoxLayout.Y_AXIS));
        JToggleButton toggleButton = new JToggleButton("Toggle Button");
       // table.getColumnModel().get
       // table.getColumnModel().getColumn(1).setCellRenderer(new MyTableCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new ToggleSwitchRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new ToggleSwitchEditor());

        crossPanel.add(new JScrollPane(table));
        //JTextField shortEma = new JTextField(10);
       // First input field
       // JTextField  longEma= new JTextField(10);
       // JLabel labelShortEma = new JLabel("Short Ema");
       // JLabel labelLongEma = new JLabel("Long Ema");
       // shortEma.setText("7");
       // longEma.setText("21");
        crossPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        crossPanel.add(searchCross);
        JTextField shortEma = new JTextField();
        shortEma.setColumns(4);
        shortEma.setText("12");
        JTextField longEma = new JTextField();
        longEma.setText("26");
        longEma.setColumns(4);
        JTextField capitalField = new JTextField();
        capitalField.setText("10000");
        capitalField.setColumns(8);
        JTextField riskPercentField = new JTextField();
        riskPercentField.setText("1");
        riskPercentField.setColumns(4);
        JTextField stopPercentField = new JTextField();
        stopPercentField.setText("4");
        stopPercentField.setColumns(4);

        JTextField trailingStopPercentField = new JTextField();
        trailingStopPercentField.setText("4");
        trailingStopPercentField.setColumns(4);


        JTextField bellowLargeEma = new JTextField();
        bellowLargeEma.setText("5");
        bellowLargeEma.setColumns(4);
        JTextField largeEmaTextField = new JTextField();
        largeEmaTextField.setText("200");
        largeEmaTextField.setColumns(5);
        crossPanel.add(componentWithLabel("totalAmount", capitalField));
        crossPanel.add(componentWithLabel("risk %", riskPercentField));
        crossPanel.add(componentWithLabel("Short Ema", shortEma));
        crossPanel.add(componentWithLabel("Big Ema", longEma));
        crossPanel.add(componentWithLabel("Large Ema", largeEmaTextField));
        crossPanel.add(componentWithLabel("stop %", stopPercentField));
        crossPanel.add(componentWithLabel("bellow Large Ema %", bellowLargeEma));
        crossPanel.add(componentWithLabel("trailing stop %", trailingStopPercentField));
       // crossPanel.add(shortEma);
        //crossPanel.add(longEma);
        Border border = new LineBorder(Color.RED, 2);
        crossPanel.setBorder(border);
        panel.add(crossPanel);



        //panel.add(stockTable);
        //panel.add(searchCross);
        frame.add(panel, BorderLayout.NORTH);
        //frame.add(searchCross, BorderLayout.EAST);
        // Create a text area to display the list
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        searchCross.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double riskPercent = Double.parseDouble(riskPercentField.getText());
                double bellowEmaPercent = Double.parseDouble(bellowLargeEma.getText());
                double stopPercent = Double.parseDouble(stopPercentField.getText());
                double capital = Double.parseDouble(capitalField.getText());
                int shortEmaValue= Integer.parseInt(shortEma.getText());
                int longEmaValue= Integer.parseInt(longEma.getText());
                int largeEma = Integer.parseInt(largeEmaTextField.getText());
                Scan scan = new CrossScan(shortEmaValue,longEmaValue, bellowEmaPercent, largeEma);
                List<String> list = scan.scan(wrapper, new PlaceOrderAction(wrapper, capital,riskPercent,  stopPercent),storageIndexTicker.get(stockIndexesListUI.getSelectedValue()));
                List<String>  messageList = new ArrayList<>();
                messageList.add(list.get(0));
                updateTextArea(textArea,messageList);
            }
        });

      /*
        stockAboveSmaSearchButton.addActionListener(actionEvent -> {
            Scan scanner = new SmaScan();
            updateTextArea(textArea, scanner.scan(wrapper, new SaveTickerAction(), storageIndexTicker.get(stockIndexesListUI.getSelectedValue())));
        });

       */
        /*
        // Add an ActionListener to the button
        showVolumeButton.addActionListener(e -> {
            double volumePercent = Double.parseDouble(volumeSettings.getText());
            Scan scanner = new VolumeScan(volumePercent);
            List <String> tickerList= scanner.scan(wrapper, new SaveTickerAction(), storageIndexTicker.get(stockIndexesListUI.getSelectedValue()));
            updateTextArea(textArea, tickerList);

        });

         */

        // Make the frame visible
        frame.setVisible(true);
    }

    private  void updateTextArea(JTextArea textArea,List <String> tickerList){
        textArea.selectAll();
        textArea.replaceSelection("");
        for (String item : tickerList) {
            textArea.append(item + "\n"); // append each item to the text area
        }
    }
}
 class ToggleSwitchRenderer extends JCheckBox implements TableCellRenderer {
    public ToggleSwitchRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        if (value != null) {
            boolean checked = (boolean) value;
            //checked = !checked;
            setSelected(checked);
            setText(checked ? "ON" : "OFF");
        }
        return this;
    }
}

// Editor class for the toggle switch
class ToggleSwitchEditor extends DefaultCellEditor {
    private JCheckBox checkBox;

    public ToggleSwitchEditor() {
        super(new JCheckBox());
        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle the checkbox state and update text
                boolean isSelected = checkBox.isSelected();
                checkBox.setText(isSelected ? "ON" : "OFF");
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        boolean checked = (boolean) value;
        checkBox.setSelected(checked);
        checkBox.setText(checked ? "ON" : "OFF");
        return checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return checkBox.isSelected();
    }
}

/*
    private final JToggleButton toggleButton;

    public MyTableCellRenderer() {
        toggleButton = new JToggleButton();
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    if (toggleButton.isSelected()) {
                        System.out.println("TRADING SCAN");
                    }
            }
        });
        toggleButton.setOpaque(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean
            hasFocus, int row, int column) {
        // Set the toggle  
        //button's state based on the table value
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
       /*
        if (isSelected) {
            if(value.equals("OFF")) {
                table.setValueAt("ON", row, column);
               // toggleButton.setText("ON");
            }
            else{
                table.setValueAt("OFF", row, column);
               // toggleButton.setText("OFF");
            }
           // toggleButton.setSelected(true);
        }

        else {
            if(toggleButton.getText().isEmpty()) {
                toggleButton.setText("OFF");
            }
            //toggleButton.setText("OFF");
            toggleButton.setSelected(false);
        }



        // Set the component's background and foreground colors
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        toggleButton.setForeground(getForeground());
        toggleButton.setBackground(getBackground());

        return toggleButton;

        */
    //}
//}


/*
class MyTableCellRenderer extends JToggleButton implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
    }
*/

