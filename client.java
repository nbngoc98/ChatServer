package Demo1;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class client extends JFrame implements ActionListener{
    
    public JTextArea msgDisplay;
    private JTextField msg;
    private JButton send;
    
    private DataOutputStream dos;
    private DataInputStream dis;
    
    public client(){
        super("Client");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        setSize(600, 400);
        addItem();
        setVisible(true);
    }
    private void addItem() {
        setLayout(new BorderLayout());
        msgDisplay = new JTextArea();
        msgDisplay.setEditable(false);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        msg = new JTextField(40);
        send = new JButton("Send");
        send.addActionListener(this);
        
        p.add(msg);
        p.add(send);
        
        add(new JLabel("   "),BorderLayout.NORTH);
        add(new JLabel("   "),BorderLayout.EAST);
        add(new JLabel("   "),BorderLayout.WEST);
        add(new JScrollPane(msgDisplay),BorderLayout.CENTER);
        add(p,BorderLayout.SOUTH);
        
    }
    public static void main(String[] args) {
        new client().go();
    }
    private void go() {
        // Tao ket noi lang nghe o day
        
        try {
            msgDisplay.append("Máy chủ bắt đầu chạy.\n");
            Socket client = new Socket("localhost",2013);
            msgDisplay.append("Đã kết nối thành công.\n");
            
            dos = new DataOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());
            
            String temp =null;
            
            while(true){
                temp = dis.readUTF();
                if(temp.toUpperCase().equals("QUIT")){
                    dos.writeUTF("QUIT");
                    dos.flush();
                    break;
                }
                else msgDisplay.append("Máy Chủ : "+temp+"\n");
            }
            
            msgDisplay.append("Máy Chủ đã ngắt kết nối với bạn.\n");
            dis.close();
            dos.close();
            client.close();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(msg.getText().compareTo("")!=0){
                try {
                    dos.writeUTF(msg.getText());
                    dos.flush();
                    msgDisplay.append("Joony : "+msg.getText()+"\n");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this, "Kết nối đã ngắt", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            msg.setText("");
        }
    }

} 