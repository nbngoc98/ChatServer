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
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class server extends JFrame implements ActionListener{
    public JTextArea msgDisplay;
    private JTextField msg;
    private JButton send;
    
    private DataOutputStream dos;
    private DataInputStream dis;
    
    public server(){
        super("Server");
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
    public static void main(String[] args) {
        new server().go();
    }
    private void go() {
        // tao socket o day
        try {
            ServerSocket server = new ServerSocket(2013);
            msgDisplay.append("Máy chủ bắt đầu chạy.\n");
            Socket client = server.accept();
            msgDisplay.append("Joony đã kết nối với bạn.\n");
            
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
                else msgDisplay.append("Joony : "+temp+"\n");
            }
            
            msgDisplay.append("Joony đã ngắt kết nối.\n");
            dis.close();
            dos.close();
            client.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(msg.getText().compareTo("")!=0){
            try {
                dos.writeUTF(msg.getText());
                dos.flush();
                msgDisplay.append("Máy Chủ : "+msg.getText()+"\n");
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "Kết nối đã ngắt", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
            msg.setText("");
        }
        
    }
}
