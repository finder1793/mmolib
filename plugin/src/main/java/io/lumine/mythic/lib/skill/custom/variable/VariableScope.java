package io.lumine.mythic.lib.skill.custom.variable;

/**
 * In which environment the skill variable is being saved.
 */
public enum VariableScope {

    /**
     * Player variables are common to ALL skills which
     * means a skill can modify a player's variable, and
     * another skill can access the same modified variable.
     */
    PLAYER,

    /**
     * By default, variables are only stored when a skill
     * is being cast. The variable will be lost at the
     * very moment the skill executes its last mechanic
     */
    SKILL;
}
