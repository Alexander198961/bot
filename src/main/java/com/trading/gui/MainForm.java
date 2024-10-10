package com.trading.gui;

import com.trading.EWrapperImpl;
import com.trading.api.Unit;
import com.trading.cache.Cache;
import com.trading.config.EmaConfiguration;
import com.trading.config.RequestConfiguration;
import com.trading.config.TradeConfiguration;
import com.trading.scan.*;
import com.trading.scheduler.TaskScheduler;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class MainForm {


    private JTextField riskPercentField = new JTextField();
    private JTextField bellowLargeEma = new JTextField();
    private JTextField stopPercentField = new JTextField();
    private JTextField capitalField = new JTextField();
    private JTextField shortEma = new JTextField();
    private String [] tickersArray = new String[5];
    private JTextField longEma = new JTextField();
    private JTextField largeEmaTextField = new JTextField();
    private final Unit unit = new Unit();
    private final JList barSizeUiList = new JList<>(unit.initUnit().keySet().toArray(new String[0]));

    private JPanel componentWithLabel(String labelText, JComponent textField, JComponent additionalComponent){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jLabel = new JLabel(labelText);
        panel.add(jLabel);
        panel.add(textField);
        if(additionalComponent != null){
            panel.add(additionalComponent);
        }
        return panel;
    }

    private  void checkboxAddListener(JCheckBox checkBox){
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle the checkbox state and update text
                boolean isSelected = checkBox.isSelected();
                checkBox.setText(isSelected ? "ON" : "OFF");
            }
        });
    }

    private void update(){
        double riskPercent = Double.parseDouble(riskPercentField.getText());
        double bellowEmaPercent = Double.parseDouble(bellowLargeEma.getText());
        double stopPercent = Double.parseDouble(stopPercentField.getText());
        double capital = Double.parseDouble(capitalField.getText());
        int shortEmaValue= Integer.parseInt(shortEma.getText());
        int longEmaValue= Integer.parseInt(longEma.getText());
        int largeEma = Integer.parseInt(largeEmaTextField.getText());
        EmaConfiguration emaConfiguration = new EmaConfiguration();
        emaConfiguration.setLargeEma(largeEma);
        emaConfiguration.setBellowEmaPercent(bellowEmaPercent);
        emaConfiguration.setLongEmaValue(longEmaValue);
        emaConfiguration.setShortEmaValue(shortEmaValue);
        TradeConfiguration tradeConfiguration = new TradeConfiguration();
        tradeConfiguration.setCapital(capital);
        tradeConfiguration.setRiskPercent(riskPercent);
        tradeConfiguration.setStopPercent(stopPercent);
        String selectedBar = barSizeUiList.getSelectedValue().toString();
        RequestConfiguration requestConfiguration = new RequestConfiguration();
        requestConfiguration.setBarSize(selectedBar);
        Cache.cache.put(Cache.Keys.RequestConfig.name(), requestConfiguration);
        Cache.cache.put(Cache.Keys.Tickers.name(), Arrays.asList(tickersArray));
        Cache.cache.put(Cache.Keys.TradeConfig.name(), tradeConfiguration);
        Cache.cache.put(Cache.Keys.EmaConfig.name(), emaConfiguration);

        //Cache.cache.put("emaConfig", emaConfiguration);
        //List<String> list = scan.scan(wrapper, new PlaceOrderAction(wrapper, capital,riskPercent,  stopPercent), Arrays.asList(tickersArray));

        //List<String>  messageList = new ArrayList<>();
        //messageList.add(list.get(0));

        //taskScheduler.run();
        //updateTextArea(textArea,messageList);
    }
    public void display() {


        JFrame frame = new JFrame("Trading scanner");
        //Object[][] data  = new Object[6][5];
        //for(int i = 0; i < 6; i++){
          //  data[i][0] = new Object[]{" ", false, "", "", ""};
        //}
       /*
        Object[][] data = {
                {"AAPL", false, "100", "10000", "1000"},
                {"NVDA", false, "50", "5000", "500"},
                {"TSLA", false, "200", "20000", "2000"}
        };

        */
        Object[][] data = {
                {"", false, "", "", ""},
                {"", false, "", "", ""},
                {"", false, "", "", ""},
                {"", false, "", "", ""},
                {"", false, "", "", ""}
        };

        for(int i =0 ; i < data.length ; i++){
            tickersArray[i]="";
        }



        String[] columnNames = {"Tickers", "ON/OFF Switch", "Quantity", "Position Size", "PNL"};

        // Create a table model and make the first column editable
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {

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
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (e.getType() == TableModelEvent.UPDATE && column == 0) {
                    // Get the new value of the edited cell
                    Object newValue = model.getValueAt(row, column);
                    tickersArray[row] = newValue.toString();
                    update();
                    System.out.println("Cell edited at row " + row + ", column " + column + ": " + newValue);
                }
            }
        });
        JTable table = new JTable(model);


       // String [] barSizeList = {"1 day", "1 hour", "4 hours", "1 min", "1 day", "1 week", "1 month"};
        //JList barSizeUiList = new JList<>(barSizeList);
        barSizeUiList.setSelectedIndex(0);
        //stockIndexesListUI.setSelectedIndex(1);
        //stockIndexesListUI.setVisibleRowCount(1);
        //JPanel panelIndexChooser = componentWithLabel("Select index",stockIndexesListUI, null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 900);
        frame.setLayout(new BorderLayout());
      //  frame.add(panelIndexChooser);
      //  frame.add(stockIndexesListUI);

        // Create a button
        JButton showVolumeButton = new JButton("Run Volume scanner");
        JPanel volumeConfigurationPanel = new JPanel(new BorderLayout());
        JTextField volumeSettings = new JTextField();
        volumeSettings.setText("1.8");
        volumeSettings.setColumns(5);
        volumeConfigurationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        volumeConfigurationPanel.add(volumeSettings);
        volumeConfigurationPanel.add(componentWithLabel("Volume settings",showVolumeButton, null));
        //JButton searchCrosssearchCross = new JButton("CrossEnterPoint search execute trade");


        // Create a panel to add the button
        JPanel panel = new JPanel();

       // panel.add(panelIndexChooser);
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
        //table.getColumnModel

        crossPanel.add(new JScrollPane(table));
        //JTextField shortEma = new JTextField(10);
       // First input field
       // JTextField  longEma= new JTextField(10);
       // JLabel labelShortEma = new JLabel("Short Ema");
       // JLabel labelLongEma = new JLabel("Long Ema");
       // shortEma.setText("7");
       // longEma.setText("21");
        crossPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
       // crossPanel.add(searchCross);

        shortEma.setColumns(4);
        shortEma.setText("12");

        longEma.setText("26");
        longEma.setColumns(4);

        capitalField.setText("10000");
        capitalField.setColumns(8);

        riskPercentField.setText("1");
        riskPercentField.setColumns(4);

        stopPercentField.setText("4");
        stopPercentField.setColumns(4);

        JTextField trailingStopPercentField = new JTextField();
        trailingStopPercentField.setText("4");
        trailingStopPercentField.setColumns(4);



        bellowLargeEma.setText("5");

        bellowLargeEma.setColumns(4);

        largeEmaTextField.setText("200");
        largeEmaTextField.setColumns(5);
        crossPanel.add(componentWithLabel("totalAmount", capitalField,null));
        JCheckBox riskCheckBox = new JCheckBox("Risk checkbox");
        JCheckBox strategyCheckbox = new JCheckBox("Strategy checkbox");
        checkboxAddListener(riskCheckBox);
        crossPanel.add(componentWithLabel("risk %", riskPercentField,riskCheckBox));
        crossPanel.add(componentWithLabel("Short Ema", shortEma, null));
        crossPanel.add(componentWithLabel("Big Ema", longEma,null));
        crossPanel.add(componentWithLabel("Large Ema", largeEmaTextField,null));
        JCheckBox stopCheckbox = new JCheckBox("Stop checkbox");
        checkboxAddListener(stopCheckbox);
        crossPanel.add(componentWithLabel("stop %", stopPercentField,stopCheckbox));
        crossPanel.add(componentWithLabel("bellow Large Ema %", bellowLargeEma,null));
        JCheckBox trailingStopCheckbox = new JCheckBox("trailing stop checkbox");
        crossPanel.add(componentWithLabel("trailing stop %", trailingStopPercentField,trailingStopCheckbox));
        crossPanel.add(componentWithLabel("bar size settings",barSizeUiList,null));
       // crossPanel.add(componentWithLabel("stock size settings",stockIndexesListUI,null));
        crossPanel.add(strategyCheckbox);
        strategyCheckbox.setSelected(true);
        strategyCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        // crossPanel.add(componentWithLabel("strategy checkbox",strategyCheckbox,null));
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
        /*
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

         */

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
    private final JCheckBox checkBox;

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







