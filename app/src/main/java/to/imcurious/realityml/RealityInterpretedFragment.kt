package to.imcurious.realityml

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import to.imcurious.realityml.databinding.FragmentRealityInterpretedBinding
import to.imcurious.realityml.databinding.FragmentRealityViewBinding


class RealityInterpretedFragment : Fragment() {

    private lateinit var binding: FragmentRealityInterpretedBinding
    private val model: RealityModel by activityViewModels()
    val TAG = "REALITY INTERPRETED"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRealityInterpretedBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
    }

    private fun configureViews() {
        with(binding) {
            originalText.text = model.visionText.text

            val code = getLanguageCode()
            if (code != "") {

                // Create an SomeLanguage-French translator:
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(code)
                    .setTargetLanguage(TranslateLanguage.FRENCH)
                    .build()
                val someLanguageToFrenchTranslator = Translation.getClient(options)

                val conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()
                someLanguageToFrenchTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        someLanguageToFrenchTranslator.translate(model.visionText.text)
                            .addOnSuccessListener { translatedText ->
                                // Translation successful.
                                binding.translatedText.text = translatedText
                                makeBitmap(translatedText)
                            }
                            .addOnFailureListener { exception ->
                                // Error.
                                RTool.showLongSnack(binding.originalText, "Error occured when trying to translate the text")
                                exception.message?.let { it1 -> Log.e(TAG, it1) }
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Model couldn’t be downloaded or other internal error.
                        RTool.showLongSnack(binding.originalText, "Model couldn’t be downloaded or other internal error")
                        exception.message?.let { it1 -> Log.e(TAG, it1) }
                    }
            }
        }
    }

    private fun getLanguageCode(): String {
        var code = ""
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(model.visionText.text)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i(TAG, "Can't identify language.")
                    RTool.showLongSnack(binding.originalText, "Can't identiry language")
                } else {
                    Log.i(TAG, "Language: $languageCode")
                    code = languageCode
                }
            }
            .addOnFailureListener {
                // Model couldn’t be loaded or other internal error.
                // ...
                RTool.showLongSnack(binding.originalText, "Error occured when trying to detect the language in which the text of the image is written")
                it.message?.let { it1 -> Log.e(TAG, it1) }
            }

        return code
    }

    private fun makeBitmap(translatedText: String) {
        binding.translatedImage.setImageBitmap(model.bitmap)
    }
}