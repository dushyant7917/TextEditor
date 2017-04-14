import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class Register extends JPanel implements ActionListener{

  JLabel userL = new JLabel("Choose a Username ");
  JTextField userTF = new JTextField();
  JLabel passL = new JLabel("Password ");
  JPasswordField passTF = new JPasswordField();
  JLabel passLC = new JLabel("Confirm Password ");
  JPasswordField passC = new JPasswordField();
  JButton register = new JButton("Register");
  JButton back = new JButton("Back");

  public Register() {
    JPanel loginP = new JPanel();
    loginP.setLayout(new GridLayout(4, 2));
    loginP.add(userL);
    loginP.add(userTF);
    loginP.add(passL);
    loginP.add(passTF);
    loginP.add(passLC);
    loginP.add(passC);
    loginP.add(register);
    loginP.add(back);
    register.addActionListener(this);
    back.addActionListener(this);
    add(loginP);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == register && passTF.getPassword().length > 0 && userTF.getText().length() > 0) {
      String pass = new String(passTF.getPassword());
      String confirm = new String(passC.getPassword());
      if(pass.equals(confirm)) {
        try {
          BufferedReader input = new BufferedReader(new FileReader("passwords.txt"));
          String line = input.readLine();
          while(line != null) {
            StringTokenizer st = new StringTokenizer(line);
            if(userTF.getText().equals(st.nextToken())) {
              System.out.println("User already exists!");
              return;
            }
            line = input.readLine();
          }
          input.close();
          MessageDigest md = MessageDigest.getInstance("SHA-256");
          md.update(pass.getBytes());
          byte byteData[] = md.digest();
          StringBuffer sb = new StringBuffer();
          for(int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
          }
          BufferedWriter output = new BufferedWriter(new FileWriter("passwords.txt", true));
          output.write(userTF.getText() + " " + sb.toString() + "\n");
          output.close();
          Login login = (Login) getParent();
          login.cl.show(login, "login");
        } catch(FileNotFoundException e1) {
          e1.printStackTrace();
        } catch(IOException e1) {
          e1.printStackTrace();
        } catch(NoSuchAlgorithmException e1) {
          e1.printStackTrace();
        }
      }
    }
    if(e.getSource() == back) {
      Login login = (Login) getParent();
      login.cl.show(login, "login");
    }
  }

}
