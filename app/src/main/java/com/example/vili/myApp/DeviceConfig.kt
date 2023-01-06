package viliApp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import viliApp.DeviceConfig.Companion.setDpValues

@Composable
fun getDeviceConfig() {

    val configuration = LocalConfiguration.current

    setDpValues(configuration.screenHeightDp.dp,configuration.screenWidthDp.dp)
}

class DeviceConfig(){

    companion object{

        var screenHeight : Dp = 0.dp
        var screenWidth : Dp = 0.dp

        fun setDpValues(height: Dp, width: Dp){
            screenHeight = height
            screenWidth = width
        }

        fun heightPercentage(targetPercentage : Int): Dp {
            return ((targetPercentage * screenHeight.toString().substringBefore('.').toInt())/100).dp
        }

        fun widthPercentage(targetPercentage : Int): Dp {
            return ((targetPercentage * screenWidth.toString().substringBefore('.').toInt())/100).dp
        }


    }

}