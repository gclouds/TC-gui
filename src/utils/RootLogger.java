package utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

public class RootLogger {
	PatternLayout layout = new PatternLayout();
	ConsoleAppender consoleAppender = new ConsoleAppender();
	FileAppender fileAppender = new FileAppender();
	Logger rootLogger = Logger.getRootLogger();
	

	public RootLogger(boolean isConsoleAppender,boolean isFileAppender) {
		init(isConsoleAppender,isFileAppender);
	}


	public void init(boolean isConsoleAppender,boolean isFileAppender){
        // creates pattern layout
        String conversionPattern = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n";
        layout.setConversionPattern(conversionPattern);
 
        // creates console appender
        consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();
 
        // creates file appender
        fileAppender.setFile("./log/truechip.log");
        fileAppender.setLayout(layout);
        fileAppender.activateOptions();
 
        // configures the root logger
        rootLogger.setLevel(Level.INFO);
        if(isConsoleAppender) rootLogger.addAppender(consoleAppender);
        if(isFileAppender)rootLogger.addAppender(fileAppender);
		
	}
}
