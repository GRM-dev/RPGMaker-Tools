package pl.grm.rvpacker;

import java.awt.HeadlessException;
import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.*;

/**
 * @author Levvy055
 *
 */
public class Runner extends SwingWorker<Integer, Void> {

	private Executor executor;
	private RVPAction type;
	private JComponent[] jComponents;

	public Runner(Executor executor) {
		this.executor = executor;
	}

	/**
	 * 
	 * @param executor
	 * @param type
	 * @param jComponents
	 */
	public Runner(Executor executor, RVPAction type, JComponent[] jComponents) {
		this(executor);
		this.type = type;
		this.jComponents = jComponents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		if (jComponents != null) {
			for (JComponent jC : jComponents) {
				if (jC instanceof JProgressBar) {
					JProgressBar pB = (JProgressBar) jC;
					pB.setIndeterminate(true);
					pB.addChangeListener(e -> {

					});
				} else {
					jC.setEnabled(false);
				}
			}
		}
		int exit = 1;
		try {
			exit = runScript(type);
		}
		catch (Exception e) {
			executor.append("Error while running script", 2);
		}
		return exit;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		executor.append("Done. Process terminating!", 0);
		if (jComponents != null) {
			for (JComponent jC : jComponents) {
				if (jC instanceof JProgressBar) {
					JProgressBar pB = (JProgressBar) jC;
					pB.setIndeterminate(false);
					try {
						int exit = 0;
						if ((exit = get()) == 0)
							JOptionPane.showMessageDialog(pB, "Executed " + type.toString().toLowerCase()
									+ " successfully", "Done!" + exit, JOptionPane.INFORMATION_MESSAGE);
						else {
							JOptionPane.showMessageDialog(pB, "Error! Return exit code: " + exit, "Exception occured",
									JOptionPane.WARNING_MESSAGE);
						}
					}
					catch (HeadlessException | InterruptedException | ExecutionException e) {
						e.printStackTrace();
						executor.append("Exception", 2);
					}
				} else {
					jC.setEnabled(true);
				}
			}
		}
	}

	private int runScript(RVPAction actionType) throws Exception {
		int exitV = 0;
		try {
			setProgress(20);
			File projectDir = new File(FileOperation.getFile(executor.getConfigValue(ConfigId.PROJECTFILE)).getParent());
			executor.append("Project directory: " + projectDir.getAbsolutePath(), 0);
			String rubyPath = executor.getConfigValue(ConfigId.RUBY_PATH);
			Backuper backup = new Backuper(projectDir);
			try {
				backup.makeBackup(actionType);
			}
			catch (Exception e) {
				executor.getLogger().log(Level.SEVERE, "Backup error", e);
				e.printStackTrace();
				executor.append(e.getMessage(), 2);
			}
			exitV = runScript(actionType, projectDir, rubyPath, true);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		return exitV;
	}

	public int runScript(RVPAction actionType, File projectDir, String rubyPath, boolean hasGui) throws IOException,
			InterruptedException {
		int exitV;
		if (hasGui && (rubyPath == null || rubyPath == "" || !new File(rubyPath).exists())) {
			executor.getAppFrame().openSettings(
					"Provide correct path to Ruby bin directory with installed gem rvpacker'");
			executor.append("Provide correct path to Ruby bin directory with installed gem rvpacker'", 1);
			return 24;
		}
		String runStr = rubyPath + "\\rvpacker.bat --verbose -f -d " + projectDir + " -t ace -a "
				+ actionType.toString().toLowerCase();
		executor.getLogger().info(runStr);
		Process p = Runtime.getRuntime().exec(runStr);
		getNewListener(new BufferedReader(new InputStreamReader(p.getInputStream())), 0).start();
		getNewListener(new BufferedReader(new InputStreamReader(p.getErrorStream())), 2).start();
		p.waitFor();
		exitV = p.exitValue();
		return exitV;
	}

	public Thread getNewListener(BufferedReader inBase, int level) {
		return new Thread(() -> {
			String line = null;
			try {
				while ((line = inBase.readLine()) != null) {
					executor.append(line, level);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
