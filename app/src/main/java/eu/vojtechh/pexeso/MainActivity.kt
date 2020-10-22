package eu.vojtechh.pexeso

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout

data class Player(val id: Int, var score: Int)

class MainActivity : AppCompatActivity() {

    private lateinit var flexLayout: FlexboxLayout
    private val currentlyShowingFace = mutableListOf<ImageButton>()
    private var player1 = Player(1, 0)
    private var player2 = Player(2, 0)
    private var currentPlayer = player1
    private var moveCount = 1

    private val drawables = mutableListOf(
        R.drawable.ic_adb,
        R.drawable.ic_android,
        R.drawable.ic_attach_email,
        R.drawable.ic_bell,
        R.drawable.ic_cafe,
        R.drawable.ic_email,
        R.drawable.ic_flower,
        R.drawable.ic_football,
        R.drawable.ic_grill,
        R.drawable.ic_palette,
        R.drawable.ic_rugby,
        R.drawable.ic_run,
        R.drawable.ic_star,
        R.drawable.ic_subway,
        R.drawable.ic_support,
        R.drawable.ic_thumbs_down,
        R.drawable.ic_thumbs_up,
        R.drawable.ic_tram,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flexLayout = findViewById(R.id.main_flex_layout)
        updateStats()

        drawables.addAll(drawables)
        drawables.shuffle()

        val tileClickListener = View.OnClickListener {
            val imageButton = it as ImageButton
            if (imageButton.drawable == null) {
                imageButton.setImageDrawable(
                    resources.getDrawable(
                        imageButton.tag as Int,
                        theme
                    )
                )
                currentlyShowingFace.add(imageButton)
                checkShownDrawables()
            }
        }

        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val size = if (height > width) height else width

        for (resource in drawables) {
            val imageButton = ImageButton(this).apply {
                setOnClickListener(tileClickListener)
                tag = resource
                minimumHeight = size / 12
                minimumWidth = size / 12
                scaleType = ImageView.ScaleType.FIT_XY
            }
            flexLayout.addView(imageButton)
        }
    }

    private fun checkShownDrawables() {
        if (currentlyShowingFace.size == 2) {
            if (
                currentlyShowingFace[0].tag ==
                currentlyShowingFace[1].tag
            ) {
                currentlyShowingFace.forEach {
                    it.isEnabled = false
                    it.setColorFilter(
                        resources.getColor(if (currentPlayer.id == player1.id) R.color.purple_500 else R.color.teal_700)
                    )

                }
                currentlyShowingFace.clear()
                currentPlayer.score++
                if (checkGameFinished()) return
            } else {
                currentPlayer = if (currentPlayer.id == player1.id) player2 else player1
            }
        } else if (currentlyShowingFace.size == 3) {
            moveCount++
            currentlyShowingFace[0].setImageDrawable(null)
            currentlyShowingFace[1].setImageDrawable(null)
            currentlyShowingFace.removeFirst()
            currentlyShowingFace.removeFirst()
        }
        updateStats()
    }

    @SuppressLint("SetTextI18n")
    private fun checkGameFinished(): Boolean {
        if (player1.score + player2.score == drawables.size / 2) {
            val text: String
            if (player1.score == player2.score) {
                text = "Remíza, oba hráči mají ${player1.score} bodů"
            } else {
                val winner = if (player1.score > player2.score) player1 else player2
                text = "Vyhrál hráč číslo ${winner.id} s ${winner.score} body"
            }
            findViewById<TextView>(R.id.status_text).text = text
            return true
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun updateStats() {
        findViewById<TextView>(R.id.status_text).text =
            "$moveCount tah, stav ${player1.score}:${player2.score}, hraje ${currentPlayer.id}. hráč"

    }
}