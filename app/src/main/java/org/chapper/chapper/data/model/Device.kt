package org.chapper.chapper.data.model

class Device {
    var bluetoothName = ""
    var bluetoothAddress = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (bluetoothName != other.bluetoothName) return false
        if (bluetoothAddress != other.bluetoothAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bluetoothName.hashCode()
        result = 31 * result + bluetoothAddress.hashCode()
        return result
    }


}