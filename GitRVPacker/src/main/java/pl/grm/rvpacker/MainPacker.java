/**
 * 
 */
package pl.grm.rvpacker;

import java.awt.EventQueue;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Levvy055
 *
 */
public class MainPacker {

	public static final String EXECUTABLE_PATH = System.getProperty("user.dir") + "\\";
	public static final String PATH_RES = EXECUTABLE_PATH + "\\src\\main\\resources\\";
	public static final String CONFIG_FILE_NAME = "config.ini";
	public static final String LOGGER_FILE_NAME = "yrvPacker.log";
	private static String CURRENT_JAR;
	private Logger logger;
	private Executor executor;
	/** Key - Identificator of config, Value - config Value */
	private HashMap<ConfigId, String> config;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CURRENT_JAR = FileOperation.getCurrentJarPath(MainPacker.class);
		}
		catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		MainPacker packer = new MainPacker();
		packer.init();
		packer.start();
	}

	/**
	 * Main private constructor called only from main()
	 */
	private MainPacker() {
		config = createDefaultConfig();
	}

	/**
	 * Initialize program
	 */
	private void init() {
		logger = FileOperation.setupLogger(LOGGER_FILE_NAME);
		if (FileOperation.configExists()) {
			HashMap<ConfigId, String> fileConf = FileOperation.readConfig();
			for (Iterator<ConfigId> it = fileConf.keySet().iterator(); it.hasNext();) {
				ConfigId key = it.next();
				String newValue = fileConf.get(key);
				String oldValue = config.get(key);
				if (!newValue.equals(oldValue)) {
					logger.info("Read " + key + ": " + newValue + " (default: "
							+ (oldValue.isEmpty() ? "\"\"" : oldValue) + ")");
					updateConfigValue(key, newValue);
				}
			}
		} else {
			FileOperation.saveConfig(config);
		}
		executor = new Executor(this);
	}

	/**
	 * Start program
	 */
	private void start() {
		EventQueue.invokeLater(() -> {
			try {
				AppFrame frame = new AppFrame(this);
				frame.applyConfig(config);
				frame.setVisible(true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @return default config map
	 */
	private HashMap<ConfigId, String> createDefaultConfig() {
		HashMap<ConfigId, String> configD = new HashMap<>();
		for (ConfigId conf : ConfigId.values()) {
			configD.put(conf, conf.getDefValue());
		}
		return configD;
	}

	/**
	 * Updates config value with new one
	 * 
	 * @param key
	 *            config of which value we want to update
	 * @param value
	 *            new value to override previous one
	 */
	public void updateConfigValue(ConfigId key, String value) {
		if (value != null) {
			config.put(key, value);
		}
	}

	/**
	 * Save config and ammends all actions when closing
	 */
	public void close() {
		FileOperation.saveConfig(config);
	}

	/**
	 * Gets Logger
	 * 
	 * @return logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Gets Executor
	 * 
	 * @return executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * @param rubyPath
	 * @return
	 */
	public String getConfigValue(ConfigId key) {
		return config.get(key);
	}
}
