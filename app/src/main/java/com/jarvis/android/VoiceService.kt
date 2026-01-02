package com.jarvis.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Handles all Voice Input (STT) and Output (TTS).
 */
class VoiceService(private val context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null

    // Callback to MainActivity to update UI or Logic
    var onSpeechResult: ((String) -> Unit)? = null
    var onSpeechError: ((String) -> Unit)? = null
    var onTTSReady: (() -> Unit)? = null

    init {
        textToSpeech = TextToSpeech(context, this)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        setupRecognitionListener()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale("pt", "BR"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("VoiceService", "Portuguese language not supported or missing data.")
            } else {
                onTTSReady?.invoke()
            }
        } else {
            Log.e("VoiceService", "TTS Initialization failed.")
        }
    }

    private fun setupRecognitionListener() {
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { Log.d("VoiceService", "Ready for speech") }
            override fun onBeginningOfSpeech() { Log.d("VoiceService", "Speech started") }
            override fun onRmsChanged(rmsdB: Float) { /* Visualizer update hook */ }
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { Log.d("VoiceService", "Speech ended") }

            override fun onError(error: Int) {
                val message = when(error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> "Não entendi."
                    SpeechRecognizer.ERROR_NETWORK -> "Erro de conexão."
                    else -> "Erro no reconhecimento: $error"
                }
                onSpeechError?.invoke(message)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onSpeechResult?.invoke(matches[0])
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun speak(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        speechRecognizer?.destroy()
    }
}
