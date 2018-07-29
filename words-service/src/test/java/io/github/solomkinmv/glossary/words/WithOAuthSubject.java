package io.github.solomkinmv.glossary.words;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithOAuthSubjectSecurityContextFactory.class)
public @interface WithOAuthSubject {

    String[] scopes() default {"myapi:write", "myapi:read"};

    String subjectId() default "a1de7cc9-1b3a-4ecd-96fa-dab6059ccf6f";

    boolean unique() default true;

}
