package to.imcurious.realityml

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

object RTool {

    fun showShortSnack(view: View, text: String) {
        Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_SHORT).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }

    fun showLongSnack(view: View, text: String) {
        Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_LONG).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }

    fun makeVanished(vararg views: View) {
        views.forEach { view -> view.visibility = View.GONE }
    }

    fun makeVisible(vararg views: View) {
        views.forEach { view -> view.visibility = View.VISIBLE }
    }

    fun makeInvisible(vararg views: View) {
        views.forEach { view -> view.visibility = View.INVISIBLE }
    }
}