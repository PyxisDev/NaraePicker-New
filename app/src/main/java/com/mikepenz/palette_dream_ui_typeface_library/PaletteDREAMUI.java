/*
 * Copyright 2014 Mike Penz
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
package com.mikepenz.palette_dream_ui_typeface_library;

import android.content.Context;
import android.graphics.Typeface;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class PaletteDREAMUI implements ITypeface {
    private static final String TTF_FILE = "palette-dream-ui-font-v2.0.ttf";
    private static Typeface typeface = null;
    private static HashMap<String, Character> mChars;

    @Override
    public IIcon getIcon(String key) {
        return Icon.valueOf(key);
    }

    @Override
    public HashMap<String, Character> getCharacters() {
        if (mChars == null) {
            HashMap<String, Character> aChars = new HashMap<String, Character>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), v.character);
            }
            mChars = aChars;
        }
        return mChars;
    }

    @Override
    public String getMappingPrefix() {
        return "Palette";
    }

    @Override
    public String getFontName() {
        return "Palette DREAM UI";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    @Override
    public int getIconCount() {
        return mChars.size();
    }

    @Override
    public Collection<String> getIcons() {
        Collection<String> icons = new LinkedList<String>();
        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }
        return icons;
    }

    @Override
    public String getAuthor() {
        return "Palette Team";
    }

    @Override
    public String getUrl() {
        return "http://palette.moe";
    }

    @Override
    public String getDescription() {
        return "...";
    }

    @Override
    public String getLicense() {
        return "CC BY-SA";
    }

    @Override
    public String getLicenseUrl() {
        return "CC BY-SA";
    }

    @Override
    public Typeface getTypeface(Context context) {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + TTF_FILE);
            } catch (Exception e) {
                return null;
            }
        }
        return typeface;
    }

    public enum Icon implements IIcon {
        Palette_activity('\ue800'),
		Palette_app('\ue801'),
		Palette_arrow('\ue802'),
		Palette_badge_heart('\ue803'),
		Palette_badge_retweet('\ue804'),
		Palette_bldck_user('\ue805'),
		Palette_camera('\ue806'),
		Palette_close_delete('\ue807'),
		Palette_delete('\ue808'),
		Palette_dm('\ue809'),
		Palette_download('\ue80a'),
		Palette_drive('\ue80b'),
		Palette_fingerprint('\ue80c'),
		Palette_folder('\ue80d'),
		Palette_follow('\ue80e'),
		Palette_font('\ue80f'),
		Palette_heart('\ue810'),
		Palette_home('\ue811'),
		Palette_information('\ue812'),
		Palette_location('\ue813'),
		Palette_lock('\ue814'),
		Palette_mention('\ue815'),
		Palette_menu_2('\ue816'),
		Palette_menu('\ue817'),
		Palette_message('\ue818'),
		Palette_music('\ue819'),
		Palette_mute_egg('\ue81a'),
		Palette_mute('\ue81b'),
		Palette_notification_view('\ue81c'),
		Palette_palette('\ue81d'),
		Palette_premium('\ue81e'),
		Palette_profile('\ue81f'),
		Palette_quote('\ue820'),
		Palette_reflesh('\ue821'),
		Palette_reply('\ue822'),
		Palette_retweet('\ue823'),
		Palette_search('\ue824'),
		Palette_setting('\ue825'),
		Palette_share('\ue826'),
		Palette_sound_mute('\ue827'),
		Palette_sound_play('\ue828'),
		Palette_sound_playing('\ue829'),
		Palette_sound_stop('\ue82a'),
		Palette_theme('\ue82b'),
		Palette_timeline('\ue82c'),
		Palette_radio('\ue82d'),
		Palette_tutorial_long_touch('\ue82e'),
		Palette_tutorial_menu('\ue82f'),
		Palette_twit('\ue830'),
		Palette_video('\ue831'),
		Palette_weather('\ue832'),
		Palette_arrow_2('\ue833'),
		Palette_back('\ue834'),
		Palette_tutorial_left_and_right('\ue835'),
		Palette_gallery('\ue836'),
		Palette_analysis('\ue837'),
		Palette_premium_check('\ue838');

        char character;

        Icon(char character) {
            this.character = character;
        }

        public String getFormattedName() {
            return "{" + name() + "}";
        }

        public char getCharacter() {
            return character;
        }

        public String getName() {
            return name();
        }

        // remember the typeface so we can use it later
        private static ITypeface typeface;

        public ITypeface getTypeface() {
            if (typeface == null) {
                typeface = new PaletteDREAMUI();
            }
            return typeface;
        }
    }
}
