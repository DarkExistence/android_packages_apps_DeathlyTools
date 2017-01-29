/*
 * Copyright (C) 2017 Liquid Death OS
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

package com.deathly.settings.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class DisplaySettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_NOTIFICATION_LIGHT = "notification_light";
    private static final String KEY_BATTERY_LIGHT = "battery_light";
    private static final String SCREENRECORD_CHORD_TYPE = "screenrecord_chord_type";

    private static final String CATEGORY_LEDS = "leds";

    private ListPreference mScreenrecordChordType;

    private Preference mNotifLedFrag;
    private Preference mBattLedFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.display_settings);
        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        final PreferenceCategory leds = (PreferenceCategory) findPreference(CATEGORY_LEDS);

/*
        mNotifLedFrag = findPreference(KEY_NOTIFICATION_LIGHT);
        //remove notification led settings if device doesnt support it
        if (!getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveNotificationLed)) {
            leds.removePreference(findPreference(KEY_NOTIFICATION_LIGHT));
        }

        mBattLedFrag = findPreference(KEY_BATTERY_LIGHT);
        //remove battery led settings if device doesnt support it
        if (!getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            leds.removePreference(findPreference(KEY_BATTERY_LIGHT));
        }

        //remove led category if device doesnt support notification or battery
        if (!getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveNotificationLed)
                && !getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            prefScreen.removePreference(findPreference(CATEGORY_LEDS)); */ 
        int recordChordValue = Settings.System.getInt(resolver,
                Settings.System.SCREENRECORD_CHORD_TYPE, 0);
        mScreenrecordChordType = initActionList(SCREENRECORD_CHORD_TYPE,
                recordChordValue);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DEATHLY;
    }

    private ListPreference initActionList(String key, int value) {
	        ListPreference list = (ListPreference) getPreferenceScreen().findPreference(key);
	        list.setValue(Integer.toString(value));
	        list.setSummary(list.getEntry());
	        list.setOnPreferenceChangeListener(this);
	        return list;
	}
	
    private void handleActionListChange(ListPreference pref, Object newValue, String setting) {
        String value = (String) newValue;
        int index = pref.findIndexOfValue(value);
        pref.setSummary(pref.getEntries()[index]);
        Settings.System.putInt(getActivity().getContentResolver(), setting, Integer.valueOf(value));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if  (preference == mScreenrecordChordType) {
            handleActionListChange(mScreenrecordChordType, newValue,
                    Settings.System.SCREENRECORD_CHORD_TYPE);
            return true;
        }
        return false;
    }
}

