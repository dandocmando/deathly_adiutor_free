/*
 * Copyright (C) 2016 Martin Bouchet
 * Copyright (C) 2015 Willi Ye
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

package com.exynos5420.deathlyadiutor.ads.utils.kernel;

import android.content.Context;
import android.content.SharedPreferences;

import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Martin Bouchet on 9/11/2016.
 */

public class GPUVoltage implements Constants {
    private static String GPU_VOLTAGE_FILE = getNodePath(GPU_VOLTAGE_EXYNOS5_FILE);
    private static String[] mGpuFreqs;

    public static void setGlobalOffset(String voltage, Context context) {
        double adjust = Utils.stringtodouble(voltage) * 1000;
        String final_voltage;
        String command;
        for (int i = 0; i < getVoltages().size(); i++) {
            final_voltage = Double.toString(Integer.parseInt(getVoltages().get(i)) + adjust);
            final_voltage = final_voltage.substring(0, final_voltage.length() - 2);
            command = getFreqs().get(i) + " " + final_voltage;
            Control.runCommand(command, GPU_VOLTAGE_FILE, Control.CommandType.GENERIC, Integer.toString(i), context);
        }

    }

    public static void setVoltage(String freq, String voltage, Context context) {
        String command;
        voltage = voltage.substring(0, voltage.length() - 2);
        for (int i = 0; i < getVoltages().size(); i++)
            if (getFreqs().get(i).equals(freq)) {
                command = freq + " " + voltage;
                Control.runCommand(command, GPU_VOLTAGE_FILE, Control.CommandType.GENERIC, Integer.toString(i), context);
                return;
            }

    }

    public static List<String> getVoltages() {
        String value = Utils.readFile(GPU_VOLTAGE_FILE);
        if (value != null) {
            String[] lines;
            lines = value.split("\n");

            String[] voltages = new String[lines.length];
            for (int i = 0; i < voltages.length; i++) {
                String[] voltageLine;
                voltageLine = lines[i].split(" ");
                if (voltageLine.length > 1) {
                    voltages[i] = voltageLine[1].trim();
                }
            }
            return new ArrayList<>(Arrays.asList(voltages));
        }
        return Collections.emptyList();
    }

    public static List<String> getFreqs() {

        if (mGpuFreqs == null) {

            String value = Utils.readFile(GPU_VOLTAGE_FILE);

            if (value != null) {
                String[] lines;
                lines = value.split("\n");

                mGpuFreqs = new String[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    mGpuFreqs[i] = lines[i].split(" ")[0].trim();

                }
            }
            return new ArrayList<>(Arrays.asList(mGpuFreqs));
        } else {
            return new ArrayList<>(Arrays.asList(mGpuFreqs));
        }
    }

    public static boolean storeVoltageTable(Context context) {
        // Have to call this function to pre-load variables
        if (!GPUVoltage.getFreqs().isEmpty() && !GPUVoltage.getVoltages().isEmpty()) {
            List<String> freqs = GPUVoltage.getFreqs();
            List<String> voltages = GPUVoltage.getVoltages();

            // Store Kernel's Stock Freq/Voltage table
            SharedPreferences.Editor preferences = context.getSharedPreferences("gpu_voltage_table", 0).edit();
            for (int i = 0; i < freqs.size(); i++) {
                preferences.putString(freqs.get(i), voltages.get(i));
            }
            preferences.apply();
            return true;
        } else {
            return false;
        }
    }

    public static String getNodePath(String paths[]){
        for (int i=0; i<paths.length; i++) {
            if (Utils.existFile(paths[i])) {
                return paths[i];
            }
        }
        return "-1";
    }
}
