package com.shortstack.hackertracker.Database

class Patches {

    lateinit internal var patches: Array<Patch>

    inner class Patch {
        internal var version: Int = 0
        lateinit internal var commands: Array<String>
    }

}
