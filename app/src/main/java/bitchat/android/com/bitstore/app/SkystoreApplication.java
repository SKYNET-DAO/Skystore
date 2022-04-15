package bitchat.android.com.bitstore.app;

import bitchat.android.com.bitstore.utils.UIUtils;
import com.android.base.app.BaseApp;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import org.apache.log4j.Level;
import org.litepal.LitePal;

public class SkystoreApplication extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        UIUtils.init(this);
    }

    private void configLog(String logFileNamePrefix) {
        LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setUseFileAppender(false);
        logConfigurator.setLevel("------bitconinj----->", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 2);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.setUseLogCatAppender(true);
        logConfigurator.setLogCatPattern(" %n ----------------------------bitconinj start----------------------------- %n  Time:%d %n lever:%-5p %n 包名:[%c{5}]-[line:%L] %n  Contents:%m %n  ----------------------------bitconinj end-----------------------------  %n %n %n");
        logConfigurator.configure();
    }



}
