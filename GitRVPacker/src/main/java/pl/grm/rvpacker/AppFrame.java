package pl.grm.rvpacker;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * @author Levvy055
 */
public class AppFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Color btnBgColor = new Color(107, 142, 35);
	private JPanel contentPane;
	private Logger logger;
	private JTextField tF_SelectedProject;
	private MainPacker packer;
	private JButton btnLoad;
	private JButton btnPack;
	private JButton btnUnpack;
	private JProgressBar progressBar;
	private boolean IsProjectLoaded;
	private SettingsDialog settingsDialog;
	
	/**
	 * Create the frame.
	 * 
	 * @param executor
	 * @param logger
	 */
	public AppFrame(MainPacker mpacker) {
		this.packer = mpacker;
		this.logger = packer.getLogger();
		packer.getExecutor().setAppFrame(this);
		setTitle("RPG Project YAML Packer");
		setResizable(false);
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setDialogTitle("Select RPG Maker project");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"RPG Maker Project file", "rvproj2"));
		chooser.setAcceptAllFileFilterUsed(false);
		JPanel top_Panel = new JPanel();
		contentPane.add(top_Panel, BorderLayout.NORTH);
		top_Panel.setLayout(new GridLayout(0, 1, 0, 0));
		JMenuBar menuBar = new JMenuBar();
		top_Panel.add(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(e -> AppFrame.this.dispose());
		mnFile.add(mntmClose);
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mntmSettings.addActionListener(e -> openSettings(""));
		mnFile.add(mntmSettings);
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem mntmAbout = new JMenuItem("About ...");
		mntmAbout.addActionListener(
				e -> JOptionPane.showMessageDialog(AppFrame.this,
						"Program is designed to pack and unpack files of RPG Maker "
								+ "to/from binary format from/to yaml files.\n"
								+ "Program uses: \n - rpgskeleton files made by akesterson and \n"
								+ " - rvpacker made by Solistra. \n"
								+ "\u00a9 GRM-Group 2015",
						"About App", JOptionPane.INFORMATION_MESSAGE));
		mnHelp.add(mntmAbout);
		JPanel main_Panel = new JPanel();
		contentPane.add(main_Panel, BorderLayout.CENTER);
		main_Panel.setLayout(new BorderLayout(0, 0));
		JPanel topInside_Panel = new JPanel();
		main_Panel.add(topInside_Panel, BorderLayout.NORTH);
		topInside_Panel.setLayout(new BorderLayout(0, 0));
		JPanel project_Panel = new JPanel();
		topInside_Panel.add(project_Panel, BorderLayout.NORTH);
		project_Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		JButton btnChooseProject = new JButton("Choose Project ...");
		btnChooseProject.addActionListener(e -> {
			if (chooser.showOpenDialog(
					AppFrame.this) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				logger.info("Selected: " + selectedFile.getPath());
				tF_SelectedProject.setText(selectedFile.getAbsolutePath());
			}
		});
		project_Panel.add(btnChooseProject);
		tF_SelectedProject = new JTextField();
		project_Panel.add(tF_SelectedProject);
		tF_SelectedProject.setColumns(34);
		tF_SelectedProject.getDocument()
				.addDocumentListener(new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent e) {
						updateLayout();
					}
					
					@Override
					public void insertUpdate(DocumentEvent e) {
						updateLayout();
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {
						updateLayout();
					}
				});
		btnLoad = new JButton("Load");
		btnLoad.setEnabled(false);
		btnLoad.setBackground(Color.CYAN.darker());
		btnLoad.addActionListener(e -> {
			IsProjectLoaded = true;
			updateLayout();
		});
		project_Panel.add(btnLoad);
		JPanel button_Panel = new JPanel();
		topInside_Panel.add(button_Panel, BorderLayout.CENTER);
		button_Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnPack = new JButton("Pack (YAML->RPG)|After Pull");
		btnPack.setEnabled(false);
		btnPack.setBackground(btnBgColor);
		btnPack.addActionListener(e -> {
			if (packer.getExecutor().canExecute()
					&& JOptionPane.showConfirmDialog(this,
							"You want to override RPG Maker files with ones from YAML",
							"Are you sure?", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
				packer.getExecutor().runPackScript(new JComponent[]{btnPack,
						btnUnpack, btnLoad, progressBar});
			}
		});
		button_Panel.add(btnPack);
		btnUnpack = new JButton("Unpack (RPG->YAML)|Before Push");
		btnUnpack.setEnabled(false);
		btnUnpack.setBackground(btnBgColor);
		btnUnpack.addActionListener(e -> {
			if (packer.getExecutor().canExecute()
					&& JOptionPane.showConfirmDialog(this,
							"You want to override YAML files with ones from RPG Maker files",
							"Are you sure?", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
				packer.getExecutor().runUnpackScript(new JComponent[]{btnPack,
						btnUnpack, btnLoad, progressBar});
			}
		});
		button_Panel.add(btnUnpack);
		JPanel console_Panel = new JPanel();
		main_Panel.add(console_Panel);
		console_Panel.setLayout(new GridLayout(0, 1, 0, 0));
		JTextPane consoleTA = new JTextPane();
		consoleTA.setEditable(false);
		consoleTA.setBounds(0, 0, 75, 200);
		consoleTA.setPreferredSize(new Dimension(80, 200));
		packer.getExecutor().setOutputConsole(consoleTA);
		JScrollPane pane = new JScrollPane(consoleTA);
		console_Panel.add(pane);
		JPanel bottom_Panel = new JPanel();
		contentPane.add(bottom_Panel, BorderLayout.SOUTH);
		bottom_Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		JLabel lblProgress = new JLabel("Progress: ");
		bottom_Panel.add(lblProgress);
		progressBar = new JProgressBar();
		bottom_Panel.add(progressBar);
		try {
			settingsDialog = new SettingsDialog(packer);
			settingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.pack();
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Closing ...");
				String projectDirPath = tF_SelectedProject.getText();
				if (projectDirPath != null
						&& projectDirPath.contains(".rvproj2")
						&& !projectDirPath.equals(
								packer.getConfigValue(ConfigId.PROJECTFILE))) {
					packer.setConfigValue(ConfigId.PROJECTFILE, projectDirPath);
				}
				packer.save();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	/**
	 * @param config
	 */
	public void applyConfig(HashMap<ConfigId, String> config) {
		for (Iterator<ConfigId> it = config.keySet().iterator(); it
				.hasNext();) {
			ConfigId conf = it.next();
			switch (conf) {
				case PROJECTFILE :
					tF_SelectedProject.setText(config.get(conf));
					break;
				case RUBY_PATH :
					break;
				default :
					break;
			}
		}
		settingsDialog.applyConfig(config);
		updateLayout();
	}
	
	public void updateLayout() {
		String filePath;
		if ((filePath = tF_SelectedProject.getText()) != null
				&& filePath.contains(".rvproj2")
				&& new File(filePath).exists()) {
			btnLoad.setEnabled(true);
		} else {
			btnLoad.setEnabled(false);
			IsProjectLoaded = false;
		}
		if (IsProjectLoaded) {
			btnPack.setEnabled(true);
			btnUnpack.setEnabled(true);
		} else {
			btnPack.setEnabled(false);
			btnUnpack.setEnabled(false);
		}
	}
	
	/**
	 * @param string
	 */
	public void openSettings(String msg) {
		settingsDialog.setVisible(true);
		settingsDialog.setWarningMessage(msg);
	}
}
