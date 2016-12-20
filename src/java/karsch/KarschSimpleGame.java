package karsch;

import karsch.level.LevelManager;
import karsch2.io.out.ConvertLevelImageToXML;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;

public class KarschSimpleGame extends SimpleApplication {
  public static final String VERSION = "1.0 ";
  public static final String AUTHORS = "David Walter & Stephan Dreyer";
  public static final boolean DEBUG = true;
  private static boolean isjar = true;
  private static boolean isforked = false;

  public static String[] assetPaths = new String[] {
      "karsch/resource/textures/", "karsch/resource/levels/",
      "karsch/resource/models/", "karsch/resource/sound/",
      "karsch/resource/sound/speech/gunther/",
      "karsch/resource/sound/speech/karsch/",
      "karsch/resource/sound/speech/mrskarsch/", "model/bin/",
      "model/ogre/tree1/", "model/ogre/fence1/", "model/ogre/house_inside/",
      "model/ogre/house/", "model/ogre/mrskarsch/", "model/ogre/hay/", };

  @Override
  public void simpleInitApp() {
    getContext().setTitle("Karsch the pig");

    for (final String path : assetPaths) {
      assetManager.registerLocator(path, ClasspathLocator.class);
    }

    final Values values = Values.getInstance();
    values.setSettings(settings);
    values.setAssetManager(assetManager);

    ConvertLevelImageToXML.convertAll();

    LevelManager.getInstance().setAppStateManager(stateManager);
    LevelManager.getInstance().loadFirstLevel();
    // LevelManager.getInstance().loadMenu();
  }

  public static void main(final String args[]) {

    // for (final String arg : args) {
    // if (arg.equalsIgnoreCase("--nofork")) {
    // isforked = true;
    // }
    // }
    //
    // if (isforked) {
    // TODO I think forking stuff is not needed anymore
    final KarschSimpleGame app = new KarschSimpleGame();
    app.setShowSettings(true);
    app.start();

    // } else {
    // try {
    // // class path, the quotes are necessary if a directory name contains
    // // spaces
    //
    // final String vmargs =
    // " -Xms128m -Xmx512m -XX:PermSize=128m -Djava.library.path=lib ";
    // String classpath = System.getProperty("java.class.path");
    // if (classpath == null || classpath.length() == 0) {
    // classpath = "Karsch.jar";
    // }
    //
    // final String params = " --nofork";
    // String command;
    //
    // if (isjar) { // if started from jar, fork the jar file
    //
    // String jarfile = "Karsch.jar";
    //
    // // if run in windows, the filename must be quoted to avoid problems
    // // with whitespaces
    // final boolean iswindows = System.getProperty("os.name").toLowerCase()
    // .contains("windows");
    // if (iswindows) {
    // System.out.println("Operation System: Windows");
    // jarfile = quote(jarfile);
    // }
    //
    // command = "java " + "-cp " + quote(".:" + classpath) + vmargs
    // + " -jar " + jarfile + params;
    //
    // } else { // fork the class file
    //
    // command = "java -cp " + classpath + vmargs + " "
    // + "karsch.KarschSimpleGame" + params;
    // }
    //
    // // get the runtime
    // final Runtime rt = Runtime.getRuntime();
    //
    // System.out.println("forking, command is: \n" + command);
    //
    // // execute command/fork process
    // final Process proc = rt.exec(command);
    //
    // System.out.println("start");
    //
    // // redirect error
    // final StreamGobbler errorGobbler = new StreamGobbler(
    // proc.getErrorStream(), StreamGobbler.TYPE_ERROR, true);
    //
    // // redirect output
    // final StreamGobbler outputGobbler = new StreamGobbler(
    // proc.getInputStream(), StreamGobbler.TYPE_OUTPUT, true);
    //
    // // start gobbler threads
    // errorGobbler.start();
    // outputGobbler.start();
    //
    // // any error???
    // final int exitVal = proc.waitFor();
    // System.out.println("exit");
    // System.out.println("ExitValue: " + exitVal);
    //
    // } catch (final Exception e) {
    // System.err.println("Fatal error");
    // e.printStackTrace();
    // }
    // }
  }

  private static String quote(final String string) {
    return " \"" + string + "\" ";
  }
}