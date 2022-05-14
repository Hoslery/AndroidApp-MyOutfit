package com.toropov.my_outfit.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.toropov.my_outfit.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*


class AboutUsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val versionElement = Element()
        versionElement.title = "Version 1.0"

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("Easy monitoring of clothing prices!")
            .setCustomFont(Typeface.createFromAsset(assets, "fonts/Bungee-Regular.ttf"))
            .setImage(R.drawable.team)
            .addItem(versionElement)
            .addGroup("Connect with us")
            .addEmail("toporik_75@mail.ru")
            .addGitHub("Hoslery")
            .addItem(getCopyRightsElement())
            .create()

        setContentView(aboutPage)
    }

    private fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights  = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR).toString())
        copyRightsElement.title = copyrights
        copyRightsElement.iconDrawable = R.drawable.copyright
        copyRightsElement.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        copyRightsElement.iconNightTint = android.R.color.white
        copyRightsElement.gravity = Gravity.CENTER
        copyRightsElement.onClickListener = View.OnClickListener {
            Toast.makeText(this, copyrights, Toast.LENGTH_SHORT).show()
        }
        return copyRightsElement
    }
}