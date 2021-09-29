package com.all.firestorecounterapp.interfaces;

import com.all.firestorecounterapp.models.CounterModel;

public interface UpdateAdapter {
    void updateRow(CounterModel model, int index, boolean liked);
}
