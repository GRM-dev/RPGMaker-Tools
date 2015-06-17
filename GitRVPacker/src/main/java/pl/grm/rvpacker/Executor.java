/**
 * 
 */
package pl.grm.rvpacker;

import java.awt.Color;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * @author Levvy055
 *
 */
public class Executor {

	private Logger logger;
	private JTextPane outputConsole;
	private Runner runner;
	private MainPacker packer;
	private AppFrame appFrame;

	/**
	 * @param mainPacker
	 */
	public Executor(MainPacker packer) {
		this.packer = packer;
		this.logger = packer.getLogger();
	}

	/**
	 * appends text msg to JTextPane, logger and console
	 * 
	 * @param msg
	 *            message to append
	 * @param level
	 *            (0-Normal,1-Warn,2-Error,3-Debug)
	 */
	synchronized void append(String msg, int level) {
		Color color = null;
		switch (level) {
			case 0 :
				color = Color.GRAY;
				logger.info(msg);
				break;
			case 1 :
				color = Color.ORANGE;
				logger.warning(msg);
				break;
			case 2 :
				color = Color.RED;
				logger.severe(msg);
				break;
			case 3 :
				color = Color.BLUE;
				logger.log(Level.FINE, msg, new Exception());
				break;
			default :
				color = Color.BLACK;
				break;
		}
		if (outputConsole == null) return;
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		int len = outputConsole.getDocument().getLength();
		StyledDocument doc = outputConsole.getStyledDocument();
		outputConsole.replaceSelection(msg);
		try {
			doc.insertString(len, msg + "\n", aset);
			outputConsole.setCaretPosition(outputConsole.getCaretPosition() + msg.length() + 1);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// e.printStackTrace();
		}

	}

	/**
	 * Runs Pack Script
	 * 
	 * @param jComponents
	 */
	public void runPackScript(JComponent[] jComponents) {
		runner = new Runner(this, RVPAction.PACK, jComponents);
		runner.execute();
	}

	/**
	 * Runs Unpack script
	 * 
	 * @param jComponents
	 */
	public void runUnpackScript(JComponent[] jComponents) {
		runner = new Runner(this, RVPAction.UNPACK, jComponents);
		runner.execute();
	}

	/**
	 * 
	 * @return true if can be executed new command in runner
	 */
	public boolean canExecute() {
		if (runner != null && !runner.isDone()) {
			return false;
		} else {
			return true;
		}
	}

	public Runner getNewRunner() {
		return new Runner(this);
	}

	/**
	 * 
	 * @return JTextField if exists
	 */
	public JTextPane getOutputConsole() {
		return outputConsole;
	}

	/**
	 * 
	 * @param outputConsole
	 *            set it to append text to it on GUI
	 */
	public void setOutputConsole(JTextPane outputConsole) {
		this.outputConsole = outputConsole;
	}

	/**
	 * @param key
	 *            key associated with value
	 * @return
	 */
	public String getConfigValue(ConfigId key) {
		return packer.getConfigValue(key);
	}

	/**
	 * 
	 * @return
	 */
	public AppFrame getAppFrame() {
		return appFrame;
	}

	/**
	 * 
	 * @param appFrame
	 */
	public void setAppFrame(AppFrame appFrame) {
		this.appFrame = appFrame;
	}

	/**
	 * Gets logger
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}
}
