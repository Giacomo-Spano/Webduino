package com.webduino.fragment.cardinfo;

import com.webduino.elements.Program;
import com.webduino.scenarios.ScenarioProgram;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class ProgramCardInfo extends CardInfo {
    public ScenarioProgram program;
    public ProgramCardInfo() {
        type = TYPE_PROGRAM;
    }
}