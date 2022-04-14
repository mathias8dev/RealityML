package to.imcurious.realityml

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import to.imcurious.realityml.databinding.FragmentRealityViewBinding


class RealityViewFragment : Fragment() {

    private lateinit var binding: FragmentRealityViewBinding
    private val model: RealityModel by activityViewModels()
    val TAG = "REALITY VIEW FRAGMENT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRealityViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
    }

    private fun configureViews() {
        val cameraView = binding.cameraView
        cameraView.mode = Mode.PICTURE
        cameraView.pictureFormat = PictureFormat.JPEG
        cameraView.setLifecycleOwner(this)
        cameraView.addCameraListener(object: CameraListener() {

            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap { bitmap ->
                    // Bitmap is ready so i can call right now ml kit
                    // Check if there is a text on the camera stream
                    // If there is a text so take a capture
                    // Use the share view model to share the bitmap to the reality interpreted fragment
                    // Let the fragment make the magic
                    bitmap?.let {

                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        val inputImage = InputImage.fromBitmap(it, result.rotation)

                        recognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                // Task completed successfully
                                if (visionText.text == "") {
                                    RTool.showLongSnack(binding.cameraView, "Aucun texte trouvé dans le flux courant")

                                }else {
                                    model.visionText = visionText
                                    model.bitmap = it
                                    // Launch another fragment
                                    findNavController().navigate(R.id.action_realityViewFragment_to_realityInterpretedFragment)
                                }
                            }
                            .addOnFailureListener { e ->
                                // Task failed with an exception
                                RTool.showLongSnack(binding.cameraView, "Aucun texte trouvé dans le flux courant")
                                e.message?.let { it1 -> Log.e(TAG, it1) }
                            }
                    }

                }
            }
        })

        val actionButton = binding.takePictureButton

        actionButton.setOnClickListener {

            cameraView.takePicture()
        }
    }

}