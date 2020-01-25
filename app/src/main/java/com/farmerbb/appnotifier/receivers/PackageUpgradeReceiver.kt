/* Copyright 2020 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.appnotifier.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.farmerbb.appnotifier.*
import com.farmerbb.appnotifier.room.AppUpdateDAO
import javax.inject.Inject

class PackageUpgradeReceiver: BroadcastReceiver() {

    @Inject lateinit var controller: NotificationController
    @Inject lateinit var dao: AppUpdateDAO

    init {
        AppNotifierApplication.component.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return

        context.apply {
            initAppNotifierService()

            getApplicationInfoSafely(BuildConfig.APPLICATION_ID, dao)?.let {
                controller.apply {
                    handleAppUpdateNotification(it)
                    replayAppInstalls()
                }
            }
        }
    }
}