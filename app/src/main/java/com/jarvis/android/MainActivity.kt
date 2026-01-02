package com.jarvis.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var voiceService: VoiceService
    private lateinit var aiHandler: AIHandler
    private lateinit var commandHandler: CommandHandler

    private lateinit var tvLog: TextView
    private lateinit var btnSpeak: Button

    // Permission request code
    private val RECORD_AUDIO_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        tvLog = findViewById(R.id.tv_log)
        tvLog.movementMethod = ScrollingMovementMethod() // Make log scrollable
        btnSpeak = findViewById(R.id.btn_speak)

        // Initialize Logic components
        aiHandler = MockAIHandler() // Swap with RealAIHandler later
        commandHandler = CommandHandler(this)
        voiceService = VoiceService(this)

        setupInteractions()
        checkPermissions()
    }

    private fun setupInteractions() {
        // Voice Service Callbacks
        voiceService.onTTSReady = {
            logToScreen("Sistema de voz: ONLINE")
            voiceService.speak("Jarvis online.")
        }

        voiceService.onSpeechResult = { text ->
            logToScreen("Você: $text")
            processInput(text)
        }

        voiceService.onSpeechError = { error ->
            logToScreen("Erro: $error")
            btnSpeak.isEnabled = true
            btnSpeak.text = getString(R.string.touch_to_speak)
        }

        // Button Click
        btnSpeak.setOnClickListener {
            if (hasPermission()) {
                startListeningState()
                voiceService.startListening()
            } else {
                requestPermission()
            }
        }
    }

    private fun processInput(input: String) {
        // Return UI to normal state
        btnSpeak.isEnabled = true
        btnSpeak.text = getString(R.string.touch_to_speak)

        // 1. Check if it's a System Command
        if (commandHandler.executeCommand(input)) {
            logToScreen("Jarvis: Executando comando...")
            return
        }

        // 2. If not, ask AI
        logToScreen("Processando...")
        CoroutineScope(Dispatchers.IO).launch {
            val response = aiHandler.getResponse(input)

            withContext(Dispatchers.Main) {
                logToScreen("Jarvis: $response")
                voiceService.speak(response)
                MemoryManager.addToHistory(input, response)
            }
        }
    }

    private fun startListeningState() {
        btnSpeak.isEnabled = false // Prevent double clicks
        btnSpeak.text = getString(R.string.listening)
        logToScreen("Ouvindo...")
    }

    private fun logToScreen(message: String) {
        val currentText = tvLog.text.toString()
        val newText = "$currentText\n$message"
        tvLog.text = newText

        // Auto scroll to bottom
        val scrollAmount = tvLog.layout?.getLineTop(tvLog.lineCount) ?: 0
        val height = tvLog.height
        if (scrollAmount > height) {
            tvLog.scrollTo(0, scrollAmount - height)
        }
    }

    // --- Permissions ---

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logToScreen("Permissão de áudio concedida.")
            } else {
                logToScreen("Permissão negada. Funcionalidade de voz indisponível.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceService.shutdown()
    }
}
