package se.work.task.management;

import java.util.function.Function;

public class Utility {

    public static <T, R> R notNullThenGet(T object, Function<T, R> getter) {
        if (object == null) {
            return null;
        }

        return getter.apply(object);
    }
}
