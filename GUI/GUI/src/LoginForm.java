import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class LoginForm extends JFrame {
    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    JTextField tfEmail;
    JPasswordField pfPassword;

    public void initialize() {
        /*************** SignUp label ***************/

        JLabel lblSignUp = new JLabel("Sign Up HERE");
        lblSignUp.setFont(mainFont);
        lblSignUp.setForeground(Color.BLUE);

        lblSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SignUpForm signUpForm = new SignUpForm();
                signUpForm.initialize();
                dispose();
            }
        });

        // signup panel
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(0, 1, 10, 10));
        signupPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        signupPanel.add(lblSignUp);

        /*************** Form Panel ***************/
        JLabel lbLoginForm = new JLabel("Please Log In", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbEmail = new JLabel("Email");
        lbEmail.setFont(mainFont);

        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(lbLoginForm);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);

        /*************** Buttons Panel ***************/
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame();
                User user = new User();
                user.email = tfEmail.getText();
                user.password = pfPassword.getText();
                user.name = "Dr. OTE LEO";
                user.phone = "0123456789";
                user.address = "Cairo";
                JOptionPane.showMessageDialog(LoginForm.this, "Login Successful");

                mainFrame.initialize(user);
                dispose();
            }

        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(mainFont);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }

        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);

        /*************** Initialise the frame ***************/
        add(formPanel, BorderLayout.NORTH);
        add(signupPanel, BorderLayout.BEFORE_LINE_BEGINS);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("EHR Application");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(350, 450));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.initialize();
    }
}
