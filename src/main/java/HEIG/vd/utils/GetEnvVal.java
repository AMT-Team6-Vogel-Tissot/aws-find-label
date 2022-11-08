package HEIG.vd.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class GetEnvVal {
    private static final Dotenv env = Dotenv.configure()
            .ignoreIfMissing()
            .systemProperties()
            .load();

    public static String getEnvVal(String name){
        return env.get(name);
    }
}
