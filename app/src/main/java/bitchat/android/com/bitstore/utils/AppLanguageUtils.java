package bitchat.android.com.bitstore.utils;

import com.android.base.app.BaseApp;
import com.android.base.cache.ACacheUtil;
import com.android.base.conts.AcacheKeys;
import com.android.base.language.bean.LanguageSettingBean;
import com.orhanobut.logger.Logger;

public class AppLanguageUtils {


    public static String getCurrentLang(){

        LanguageSettingBean languageSettingBean = (LanguageSettingBean) ACacheUtil.get(BaseApp.getContext()).getAsObject(AcacheKeys.LANGUAGELOCALE);
        if(languageSettingBean!=null){
            languageSettingBean.getLocale();
            Logger.d("------languageSettingBean.getLocale()---->"+languageSettingBean.getLocale()+"  "+languageSettingBean.getName());
        }

        String lang=languageSettingBean.getLocale().toString();
        if(lang.contains("_")){
            lang=lang.substring(0,lang.indexOf("_"));
        }

        return lang;

    }
}
