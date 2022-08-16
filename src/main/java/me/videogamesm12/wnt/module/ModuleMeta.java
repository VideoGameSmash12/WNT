package me.videogamesm12.wnt.module;

import org.intellij.lang.annotations.Subst;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleMeta
{
    @Subst("wnt:unknown")
    String namespace();

    @Subst("Undefined")
    String description();
}
