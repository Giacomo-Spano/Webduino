package com.webduino.fragment.cardinfo;

import com.webduino.scenarios.Condition;
import com.webduino.scenarios.ProgramAction;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class ConditionCardInfo extends CardInfo {
    public Condition condition;

    public ConditionCardInfo() {
        type = TYPE_CONDITION;
    }
}