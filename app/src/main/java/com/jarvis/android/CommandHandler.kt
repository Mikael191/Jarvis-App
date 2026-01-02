package com.jarvis.android

import android.content.Context
import android.widget.Toast

/**
 * Handles system commands triggered by voice.
 * This acts as the "hands" of Jarvis.
 */
class CommandHandler(private val context: Context) {

    fun executeCommand(command: String): Boolean {
        val lowerCmd = command.lowercase()

        return when {
            lowerCmd.contains("horas") || lowerCmd.contains("que horas") -> {
                // In a real app, VoiceService would speak this.
                // Here we return true to indicate it was handled as a command.
                true
            }
            lowerCmd.contains("bateria") -> {
                // Logic to check battery level
                true
            }
            lowerCmd.contains("abrir youtube") -> {
                Toast.makeText(context, "Abrindo YouTube...", Toast.LENGTH_SHORT).show()
                // Intent logic would go here
                true
            }
            else -> false // Not a system command, pass to AI
        }
    }
}
