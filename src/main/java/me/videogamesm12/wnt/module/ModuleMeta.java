package me.videogamesm12.wnt.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleMeta
{
    String name();

    String description() default "Default description";
}