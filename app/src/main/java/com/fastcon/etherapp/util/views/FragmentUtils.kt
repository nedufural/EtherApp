package com.fastcon.etherapp.util.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class FragmentUtils {
    companion object {
        /**
         * This method is similar to the Activity finsh()
         * It is an equivalent for fragments
         * @param FragmentManager
         * @param Fragment
         * */
        fun popBackStack(manager: FragmentManager,context: Fragment) {
            manager.beginTransaction()
                .remove(context)
                .commit()
        }
    }
}