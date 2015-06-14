/**
 * 
 */
package pl.grm.rvpacker;

import java.io.*;

import javax.swing.*;

/**
 * @author Levvy055
 *
 */
public class Runner extends SwingWorker<Integer, Void> {

	private Executor executor;
	private String type;
	private JComponent[] jComponents;

	/**
	 * 
	 * @param executor
	 * @param type
	 * @param jComponents
	 */
	public Runner(Executor executor, String type, JComponent[] jComponents) {
		this.executor = executor;
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
		for (JComponent jC : jComponents) {
			if (jC instanceof JProgressBar) {
				JProgressBar pB = (JProgressBar) jC;
				pB.setIndeterminate(true);
			} else {
				jC.setEnabled(false);
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
		for (JComponent jC : jComponents) {
			if (jC instanceof JProgressBar) {
				JProgressBar pB = (JProgressBar) jC;
				pB.setIndeterminate(false);
			} else {
				jC.setEnabled(true);
			}
		}
	}

	private int runScript(String type) throws Exception {
		int exitV = 0;
		try {
			File makeDir = new File(FileOperation.getFile(executor.getConfigValue(ConfigId.PROJECTFILE)).getParent());
			executor.append("Project directory: " + makeDir.getAbsolutePath(), 0);
			String envPacker = executor.getConfigValue(ConfigId.RUBY_PATH);
			if (envPacker == null || envPacker == "" || !new File(envPacker).exists()) {
				executor.getAppFrame().openSettings(
						"Provide correct path to Ruby bin directory with installed gem rvpacker'");
				executor.append("Provide correct path to Ruby bin directory with installed gem rvpacker'", 1);
				return 24;
			}
			Process p = Runtime.getRuntime().exec(
					envPacker + "\\rvpacker.bat --verbose -f -d " + makeDir + " -t ace -a " + type);
			getNewListener(new BufferedReader(new InputStreamReader(p.getInputStream())), 0).start();
			getNewListener(new BufferedReader(new InputStreamReader(p.getErrorStream())), 2).start();
			p.waitFor();
			exitV = p.exitValue();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
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
