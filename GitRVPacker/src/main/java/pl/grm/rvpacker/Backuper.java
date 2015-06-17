/**
 * 
 */
package pl.grm.rvpacker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zeroturnaround.zip.*;

/**
 * @author Levvy055
 *
 */
public class Backuper {

	private File projectDir;
	private static final String prefixRPG = "Backups/Backup_RPGMaker_";
	private static final String prefixYAML = "Backups/Backup_YAML_";

	/**
	 * @param projectDir
	 */
	public Backuper(File projectDir) {
		this.projectDir = projectDir;
	}

	/**
	 * Creates backup of directories which will be overwritten
	 * 
	 * @param action
	 */
	public void makeBackup(RVPAction action) {
		if (!new File("Backups").exists()) {
			new File("Backups").mkdir();
		}
		switch (action) {
			case PACK :
				backupRPGMakerFiles();
				break;
			case UNPACK :
				backupYAMLFiles();
				break;
			default :
				break;
		}
	}

	/**
	 * Backup of Data directory
	 */
	private void backupRPGMakerFiles() {
		File dataDir;
		if ((dataDir = new File(projectDir.getAbsolutePath() + "\\Data")).exists() && dataDir.isDirectory()) {
			String zipFileName = prefixRPG + getDate() + ".zip";
			System.out.println(zipFileName);
			ZipUtil.pack(dataDir, new File(zipFileName));
		}
	}

	/**
	 * Backup of YAML and Scripts directories
	 */
	private void backupYAMLFiles() {
		File scriptsDir;
		File yamlDir;
		if ((scriptsDir = new File(projectDir.getAbsolutePath() + "\\Scripts")).exists() && scriptsDir.isDirectory()
				&& (yamlDir = new File(projectDir.getAbsolutePath() + "\\YAML")).exists() && yamlDir.isDirectory()) {
			String zipFileName = prefixYAML + getDate() + ".zip";
			System.out.println("Creating backup file " + zipFileName);
			File zipFile = new File(zipFileName);
			File[] sFiles = scriptsDir.listFiles();
			File[] yFiles = yamlDir.listFiles();
			ZipEntrySource[] fS = new ZipEntrySource[sFiles.length + yFiles.length];
			for (int i = 0; i < sFiles.length + yFiles.length; i++) {
				if (i < sFiles.length) {
					File file = sFiles[i];
					fS[i] = new FileSource("Scripts/" + file.getName(), file);
				} else {
					File file = yFiles[i - sFiles.length];
					fS[i] = new FileSource("YAML/" + file.getName(), file);
				}
			}
			ZipUtil.pack(fS, zipFile);
		}
	}

	private String getDate() {
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	}
}
