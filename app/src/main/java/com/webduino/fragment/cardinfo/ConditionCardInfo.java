package com.webduino.fragment.cardinfo;

import com.webduino.scenarios.Condition;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class ConditionCardInfo extends CardInfo {
    public Condition condition;

    public ConditionCardInfo() {
        type = TYPE_CONDITION;
    }
}