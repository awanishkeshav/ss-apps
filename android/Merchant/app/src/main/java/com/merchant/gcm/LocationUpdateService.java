/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.merchant.gcm;

import java.util.List;

import com.merchant.common.PreferanceUtil;
import com.merchant.services.APIService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class LocationUpdateService extends IntentService {
	
	private long updateIntervalMilis = 1000*60*30;// 30 minutes
	//private long updateIntervalMilis = 1000*10*1;// 30 minutes

	public LocationUpdateService() {
		super("LocationUpdateService");
	}

	public static final String TAG = "LocationUpdateService";

	@Override
	protected void onHandleIntent(Intent intent) {
		
		boolean shouldRunLocationService = false;//new PreferanceUtil(this).shouldRunLocationUpdateService();
		while (shouldRunLocationService) {
			LocationUpdateService.updateLocation(LocationUpdateService.getGPS(this),this);
			try {
				Thread.sleep(updateIntervalMilis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			
		}
	}

	public static Location getGPS(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */
		Location l = null;
		if (providers.size() > 0) {
			l = lm.getLastKnownLocation(providers.get(providers.size() - 1));
		}

		/*
		 * for (int i = providers.size() - 1; i >= 0; i--) { l =
		 * lm.getLastKnownLocation(providers.get(i)); if (l != null) break; }
		 */

		return l;
	}

	public static void updateLocation(Location loc, Context context) {
		if (loc != null) {
			Double latitude = loc.getLatitude();
			Double longitude = loc.getLongitude();
			APIService service = APIService.updateUserLocation(context, latitude, longitude);
			service.getData(false);
		} else {
			// User possibly declined location permission
		}

	}

}
