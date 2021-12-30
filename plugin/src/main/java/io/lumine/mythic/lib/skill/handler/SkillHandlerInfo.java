package io.lumine.mythic.lib.skill.handler;

public @interface SkillHandlerInfo {
    public String plugin() default "mythiclib";

    public String author();
}
