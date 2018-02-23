package com.webduino.fragment.cardinfo;

import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class OptionCardInfo extends CardInfo {

    public OptionCardValue value;

    public OptionCardInfo() {
        type = TYPE_OPTION;
    }
}
