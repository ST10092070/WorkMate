package com.opsc.workmate.data
//Singleton class for storing live data
class Global {

    @Volatile
    private var INSTANCE: Global? = null
    //Multi-thread lazy initialisation
    // public function to retrieve the singleton instance
    fun getInstance(): Global? {
        // Check if the instance is already created
        if (INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized(this) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = Global()
                }
            }
        }
        // return the singleton instance
        return INSTANCE
    }

    //=======================================================================================//




}