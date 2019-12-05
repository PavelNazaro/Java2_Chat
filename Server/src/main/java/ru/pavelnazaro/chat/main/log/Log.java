package ru.pavelnazaro.chat.main.log;

import java.io.IOException;
import java.util.logging.*;

public class Log {
    protected static final Logger logger = Logger.getLogger("");

    public static void main(String[] args) throws IOException {

    }

    public static void init() throws IOException {
        logger.setLevel(Level.ALL);
        logger.getHandlers()[0].setLevel(Level.ALL);

        logger.getHandlers()[0].setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getLevel() + "\t" + record.getMessage() + "\t" + record.getMillis() + "\n";
            }
        });

        //logger.getHandlers()[0].setFilter(record -> record.getMessage().startsWith("Checkpoint"));

        Handler handler = new FileHandler("mylog.log", true);
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);

        setLog(Level.INFO, "init");
    }

    public static void setLog(Level level, String msg){
        logger.log(level, msg);
        System.out.println(msg);
//        logger.log(Level.SEVERE, " S");
//        logger.log(Level.INFO, "Checkpoint I");
//        logger.log(Level.CONFIG, " C");
//        logger.log(Level.FINE, "Checkpoint F");
        //OFF
        //SEVERE
        //WARNING
        //INFO
        //CONFIG
        //FINE(3)
        //ALL
    }
}
