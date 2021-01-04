package ru.test.testjni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.layout_can_debug.*
import ru.test.testjni.callback.ResultReceiverCallback
import ru.test.testjni.canbus.CanUtils
import ru.test.testjni.canbus.response.CanBusBean
import ru.test.testjni.service.CanService
import java.lang.Exception

/**
 * @author wyl
 * @description:
 * @date :2021/1/4 16:40
 */
class CanDebugActivity : AppCompatActivity() {
    private var bitrate = 125000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_can_debug)
        val callback = object : ResultReceiverCallback<CanBusBean> {
            override fun onSuccess(data: CanBusBean?) {
                data?.let {
                    tvRecord.append(HexUtils.bytesToHexString(it.canId))
                    tvRecord.append(":")
                    tvRecord.append(HexUtils.bytesToHexString(it.data))
                    tvRecord.append("\n")
                }
            }

            override fun onError(exception: Exception?) {

            }

        }
        CanService.listenCanInterface(this, callback)
    }

    fun applyBitrate() {
        if (!TextUtils.isEmpty(etBitrate.text)) {
            bitrate = etBitrate.text.toString().toInt()
            CanUtils.setBitrate(bitrate)
        }
    }

    fun sendData() {
        if (!TextUtils.isEmpty(etCanId.text) && !TextUtils.isEmpty(etData.text)) {
            CanUtils.sendCan0Data(HexUtils.byteArrayToInt(HexUtils.hexStringToBytes(etCanId.text.toString().replace(" ", ""))),
                    HexUtils.hexStringToBytes(etData.text.toString().replace(" ", "")))
        }
    }

    fun clearRecord() {
        tvRecord.text = ""
    }
}