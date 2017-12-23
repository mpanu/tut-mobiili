package fi.tut.matti.thereminseq;

import com.csounds.CsoundObj;
import com.csounds.bindings.CsoundBinding;

import csnd6.Csound;

class PitchBinding implements CsoundBinding {
    private CsoundObj csoundObj = null;
    public double pitch;

    @Override
    public void setup(CsoundObj csoundObj) {
        this.csoundObj = csoundObj;
    }

    @Override
    public void updateValuesToCsound() {

    }

    @Override
    public void updateValuesFromCsound() {
        Csound csound = csoundObj.getCsound();
        pitch = csound.GetChannel("pitch");
    }

    @Override
    public void cleanup() {

    }
}