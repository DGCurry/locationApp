package com.example.locationwake.Backend.Behaviour;

public abstract class Component {

    /**
     * Whether the object the component is added to should be activated or not
     * @return True iff the criteria for the component has been met, else False
     */
    public abstract boolean isActive();

}
