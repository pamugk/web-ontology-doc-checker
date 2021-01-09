import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mToolbar
import react.*

class App(props: RProps) : RComponent<RProps, RState>(props) {

    init { }

    override fun RBuilder.render() {
        mAppBar {
            mToolbar {
                mButton("Material Button")
            }
        }
    }
}
