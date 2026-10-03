package Singleton_pattern;

// Eager Loading (thread safe)
class JudgeAnalyticsEagerLoading{

    private static final JudgeAnalyticsEagerLoading judge = new JudgeAnalyticsEagerLoading();
    private JudgeAnalyticsEagerLoading(){}

    public static JudgeAnalyticsEagerLoading getInstance(){
        return judge;
    }

}

// Lazy Loading (Not thread safe) - multiple instance can form because of preemption of threads
class JudgeAnalyticsLazyLoading{
    private static JudgeAnalyticsLazyLoading judge;
    private JudgeAnalyticsLazyLoading(){}

    public static JudgeAnalyticsLazyLoading getInstance(){
        if(judge == null){
            judge = new JudgeAnalyticsLazyLoading();
        }
        return judge;
    }

}

// Thread Safety of Lazy loading using synchronized keyword // synchronizing every call is overhead.
class JudgeAnalyticsLazyLoadingSync{
    private static JudgeAnalyticsLazyLoadingSync judge;
    private JudgeAnalyticsLazyLoadingSync(){}

    public static synchronized JudgeAnalyticsLazyLoadingSync getInstance(){
        if(judge == null){
            judge = new JudgeAnalyticsLazyLoadingSync();
        }
        return judge;
    }

}

// Thread safety using  Double-Checked Locking
class JudgeAnalyticsLazyLoadingDoubleCheckedLocking{
    private static volatile JudgeAnalyticsLazyLoadingDoubleCheckedLocking judge;
    private JudgeAnalyticsLazyLoadingDoubleCheckedLocking(){}

    public static JudgeAnalyticsLazyLoadingDoubleCheckedLocking getInstance(){
        if(judge == null){
            synchronized (JudgeAnalyticsLazyLoadingDoubleCheckedLocking.class) {
                if (judge == null) {
                    judge = new JudgeAnalyticsLazyLoadingDoubleCheckedLocking();
                }
            }

        }
        return judge;
    }

}


public class Main {
    public static void main(String[] args){
        JudgeAnalyticsEagerLoading judge = JudgeAnalyticsEagerLoading.getInstance();
        JudgeAnalyticsLazyLoading judge2 = JudgeAnalyticsLazyLoading.getInstance();
        JudgeAnalyticsLazyLoadingSync judge3 = JudgeAnalyticsLazyLoadingSync.getInstance();
        JudgeAnalyticsLazyLoadingDoubleCheckedLocking judge4 = JudgeAnalyticsLazyLoadingDoubleCheckedLocking.getInstance();
        System.out.println(judge);
        System.out.println(judge2);
        System.out.println(judge3);
        System.out.println(judge4);
    }

}
