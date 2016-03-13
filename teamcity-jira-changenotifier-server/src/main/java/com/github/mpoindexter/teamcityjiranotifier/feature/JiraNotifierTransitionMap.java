/*
 * Copyright 2016 Michael Poindexter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mpoindexter.teamcityjiranotifier.feature;

import java.util.HashMap;
import java.util.Map;

public class JiraNotifierTransitionMap {
    public static final String ANY_STATE = "*";

    public static JiraNotifierTransitionMap parse(String map) {
        if (map == null) {
            return null;
        }

        Map<String, String> transitions = new HashMap<String, String>();
        String[] parts = map.split(";");
        for (String part : parts) {
            String[] kvp = part.split("->");
            if (kvp.length != 2) {
                return null;
            }
            transitions.put(kvp[0].trim(), kvp[1].trim());
        }

        return new JiraNotifierTransitionMap(transitions);
    }

    private final Map<String, String> transitions;

    private JiraNotifierTransitionMap(Map<String, String> transitions) {
        this.transitions = transitions;
    }

    public boolean hasTransitionFrom(String state) {
        return transitions.containsKey(state) || transitions.containsKey(ANY_STATE);
    }

    public String getTransition(String state) {
        if (transitions.containsKey(state)) {
            return transitions.get(state);
        } else if (transitions.containsKey(ANY_STATE)) {
            return transitions.get(ANY_STATE);
        }

        return null;
    }
}
