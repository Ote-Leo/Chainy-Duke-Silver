import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.*;

public class MatrixView extends JFrame {
    final private Font mainFont = new Font("Sans Serif", Font.BOLD, 18);
    JTable table;
    JPanel mainPanel = new JPanel();

    // create a hashtable with string key and another hashtable as a value
    Hashtable<String, Hashtable<String, ArrayList<ArrayList<String>>>> matrix = new Hashtable<String, Hashtable<String, ArrayList<ArrayList<String>>>>();
    Hashtable<String, ArrayList<ArrayList<String>>> drJonesPatientsInfo = new Hashtable<String, ArrayList<ArrayList<String>>>();
    Hashtable<String, ArrayList<ArrayList<String>>> drSmithPatientsInfo = new Hashtable<String, ArrayList<ArrayList<String>>>();

    ArrayList<ArrayList<String>> visitsOfPatient1 = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> visitsOfPatient2 = new ArrayList<ArrayList<String>>();

    ArrayList<String> visit1 = new ArrayList<String>();
    ArrayList<String> visit2 = new ArrayList<String>();
    ArrayList<String> visit3 = new ArrayList<String>();

    public void initialize() {
        visit1.add("did a regular check up on the chest");
        visit2.add("had a CT scan");
        visit3.add("had a MRI scan");

        visitsOfPatient1.add(visit1);
        visitsOfPatient1.add(visit2);
        visitsOfPatient2.add(visit3);

        drJonesPatientsInfo.put("patient1", visitsOfPatient1);
        drJonesPatientsInfo.put("patient2", visitsOfPatient2);

        // drSmithPatientsInfo.put("patient1", visitsOfPatient1);
        drSmithPatientsInfo.put("patient2", visitsOfPatient2);

        matrix.put("Dr. Jones", drJonesPatientsInfo);
        matrix.put("Dr. Smith", drSmithPatientsInfo);

        ///////////////// create a table
        table = new JTable(0, 3);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.setEnabled(false);
        table.setFont(new Font("Segoe print", Font.BOLD, 16));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setRowHeight(20);

        // int s = table.getModel().getValueAt(1, 1).toString().length() * 12;

        String[] data = { "Patient ID", "Visit ID", "Visit Description" };
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(data);
        table.getColumnModel().getColumn(0).setPreferredWidth(table.getColumnModel().getColumn(0).getWidth() * 2);
        table.getColumnModel().getColumn(1).setPreferredWidth(table.getColumnModel().getColumn(1).getWidth() * 2);
        table.getColumnModel().getColumn(2).setPreferredWidth(table.getColumnModel().getColumn(2).getWidth() * 10 + 47);

        // create a grid layout with 2 rows and 2 columns
        mainPanel.setLayout(new GridLayout(4, 2, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        JLabel welcome = new JLabel("Welcome to The Matrix View", SwingConstants.CENTER);
        mainPanel.add(welcome);
        welcome.setFont(mainFont);

        JLabel chooseDoctor = new JLabel("Choose a Doctor From The list");
        mainPanel.add(chooseDoctor);
        welcome.setFont(mainFont);

        // make a draggable JList with data items below
        String[] d = { "Dr. Smith", "Dr. Jones", "Dr. Brown", "Dr. OTE LEO" };
        JComboBox list = new JComboBox(d);

        JScrollPane listScroller = new JScrollPane(list);

        // when option is choosen from the list
        list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();
                String selected = (String) cb.getSelectedItem();
                if (matrix.get(selected) == null) {
                    // clear all the rows in the table
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    JOptionPane.showMessageDialog(null, "No Visits for this Doctor");

                } else {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    createTable(matrix.get(selected));
                }

            }
        });

        mainPanel.add(listScroller);

        // JLabel displayPublicKeyOfDoctor = new JLabel("Public Key of Doctor");
        // mainPanel.add(displayPublicKeyOfDoctor);
        // displayPublicKeyOfDoctor.setFont(mainFont);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(1100, 100));

        scrollPane.setColumnHeaderView(table.getTableHeader());
        // mainPanel.add(scrollPane, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.BEFORE_LINE_BEGINS);
        setTitle("Matrix View");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {
        MatrixView matrixView = new MatrixView();
        matrixView.initialize();
    }

    public void createTable(Hashtable<String, ArrayList<ArrayList<String>>> allPatientsInfoForSelectedDoctor) {

        Set<String> allPatientsofThisDoctor = allPatientsInfoForSelectedDoctor.keySet();
        ArrayList<String> result = new ArrayList<String>();
        for (String patient : allPatientsofThisDoctor) {
            ArrayList<ArrayList<String>> visitsOfFirstPatient = allPatientsInfoForSelectedDoctor.get(patient);
            for (ArrayList<String> visit : visitsOfFirstPatient) {
                result.add(patient);
                result.add(Integer.toString(visitsOfFirstPatient.indexOf(visit)));
                result.addAll(visit);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (!model.getDataVector().contains(new Vector<String>(result))) {
                    model.addRow(result.toArray());
                    result.clear();
                }

            }

        }

    }

}
