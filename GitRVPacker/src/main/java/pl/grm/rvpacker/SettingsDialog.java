/**
 * 
 */
package pl.grm.rvpacker;

import java.awt.*;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author Levvy055
 *
 */
public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tF_RubyPath;
	private JFileChooser chooser;
	private MainPacker packer;
	private JLabel lbl_Warn;

	/**
	 * Create the dialog.
	 * 
	 * @param packer
	 */
	public SettingsDialog(MainPacker packerA) {
		this.packer = packerA;
		setBounds(100, 100, 450, 300);
		setTitle("Settings");
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel topPanel = new JPanel();
			getContentPane().add(topPanel, BorderLayout.NORTH);
			{
				lbl_Warn = new JLabel("");
				lbl_Warn.setForeground(new Color(153, 0, 0));
				lbl_Warn.setFont(new Font("Tahoma", Font.PLAIN, 14));
				topPanel.add(lbl_Warn);
			}
		}
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblRubyBinPath = new JLabel("Ruby bin path: ");
			contentPanel.add(lblRubyBinPath);
		}
		{
			tF_RubyPath = new JTextField();
			contentPanel.add(tF_RubyPath);
			tF_RubyPath.setColumns(30);
		}
		{
			chooser = new JFileChooser(new File(".")) {

				private static final long serialVersionUID = 1L;

				@Override
				public void approveSelection() {
					if (getSelectedFile().isFile()) {
						return;
					} else {
						File f1 = new File(getSelectedFile().getAbsolutePath() + "\\rvpacker");
						File f2 = new File(getSelectedFile().getAbsolutePath() + "\\rvpacker.sh");
						File f3 = new File(getSelectedFile().getAbsolutePath() + "\\rvpacker.bat");
						System.out.println(".... " + f1.exists() + " " + f2.exists() + " " + f3.exists());
						if (f1.exists() || f2.exists() || f3.exists()) {
							super.approveSelection();
						} else {
							fireActionPerformed("WRONG_SELECTION");
						}
					}
				};
			};
			chooser.setDialogTitle("Choose Ruby bin folder and check if you have rvpacker there");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.addActionListener(e -> {
				if (e.getActionCommand().equals("WRONG_SELECTION")) {
					JOptionPane.showMessageDialog(SettingsDialog.this, "There is no rvpacker", "Wrong selection",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		{
			JButton btnChange = new JButton("Change ...");
			btnChange.addActionListener(e -> {
				if (chooser.showOpenDialog(SettingsDialog.this) == JFileChooser.APPROVE_OPTION) {
					File dir = chooser.getSelectedFile();
					packer.setConfigValue(ConfigId.RUBY_PATH, dir.getAbsolutePath());
					setWarningMessage("");
					JOptionPane.showMessageDialog(SettingsDialog.this, "Saved", "Saved",
							JOptionPane.INFORMATION_MESSAGE);
					tF_RubyPath.setText(packerA.getConfigValue(ConfigId.RUBY_PATH));
				}
			});
			contentPanel.add(btnChange);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Apply");
				okButton.addActionListener(e -> {
					setWarningMessage("");
					this.dispose();
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(e -> {
					this.dispose();
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * @param msg
	 */
	public void setWarningMessage(String msg) {
		lbl_Warn.setText(msg);
	}

	/**
	 * @param config
	 */
	public void applyConfig(HashMap<ConfigId, String> config) {
		for (Iterator<ConfigId> it = config.keySet().iterator(); it.hasNext();) {
			ConfigId conf = it.next();
			switch (conf) {
				case PROJECTFILE :
					break;
				case RUBY_PATH :
					tF_RubyPath.setText(config.get(conf));
					break;
				default :
					break;
			}
		}
	}

}
