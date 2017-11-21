package com.example.bluedream.memorizevocabularyword;


import android.app.Activity;
import android.app.Application;
import java.util.LinkedList;
import java.util.List;


class AppUtils extends Application{
    private List<Activity> activityList = new LinkedList<Activity>();
    private static AppUtils instance;
    private AppUtils()
    {
    }
    //单例模式中获取唯一的app实例
    public static AppUtils getInstance()
    {
        if(null == instance)
        {
            instance = new AppUtils();
        }
        return instance;
    }
    //添加Activity到容器中   何问起 hovertree.com
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    //遍历所有Activity并finish
    public void exit()
    {
        for(Activity activity:activityList)
        {
            activity.finish();
        }
        System.exit(0);
    }
}  