package com.msl.talfinalproject.ui.fonts

import java.util.*

enum class FontText(val mId : Int) {
    OPEN_SANS_HEBREW_BOLD(0),
    OPEN_SANS_HEBREW_BOLD_2(1),
    OPEN_SANS_HEBREW_BOLD_ITALIC(2),
    OPEN_SANS_HEBREW_EXTRA_BOLD(3),
    OPEN_SANS_HEBREW_EXTRA_BOLD_ITALIC(4),
    OPEN_SANS_HEBREW_ITALIC(5),
    OPEN_SANS_HEBREW_LIGHT(6),
    OPEN_SANS_HEBREW_LIGHT2(7),
    OPEN_SANS_HEBREW_LIGHT_ITALIC(8),
    OPEN_SANS_HEBREW_REGULAR(9);



    companion object {
        val nameToStringMap = HashMap<Int, String>()

        init {
            nameToStringMap[0] = "fonts/OpenSansHebrewBold.ttf"
            nameToStringMap[1] = "fonts/OpenSansHebrewBold2.ttf"
            nameToStringMap[2] = "fonts/OpenSansHebrewBoldItalic.ttf"
            nameToStringMap[3] = "fonts/OpenSansHebrewExtraBold.ttf"
            nameToStringMap[4] = "fonts/OpenSansHebrewExtraBoldItalic.ttf"
            nameToStringMap[5] = "fonts/OpenSansHebrewItalic.ttf"
            nameToStringMap[6] = "fonts/OpenSansHebrewLight.ttf"
            nameToStringMap[7] = "fonts/OpenSansHebrewLight2.ttf"
            nameToStringMap[8] = "fonts/OpenSansHebrewLightItalic.ttf"
            nameToStringMap[9] = "fonts/OpenSansHebrewRegular.ttf"
        }

        fun nameFromId(id: Int?): String? {
            if (isValidEnum(id)) {
                return nameToStringMap[id]
            }
            throw IllegalArgumentException("The supplied id is not mapped to a font")
        }

        fun isValidEnum(value: Int?): Boolean {
            if(value != null)
                return value < nameToStringMap.size && value >= 0
            return false
        }
    }




}