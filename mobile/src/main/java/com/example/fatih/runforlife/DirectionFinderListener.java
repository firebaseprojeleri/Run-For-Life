package com.example.fatih.runforlife;

import java.util.List;

/**
 * Created by Fatih on 21.04.2017.
 */



public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
