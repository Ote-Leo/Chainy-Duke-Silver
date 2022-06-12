import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Sans Serif", Font.BOLD, 18);
    JTextField tfTransactionData;
    JTable table;

    public void initialize(User user) {

        // create a grid layout with 2 rows and 2 columns
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel welcome = new JLabel("Welcome " + user.name, SwingConstants.CENTER);
        welcome.setFont(mainFont);

        JButton addTransaction = new JButton("Add Tranasction");
        tfTransactionData = new JTextField();
        tfTransactionData.setText("Enter transaction data here");
        tfTransactionData.setForeground(Color.GRAY);
        tfTransactionData.setHorizontalAlignment(JTextField.CENTER);
        tfTransactionData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tfTransactionData.setText("");
                tfTransactionData.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfTransactionData.getText().isEmpty()) {
                    tfTransactionData.setText("Enter transaction data here");
                    tfTransactionData.setForeground(Color.GRAY);
                }
            }
        });

        JButton displayMatrixView = new JButton("Display Matrix View");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 10, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        buttonsPanel.add(addTransaction);
        buttonsPanel.add(displayMatrixView);
        buttonsPanel.add(tfTransactionData);

        JLabel tHistory = new JLabel("Your Transaction History:", SwingConstants.LEFT);
        tHistory.setFont(mainFont);

        mainPanel.add(welcome);
        mainPanel.add(buttonsPanel);
        mainPanel.add(tHistory);

        table = new JTable(0, 4);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.setEnabled(false);
        table.setFont(new Font("Segoe print", Font.BOLD, 16));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setRowHeight(20);

        // int s = table.getModel().getValueAt(1, 1).toString().length() * 12;
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        String[] data = { "Block Number", "Previous Block Hash", "Time", "Data" };
        model.setColumnIdentifiers(data);
        table.getColumnModel().getColumn(0).setPreferredWidth(table.getColumnModel().getColumn(0).getWidth() * 2);
        table.getColumnModel().getColumn(2).setPreferredWidth(table.getColumnModel().getColumn(2).getWidth() * 3);
        table.getColumnModel().getColumn(3).setPreferredWidth(table.getColumnModel().getColumn(3).getWidth() * 4);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);

        addTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String transactionData = tfTransactionData.getText();
                if (transactionData.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter transaction data");
                } else {
                    // create an arraylist of strings
                    ArrayList<String> transactionDataList = new ArrayList<>();
                    transactionDataList.add(String.valueOf(table.getRowCount()));

                    if (table.getRowCount() == 0) {
                        transactionDataList.add("0");
                    } else {
                        transactionDataList.add("get previous block hash");
                    }
                    // add current time to the arraylist
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    transactionDataList.add(String.valueOf(dtf.format(now)));

                    transactionDataList.add(transactionData);

                    // add the arraylist to the table
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.addRow(transactionDataList.toArray());

                    JOptionPane.showMessageDialog(null, "Transaction added");
                    tfTransactionData.setText("");
                }
            }
        });

        // add scroll pane to the table
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(500, 100));

        scrollPane.setColumnHeaderView(table.getTableHeader());

        mainPanel.add(scrollPane);
        add(mainPanel, BorderLayout.NORTH);

        // add(scrollPane);
        setTitle("Dashboard");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
