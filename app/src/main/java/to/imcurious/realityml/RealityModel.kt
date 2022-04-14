package to.imcurious.realityml

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text

class RealityModel: ViewModel() {

    lateinit var bitmap: Bitmap
    lateinit var visionText: Text

}