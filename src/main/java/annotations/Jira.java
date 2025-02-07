package annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Jira {
    // cannot throw exceptions
    // primitive data types (int, boolean, double, char...), String, enum, Class, array of these data types
    // cannot have any parameters
    // Can have Default value
    String jiraID();
    String owner() default "Regression Team";
    String author() default "";
}
