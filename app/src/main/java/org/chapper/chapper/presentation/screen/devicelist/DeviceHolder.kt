package org.chapper.chapper.presentation.screen.devicelist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Device

class DeviceHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mBluetoothName: TextView by bindView(R.id.bluetooth_name)
    private val mBluetoothAddress: TextView by bindView(R.id.bluetooth_address)

    fun bind(device: Device, listener: DeviceListAdapter.OnItemClickListener) {
        mBluetoothName.text = device.bluetoothName
        mBluetoothAddress.text = device.bluetoothAddress

        itemView.setOnClickListener {
            listener.onItemClick(device)
        }
    }
}