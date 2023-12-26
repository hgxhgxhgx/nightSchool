package annotation;

import com.tencent.wxcloudrun.model.enumList.GenerationType;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

    GenerationType strategy() default GenerationType.AUTO;
}
