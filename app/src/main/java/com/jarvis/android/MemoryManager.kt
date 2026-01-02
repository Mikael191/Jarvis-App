package com.jarvis.android

/**
 * Manages conversation context and user data.
 */
object MemoryManager {
    private val conversationHistory = mutableListOf<Pair<String, String>>()
    private val userData = mutableMapOf<String, Any>()

    fun addToHistory(user: String, ai: String) {
        conversationHistory.add(user to ai)
        // Keep only last 10 turns for context to save memory/tokens
        if (conversationHistory.size > 10) {
            conversationHistory.removeAt(0)
        }
    }

    fun getHistory(): List<Pair<String, String>> {
        return conversationHistory.toList()
    }

    fun setPreference(key: String, value: Any) {
        userData[key] = value
    }

    fun getPreference(key: String): Any? {
        return userData[key]
    }
}
