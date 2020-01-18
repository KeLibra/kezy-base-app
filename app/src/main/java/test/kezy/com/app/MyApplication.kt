package test.kezy.com.app

import androidx.multidex.MultiDexApplication
import test.kezy.libs.common.config.AppConfigLib


/**
 */
class MyApplication : MultiDexApplication() {

    private var channelId: String = ""

    override fun onCreate() {
        super.onCreate()

        // 高德地图

        AppConfigLib.init(this)

        initPageRouter()
        initNetWorkEnv()

        initScreenInfo()

        initRouterAndImpl()

    }

    private fun initPageRouter() {

    }

    private fun initNetWorkEnv() {

    }

    private fun initRouterAndImpl() {


    }

    private fun initScreenInfo() {
    }
}
