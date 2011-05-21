package view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Contact;
import model.SerializableBufferedImage;


public class ContactEditor extends JDialog {
	
    public static final int CANCEL_OPTION = 1;
    public static final int APPROVE_OPTION = 0;
    private int returnvalue = CANCEL_OPTION;

	private static final long serialVersionUID = 1L;
	private Contact contact = null;
	private Label label1 = new Label("First Name:");
	private Label label2 = new Label("Last Name:");
	private Label label3 = new Label("Phone Number:");
	private TextField fieldFirstName = new TextField(20);
	private TextField fieldLastName = new TextField(20);
	private TextField fieldPhoneNumber = new TextField(20);
	private SerializableBufferedImage serImg = null;
	private JButton photoButton = null;
	private JButton okButton = new JButton("Save");
	private JButton cancelButton = new JButton("Cancel");
	private Contact dummyContact = new Contact(
			"Ad giriniz...",
			"Soyad giriniz...",
			"Telefon giriniz...",
			"./res/list-add-contact.png");
	private File photoFile = null;
	private ContactEditor ctEditor = this;
	
	public ContactEditor(Contact ct, Frame owner) {
		super(owner);
		setTitle("Contact Editor");
		contact = ct;
		if (contact==null) contact = dummyContact;
		
		fieldFirstName.setText(contact.getFirstName());
		fieldLastName.setText(contact.getLastName());
		fieldPhoneNumber.setText(contact.getPhoneNumber());
		serImg = contact.getPhoto();

		Image img = serImg.getBufferedImage().getScaledInstance(60, 80, Image.SCALE_FAST);
		photoButton = new JButton(new ImageIcon(img));
		
		photoButton.addActionListener(photoBtnListener);
		okButton.addActionListener(okBtnListener);
		cancelButton.addActionListener(cancelBtnListener);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(label1, c);
		c.gridy = 1;
		add(label2, c);
		c.gridy = 2;
		add(label3, c);
	
		c.gridx = 1;
		c.gridy = 0;
		add(fieldFirstName, c);
		c.gridy = 1;
		add(fieldLastName, c);
		c.gridy = 2;
		add(fieldPhoneNumber, c);
	
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		add(photoButton, c);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,2,60,0));
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.insets = new Insets(20,0,10,0);
		add(bottomPanel, c);
	}
	
	public int showEditor() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);		
		validate();
		setModal(true);
		setVisible(true);
		return returnvalue;
	}
	
	public Contact getContact() {
		return contact;
	}


	private ActionListener photoBtnListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogTitle("Please select the picture file...");
			int retval = fileChooser.showOpenDialog(ctEditor);
			if (retval == JFileChooser.APPROVE_OPTION) {
				photoFile = fileChooser.getSelectedFile();
				try {
					serImg = new SerializableBufferedImage(ImageIO.read(photoFile));
					photoButton.setIcon(new ImageIcon(serImg.getBufferedImage().getScaledInstance(
							60, 80, Image.SCALE_SMOOTH)));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		}	
	};

	private ActionListener cancelBtnListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			returnvalue = CANCEL_OPTION;
			setVisible(false);
		}	
	};

	private ActionListener okBtnListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
					
			if (!fieldPhoneNumber.getText().isEmpty()
					& (!fieldFirstName.getText().isEmpty() | !fieldLastName
							.getText().isEmpty())) {
				contact.setFirstName(fieldFirstName.getText());
				contact.setLastName(fieldLastName.getText());
				contact.setPhoneNumber(fieldPhoneNumber.getText());
				contact.setPhoto(serImg);
			}
			returnvalue = APPROVE_OPTION; 
			setVisible(false);
		}
	};
}
