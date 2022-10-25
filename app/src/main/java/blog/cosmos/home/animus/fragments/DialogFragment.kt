package blog.cosmos.home.animus.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import blog.cosmos.home.animus.R
import java.lang.Math.abs

class MyDialogFragment : DialogFragment(), View.OnTouchListener {

    private var rootLayoutY: Int = 0
    private val rootLayout by lazy {
        requireView().findViewById<ConstraintLayout>(R.id.dialog_root)
    }

    private var oldY = 0
    private var baseLayoutPosition = 0
    private var defaultViewHeight = 0
    private var isScrollingUp = false
    private var isScrollingDown = false

    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_dialog_facebook_comment, container,
            false
        )
        view.setBackgroundResource(R.drawable.rounded_background)

        return view
    }

    override fun onStart() {
        super.onStart()
        // Making the dialog full screen
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootLayout.setOnTouchListener(this)

        rootLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rootLayout.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                // save default base layout height
                defaultViewHeight = rootLayout.height
            }
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // Get finger position on screen
        val y = event!!.rawY.toInt()


        // Switch on motion event type
        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {

                // Init finger and view position
                oldY = y
                baseLayoutPosition = rootLayout.y.toInt()
            }

            MotionEvent.ACTION_UP -> {

                if (rootLayoutY >= defaultViewHeight / 2) {
                    dismiss()
                    return true
                }

                // If user was doing a scroll up
                if (isScrollingUp) {
                    // Reset baselayout position
                    rootLayout.y = 0f
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false
                }

                // If user was doing a scroll down
                if (isScrollingDown) {
                    // Reset baselayout position
                    rootLayout.y = 0f
                    //  Reset base layout size
                    rootLayout.layoutParams.height = defaultViewHeight
                    rootLayout.requestLayout()
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false
                }

            }

            MotionEvent.ACTION_MOVE -> {

                rootLayoutY = abs(rootLayout.y.toInt())

                // Change base layout size and position (must change position because view anchor is top left corner)
                rootLayout.y = rootLayout.y + (y - oldY)

                if (oldY > y) { // scrolling up
                    if (!isScrollingUp) isScrollingUp = true
                } else { // Scrolling down
                    if (!isScrollingDown) isScrollingDown = true
                }

                // Update y position
                oldY = y
            }
        }
        return true
    }


}