/**
 * 
 */
package pl.grm.rvpacker;

import java.awt.EventQueue;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * @author Levvy055
 *
 */
public class MainPacker {

	public static final String EXECUTABLE_PATH = System.getProperty("user.dir") + "\\";
	public static final String PATH_RES = EXECUTABLE_PATH + "\\src\\main\\resources\\";
	public static final String CONFIG_FILE_NAME = "config.ini";
	public static final String LOGGER_FILE_NAME = "yrvPacker.log";
	private Logger logger;
	private Executor executor;
	/** Key - Identificator of config, Value - config Value */
	private HashMap<ConfigId, String> config;
	private HashMap<ArgType, HashMap<String, String>> argsMap;
	private boolean needsSave;
	private AppFrame appFrame;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainPacker packer = new MainPacker();
		if (args != null && args.length > 0) {
			packer.parseArgs(args);
			packer.verify();

		} else {
			packer.init();
			packer.start();
		}
	}

	/**
	 * Main private constructor called only from main()
	 */
	private MainPacker() {
		config = createDefaultConfig();
		argsMap = new HashMap<>();
	}

	/**
	 * Collects args to appropriate list and save the in argsMap
	 * 
	 * @param args
	 */
	private void parseArgs(String[] args) {
		HashMap<String, String> argsList = new HashMap<String, String>();
		HashMap<String, String> optsList = new HashMap<String, String>();
		HashMap<String, String> doubleOptsList = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
				case '-' :
					if (args[i].length() < 2) throw new IllegalArgumentException("Not a valid argument: " + args[i]);
					if (args[i].charAt(1) == '-') {
						if (args[i].length() < 3)
							throw new IllegalArgumentException("Not a valid argument: " + args[i]);
						doubleOptsList.put(args[i].substring(2, args[i].length()), "");
					} else {
						if (args.length - 1 == i) throw new IllegalArgumentException("Expected arg after: " + args[i]);
						optsList.put(args[i], args[i + 1]);
						i++;
					}
					break;
				default :
					argsList.put(args[i], "");
					break;
			}
		}
		argsMap.put(ArgType.ARGS, argsList);
		argsMap.put(ArgType.OPTS, optsList);
		argsMap.put(ArgType.DOUBLE_OPTS, doubleOptsList);
		Iterator<String> it1 = optsList.keySet().iterator();
		while (it1.hasNext()) {
			String arg = it1.next();
			System.out.println(arg + ": " + argsList.get(arg));
		}
		System.out.println("____");
		Iterator<String> it2 = argsList.keySet().iterator();
		while (it2.hasNext()) {
			String arg = it2.next();
			System.out.println(arg);
		}
		System.out.println("____");
		Iterator<String> it3 = doubleOptsList.keySet().iterator();
		while (it3.hasNext()) {
			String arg = it3.next();
			System.out.println(arg);
		}
	}

	/**
	 * 
	 */
	private boolean verify() {
		HashMap<String, String> optsMap = argsMap.get(ArgType.OPTS);
		if (!optsMap.containsKey("p") || !optsMap.containsKey("r")) return false;
		return true;
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
					setConfigValue(key, newValue);
					needsSave = false;
				}
			}
		} else {
			FileOperation.saveConfig(config);
		}
		executor = new Executor(this);
		appFrame = new AppFrame(this);
		String path;
		if ((path = getConfigValue(ConfigId.RUBY_PATH)) == null || !new File(path).exists()
				|| !new File(path + "\\rvpacker.bat").exists()) {
			tryFindRuby();
			save();
		}
	}

	/**
	 * Looking for Ruby path and rvpacker gem there
	 */
	private void tryFindRuby() {
		String path = "";
		boolean rubyPathFound = false;
		boolean rvpackerFound = false;
		Map<String, String> envs = System.getenv();
		if (envs.containsKey("RUBY")) {
			String env = envs.get("RUBY");
			if (rubyPathFound = findRuby(env)) {
				rvpackerFound = findRVPacker(env);
			}
		}
		if (!rvpackerFound && envs.containsKey("RUBYPATH")) {
			String env = envs.get("RUBYPATH");
			if (rubyPathFound = findRuby(env)) {
				rvpackerFound = findRVPacker(env);
			}
		}
		if (!rvpackerFound && envs.containsKey("PATH")) {
			String env = envs.get("PATH");
			if (rubyPathFound = findRuby(env)) {
				rvpackerFound = findRVPacker(env);
			}
		}

		if (rubyPathFound) {
			if (rvpackerFound) {
				setConfigValue(ConfigId.RUBY_PATH, path);
			} else {
				JOptionPane.showMessageDialog(appFrame, "Can't find gem rvpacker in Ruby path",
						"Ruby found, but can't find gems", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(appFrame, "Wrong Ruby Path or no path in ENV", "Ruby path not found",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @param env
	 * @return
	 */
	private boolean findRuby(String env) {
		String[] paths = env.split(";");
		for (String path : paths) {
			File file;
			if ((file = new File(path)).exists() && file.isDirectory() && new File(path + "\\ruby.exe").exists()) { return true; }
		}
		return false;
	}

	/**
	 * @param env
	 * @return
	 */
	private boolean findRVPacker(String env) {
		String[] paths = env.split(";");
		for (String path : paths) {
			if (new File(path).exists() && new File(path + "\\rvpacker.bat").exists()) { return true; }
		}
		return false;
	}

	/**
	 * Start program
	 */
	private void start() {
		EventQueue.invokeLater(() -> {
			try {
				appFrame.applyConfig(config);
				appFrame.setVisible(true);
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
	public void setConfigValue(ConfigId key, String value) {
		if (value != null) {
			needsSave = true;
			config.put(key, value);
		}
	}

	/**
	 * Save config and ammends all actions when closing
	 */
	public void save() {
		if (needsSave) {
			FileOperation.saveConfig(config);
		}
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

	public enum ArgType {
		ARGS,
		OPTS,
		DOUBLE_OPTS
	}
}
