package company.twoweeks.twoweeks;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Created by hoon on 2015-10-18.
 */
public class SettingActivity extends PreferenceActivity{

    Preference preference_datasync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        preference_datasync = (Preference)findPreference("datasync");

        preference_datasync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getApplicationContext(), "Data Sync", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

    }

}
